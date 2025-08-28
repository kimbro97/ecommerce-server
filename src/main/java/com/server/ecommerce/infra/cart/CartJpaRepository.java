package com.server.ecommerce.infra.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.ecommerce.domain.cart.Cart;

public interface CartJpaRepository extends JpaRepository<Cart, Long>, CartJpaRepositoryCustom {
	Optional<Cart> findByProductId(Long productId);
	Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);
	Optional<Cart> findByUserIdAndId(Long userId, Long cartId);
	void deleteByUserIdAndId(Long userId, Long cartId);
}
