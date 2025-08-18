package com.server.ecommerce.domain.order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long orderId);
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findByUserId(Long userId);
}