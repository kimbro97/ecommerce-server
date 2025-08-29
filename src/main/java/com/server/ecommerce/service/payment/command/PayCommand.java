package com.server.ecommerce.service.payment.command;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class PayCommand {
	private Long userId;
	private Long orderId;
	private BigDecimal payAmount;

	public PayCommand(Long userId, Long orderId, BigDecimal payAmount) {
		this.userId = userId;
		this.orderId = orderId;
		this.payAmount = payAmount;
	}
}
