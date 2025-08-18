package com.server.ecommerce.interfaces.order;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.server.ecommerce.service.order.OrderService;
import com.server.ecommerce.service.order.info.OrderInfo;
import com.server.ecommerce.service.user.CustomUserDetails;
import com.server.ecommerce.support.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUserId();
    }

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderInfo>> createOrder(@RequestBody CreateOrderRequest request) {
        Long userId = getCurrentUserId();
        OrderInfo orderInfo = orderService.createOrder(request.toCommand(userId));
        return ApiResponse.CREATE(orderInfo);
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderInfo>>> getUserOrders() {
        Long userId = getCurrentUserId();
        List<OrderInfo> orders = orderService.getUserOrders(userId);
        return ApiResponse.OK(orders);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderInfo>> getOrder(@PathVariable Long orderId) {
        OrderInfo orderInfo = orderService.getOrder(orderId);
        return ApiResponse.OK(orderInfo);
    }

    @GetMapping("/orders/number/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderInfo>> getOrderByNumber(@PathVariable String orderNumber) {
        OrderInfo orderInfo = orderService.getOrderByOrderNumber(orderNumber);
        return ApiResponse.OK(orderInfo);
    }
}