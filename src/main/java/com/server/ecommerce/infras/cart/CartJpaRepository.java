package com.server.ecommerce.infras.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.ecommerce.domain.cart.Cart;

public interface CartJpaRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);
	List<Cart> findByUserId(Long userId);
}