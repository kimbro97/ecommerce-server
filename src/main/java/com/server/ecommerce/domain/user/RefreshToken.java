package com.server.ecommerce.domain.user;

import java.time.LocalDateTime;

import com.server.ecommerce.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseEntity {

	@Id
	@Column(name = "refresh_token_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;
	private String refreshToken;
	private LocalDateTime expiredAt;

	public RefreshToken(Long userId, String refreshToken, LocalDateTime expiredAt) {
		this.userId = userId;
		this.refreshToken = refreshToken;
		this.expiredAt = expiredAt;
	}

	public static RefreshToken create(Long userId, String refreshToken, LocalDateTime expiredAt) {
		return new RefreshToken(userId, refreshToken, expiredAt);
	}
}
