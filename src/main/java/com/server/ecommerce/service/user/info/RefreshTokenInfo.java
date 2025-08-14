package com.server.ecommerce.service.user.info;

import java.time.LocalDateTime;

import com.server.ecommerce.domain.user.RefreshToken;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RefreshTokenInfo {

	private Long userId;
	private String refreshToken;
	private LocalDateTime expiredAt;

	@Builder
	private RefreshTokenInfo(Long userId, String refreshToken, LocalDateTime expiredAt) {
		this.userId = userId;
		this.refreshToken = refreshToken;
		this.expiredAt = expiredAt;
	}

	public static RefreshTokenInfo from(RefreshToken refreshToken) {
		return RefreshTokenInfo.builder()
			.userId(refreshToken.getUserId())
			.refreshToken(refreshToken.getRefreshToken())
			.expiredAt(refreshToken.getExpiredAt())
			.build();
	}
}
