package com.server.ecommerce.service.order.info;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.server.ecommerce.domain.order.Order;

import lombok.Getter;

@Getter
public class OrderInfo {
	private String orderNo;
	private Long orderId;
	private String status;
	private BigDecimal totalPrice;
	private LocalDateTime createdAt;

	public OrderInfo(String orderNo, Long orderId, String status, BigDecimal totalPrice, LocalDateTime createdAt) {
		this.orderNo = orderNo;
		this.orderId = orderId;
		this.status = status;
		this.totalPrice = totalPrice;
		this.createdAt = createdAt;
	}

	public static OrderInfo from(Order order) {
		return new OrderInfo(order.getOrderNo(), order.getId(), order.getStatus().getDescription(), order.getTotalPrice(), order.getCreatedAt());
	}
}
