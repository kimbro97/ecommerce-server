package com.server.ecommerce.interfaces.order;

import com.server.ecommerce.facade.order.OrderCriteria;

import lombok.Getter;

@Getter
public class CreateOrderRequest {
	private Long userId;

	public CreateOrderRequest(Long userId) {
		this.userId = userId;
	}

	public OrderCriteria toCriteria() {
		return  new OrderCriteria(userId);
	}
}
