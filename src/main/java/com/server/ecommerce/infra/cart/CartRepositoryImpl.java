package com.server.ecommerce.infra.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.server.ecommerce.domain.cart.Cart;
import com.server.ecommerce.domain.cart.CartRepository;
import com.server.ecommerce.domain.cart.dto.CartWithProductDto;

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
	public Optional<Cart> findByProductId(Long productId) {
		return cartJpaRepository.findByProductId(productId);
	}

	@Override
	public Optional<Cart> findByUserIdAndProductId(Long userId, Long productId) {
		return cartJpaRepository.findByUserIdAndProductId(userId, productId);
	}

	@Override
	public Optional<Cart> findByUserIdAndProductIdWithLock(Long userId, Long productId) {
		return cartJpaRepository.findByUserIdAndProductIdWithLock(userId, productId);
	}

	@Override
	public List<CartWithProductDto> findAllByUserId(Long userId) {
		return cartJpaRepository.findCartListWithProductByUserId(userId);
	}
	@Override
	public Optional<Cart> findByUserIdAndCartId(Long userId, Long cartId) {
		return cartJpaRepository.findByUserIdAndId(userId, cartId);
	}

	@Override
	public Optional<Cart> findByUserIdAndCartIdWithLock(Long userId, Long cartId) {
		return cartJpaRepository.findByUserIdAndIdWithLock(userId, cartId);
	}

	@Override
	public void deleteByUserIdAndCartId(Long userId, Long cartId) {
		cartJpaRepository.deleteByUserIdAndId(userId, cartId);
	}

	@Override
	public void deleteByIds(List<Long> cartIds) {
		cartJpaRepository.deleteByIdIn(cartIds);
	}
}
