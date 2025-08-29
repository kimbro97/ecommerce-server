package com.server.ecommerce.service.cart;

import java.util.List;

import lombok.Getter;

@Getter
public class ClearCartCommand {

	private List<Long> cartIds;

	public ClearCartCommand(List<Long> cartIds) {
		this.cartIds = cartIds;
	}
}
