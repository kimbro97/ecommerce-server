package com.server.ecommerce.domain.payment.client;

public interface PaymentClient {
    PaymentResponse processPayment(PaymentRequest request);
}
