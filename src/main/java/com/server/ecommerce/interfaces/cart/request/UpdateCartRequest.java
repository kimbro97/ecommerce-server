package com.server.ecommerce.interfaces.cart.request;

import static com.server.ecommerce.support.exception.ValidationError.*;

import com.server.ecommerce.service.cart.command.UpdateCartCommand;

import lombok.Getter;

@Getter
public class UpdateCartRequest {

	private Long userId;
	private Integer quantity;

	public UpdateCartRequest(Long userId, Integer quantity) {
		this.userId = userId;
		this.quantity = quantity;
	}

	public UpdateCartCommand toCommand(Long cartId) {

		validate(cartId);

		return new UpdateCartCommand(userId, cartId, quantity);
	}

	private void validate(Long cartId) {
		if (userId == null) {
			throw UPDATE_CART_USER_ID_REQUIRED.exception();
		}

		if (cartId == null) {
			throw UPDATE_CART_CART_ID_REQUIRED.exception();
		}

		if (quantity == null) {
			throw UPDATE_CART_QUANTITY_REQUIRED.exception();
		}

		if (quantity <= 1) {
			throw UPDATE_CART_QUANTITY_INVALID.exception();
		}
	}
}
