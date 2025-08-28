package com.server.ecommerce.infra.cart;

import static com.server.ecommerce.domain.cart.QCart.*;
import static com.server.ecommerce.domain.product.QProduct.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.ecommerce.domain.cart.dto.CartWithProductDto;
import com.server.ecommerce.domain.cart.dto.QCartWithProductDto;

import jakarta.persistence.EntityManager;

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
}
