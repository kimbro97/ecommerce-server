package com.server.ecommerce.support.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.ecommerce.domain.user.User;
import com.server.ecommerce.service.user.CustomUserDetails;
import com.server.ecommerce.support.ApiResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String authorization = request.getHeader("Authorization");
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태코드

			ApiResponse<String> body = new ApiResponse<>(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "토큰이 없거나 만료되었습니다", null);
			ObjectMapper objectMapper = new ObjectMapper();

			String json = objectMapper.writeValueAsString(body);
			response.getWriter().write(json);
			response.getWriter().flush();
			return;
		}

		String token = authorization.split(" ")[1];

		if (jwtUtil.isExpired(token)) {
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태코드

			ApiResponse<String> body = new ApiResponse<>(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "토큰이 없거나 만료되었습니다", null);
			ObjectMapper objectMapper = new ObjectMapper();

			String json = objectMapper.writeValueAsString(body);
			response.getWriter().write(json);
			response.getWriter().flush();
			return;
		}

		Long userId = jwtUtil.getUserId(token);

		User user = new User(userId);

		CustomUserDetails customUserDetails = new CustomUserDetails(user);

		Authentication authToken = new UsernamePasswordAuthenticationToken(
			customUserDetails, null, customUserDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return path.equals("/login") || path.equals("/") || path.equals("/users") || path.equals("/refresh_token")
			|| path.equals("/products") || path.startsWith("/products/");
	}

}
