package com.server.ecommerce.domain.payment.client;

import java.math.BigDecimal;

public record PaymentRequest(
    Long orderId,
    BigDecimal amount
) {
}
