package com.server.ecommerce.interfaces.cart;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.server.ecommerce.service.cart.CartService;
import com.server.ecommerce.service.cart.info.CartInfo;
import com.server.ecommerce.service.user.CustomUserDetails;
import com.server.ecommerce.support.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	@PostMapping("/carts")
	public ResponseEntity<ApiResponse<CartInfo>> addCart(@RequestBody AddCartRequest request) {
		Long userId = getCurrentUserId();
		CartInfo cartInfo = cartService.addCart(request.toCommand(userId));
		return ApiResponse.CREATE(cartInfo);
	}

	@PutMapping("/carts/{cartId}")
	public ResponseEntity<ApiResponse<CartInfo>> updateCart(
		@PathVariable Long cartId,
		@RequestBody UpdateCartRequest request) {
		CartInfo cartInfo = cartService.updateCart(request.toCommand(cartId));
		return ApiResponse.OK(cartInfo);
	}

	@DeleteMapping("/carts/{cartId}")
	public ResponseEntity<ApiResponse<String>> deleteCart(@PathVariable Long cartId) {
		cartService.deleteCart(cartId);
		return ApiResponse.OK("장바구니 항목이 삭제되었습니다.", "삭제 완료");
	}

	@GetMapping("/carts")
	public ResponseEntity<ApiResponse<List<CartInfo>>> getUserCarts() {
		Long userId = getCurrentUserId();
		List<CartInfo> cartInfos = cartService.getUserCarts(userId);
		return ApiResponse.OK(cartInfos);
	}

	@GetMapping("/carts/{cartId}")
	public ResponseEntity<ApiResponse<CartInfo>> getCart(@PathVariable Long cartId) {
		CartInfo cartInfo = cartService.getCart(cartId);
		return ApiResponse.OK(cartInfo);
	}

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		return userDetails.getUserId();
	}
}
