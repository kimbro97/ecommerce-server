package com.server.ecommerce.interfaces.cart.request;

import lombok.Getter;

@Getter
public class findCartsRequest {

	private Long userId;

	public findCartsRequest(Long userId) {
		this.userId = userId;
	}
}
