package com.server.ecommerce.service.cart.command;

import lombok.Getter;

@Getter
public class UpdateCartCommand {

	private Long userId;
	private Long cartId;
	private Integer quantity;

	public UpdateCartCommand(Long userId, Long cartId, Integer quantity) {
		this.userId = userId;
		this.cartId = cartId;
		this.quantity = quantity;
	}
}
