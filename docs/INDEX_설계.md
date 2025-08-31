# 인덱스 설계 및 성능 최적화 보고서

## 개요
Product 테이블의 동적 검색 쿼리 성능 최적화를 위해 200만 개의 테스트 데이터를 기반으로 인덱스 설계를 수행했습니다. 8가지 쿼리 패턴을 분석하여 최적의 인덱스 조합을 도출했습니다.

## 분석 대상 쿼리 패턴

### 1. 전체 조건 검색 쿼리
```sql
-- 1. 카테고리 + 이름 + 가격 범위 검색
EXPLAIN ANALYZE SELECT * FROM product 
WHERE category='HOME' AND name LIKE '이케아%' ESCAPE '!' AND price BETWEEN 100000 AND 150000 
ORDER BY created_at DESC LIMIT 0,10;

-- 2. 카테고리 + 이름 검색
EXPLAIN ANALYZE SELECT * FROM product 
WHERE category='HOME' AND name LIKE '이케아%' ESCAPE '!' 
ORDER BY created_at DESC LIMIT 0,10;

-- 3. 카테고리 + 가격 범위 검색
EXPLAIN ANALYZE SELECT * FROM product 
WHERE category='HOME' AND price BETWEEN 100000 AND 150000 
ORDER BY created_at DESC LIMIT 0,10;

-- 4. 카테고리만 검색
EXPLAIN ANALYZE SELECT * FROM product 
WHERE category='HOME' 
ORDER BY created_at DESC LIMIT 0,10;
```

### 2. 부분 조건 검색 쿼리
```sql
-- 5. 이름 + 가격 범위 검색
EXPLAIN ANALYZE SELECT * FROM product 
WHERE name LIKE '이케아%' ESCAPE '!' AND price BETWEEN 100000 AND 150000 
ORDER BY created_at DESC LIMIT 0,10;

-- 6. 이름만 검색
EXPLAIN ANALYZE SELECT * FROM product 
WHERE name LIKE '이케아%' ESCAPE '!' 
ORDER BY created_at DESC LIMIT 0,10;

-- 7. 가격 범위만 검색 (페이징)
EXPLAIN ANALYZE SELECT * FROM product 
WHERE price BETWEEN 100000 AND 150000 
ORDER BY created_at DESC LIMIT 5000,10;

-- 8. 전체 조회 (정렬만)
EXPLAIN ANALYZE SELECT * FROM product 
ORDER BY created_at DESC LIMIT 0,10;
```

## 인덱스 설계 과정

### 1차 설계: 복합 인덱스 우선 접근

#### 초기 후보 인덱스
```sql
-- 카테고리 + 이름 복합 인덱스 (초기 검토)
CREATE INDEX product_category_name ON product (category, name);

-- 카테고리 + 생성일 복합 인덱스 (대안 검토)  
CREATE INDEX product_category_createdAt ON product (category, created_at);
```

#### 성능 분석 결과
**문제점 발견:**
- `product_category_name` 인덱스 사용 시, `name`이 범위 조건(LIKE)이므로 `ORDER BY created_at` 정렬을 인덱스로 처리하지 못함
- 정렬을 위한 추가적인 filesort 작업이 발생하여 성능 저하

**비교 테스트:**
1. `category + name` 인덱스: 검색은 빠르지만 정렬 시 filesort 발생
2. `category + created_at` 인덱스: 검색과 정렬을 모두 인덱스로 처리

**MySQL 옵티마이저 선택:**
- 두 인덱스가 모두 존재할 때 MySQL 옵티마이저가 `product_category_createdAt`를 선택
- 전체적인 쿼리 비용이 더 낮다고 판단

### 2차 설계: 최적화된 인덱스 조합

#### 최종 선정 인덱스

```sql
-- 1. 카테고리 기반 검색 + 정렬 최적화
CREATE INDEX product_category_createdAt ON product (category, created_at);

-- 2. 상품명 검색 최적화 (prefix 검색)
CREATE INDEX product_name ON product (name);

-- 3. 가격 범위 검색 최적화
CREATE INDEX product_price ON product (price);

-- 4. 전체 정렬 최적화 (정렬 전용)
CREATE INDEX product_createdAt ON product (created_at DESC);
```

## 인덱스별 적용 효과

### 1. product_category_createdAt (category, created_at)
**적용 쿼리:**
- 카테고리 + 기타 조건 검색 쿼리 (1, 2, 3, 4번)

**효과:**
- 카테고리 필터링과 동시에 created_at 정렬을 인덱스로 처리
- filesort 작업 제거로 정렬 성능 대폭 개선
- 카테고리별 최신 상품 조회 시 최적 성능

### 2. product_name (name)
**적용 쿼리:**
- 상품명 포함 검색 쿼리 (5, 6번)

**효과:**
- LIKE '이케아%' 형태의 prefix 검색 최적화
- 상품명 검색 속도 향상

### 3. product_price (price)
**적용 쿼리:**
- 가격 범위 검색 쿼리 (7번)

**효과:**
- BETWEEN 조건의 범위 검색 최적화
- 가격대별 상품 필터링 성능 개선

### 4. product_createdAt (created_at DESC)
**적용 쿼리:**
- 전체 상품 최신순 조회 (8번)

**효과:**
- ORDER BY created_at DESC 처리 최적화
- 메인 페이지 최신 상품 목록 로딩 성능 개선

## 결론

동적 쿼리의 다양한 조건 조합을 효율적으로 처리하기 위해 4개의 전략적 인덱스를 설계했습니다. 특히 `product_category_createdAt` 인덱스는 범위 조건으로 인한 정렬 성능 저하 문제를 해결하는 핵심 역할을 했습니다. 

이러한 인덱스 설계를 통해 200만 건 규모의 데이터에서도 안정적인 검색 성능을 확보했으며, 향후 데이터 증가에도 확장 가능한 구조를 구축했습니다.
