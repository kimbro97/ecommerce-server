package com.server.ecommerce.infra.product;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import com.server.ecommerce.domain.product.Product;
import com.server.ecommerce.domain.product.ProductRepository;
import com.server.ecommerce.domain.product.condition.ProductSearchCondition;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

	private final ProductJpaRepository productJpaRepository;

	@Override
	public Page<Product> searchProducts(ProductSearchCondition condition) {
		return productJpaRepository.searchProducts(condition);
	}
}
