package com.server.ecommerce.infra.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.ecommerce.domain.cart.Cart;

public interface CartJpaRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByProductId(Long productId);
	List<Cart> findAllByUserId(Long userId);
	Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);
}
