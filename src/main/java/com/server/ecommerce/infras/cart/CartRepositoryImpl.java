package com.server.ecommerce.infras.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.server.ecommerce.domain.cart.Cart;
import com.server.ecommerce.domain.cart.CartRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {

	private final CartJpaRepository cartJpaRepository;

	@Override
	public Cart save(Cart cart) {
		return cartJpaRepository.save(cart);
	}

	@Override
	public Optional<Cart> findById(Long cartId) {
		return cartJpaRepository.findById(cartId);
	}

	@Override
	public Optional<Cart> findByUserIdAndProductId(Long userId, Long productId) {
		return cartJpaRepository.findByUserIdAndProductId(userId, productId);
	}

	@Override
	public List<Cart> findByUserId(Long userId) {
		return cartJpaRepository.findByUserId(userId);
	}

	@Override
	public void deleteById(Long cartId) {
		cartJpaRepository.deleteById(cartId);
	}
}