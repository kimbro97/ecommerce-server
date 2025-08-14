package com.server.ecommerce.support.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ValidationError {

	USER_JOIN_EMAIL_REQUIRED(BAD_REQUEST, "이메일을 입력해주세요"),
	USER_JOIN_PASSWORD_REQUIRED(BAD_REQUEST, "비밀번호를 입력해주세요"),
	USER_JOIN_CONFIRM_PASSWORD_REQUIRED(BAD_REQUEST, "확인 비밀번호를 입력해주세요"),
	USER_JOIN_PASSWORD_CONFIRM_PASSWORD_NOT_MATCH(BAD_REQUEST, "비밀번호와 확인 비밀번호가 일칠하지 않습니다"),
	USER_JOIN_NAME_REQUIRED(BAD_REQUEST, "이름을 입력해주세요"),
	REFRESH_TOKEN_REQUIRED(BAD_REQUEST, "리프레시 토큰이 없습니다"),
	REFRESH_TOKEN_EXPIRED(BAD_REQUEST, "리프레시 토큰이 만료되었습니다"),
	REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "리프레시 토큰을 찾을 수 없습니다");

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
