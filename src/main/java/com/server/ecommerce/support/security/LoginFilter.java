package com.server.ecommerce.support.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.ecommerce.service.user.CustomUserDetails;
import com.server.ecommerce.service.user.RefreshTokenService;
import com.server.ecommerce.service.user.command.CreateRefreshTokeCommand;
import com.server.ecommerce.support.ApiResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	private final JWTUtil jwtUtil;
	private final RefreshTokenService refreshTokenService;
	private final AuthenticationManager authenticationManager;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		String email = obtainEmail(request);
		String password = obtainPassword(request);

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

		return authenticationManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws
		IOException {

		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		Long userId = customUserDetails.getUserId();

		String accessToken = jwtUtil.createJwt("access", userId, 600000L);
		String refreshToken = jwtUtil.createJwt("refresh", userId, 86400000L);

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_CREATED); // 201 상태코드
		response.setHeader("accessToken", accessToken);
		response.addCookie(createCookie("refreshToken", refreshToken));

		refreshTokenService.createRefreshToken(
			new CreateRefreshTokeCommand(userId, refreshToken, LocalDateTime.now().plusDays(1)));

		ApiResponse<String> body = new ApiResponse<>(HttpStatus.CREATED, HttpStatus.CREATED.value(), "로그인 되었습니다", null);
		ObjectMapper objectMapper = new ObjectMapper();

		String json = objectMapper.writeValueAsString(body);
		response.getWriter().write(json);
		response.getWriter().flush();
		log.info("successful authentication");
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws
		IOException {

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태코드

		ApiResponse<String> body = new ApiResponse<>(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), failed.getMessage(), null);
		ObjectMapper objectMapper = new ObjectMapper();

		String json = objectMapper.writeValueAsString(body);
		response.getWriter().write(json);
		response.getWriter().flush();
		log.info("unsuccessfulAuthentication");
	}

	private String obtainEmail(HttpServletRequest request) {
		return request.getParameter("email");
	}

	private Cookie createCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(24 * 60 * 60);
		cookie.setHttpOnly(true);
		return cookie;
	}
}
