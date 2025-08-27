package com.server.ecommerce.interfaces.product.reqeust;

import static com.server.ecommerce.support.exception.ValidationError.*;

import java.math.BigDecimal;

import com.server.ecommerce.service.product.command.ProductSearchCommand;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchRequest {

	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_SIZE = 10;


	private String category;
	private String keyword;
	private Integer minPrice;
	private Integer maxPrice;
	private int page = DEFAULT_PAGE;
	private int size = DEFAULT_SIZE;

	public ProductSearchCommand toCommand() {

		if (page < 0) {
			throw SEARCH_PRODUCT_PAGE_INVALID.exception();
		}
		if (size < 2) {
			throw SEARCH_PRODUCT_SIZE_INVALID.exception();
		}

		if ((minPrice == null && maxPrice != null) || (minPrice != null && maxPrice == null)) {
			throw SEARCH_PRODUCT_PRICE_INVALID.exception();
		}
		if (minPrice != null && maxPrice != null && minPrice >= maxPrice) {
			throw SEARCH_PRODUCT_PRICE_RANGE_INVALID.exception();
		}

		return ProductSearchCommand.of(
			category,
			keyword,
			minPrice != null ? BigDecimal.valueOf(minPrice) : null,
			maxPrice != null ? BigDecimal.valueOf(maxPrice) : null,
			page,
			size
		);
	}
}
