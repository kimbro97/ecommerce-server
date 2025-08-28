package com.server.ecommerce.service.cart.info;

import java.math.BigDecimal;

import com.server.ecommerce.domain.cart.dto.CartWithProductDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CartWithProductInfo {

	private final Long cartId;
	private final Long userId;
	private final Long productId;
	private final Integer quantity;
	private final String productName;
	private final BigDecimal productPrice;
	private final boolean isSoldOut;

	@Builder
	public CartWithProductInfo(Long cartId, Long userId, Long productId, Integer quantity, String productName, BigDecimal productPrice,  boolean isSoldOut) {
		this.cartId = cartId;
		this.userId = userId;
		this.productId = productId;
		this.quantity = quantity;
		this.productName = productName;
		this.productPrice = productPrice;
		this.isSoldOut = isSoldOut;
	}

	public static CartWithProductInfo from(CartWithProductDto cartWithProductDto) {
		return CartWithProductInfo.builder()
			.cartId(cartWithProductDto.getCartId())
			.userId(cartWithProductDto.getUserId())
			.productId(cartWithProductDto.getProductId())
			.quantity(cartWithProductDto.getQuantity())
			.productName(cartWithProductDto.getProductName())
			.productPrice(cartWithProductDto.getProductPrice())
			.isSoldOut(cartWithProductDto.isSoldOut())
			.build();
	}
}
