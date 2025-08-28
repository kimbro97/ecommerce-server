package com.server.ecommerce.service.order;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class OrderProduct {
	private Long productId;
	private Integer quantity;
	private BigDecimal price;

	public OrderProduct(Long productId, Integer quantity, BigDecimal price) {
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
	}
}
