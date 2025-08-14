package com.server.ecommerce.support.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.server.ecommerce.service.user.RefreshTokenService;
import com.server.ecommerce.support.security.JWTFilter;
import com.server.ecommerce.support.security.JWTUtil;
import com.server.ecommerce.support.security.LoginFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JWTUtil jwtUtil;
	private final RefreshTokenService refreshTokenService;
	private final AuthenticationConfiguration authenticationConfiguration;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(AbstractHttpConfigurer::disable);

		http.formLogin(AbstractHttpConfigurer::disable);

		http.httpBasic(AbstractHttpConfigurer::disable);

		http.authorizeHttpRequests(
			auth -> auth
				.requestMatchers("/login", "/users", "/", "/refresh_token").permitAll()
				.requestMatchers("/products", "/products/**").permitAll()
				.anyRequest().authenticated());

		http
			.addFilterAt(new LoginFilter(jwtUtil, refreshTokenService, authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);

		http
			.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

		http.sessionManagement(session -> session
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

}
