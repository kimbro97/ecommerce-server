package com.server.ecommerce.service.product;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.product.Product;
import com.server.ecommerce.domain.product.ProductRepository;
import com.server.ecommerce.service.product.command.ProductSearchCommand;
import com.server.ecommerce.service.product.info.ProductInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

	private final ProductRepository productRepository;

	public Page<ProductInfo> searchProducts(ProductSearchCommand command) {
		Page<Product> products = productRepository.searchProducts(command.toCondition());
		return products.map(ProductInfo::from);
	}
}
