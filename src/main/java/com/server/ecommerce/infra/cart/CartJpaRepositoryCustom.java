package com.server.ecommerce.infra.cart;

import java.util.List;
import java.util.Optional;

import com.server.ecommerce.domain.cart.Cart;
import com.server.ecommerce.domain.cart.dto.CartWithProductDto;

public interface CartJpaRepositoryCustom {
	List<CartWithProductDto> findCartListWithProductByUserId(Long userId);
	Optional<Cart> findByUserIdAndIdWithLock(Long userId, Long cartId);
	Optional<Cart> findByUserIdAndProductIdWithLock(Long userId, Long productId);
}
