package com.server.ecommerce.service.payment.command;

import com.server.ecommerce.domain.payment.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProcessPaymentCommand {
    private final Long userId;
    private final Long orderId;
    private final PaymentMethod paymentMethod;
}