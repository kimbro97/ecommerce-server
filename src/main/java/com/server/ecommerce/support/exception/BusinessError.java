package com.server.ecommerce.support.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum BusinessError {

	PRODUCT_CATEGORY_NOT_VALID(BAD_REQUEST, "유효하지않은 카테고리값입니다"),
	CART_QUANTITY_INVALID(BAD_REQUEST, "유효하지 않은 수량입니다"),
	CART_NOT_FOUND(NOT_FOUND, "해당 장바구니를 찾을 수 없습니다"),
	PRODUCT_NOT_FOUND(NOT_FOUND, "해당 상품을 찾을 수 없습니다"),
	PRODUCT_OUT_OF_STOCK(BAD_REQUEST, "상품이 품절되었습니다"),
	ORDER_LIEN_IS_EMPTY(NOT_FOUND, "주문 항목을 찾을 수 없습니다"),
	PAY_FAILED(BAD_REQUEST, "결제에 실패했습니다"),

	TEST_ERROR(BAD_REQUEST, "test error");

	private final HttpStatus httpStatus;
	private final String message;

	BusinessError(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public BusinessException exception() {
		return new BusinessException(httpStatus, message);
	}
}
