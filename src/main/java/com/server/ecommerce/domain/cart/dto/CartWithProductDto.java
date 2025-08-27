package com.server.ecommerce.domain.cart.dto;

import java.math.BigDecimal;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class CartWithProductDto {

    private final Long cartId;
    private final Long userId;
    private final Long productId;
    private final Integer quantity;
    private final String productName;
    private final BigDecimal productPrice;
    private final boolean isSoldOut;

	@QueryProjection
    public CartWithProductDto(Long cartId, Long userId, Long productId, Integer quantity, String productName, BigDecimal productPrice,  boolean isSoldOut) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productPrice = productPrice;
		this.isSoldOut = isSoldOut;
    }
}
