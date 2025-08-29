package com.server.ecommerce.service.order.command;

import java.util.List;

import com.server.ecommerce.service.order.OrderProduct;

import lombok.Getter;

@Getter
public class CreateOrderCommand {

	private Long userId;
	private List<OrderProduct> products;

	public CreateOrderCommand(Long userId, List<OrderProduct> products) {
		this.userId = userId;
		this.products = products;
	}
}
