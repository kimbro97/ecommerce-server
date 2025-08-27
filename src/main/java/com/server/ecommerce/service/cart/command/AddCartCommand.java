package com.server.ecommerce.service.cart.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AddCartCommand {
	private Long userId;
	private Long productId;
	private Integer quantity;

	@Builder
	private AddCartCommand(Long userId, Long productId, Integer quantity) {
		this.userId = userId;
		this.productId = productId;
		this.quantity = quantity;
	}

}
