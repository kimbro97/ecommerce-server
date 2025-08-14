package com.server.ecommerce.service.user;

import static com.server.ecommerce.support.exception.ValidationError.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.user.RefreshToken;
import com.server.ecommerce.domain.user.RefreshTokenRepository;
import com.server.ecommerce.service.user.command.CreateRefreshTokeCommand;
import com.server.ecommerce.service.user.info.RefreshTokenInfo;
import com.server.ecommerce.support.exception.ValidationError;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public RefreshTokenInfo createRefreshToken(CreateRefreshTokeCommand command) {

		refreshTokenRepository.findByUserId(command.getUserId())
			.ifPresent(refreshToken -> {
				refreshTokenRepository.deleteByRefreshToken(refreshToken.getRefreshToken());
			});

		RefreshToken refreshToken = RefreshToken.create(command.getUserId(), command.getRefreshToken(),
			command.getExpiredAt());

		refreshTokenRepository.save(refreshToken);

		return RefreshTokenInfo.from(refreshToken);
	}

	public RefreshTokenInfo findByRefreshToken(String value) {
		RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(value)
			.orElseThrow(REFRESH_TOKEN_NOT_FOUND::exception);
		return RefreshTokenInfo.from(refreshToken);
	}
}
