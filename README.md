## 올라 마켓 시스템 프로젝트

지원자: 김형재

Spring Boot 기반의 E-commerce 백엔드 시스템으로, 5-Layer 아키텍쳐와 클린 아키텍쳐를 적용하여 구현했습니다.

## 📋 프로젝트 관리
**GitHub Projects**를 통해 개발 프로세스를 관리했습니다.
- [📌 프로젝트 보드](https://github.com/users/kimbro97/projects/7) - 이슈 트래킹 및 진행 상황 관리

## 📋 기술 문서

### 🏗️ 아키텍쳐 및 설계
- [📊 프로젝트 아키텍쳐](docs/프로젝트_아키텍쳐.md) - 5-Layer 아키텍쳐와 Domain Layer 인터페이스 설계
- [🛒 주문 로직 설계](docs/주문로직_설계.md) - Facade 패턴을 활용한 복합 트랜잭션 처리

### ⚡ 성능 최적화
- [📈 인덱스 설계](docs/INDEX_설계.md) - 200만 데이터 기반 동적 쿼리 최적화
- [🚀 Redis 캐시 설계](docs/redis_캐시_설계.md) - 캐시 무효화 전략과 데이터 정합성

### 🔧 개발 가이드
- [📋 API 명세서](docs/API_명세서.md) - REST API 엔드포인트 상세 문서
---

## 시작하기

### 1. 데이터베이스 실행
```shell
docker compose up -d
```
