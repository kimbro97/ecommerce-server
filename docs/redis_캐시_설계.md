# Redis 캐시 설계 및 데이터 정합성 보장 전략

## 개요
장바구니 조회 성능 최적화를 위해 Redis 캐시를 도입했습니다. 캐시 사용 시 발생할 수 있는 데이터 정합성 문제를 해결하기 위해 **캐시 무효화 전략(Cache Invalidation Strategy)**을 적용하여 Redis와 데이터베이스 간 데이터 일관성을 보장합니다.

## 캐시 적용 대상

### 장바구니 조회 기능
- **대상 API**: `GET /api/carts` - 사용자별 장바구니 목록 조회
- **캐시 키**: `carts:user:{userId}` 
- **캐시 TTL**: 5분 (300초)
- **데이터 타입**: 장바구니 상품 정보가 포함된 복합 객체 리스트

### 캐시 도입 이유
1. **높은 조회 빈도**: 장바구니는 사용자가 자주 확인하는 데이터
2. **복잡한 조인 쿼리**: Cart + Product 조인으로 인한 DB 부하
3. **실시간성 요구 완화**: 5분 내 데이터 변경은 허용 가능한 범위

## 데이터 정합성 문제 분석

### 문제 상황
```
1. 사용자 A가 장바구니 조회 → Redis 캐시 저장
2. 사용자 A가 장바구니 상품 수량 변경 → DB만 업데이트
3. 사용자 A가 다시 장바구니 조회 → 기존 캐시 반환 (변경 전 데이터)
4. 결과: Redis 캐시와 DB 데이터 불일치
```

### 정합성 깨짐 시나리오
- **장바구니 상품 추가**: 새 상품이 캐시에 반영되지 않음
- **장바구니 상품 수량 수정**: 변경된 수량이 캐시에 반영되지 않음  
- **장바구니 상품 삭제**: 삭제된 상품이 캐시에 남아있음
- **장바구니 전체 비우기**: 빈 장바구니가 캐시에 반영되지 않음

## 캐시 무효화 전략 설계

### 1. Write-Through 전략 배제
**배제 이유:**
- 장바구니 수정 시마다 캐시 업데이트 오버헤드
- 복잡한 Cart + Product 조인 데이터의 정확한 캐시 업데이트 어려움
- 캐시 업데이트 실패 시 데이터 불일치 위험

### 2. Cache-Aside with Invalidation 채택
**선택 이유:**
- 간단하고 안전한 구현
- 데이터 무결성 보장
- 성능과 일관성의 균형점 확보

## 구현 전략

### Spring Cache 어노테이션 활용

#### 캐시 저장 (@Cacheable)
```java
@Cacheable(cacheNames = "findCarts", key = "'carts:user:' + #userId", cacheManager = "cartCacheManager")
public List<CartWithProductInfo> findCarts(Long userId) {
    // 캐시에 데이터가 없을 때만 DB 조회 후 캐시 저장
    return getCartWithProductInfos(userId);
}
```

**동작 과정:**
1. Redis에서 `carts:user:{userId}` 키 조회
2. 캐시 HIT: 캐시 데이터 반환
3. 캐시 MISS: DB 조회 후 결과를 Redis에 저장하고 반환

#### 캐시 무효화 (@CacheEvict)
```java
// 장바구니 상품 추가 시 캐시 삭제
@CacheEvict(cacheNames = "findCarts", key = "'carts:user:' + #command.userId")
public CartInfo addCart(AddCartCommand command) {
    // DB에 장바구니 추가
    // 해당 사용자의 캐시 자동 삭제
}

// 장바구니 상품 수량 수정 시 캐시 삭제
@CacheEvict(cacheNames = "findCarts", key = "'carts:user:' + #command.userId")
public CartInfo updateCart(UpdateCartCommand command) {
    // DB에서 장바구니 수량 수정
    // 해당 사용자의 캐시 자동 삭제
}

// 장바구니 상품 삭제 시 캐시 삭제
@CacheEvict(cacheNames = "findCarts", key = "'carts:user:' + #command.userId")
public CartInfo deleteCart(DeleteCartCommand command) {
    // DB에서 장바구니 항목 삭제
    // 해당 사용자의 캐시 자동 삭제
}

// 장바구니 전체 비우기 시 캐시 삭제
@CacheEvict(cacheNames = "findCarts", key = "'carts:user:' + #command.userId")
public void clearCart(ClearCartCommand command) {
    // DB에서 장바구니 전체 삭제
    // 해당 사용자의 캐시 자동 삭제
}
```

### 무효화 전략의 핵심 원칙

#### 1. 수정 시 즉시 무효화
- 장바구니 관련 CUD(Create, Update, Delete) 작업 시 즉시 캐시 삭제
- 다음 조회 시 최신 데이터로 캐시 재생성

#### 2. 사용자별 격리된 캐시 키
- `carts:user:{userId}` 형태로 사용자별 독립적인 캐시 키 사용
- 다른 사용자의 캐시에 영향을 주지 않음

## 캐시 설정

### Redis 캐시 매니저 구성
```java
@Configuration
public class RedisCacheConfig {
    
    @Bean
    public RedisCacheManager cartCacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(5))  // TTL 5분
            .serializeKeysWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new Jackson2JsonRedisSerializer<>(Object.class)));
                
        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .build();
    }
}
```

### 주요 설정 요소
- **TTL 5분**: 캐시 자동 만료로 장기간 stale data 방지
- **Jackson2JsonRedisSerializer**: 복잡한 객체 직렬화 지원
- **StringRedisSerializer**: 사람이 읽기 쉬운 키 형태

## 성능 및 정합성 효과

### 성능 개선 효과
1. **캐시 HIT 시**: DB 조회 없이 Redis에서 즉시 응답
2. **복잡한 조인 쿼리 제거**: Cart + Product 조인 부하 감소
3. **DB 커넥션 풀 절약**: 조회 요청의 DB 연결 사용량 감소

### 정합성 보장 효과
1. **즉시 무효화**: 데이터 변경 시 즉시 캐시 삭제로 stale data 방지
2. **원자적 연산**: DB 트랜잭션과 캐시 무효화의 일관성 보장
3. **격리된 캐시**: 사용자별 독립적인 캐시로 데이터 간섭 방지

## 캐시 무효화 시나리오별 처리

### 시나리오 1: 장바구니 상품 추가
```
1. 사용자가 상품을 장바구니에 추가 요청
2. DB에 새로운 Cart 레코드 삽입
3. @CacheEvict로 해당 사용자 캐시 삭제
4. 다음 장바구니 조회 시 DB에서 최신 데이터 조회하여 캐시 재생성
```

### 시나리오 2: 장바구니 상품 수량 수정
```
1. 사용자가 장바구니 상품 수량 변경 요청
2. DB의 Cart 레코드 수량 업데이트
3. @CacheEvict로 해당 사용자 캐시 삭제
4. 다음 조회 시 변경된 수량이 반영된 데이터로 캐시 재생성
```

### 시나리오 3: 장바구니 상품 삭제
```
1. 사용자가 장바구니 상품 삭제 요청
2. DB에서 Cart 레코드 삭제
3. @CacheEvict로 해당 사용자 캐시 삭제
4. 다음 조회 시 삭제된 상품이 제외된 데이터로 캐시 재생성
```

## 결론

Redis 캐시와 캐시 무효화 전략을 통해 다음과 같은 효과를 달성했습니다:

1. **성능 향상**: 장바구니 조회 성능 개선
2. **데이터 정합성**: 캐시 무효화를 통한 Redis-DB 간 일관성 보장
3. **확장성**: 사용자별 독립적인 캐시로 확장 가능한 구조
4. **안정성**: Spring Cache 추상화를 통한 안정적인 캐시 관리
