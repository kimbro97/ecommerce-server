package com.server.ecommerce.interfaces.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.ecommerce.facade.order.OrderFacade;
import com.server.ecommerce.facade.order.OrderResult;
import com.server.ecommerce.support.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OrderController {

	private final OrderFacade orderFacade;

	@PostMapping("/orders")
	public ResponseEntity<ApiResponse<OrderResult>> createOrder(@RequestBody CreateOrderRequest request) {
		return ApiResponse.CREATE(orderFacade.order(request.toCriteria()));
	}
}
