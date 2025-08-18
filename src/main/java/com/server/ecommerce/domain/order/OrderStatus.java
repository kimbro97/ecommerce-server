package com.server.ecommerce.domain.order;

public enum OrderStatus {
    PENDING,    // 주문 대기
    PAID,       // 결제 완료
    COMPLETED,  // 주문 완료
    CANCELLED   // 주문 취소
}
