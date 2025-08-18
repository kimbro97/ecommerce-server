package com.server.ecommerce.service.cart.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddCartCommand {
	private final Long userId;
	private final Long productId;
	private final Integer quantity;
}