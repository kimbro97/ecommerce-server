package com.server.ecommerce.service.payment;

import java.util.concurrent.ThreadLocalRandom;

import com.server.ecommerce.domain.payment.client.PaymentClient;
import com.server.ecommerce.domain.payment.client.PaymentRequest;
import com.server.ecommerce.domain.payment.client.PaymentResponse;

public class FailedPaymentClientStub implements PaymentClient {
	@Override
	public PaymentResponse processPayment(PaymentRequest request) {
		return new PaymentResponse("FAILED", null, "Something wrong!");
	}
}
