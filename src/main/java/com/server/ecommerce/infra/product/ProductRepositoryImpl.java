package com.server.ecommerce.infra.product;

import java.util.List;

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

	@Override
	public List<Product> findAllByIds(List<Long> ids) {
		return productJpaRepository.findAllByIdIn(ids);
	}

	@Override
	public List<Product> findAllByIdsWithLock(List<Long> ids) {
		return productJpaRepository.findAllByIdsWithLock(ids);
	}
}
