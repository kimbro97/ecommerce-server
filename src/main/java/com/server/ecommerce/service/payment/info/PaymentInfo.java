package com.server.ecommerce.service.payment.info;

import java.math.BigDecimal;

import com.server.ecommerce.domain.payment.Payment;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentInfo {

	private Long paymentId;
	private Long orderId;
	private Long userId;
	private BigDecimal payAmount;

	@Builder
	public PaymentInfo(Long paymentId, Long orderId, Long userId, BigDecimal payAmount) {
		this.paymentId = paymentId;
		this.orderId = orderId;
		this.userId = userId;
		this.payAmount = payAmount;
	}

	public static PaymentInfo from(Payment payment) {

		return PaymentInfo.builder()
			.paymentId(payment.getId())
			.orderId(payment.getOrderId())
			.userId(payment.getUserId())
			.payAmount(payment.getPayAmount())
			.build();
	}
}
