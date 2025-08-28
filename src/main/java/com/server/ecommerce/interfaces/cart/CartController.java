package com.server.ecommerce.interfaces.cart;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.ecommerce.interfaces.cart.request.CreateCartRequest;
import com.server.ecommerce.interfaces.cart.request.DeleteCartRequest;
import com.server.ecommerce.interfaces.cart.request.UpdateCartRequest;
import com.server.ecommerce.interfaces.cart.request.findCartsRequest;
import com.server.ecommerce.service.cart.CartService;
import com.server.ecommerce.service.cart.info.CartInfo;
import com.server.ecommerce.service.cart.info.CartWithProductInfo;
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

	@GetMapping("/carts")
	public ResponseEntity<ApiResponse<List<CartWithProductInfo>>> findCarts(@RequestBody findCartsRequest request) {
		return ApiResponse.OK(cartService.findCarts(request.getUserId()));
	}

	@DeleteMapping("/carts/{cartId}")
	public ResponseEntity<ApiResponse<CartInfo>> deleteCart(@PathVariable Long cartId, @RequestBody DeleteCartRequest request) {
		return ApiResponse.OK(cartService.deleteCart(request.toCommand(cartId)));
	}

	@PatchMapping("/carts/{cartId}")
	public ResponseEntity<ApiResponse<CartInfo>> updateCart(@PathVariable Long cartId, @RequestBody UpdateCartRequest request) {
		return ApiResponse.OK(cartService.updateCart(request.toCommand(cartId)));
	}
}
