package com.server.ecommerce.domain.payment;

public enum PaymentMethod {
    POINT,      // 포인트 결제
    CARD,       // 카드 결제 (향후 확장)
    BANK        // 계좌이체 (향후 확장)
}