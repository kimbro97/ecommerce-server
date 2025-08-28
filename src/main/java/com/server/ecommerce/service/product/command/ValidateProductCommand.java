package com.server.ecommerce.service.product.command;

import java.util.List;

import lombok.Getter;

@Getter
public class ValidateProductCommand {

	private List<Long> productIds;

	public ValidateProductCommand(List<Long> productIds) {
		this.productIds = productIds;
	}
}
