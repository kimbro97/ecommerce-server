package com.server.ecommerce.facade.order;

import lombok.Getter;

@Getter
public class OrderCriteria {
	private Long userId;

	public OrderCriteria(Long userId) {
		this.userId = userId;
	}
}
