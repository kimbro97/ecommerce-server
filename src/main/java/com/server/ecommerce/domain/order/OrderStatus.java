package com.server.ecommerce.domain.order;

import lombok.Getter;

@Getter
public enum OrderStatus {

	PENDING("결제대기");

	private final String description;

	OrderStatus(String description) {
		this.description = description;
	}
}
