package com.server.ecommerce.interfaces.payment;

import com.server.ecommerce.domain.payment.PaymentMethod;
import com.server.ecommerce.service.payment.command.ProcessPaymentCommand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentRequest {
    private PaymentMethod paymentMethod;

    public ProcessPaymentCommand toCommand(Long userId, Long orderId) {
        return new ProcessPaymentCommand(userId, orderId, paymentMethod);
    }
}