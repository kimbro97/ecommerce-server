package com.server.ecommerce.interfaces.user;

import static com.server.ecommerce.support.exception.ValidationError.*;

import com.server.ecommerce.service.user.command.UserJoinCommand;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateUserRequest {
	private String email;
	private String password;
	private String confirmPassword;
	private String name;

	public CreateUserRequest(String email, String password, String confirmPassword, String name) {
		this.email = email;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.name = name;
	}

	public UserJoinCommand toCommand() {
		if (this.email.isEmpty() || this.email.isBlank()) {
			throw USER_JOIN_EMAIL_REQUIRED.exception();
		}
		if (this.password.isEmpty() || this.password.isBlank()) {
			throw USER_JOIN_PASSWORD_REQUIRED.exception();
		}
		if (this.confirmPassword.isEmpty() || this.confirmPassword.isBlank()) {
			throw USER_JOIN_CONFIRM_PASSWORD_REQUIRED.exception();
		}
		if (!this.password.equals(this.confirmPassword)) {
			throw USER_JOIN_PASSWORD_CONFIRM_PASSWORD_NOT_MATCH.exception();
		}
		if (this.name.isEmpty() || this.name.isBlank()) {
			throw USER_JOIN_NAME_REQUIRED.exception();
		}
		return new UserJoinCommand(this.email, this.name, this.password);
	}
}
