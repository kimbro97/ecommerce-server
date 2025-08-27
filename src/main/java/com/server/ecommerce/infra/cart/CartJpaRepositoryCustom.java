package com.server.ecommerce.infra.cart;

import java.util.List;

import com.server.ecommerce.domain.cart.dto.CartWithProductDto;

public interface CartJpaRepositoryCustom {
	List<CartWithProductDto> findCartListWithProductByUserId(Long userId);
}
