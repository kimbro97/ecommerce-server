package com.server.ecommerce.infra.cart;

import static com.server.ecommerce.domain.cart.QCart.*;
import static com.server.ecommerce.domain.product.QProduct.*;

import java.util.List;
import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.ecommerce.domain.cart.Cart;
import com.server.ecommerce.domain.cart.dto.CartWithProductDto;
import com.server.ecommerce.domain.cart.dto.QCartWithProductDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

public class CartJpaRepositoryImpl implements CartJpaRepositoryCustom {

	private JPAQueryFactory queryFactory;

	public CartJpaRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public List<CartWithProductDto> findCartListWithProductByUserId(Long userId) {
		return queryFactory
			.select(new QCartWithProductDto(
				cart.id,
				cart.userId,
				cart.productId,
				cart.quantity,
				product.name,
				product.price,
				product.stock.loe(0)))
			.from(cart)
			.join(product).on(cart.productId.eq(product.id))
			.where(cart.userId.eq(userId))
			.fetch();
	}

	@Override
	public Optional<Cart> findByUserIdAndIdWithLock(Long userId, Long cartId) {
		Cart result = queryFactory
			.selectFrom(cart)
			.where(cart.userId.eq(userId).and(cart.id.eq(cartId)))
			.setLockMode(LockModeType.PESSIMISTIC_WRITE)
			.fetchOne();
		
		return Optional.ofNullable(result);
	}

	@Override
	public Optional<Cart> findByUserIdAndProductIdWithLock(Long userId, Long productId) {
		Cart result = queryFactory
			.selectFrom(cart)
			.where(cart.userId.eq(userId).and(cart.productId.eq(productId)))
			.setLockMode(LockModeType.PESSIMISTIC_WRITE)
			.fetchOne();
		
		return Optional.ofNullable(result);
	}
}
