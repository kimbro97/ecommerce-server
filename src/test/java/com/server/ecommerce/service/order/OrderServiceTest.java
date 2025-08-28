package com.server.ecommerce.service.order;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.order.OrderStatus;
import com.server.ecommerce.service.order.command.CreateOrderCommand;
import com.server.ecommerce.service.order.info.OrderInfo;
import com.server.ecommerce.support.exception.BusinessException;

@Transactional
@SpringBootTest
class OrderServiceTest {

	@Autowired
	private OrderService orderService;

	@Test
	@DisplayName("userId와 주문할 상품을 받아 주문을 만들 수 있다")
	void create_order() {
		Long userId = 1L;

		List<OrderProduct> orderProductsLis = List.of(new OrderProduct(1L, 3, BigDecimal.valueOf(10000)));
		CreateOrderCommand command = new CreateOrderCommand(userId, orderProductsLis);

		OrderInfo orderInfo = orderService.createOrder(command);
		assertThat(orderInfo.getTotalPrice()).isEqualTo(BigDecimal.valueOf(30000));
		assertThat(orderInfo.getStatus()).isEqualTo(OrderStatus.PENDING.getDescription());

	}

	@Test
	@DisplayName("주문할 상품이 없다면 예외가 발생한다")
	void create_order_exception() {
		Long userId = 1L;

		List<OrderProduct> orderProductsLis = List.of();
		CreateOrderCommand command = new CreateOrderCommand(userId, orderProductsLis);

		assertThatThrownBy(() -> orderService.createOrder(command)).isInstanceOf(BusinessException.class);

	}

}
