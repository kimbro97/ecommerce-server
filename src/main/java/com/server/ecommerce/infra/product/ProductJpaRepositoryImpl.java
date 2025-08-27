package com.server.ecommerce.infra.product;

import static com.server.ecommerce.domain.product.QProduct.*;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.ecommerce.domain.product.Product;
import com.server.ecommerce.domain.product.ProductCategory;
import com.server.ecommerce.domain.product.condition.ProductSearchCondition;

import jakarta.persistence.EntityManager;

public class ProductJpaRepositoryImpl implements ProductJpaRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public ProductJpaRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public Page<Product> searchProducts(ProductSearchCondition condition) {
		List<Product> content = queryFactory
			.selectFrom(product)
			.where(
				categoryEq(condition.getCategory()),
				keywordLike(condition.getKeyword()),
				priceBetween(condition.getMinPrice(), condition.getMaxPrice())
			)
			.orderBy(product.createdAt.desc())
			.offset(condition.getPageable().getOffset())
			.limit(condition.getPageable().getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(product.count())
			.from(product)
			.where(
				categoryEq(condition.getCategory()),
				keywordLike(condition.getKeyword()),
				priceBetween(condition.getMinPrice(), condition.getMaxPrice())
			);

		return PageableExecutionUtils.getPage(content, condition.getPageable(), countQuery::fetchOne);
	}

	private BooleanExpression categoryEq(ProductCategory category) {
		return category != null ? product.category.eq(category) : null;
	}

	private BooleanExpression keywordLike(String keyword) {
		return StringUtils.hasText(keyword) ?
			product.name.like(keyword + "%") : null;
	}

	private BooleanExpression priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
		return minPrice != null && maxPrice != null  ? product.price.between(minPrice, maxPrice) : null;
	}
}
