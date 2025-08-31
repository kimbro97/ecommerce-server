package com.server.ecommerce.service.cart;

import static com.server.ecommerce.support.exception.BusinessError.*;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.cart.Cart;
import com.server.ecommerce.domain.cart.CartRepository;
import com.server.ecommerce.domain.cart.dto.CartWithProductDto;
import com.server.ecommerce.service.cart.command.AddCartCommand;
import com.server.ecommerce.service.cart.command.ClearCartCommand;
import com.server.ecommerce.service.cart.command.DeleteCartCommand;
import com.server.ecommerce.service.cart.command.UpdateCartCommand;
import com.server.ecommerce.service.cart.info.CartInfo;
import com.server.ecommerce.service.cart.info.CartWithProductInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

	private final CartRepository cartRepository;

	@Transactional
	@CacheEvict(cacheNames = "findCarts", key = "'carts:user:' + #command.userId")
	public CartInfo addCart(AddCartCommand command) {

		// 유저 유효성 검사

		Optional<Cart> existCart = cartRepository.findByUserIdAndProductIdWithLock(command.getUserId(),
			command.getProductId());

		if (existCart.isPresent()) {
			Cart cart = existCart.get();
			cart.increaseQuantity(command.getQuantity());
			cartRepository.save(cart);
			return CartInfo.from(cart);
		}

		try {
			Cart newCart = Cart.create(command.getUserId(), command.getProductId(), command.getQuantity());
			cartRepository.save(newCart);
			return CartInfo.from(newCart);
		} catch (DataIntegrityViolationException e) {
			throw CART_ALREADY_EXISTS.exception();
		}
	}

	@Cacheable(cacheNames = "findCarts", key = "'carts:user:' + #userId", cacheManager = "cartCacheManager")
	public List<CartWithProductInfo> findCarts(Long userId) {

		return getCartWithProductInfos(userId);
	}

	public List<CartWithProductInfo> findCartsWithoutCache(Long userId) {
		return getCartWithProductInfos(userId);
	}

	@Transactional
	@CacheEvict(cacheNames = "findCarts", key = "'carts:user:' + #command.userId")
	public CartInfo deleteCart(DeleteCartCommand command) {

		// 유저 유효성 검사

		Cart cart = cartRepository.findByUserIdAndCartId(command.getUserId(), command.getCartId())
			.orElseThrow(CART_NOT_FOUND::exception);

		cartRepository.deleteByUserIdAndCartId(command.getUserId(), command.getCartId());

		return CartInfo.from(cart);
	}

	@Transactional
	@CacheEvict(cacheNames = "findCarts", key = "'carts:user:' + #command.userId")
	public CartInfo updateCart(UpdateCartCommand command) {

		// 유저 유효성 검사

		Cart cart = cartRepository.findByUserIdAndCartIdWithLock(command.getUserId(), command.getCartId())
			.orElseThrow(CART_NOT_FOUND::exception);

		cart.updateQuantity(command.getQuantity());

		return CartInfo.from(cart);
	}

	@Transactional
	@CacheEvict(cacheNames = "findCarts", key = "'carts:user:' + #command.userId")
	public void clearCart(ClearCartCommand command) {
		cartRepository.deleteByIds(command.getCartIds());
	}

	private List<CartWithProductInfo> getCartWithProductInfos(Long userId) {
		// 유저 유효성 검사

		List<CartWithProductDto> carts = cartRepository.findAllByUserId(
			userId);
		return carts.stream()
			.map(CartWithProductInfo::from)
			.toList();
	}
}
