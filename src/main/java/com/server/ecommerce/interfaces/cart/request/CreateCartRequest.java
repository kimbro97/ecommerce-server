package com.server.ecommerce.interfaces.cart.request;

import static com.server.ecommerce.support.exception.ValidationError.*;

import com.server.ecommerce.service.cart.command.AddCartCommand;

import lombok.Getter;

@Getter
public class CreateCartRequest {

	private Long userId;
	private Long productId;
	private Integer quantity;

	public CreateCartRequest(Long userId, Long productId, Integer quantity) {
		this.userId = userId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public AddCartCommand toCommand() {

		validate();

		return AddCartCommand.builder()
			.userId(userId)
			.productId(productId)
			.quantity(quantity)
			.build();
	}

	private void validate() {
		if (userId == null) {
			throw ADD_CART_USER_ID_REQUIRED.exception();
		}

		if (productId == null) {
			throw ADD_CART_PRODUCT_ID_REQUIRED.exception();
		}

		if (quantity == null) {
			throw ADD_CART_QUANTITY_REQUIRED.exception();
		}

		if (quantity <= 0) {
			throw ADD_CART_QUANTITY_INVALID.exception();
		}
	}
}
