package com.server.ecommerce.service.payment;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.payment.PaymentRepository;
import com.server.ecommerce.infra.payment.client.MockPaymentClient;
import com.server.ecommerce.service.payment.command.PayCommand;
import com.server.ecommerce.service.payment.info.PaymentInfo;

@Transactional
@SpringBootTest
class PaymentServiceTest {

	@Autowired
	private PaymentRepository paymentRepository;

	@Test
	void pay() {

		PaymentService paymentService = new PaymentService(new SuccessPaymentClientStub(), paymentRepository);

		Long userId = 1L;
		Long orderId = 1L;
		BigDecimal payAmount = new BigDecimal("1000000");
		PayCommand command = new PayCommand(userId, orderId, payAmount);
		PaymentInfo paymentInfo = paymentService.pay(command);

		assertThat(paymentInfo.getOrderId()).isEqualTo(orderId);
		assertThat(paymentInfo.getUserId()).isEqualTo(userId);
		assertThat(paymentInfo.getPayAmount()).isEqualTo(payAmount);
	}

	@Test
	@DisplayName("결제에 실패하면 예외가 발생한다")
	void pay_exception() {

		PaymentService paymentService = new PaymentService(new FailedPaymentClientStub(), paymentRepository);

		Long userId = 1L;
		Long orderId = 1L;
		BigDecimal payAmount = new BigDecimal("1000000");
		PayCommand command = new PayCommand(userId, orderId, payAmount);

		assertThatThrownBy(() -> paymentService.pay(command)).isInstanceOf(RuntimeException.class);
	}
}
