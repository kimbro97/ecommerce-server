package com.server.ecommerce.domain.order;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.server.ecommerce.support.exception.BusinessException;

class OrderTest {

	@Test
	@DisplayName("create 메서드르 사용하여 order 객체를 생성할 수 있다")
	void order_create() {
		Long userId = 1L;
		Order order = Order.create(userId);

		assertThat(order.getOrderLines()).isEmpty();
		assertThat(order.getOrderNo()).isNotNull();
		assertThat(order.getTotalPrice()).isEqualTo(BigDecimal.ZERO);
		assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
	}

	@Test
	@DisplayName("order에 orderline이 비어있을때 calculateTotalPrice를 호출하면 예외가 발생한다")
	void order_calculate_total_price_exception() {
		Long userId = 1L;
		Order order = Order.create(userId);
		assertThatThrownBy(order::calculateTotalPrice).isInstanceOf(BusinessException.class);
	}

	@Test
	@DisplayName("order에 orderline있을때 calculateTotalPrice를 호출하면 order의 totalPrice가 정해진다")
	void order_calculate_total_price() {
		Long userId = 1L;
		Order order = Order.create(userId);

		OrderLine orderLine1 = OrderLine.create(1L, 3, BigDecimal.valueOf(10000));
		OrderLine orderLine2 = OrderLine.create(2L, 1, BigDecimal.valueOf(10000));

		order.addOrderLine(orderLine1);
		order.addOrderLine(orderLine2);

		order.calculateTotalPrice();

		assertThat(order.getTotalPrice()).isEqualTo(BigDecimal.valueOf(40000));
	}

	@Test
	@DisplayName("addOrderLine을 호출하면 orderLine에도 order값이 들어간다")
	void order_add_order_line() {
		Long userId = 1L;

		Order order = Order.create(userId);

		OrderLine orderLine1 = OrderLine.create(1L, 3, BigDecimal.valueOf(10000));
		OrderLine orderLine2 = OrderLine.create(2L, 1, BigDecimal.valueOf(10000));

		order.addOrderLine(orderLine1);
		order.addOrderLine(orderLine2);

		assertThat(order.getOrderLines()).hasSize(2);
		assertThat(orderLine1.getOrder()).isEqualTo(order);
		assertThat(orderLine2.getOrder()).isEqualTo(order);
	}

	@Test
	@DisplayName("complete 메서드를 호출하면 status가 COMPLETE로 변경된다")
	void order_complete() {
		Long userId = 1L;
		Order order = Order.create(userId);

		OrderLine orderLine1 = OrderLine.create(1L, 3, BigDecimal.valueOf(10000));
		OrderLine orderLine2 = OrderLine.create(2L, 1, BigDecimal.valueOf(10000));

		order.addOrderLine(orderLine1);
		order.addOrderLine(orderLine2);

		order.calculateTotalPrice();
		assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
		assertThat(order.getPaidAt()).isNull();

		order.complete();
		assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETE);
		assertThat(order.getPaidAt()).isNotNull();
	}

	@Test
	@DisplayName("이미 상태가 complete인 주문이 complete 메서드를 실행하면 예외가 발생한다")
	void order_complete_exception() {
		Long userId = 1L;
		Order order = Order.create(userId);

		OrderLine orderLine1 = OrderLine.create(1L, 3, BigDecimal.valueOf(10000));
		OrderLine orderLine2 = OrderLine.create(2L, 1, BigDecimal.valueOf(10000));

		order.addOrderLine(orderLine1);
		order.addOrderLine(orderLine2);

		order.calculateTotalPrice();
		order.complete();

		assertThatThrownBy(order::complete)
			.isInstanceOf(BusinessException.class);
	}
}
