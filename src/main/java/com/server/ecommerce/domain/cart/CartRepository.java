package com.server.ecommerce.domain.cart;

import java.util.List;
import java.util.Optional;

import com.server.ecommerce.domain.cart.dto.CartWithProductDto;

public interface CartRepository {
	Cart save(Cart cart);
	Optional<Cart> findById(Long cartId);
	Optional<Cart> findByProductId(Long productId);
	Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);
	List<CartWithProductDto> findAllByUserId(Long userId);
	Optional<Cart> findByUserIdAndCartId(Long userId, Long cartId);
	void deleteByUserIdAndCartId(Long userId, Long cartId);
}
