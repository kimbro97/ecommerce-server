package com.server.ecommerce.service.order;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class OrderProducts {
	private Long productId;
	private Integer quantity;
	private BigDecimal price;
}
