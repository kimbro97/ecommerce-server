package com.server.ecommerce.infras.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.server.ecommerce.domain.user.User;
import com.server.ecommerce.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;

	@Override
	public User save(User user) {
		return userJpaRepository.save(user);
	}

	@Override
	public Optional<User> findByUserId(Long userId) {
		return userJpaRepository.findById(userId);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userJpaRepository.findByEmail(email);
	}
}
