package com.server.ecommerce.domain.cart;

import java.util.List;
import java.util.Optional;

public interface CartRepository {
	Cart save(Cart cart);
	Optional<Cart> findById(Long cartId);
	Optional<Cart> findByProductId(Long productId);
	Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);
	List<Cart> findAllByUserId(Long userId);
}
