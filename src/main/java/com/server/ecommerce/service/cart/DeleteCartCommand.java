package com.server.ecommerce.service.cart;

import lombok.Getter;

@Getter
public class DeleteCartCommand {

	private Long userId;
	private Long cartId;

	public DeleteCartCommand(Long userId, Long cartId) {
		this.userId = userId;
		this.cartId = cartId;
	}
}
