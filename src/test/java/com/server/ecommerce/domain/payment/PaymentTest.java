package com.server.ecommerce.domain.payment;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentTest {

	@Test
	@DisplayName("create 메서드로 payment 객체를 만들 수 있다")
	void create_payment() {
		Long userId = 1L;
		Long orderId = 1L;
		BigDecimal payAmount = new BigDecimal("1000000");
		Payment payment = Payment.create(userId, orderId, payAmount);

		assertThat(payment.getOrderId()).isEqualTo(orderId);
		assertThat(payment.getUserId()).isEqualTo(userId);
		assertThat(payment.getPayAmount()).isEqualTo(payAmount);
	}

	@Test
	@DisplayName("pay의 인자로 transactionId를 받아서 payment의 transactionId를 갱신할 수 있다")
	void pay() {
		Long userId = 1L;
		Long orderId = 1L;
		String transactionId = "transactionId";
		BigDecimal payAmount = new BigDecimal("1000000");
		Payment payment = Payment.create(userId, orderId, payAmount);

		assertThat(payment.getTransactionId()).isEqualTo(null);

		payment.pay(transactionId);

		assertThat(payment.getTransactionId()).isEqualTo(transactionId);
	}
}
