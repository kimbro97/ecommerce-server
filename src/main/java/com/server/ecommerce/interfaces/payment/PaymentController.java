package com.server.ecommerce.interfaces.payment;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.server.ecommerce.service.payment.PaymentService;
import com.server.ecommerce.service.payment.info.PaymentInfo;
import com.server.ecommerce.service.user.CustomUserDetails;
import com.server.ecommerce.support.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUserId();
    }

    @PostMapping("/orders/{orderId}/payment")
    public ResponseEntity<ApiResponse<PaymentInfo>> processPayment(
        @PathVariable Long orderId,
        @RequestBody ProcessPaymentRequest request) {
        
        Long userId = getCurrentUserId();
        PaymentInfo paymentInfo = paymentService.processPayment(request.toCommand(userId, orderId));
        return ApiResponse.CREATE(paymentInfo);
    }

    @GetMapping("/payments")
    public ResponseEntity<ApiResponse<List<PaymentInfo>>> getUserPayments() {
        Long userId = getCurrentUserId();
        List<PaymentInfo> payments = paymentService.getUserPayments(userId);
        return ApiResponse.OK(payments);
    }

    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentInfo>> getPayment(@PathVariable Long paymentId) {
        PaymentInfo paymentInfo = paymentService.getPayment(paymentId);
        return ApiResponse.OK(paymentInfo);
    }

    @GetMapping("/orders/{orderId}/payment")
    public ResponseEntity<ApiResponse<PaymentInfo>> getPaymentByOrderId(@PathVariable Long orderId) {
        PaymentInfo paymentInfo = paymentService.getPaymentByOrderId(orderId);
        return ApiResponse.OK(paymentInfo);
    }
}