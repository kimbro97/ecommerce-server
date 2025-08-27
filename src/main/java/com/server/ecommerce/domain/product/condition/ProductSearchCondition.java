package com.server.ecommerce.domain.product.condition;

import java.math.BigDecimal;

import org.springframework.data.domain.Pageable;

import com.server.ecommerce.domain.product.ProductCategory;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductSearchCondition {

	private ProductCategory category;
	private String keyword;
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private Pageable pageable;

	@Builder
	public ProductSearchCondition(String category, String keyword, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
		this.category = ProductCategory.parseCategory(category);
		this.keyword = keyword;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.pageable = pageable;
	}
}
