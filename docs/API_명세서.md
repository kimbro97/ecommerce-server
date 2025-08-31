# 올라 마켓 (Allra Market) API 명세서

## 개요

이 문서는 올라 마켓 e-commerce 백엔드 API의 상세 명세를 제공합니다.

**Base URL**: `http://localhost:8080/api/v1`

**공통 응답 형식**: 모든 API는 `ApiResponse<T>` 형식으로 응답합니다.

```json
{
  "httpStatus": "OK",
  "httpStatusCode": 200,
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    // 실제 데이터
  }
}
```

---

## 상품 (Product) API

### 1. 상품 검색

**Endpoint**: `GET /api/v1/products`

**설명**: 다양한 조건으로 상품을 검색할 수 있습니다. 카테고리, 키워드, 가격 범위 등의 필터를 지원합니다.

**Query Parameters**:

| 매개변수 | 타입 | 필수 | 기본값 | 설명 |
|---------|------|------|--------|------|
| category | String | No | - | 상품 카테고리 (ELECTRONICS, CLOTHING, BOOKS, HOME, SPORTS, FOOD) |
| keyword | String | No | - | 상품명 검색 키워드 (prefix 매칭) |
| minPrice | Integer | No | - | 최소 가격 (maxPrice와 함께 사용) |
| maxPrice | Integer | No | - | 최대 가격 (minPrice와 함께 사용) |
| page | Integer | No | 0 | 페이지 번호 (0부터 시작) |
| size | Integer | No | 10 | 페이지 크기 (최소 2) |

**Request Example**:
```http
GET /api/v1/products?category=ELECTRONICS&keyword=스마트폰&minPrice=100000&maxPrice=500000&page=0&size=10
```

**Response Example**:
```json
{
  "httpStatus": "OK",
  "httpStatusCode": 200,
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "content": [
      {
        "productId": 1,
        "productName": "스마트폰 Galaxy S24",
        "price": 299000,
        "description": "최신 갤럭시 스마트폰",
        "category": "전자제품",
        "isSoldOut": false,
        "createdAt": "2024-01-15T10:30:00"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10
    },
    "totalElements": 25,
    "totalPages": 3,
    "first": true,
    "last": false
  }
}
```

**Error Cases**:
- `400 Bad Request`: 잘못된 검색 조건
- `400 Bad Request`: 잘못된 카테고리

---

## 장바구니 (Cart) API

### 1. 장바구니 상품 추가

**Endpoint**: `POST /api/v1/carts`

**설명**: 사용자의 장바구니에 상품을 추가합니다. Redis 캐시를 활용하여 성능을 최적화했습니다.

**Request Body**:
```json
{
  "userId": 1,
  "productId": 10,
  "quantity": 2
}
```

**Request Parameters**:

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| userId | Long | Yes | 사용자 ID |
| productId | Long | Yes | 상품 ID |
| quantity | Integer | Yes | 수량 (1 이상) |

**Response Example**:
```json
{
  "httpStatus": "CREATED",
  "httpStatusCode": 201,
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "userId": 1,
    "productId": 10,
    "quantity": 2
  }
}
```

### 2. 장바구니 조회

**Endpoint**: `GET /api/v1/carts`

**설명**: 사용자의 장바구니 목록을 조회합니다.

**Request Body**:
```json
{
  "userId": 1
}
```

**Response Example**:
```json
{
  "httpStatus": "OK",
  "httpStatusCode": 200,
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": [
    {
      "cartId": 1,
      "userId": 1,
      "productId": 10,
      "quantity": 2,
      "productName": "스마트폰 Galaxy S24",
      "productPrice": 299000,
      "isSoldOut": false
    }
  ]
}
```

### 3. 장바구니 상품 수량 수정

**Endpoint**: `PATCH /api/v1/carts/{cartId}`

**설명**: 장바구니에 있는 특정 상품의 수량을 수정합니다. 동시성 제어를 위해 비관적 락을 사용합니다.

**Path Parameters**:

| 매개변수 | 타입 | 설명 |
|---------|------|------|
| cartId | Long | 수정할 장바구니 항목 ID |

**Request Body**:
```json
{
  "userId": 1,
  "quantity": 5
}
```

**Response Example**:
```json
{
  "httpStatus": "OK",
  "httpStatusCode": 200,
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "userId": 1,
    "productId": 10,
    "quantity": 5
  }
}
```

### 4. 장바구니 상품 삭제

**Endpoint**: `DELETE /api/v1/carts/{cartId}`

**설명**: 장바구니에서 특정 상품을 삭제합니다. 캐시도 함께 갱신됩니다.

**Path Parameters**:

| 매개변수 | 타입 | 설명 |
|---------|------|------|
| cartId | Long | 삭제할 장바구니 항목 ID |

**Request Body**:
```json
{
  "userId": 1
}
```

**Response Example**:
```json
{
  "httpStatus": "OK",
  "httpStatusCode": 200,
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "userId": 1,
    "productId": 10,
    "quantity": 0
  }
}
```

---

## 주문 (Order) API

### 1. 주문 생성

**Endpoint**: `POST /api/v1/orders`

**설명**: 사용자의 장바구니 상품들로 주문을 생성하고 결제를 처리합니다. Facade 패턴을 사용하여 복잡한 비즈니스 로직을 관리합니다.

**Request Body**:
```json
{
  "userId": 1
}
```

**Response Example**:
```json
{
  "httpStatus": "CREATED",
  "httpStatusCode": 201,
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "orderNo": "ORD-2024011501",
    "orderId": 1,
    "paymentId": 1,
    "status": "COMPLETED",
    "totalPrice": 388000,
    "payAmount": 388000,
    "createdAt": "2024-01-15T14:30:00"
  }
}
```

---

## 상품 카테고리 목록

| 영문코드 | 한글명 |
|----------|--------|
| ELECTRONICS | 전자제품 |
| CLOTHING | 의류 |
| BOOKS | 도서 |
| HOME | 홈&가든 |
| SPORTS | 스포츠 |
| FOOD | 식품 |

---
