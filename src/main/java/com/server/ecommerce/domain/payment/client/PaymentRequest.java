package com.server.ecommerce.domain.payment.client;

import java.math.BigDecimal;

public record PaymentRequest(
    String orderId,
    BigDecimal amount
) {
}
