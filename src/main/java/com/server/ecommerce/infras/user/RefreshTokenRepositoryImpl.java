package com.server.ecommerce.infras.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.server.ecommerce.domain.user.RefreshToken;
import com.server.ecommerce.domain.user.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

	private final RefreshTokenJpaRepository refreshTokenJpaRepository;

	@Override
	public RefreshToken save(RefreshToken refreshToken) {
		return refreshTokenJpaRepository.save(refreshToken);
	}

	@Override
	public Optional<RefreshToken> findByRefreshTokenId(Long refreshTokenId) {
		return refreshTokenJpaRepository.findById(refreshTokenId);
	}

	@Override
	public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
		return refreshTokenJpaRepository.findByRefreshToken(refreshToken);
	}

	@Override
	public Optional<RefreshToken> findByUserId(Long userId) {
		return refreshTokenJpaRepository.findByUserId(userId);
	}

	@Override
	public void deleteByRefreshToken(String refreshToken) {
		refreshTokenJpaRepository.deleteByRefreshToken(refreshToken);
	}
}
