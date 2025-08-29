package com.server.ecommerce.domain.payment.client;

public record PaymentResponse(
    String status,
    String transactionId,
    String message
) {
}
