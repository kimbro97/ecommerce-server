package com.server.ecommerce.interfaces.cart.request;

import static com.server.ecommerce.support.exception.ValidationError.*;

import com.server.ecommerce.service.cart.command.DeleteCartCommand;

import lombok.Getter;

@Getter
public class DeleteCartRequest {

	private Long userId;

	public DeleteCartRequest(Long userId) {
		this.userId = userId;
	}

	public DeleteCartCommand toCommand(Long cartId) {

		validate(cartId);

		return new DeleteCartCommand(userId, cartId);
	}

	private void validate(Long cartId) {
		if (userId == null) {
			throw DELETE_CART_USER_ID_REQUIRED.exception();
		}

		if (cartId == null) {
			throw DELETE_CART_CART_ID_REQUIRED.exception();
		}
	}
}
