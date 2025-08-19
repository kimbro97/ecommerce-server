package com.server.ecommerce.service.payment.info;

import java.time.LocalDateTime;

import com.server.ecommerce.domain.payment.Payment;
import com.server.ecommerce.domain.payment.PaymentMethod;
import com.server.ecommerce.domain.payment.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentInfo {
    private final Long paymentId;
    private final Long orderId;
    private final Long userId;
    private final Long amount;
    private final PaymentMethod paymentMethod;
    private final PaymentStatus status;
    private final String failureReason;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static PaymentInfo from(Payment payment) {
        return new PaymentInfo(
            payment.getId(),
            payment.getOrderId(),
            payment.getUserId(),
            payment.getAmount(),
            payment.getPaymentMethod(),
            payment.getStatus(),
            payment.getFailureReason(),
            payment.getCreatedAt(),
            payment.getUpdatedAt()
        );
    }
}