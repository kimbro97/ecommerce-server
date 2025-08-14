package com.server.ecommerce.service.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.server.ecommerce.domain.user.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

	private final User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	public Long getUserId() {
		return user.getId();
	}

	public String getEmail() {
		return user.getEmail();
	}

	@Override
	public String getUsername() {
		return this.getEmail();
	}
}
