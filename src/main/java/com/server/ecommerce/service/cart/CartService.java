package com.server.ecommerce.service.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.cart.Cart;
import com.server.ecommerce.domain.cart.dto.CartWithProductDto;
import com.server.ecommerce.infra.cart.CartJpaRepository;
import com.server.ecommerce.service.cart.command.AddCartCommand;
import com.server.ecommerce.service.cart.info.CartInfo;
import com.server.ecommerce.service.cart.info.CartWithProductInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

	private final CartJpaRepository cartJpaRepository;

	@Transactional
	public CartInfo addCart(AddCartCommand command) {

		Optional<Cart> existCart = cartJpaRepository.findByUserIdAndProductId(command.getUserId(),
			command.getProductId());

		if (existCart.isPresent()) {
			Cart cart = existCart.get();
			cart.increaseQuantity(command.getQuantity());
			cartJpaRepository.save(cart);
			return CartInfo.from(cart);
		}

		Cart newCart = Cart.create(command.getUserId(), command.getProductId(), command.getQuantity());
		cartJpaRepository.save(newCart);
		return CartInfo.from(newCart) ;
	}

	public List<CartWithProductInfo> findCarts(Long userId) {
		List<CartWithProductDto> carts = cartJpaRepository.findCartListWithProductByUserId(
			userId);
		return carts.stream()
			.map(CartWithProductInfo::from)
			.toList();
	}

}
