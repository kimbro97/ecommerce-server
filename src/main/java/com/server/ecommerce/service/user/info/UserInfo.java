package com.server.ecommerce.service.user.info;

import java.time.LocalDateTime;

import com.server.ecommerce.domain.user.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfo {
	private final Long userId;
	private final String email;
	private final String name;
	private final LocalDateTime joinedAt;

	@Builder
	private UserInfo(Long userId, String email, String name, LocalDateTime joinedAt) {
		this.userId = userId;
		this.email = email;
		this.name = name;
		this.joinedAt = joinedAt;
	}

	public static UserInfo from(User user) {
		return UserInfo.builder()
			.userId(user.getId())
			.email(user.getEmail())
			.name(user.getName())
			.joinedAt(user.getCreatedAt())
			.build();
	}
}
