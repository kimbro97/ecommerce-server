package com.server.ecommerce.service.cart.command;

import java.util.List;

import lombok.Getter;

@Getter
public class ClearCartCommand {

	private Long userId;
	private List<Long> cartIds;

	public ClearCartCommand(Long userId, List<Long> cartIds) {
		this.userId = userId;
		this.cartIds = cartIds;
	}
}
