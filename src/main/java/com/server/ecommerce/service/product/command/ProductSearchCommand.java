package com.server.ecommerce.service.product.command;

import java.math.BigDecimal;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.server.ecommerce.domain.product.condition.ProductSearchCondition;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSearchCommand {

	private String category;
	private String keyword;
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private Pageable pageable;

	public static ProductSearchCommand of(String category, String keyword, BigDecimal minPrice, BigDecimal maxPrice, Integer page, Integer size) {
		return ProductSearchCommand.builder()
			.category(category)
			.keyword(keyword)
			.minPrice(minPrice)
			.maxPrice(maxPrice)
			.pageable(PageRequest.of(page, size))
			.build();
	}

	public ProductSearchCondition toCondition() {

		return ProductSearchCondition.builder()
			.category(category)
			.keyword(keyword)
			.minPrice(minPrice)
			.maxPrice(maxPrice)
			.pageable(pageable)
			.build();
	}
}
