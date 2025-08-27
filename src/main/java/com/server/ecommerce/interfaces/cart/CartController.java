package com.server.ecommerce.interfaces.cart;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.ecommerce.interfaces.cart.request.CreateCartRequest;
import com.server.ecommerce.service.cart.CartService;
import com.server.ecommerce.service.cart.info.CartInfo;
import com.server.ecommerce.support.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CartController {

	private final CartService cartService;

	@PostMapping("/carts")
	public ResponseEntity<ApiResponse<CartInfo>> createCart(@RequestBody CreateCartRequest request) {
		return ApiResponse.CREATE(cartService.addCart(request.toCommand()));
	}
}
