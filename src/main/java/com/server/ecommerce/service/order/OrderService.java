package com.server.ecommerce.service.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.order.Order;
import com.server.ecommerce.domain.order.OrderLine;
import com.server.ecommerce.domain.order.OrderRepository;
import com.server.ecommerce.service.order.command.CreateOrderCommand;
import com.server.ecommerce.service.order.info.OrderInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;

	@Transactional
	public OrderInfo createOrder(CreateOrderCommand command) {

		Order order = Order.create(command.getUserId());

		command.getProducts()
			.stream()
			.map((product) -> OrderLine.create(product.getProductId(), product.getQuantity(), product.getPrice()))
			.forEach(order::addOrderLine);

		order.calculateTotalPrice();
		orderRepository.save(order);

		return OrderInfo.from(order);
	}
}
