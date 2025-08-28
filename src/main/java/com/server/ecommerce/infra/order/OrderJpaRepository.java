package com.server.ecommerce.infra.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.ecommerce.domain.order.Order;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
