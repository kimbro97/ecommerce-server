package com.server.ecommerce.service.user;

import static com.server.ecommerce.support.exception.BusinessError.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.server.ecommerce.domain.user.User;
import com.server.ecommerce.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(email).orElseThrow(USER_NOT_FOUND::exception);

		return new CustomUserDetails(user);
	}
}
