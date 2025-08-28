package com.server.ecommerce.service.payment;

import static com.server.ecommerce.support.exception.BusinessError.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.payment.Payment;
import com.server.ecommerce.domain.payment.PaymentRepository;
import com.server.ecommerce.domain.payment.client.PaymentClient;
import com.server.ecommerce.domain.payment.client.PaymentRequest;
import com.server.ecommerce.domain.payment.client.PaymentResponse;
import com.server.ecommerce.service.payment.command.PayCommand;
import com.server.ecommerce.service.payment.info.PaymentInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentClient paymentClient;
	private final PaymentRepository paymentRepository;

	@Transactional
	public PaymentInfo pay(PayCommand command) {
		Payment payment = Payment.create(command.getUserId(), command.getOrderId(), command.getPayAmount());

		PaymentResponse response = paymentClient.processPayment(
			new PaymentRequest(command.getOrderId(), command.getPayAmount()));

		if (response.status().equals("FAILED")) {
			throw PAY_FAILED.exception();
		}

		payment.pay(response.transactionId());
		paymentRepository.save(payment);
		return PaymentInfo.from(payment);
	}
}
