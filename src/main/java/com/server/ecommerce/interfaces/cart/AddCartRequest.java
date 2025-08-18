package com.server.ecommerce.interfaces.cart;

import com.server.ecommerce.service.cart.command.AddCartCommand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddCartRequest {
	private Long productId;
	private Integer quantity;

	public AddCartCommand toCommand(Long userId) {
		return new AddCartCommand(userId, productId, quantity);
	}
}