package com.server.ecommerce.service.user.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserJoinCommand {
	private final String email;
	private final String name;
	private final String password;

	@Builder
	public UserJoinCommand(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}
}
