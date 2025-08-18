package com.server.ecommerce.interfaces.cart;

import com.server.ecommerce.service.cart.command.UpdateCartCommand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartRequest {
	private Integer quantity;

	public UpdateCartCommand toCommand(Long cartId) {
		return new UpdateCartCommand(cartId, quantity);
	}
}