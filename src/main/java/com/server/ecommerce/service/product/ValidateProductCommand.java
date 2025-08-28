package com.server.ecommerce.service.product;

import java.util.List;

import lombok.Getter;

@Getter
public class ValidateProductCommand {

	private List<Long> productIds;

	public ValidateProductCommand(List<Long> productIds) {
		this.productIds = productIds;
	}
}
