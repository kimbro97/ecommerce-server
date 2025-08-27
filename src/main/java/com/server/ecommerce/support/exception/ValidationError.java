package com.server.ecommerce.support.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ValidationError {

	SEARCH_PRODUCT_PAGE_INVALID(BAD_REQUEST, "page는 0 이상이어야 합니다"),
	SEARCH_PRODUCT_SIZE_INVALID(BAD_REQUEST, "size는 2 이상이어야 합니다"),
	SEARCH_PRODUCT_PRICE_INVALID(BAD_REQUEST, "minPrice와 maxPrice는 함께 제공되어야 합니다"),
	SEARCH_PRODUCT_PRICE_RANGE_INVALID(BAD_REQUEST, "minPrice는 maxPrice보다 작아야 합니다"),

	TEST_ERROR(BAD_REQUEST, "test error");

	private final HttpStatus httpStatus;
	private final String message;

	ValidationError(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public ValidationException exception() {
		return new ValidationException(httpStatus, message);
	}
}
