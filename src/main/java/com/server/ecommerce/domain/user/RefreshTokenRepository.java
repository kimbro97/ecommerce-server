package com.server.ecommerce.domain.user;

import java.util.Optional;

public interface RefreshTokenRepository {
	RefreshToken save(RefreshToken refreshToken);
	Optional<RefreshToken> findByRefreshTokenId(Long refreshTokenId);
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
	Optional<RefreshToken> findByUserId(Long userId);
	void deleteByRefreshToken(String refreshToken);
}
