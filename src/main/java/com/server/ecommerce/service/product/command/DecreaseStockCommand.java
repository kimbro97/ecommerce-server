package com.server.ecommerce.service.product.command;

import java.util.List;

import com.server.ecommerce.service.order.OrderProduct;

import lombok.Getter;

@Getter
public class DecreaseStockCommand {

	private List<OrderProduct> products;

	public DecreaseStockCommand(List<OrderProduct> products) {
		this.products = products;
	}
}
