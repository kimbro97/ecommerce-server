package com.server.ecommerce.infras.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.ecommerce.domain.user.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
