package com.server.ecommerce.interfaces.user;

import static com.server.ecommerce.support.exception.ValidationError.*;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.ecommerce.service.user.RefreshTokenService;
import com.server.ecommerce.service.user.command.CreateRefreshTokeCommand;
import com.server.ecommerce.service.user.info.RefreshTokenInfo;
import com.server.ecommerce.support.ApiResponse;
import com.server.ecommerce.support.exception.ValidationError;
import com.server.ecommerce.support.security.JWTUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

	private final JWTUtil jwtUtil;
	private final RefreshTokenService refreshTokenService;

	@PostMapping("/refresh_token")
	public ResponseEntity<ApiResponse<Void>> createRefreshToken(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = null;
		Cookie[] cookies = request.getCookies();

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("refreshToken")) {
				refreshToken = cookie.getValue();
			}
		}

		if (refreshToken == null) {
			throw REFRESH_TOKEN_REQUIRED.exception();
		}

		if (jwtUtil.isExpired(refreshToken)) {
			throw REFRESH_TOKEN_EXPIRED.exception();
		}

		if (!jwtUtil.getCategory(refreshToken).equals("refresh")) {
			throw REFRESH_TOKEN_REQUIRED.exception();
		}

		RefreshTokenInfo refreshTokenInfo = refreshTokenService.findByRefreshToken(refreshToken);

		String newAccessToken = jwtUtil.createJwt("accessToken", jwtUtil.getUserId(refreshToken), 600000L);
		String newRefreshToken = jwtUtil.createJwt("refreshToken", jwtUtil.getUserId(refreshToken), 86400000L);

		response.setHeader("accessToken", newAccessToken);
		response.addCookie(createCookie("refreshToken", newRefreshToken));

		refreshTokenService.createRefreshToken(
			new CreateRefreshTokeCommand(refreshTokenInfo.getUserId(), newRefreshToken,
				LocalDateTime.now().plusDays(1)));

		return ApiResponse.CREATE(null);
	}

	private Cookie createCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(24 * 60 * 60);
		cookie.setHttpOnly(true);
		return cookie;
	}
}
