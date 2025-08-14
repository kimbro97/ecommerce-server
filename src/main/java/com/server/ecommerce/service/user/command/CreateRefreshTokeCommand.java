package com.server.ecommerce.service.user.command;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateRefreshTokeCommand {

	private Long userId;
	private String refreshToken;
	private LocalDateTime expiredAt;

	@Builder
	public CreateRefreshTokeCommand(Long userId, String refreshToken, LocalDateTime expiredAt) {
		this.userId = userId;
		this.refreshToken = refreshToken;
		this.expiredAt = expiredAt;
	}
}
