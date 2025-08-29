package com.server.ecommerce.domain.product;

import java.util.List;

import org.springframework.data.domain.Page;

import com.server.ecommerce.domain.product.condition.ProductSearchCondition;

public interface ProductRepository {
	Page<Product> searchProducts(ProductSearchCondition condition);
	List<Product> findAllByIds(List<Long> ids);
	List<Product> findAllByIdsWithLock(List<Long> ids);
}
