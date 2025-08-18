package com.server.ecommerce.service.order.info;

import java.time.LocalDateTime;

import com.server.ecommerce.domain.order.OrderLine;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderLineInfo {
    private final Long orderLineId;
    private final Long productId;
    private final Integer quantity;
    private final Long unitPrice;
    private final Long totalPrice;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static OrderLineInfo from(OrderLine orderLine) {
        return new OrderLineInfo(
            orderLine.getId(),
            orderLine.getProductId(),
            orderLine.getQuantity(),
            orderLine.getUnitPrice(),
            orderLine.getTotalPrice(),
            orderLine.getCreatedAt(),
            orderLine.getUpdatedAt()
        );
    }
}