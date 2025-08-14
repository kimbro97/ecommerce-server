package com.server.ecommerce.infras.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.ecommerce.domain.user.RefreshToken;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
	void deleteByRefreshToken(String refreshToken);

	Optional<RefreshToken> findByUserId(Long userId);
}
