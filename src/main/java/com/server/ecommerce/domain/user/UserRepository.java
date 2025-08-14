package com.server.ecommerce.domain.user;

import java.util.Optional;

public interface UserRepository {
	User save(User user);

	Optional<User> findByUserId(Long userId);

	Optional<User> findByEmail(String email);
}
