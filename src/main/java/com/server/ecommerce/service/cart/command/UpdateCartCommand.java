package com.server.ecommerce.service.cart.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateCartCommand {
	private final Long cartId;
	private final Integer quantity;
}