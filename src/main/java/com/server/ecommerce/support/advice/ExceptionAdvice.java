package com.server.ecommerce.support.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.server.ecommerce.support.ApiResponse;
import com.server.ecommerce.support.exception.BusinessException;
import com.server.ecommerce.support.exception.ValidationException;

@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse<Void>> BusinessExceptionHandler(BusinessException e) {
		return ApiResponse.BusinessException(e.getHttpStatus(), e.getMessage());
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ApiResponse<Void>> ValidationExceptionHandler(ValidationException e) {
		return ApiResponse.ValidationException(e.getHttpStatus(), e.getMessage());
	}
}
