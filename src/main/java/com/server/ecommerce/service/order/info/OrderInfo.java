package com.server.ecommerce.service.order.info;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.server.ecommerce.domain.order.Order;
import com.server.ecommerce.domain.order.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderInfo {
    private final Long orderId;
    private final String orderNumber;
    private final Long userId;
    private final Long totalAmount;
    private final OrderStatus status;
    private final List<OrderLineInfo> orderLines;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static OrderInfo from(Order order) {
        List<OrderLineInfo> orderLineInfos = order.getOrderLines().stream()
            .map(OrderLineInfo::from)
            .collect(Collectors.toList());

        return new OrderInfo(
            order.getId(),
            order.getOrderNumber(),
            order.getUserId(),
            order.getTotalAmount(),
            order.getStatus(),
            orderLineInfos,
            order.getCreatedAt(),
            order.getUpdatedAt()
        );
    }
}