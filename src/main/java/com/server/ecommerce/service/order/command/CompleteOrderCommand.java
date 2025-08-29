package com.server.ecommerce.service.order.command;

import lombok.Getter;

@Getter
public class CompleteOrderCommand {
	private Long orderId;

	public CompleteOrderCommand(Long orderId) {
		this.orderId = orderId;
	}
}
