package com.server.ecommerce.service.cart.info;

import com.server.ecommerce.domain.cart.Cart;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CartInfo {
	private Long userId;
	private Long productId;
	private Integer quantity;

	@Builder
	private CartInfo(Long userId, Long productId, Integer quantity) {
		this.userId = userId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static CartInfo from(Cart cart) {
		return CartInfo.builder()
			.userId(cart.getUserId())
			.productId(cart.getProductId())
			.quantity(cart.getQuantity())
			.build();
	}
}
