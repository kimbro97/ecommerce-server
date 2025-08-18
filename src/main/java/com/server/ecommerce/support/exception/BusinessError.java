package com.server.ecommerce.support.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum BusinessError {

	// 유저 관련 Error
	USER_JOIN_EMAIL_DUPLICATION(BAD_REQUEST, "이미 가입된 이메일입니다."),
	USER_NOT_FOUND(BAD_REQUEST, "회원을 찾을 수 없습니다"),

	// 포인트 관련 Error
	POINT_INVALID_CHARGE_AMOUNT(BAD_REQUEST, "충전 금액은 0보다 커야 합니다."),
	POINT_INVALID_USE_AMOUNT(BAD_REQUEST, "사용 금액은 0보다 커야 합니다."),
	POINT_INSUFFICIENT_BALANCE(BAD_REQUEST, "포인트가 부족합니다."),
	POINT_ACCOUNT_NOT_FOUND(BAD_REQUEST, "포인트 계정이 존재하지 않습니다."),

	// 장바구니 관련 Error
	CART_NOT_FOUND(BAD_REQUEST, "장바구니 항목을 찾을 수 없습니다."),

	// 주문 관련 Error
	ORDER_NOT_FOUND(BAD_REQUEST, "주문을 찾을 수 없습니다."),
	INVALID_CART_ACCESS(BAD_REQUEST, "다른 사용자의 장바구니에 접근할 수 없습니다."),
	PRODUCT_NOT_FOUND(BAD_REQUEST, "상품을 찾을 수 없습니다."),
	INSUFFICIENT_STOCK(BAD_REQUEST, "재고가 부족합니다.");

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
