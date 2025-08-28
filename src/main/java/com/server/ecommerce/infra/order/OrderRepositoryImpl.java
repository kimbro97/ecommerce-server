package com.server.ecommerce.infra.order;

import org.springframework.stereotype.Repository;

import com.server.ecommerce.domain.order.Order;
import com.server.ecommerce.domain.order.OrderRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

	private final OrderJpaRepository orderJpaRepository;

	@Override
	public Order save(Order order) {
		return orderJpaRepository.save(order);
	}
}
