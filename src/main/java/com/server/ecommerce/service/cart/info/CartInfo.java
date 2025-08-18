package com.server.ecommerce.service.cart.info;

import java.time.LocalDateTime;

import com.server.ecommerce.domain.cart.Cart;
import com.server.ecommerce.service.product.info.ProductInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartInfo {
	private final Long cartId;
	private final Long userId;
	private final Long productId;
	private final Integer quantity;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;
	private final ProductInfo product;

	public static CartInfo from(Cart cart) {
		return new CartInfo(
			cart.getId(),
			cart.getUserId(),
			cart.getProductId(),
			cart.getQuantity(),
			cart.getCreatedAt(),
			cart.getUpdatedAt(),
			null
		);
	}

	public static CartInfo from(Cart cart, ProductInfo productInfo) {
		return new CartInfo(
			cart.getId(),
			cart.getUserId(),
			cart.getProductId(),
			cart.getQuantity(),
			cart.getCreatedAt(),
			cart.getUpdatedAt(),
			productInfo
		);
	}
}