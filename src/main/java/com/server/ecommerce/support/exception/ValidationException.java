package com.server.ecommerce.support.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

	private final HttpStatus httpStatus;

	public ValidationException(HttpStatus httpStatus, String message) {
		super(message);
		this.httpStatus = httpStatus;
	}
}
