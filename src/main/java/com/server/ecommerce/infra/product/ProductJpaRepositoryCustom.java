package com.server.ecommerce.infra.product;

import org.springframework.data.domain.Page;

import com.server.ecommerce.domain.product.Product;
import com.server.ecommerce.domain.product.condition.ProductSearchCondition;

public interface ProductJpaRepositoryCustom {
	Page<Product> searchProducts(ProductSearchCondition condition);
}
