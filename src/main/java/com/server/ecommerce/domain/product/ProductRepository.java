package com.server.ecommerce.domain.product;

import org.springframework.data.domain.Page;

import com.server.ecommerce.domain.product.condition.ProductSearchCondition;

public interface ProductRepository {
	Page<Product> searchProducts(ProductSearchCondition condition);
}
