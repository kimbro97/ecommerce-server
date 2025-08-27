package com.server.ecommerce.interfaces.product;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.ecommerce.interfaces.product.reqeust.ProductSearchRequest;
import com.server.ecommerce.service.product.ProductService;
import com.server.ecommerce.service.product.info.ProductInfo;
import com.server.ecommerce.support.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping("/products")
	public ResponseEntity<ApiResponse<Page<ProductInfo>>> searchProducts(ProductSearchRequest request) {
		return ApiResponse.OK(productService.searchProducts(request.toCommand()));
	}
}
