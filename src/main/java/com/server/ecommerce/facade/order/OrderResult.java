package com.server.ecommerce.facade.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.server.ecommerce.service.order.info.OrderInfo;
import com.server.ecommerce.service.payment.info.PaymentInfo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderResult {

	private String orderNo;
	private Long orderId;
	private Long paymentId;
	private String status;
	private BigDecimal payAmount;
	private BigDecimal totalPrice;
	private LocalDateTime createdAt;

	@Builder
	public OrderResult(String orderNo, Long orderId, Long paymentId, String status, BigDecimal totalPrice, BigDecimal payAmount, LocalDateTime createdAt) {
		this.orderNo = orderNo;
		this.orderId = orderId;
		this.paymentId = paymentId;
		this.status = status;
		this.totalPrice = totalPrice;
		this.payAmount = payAmount;
		this.createdAt = createdAt;
	}

	public static OrderResult from(OrderInfo orderInfo, PaymentInfo paymentInfo) {
		return OrderResult.builder()
			.orderId(orderInfo.getOrderId())
			.orderNo(orderInfo.getOrderNo())
			.paymentId(paymentInfo.getPaymentId())
			.status(orderInfo.getStatus())
			.totalPrice(orderInfo.getTotalPrice())
			.payAmount(paymentInfo.getPayAmount())
			.createdAt(orderInfo.getCreatedAt())
			.build() ;
	}
}
