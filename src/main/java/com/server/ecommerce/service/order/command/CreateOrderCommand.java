package com.server.ecommerce.service.order.command;

import java.util.List;

import com.server.ecommerce.service.order.OrderProducts;

import lombok.Getter;

@Getter
public class CreateOrderCommand {

	private Long userId;
	private List<OrderProducts> products;

	public CreateOrderCommand(Long userId, List<OrderProducts> products) {
		this.userId = userId;
		this.products = products;
	}
}
