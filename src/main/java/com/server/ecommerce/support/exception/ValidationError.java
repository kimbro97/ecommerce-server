package com.server.ecommerce.support.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ValidationError {

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
