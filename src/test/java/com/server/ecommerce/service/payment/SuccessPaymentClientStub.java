package com.server.ecommerce.service.payment;

import java.util.concurrent.ThreadLocalRandom;

import com.server.ecommerce.domain.payment.client.PaymentClient;
import com.server.ecommerce.domain.payment.client.PaymentRequest;
import com.server.ecommerce.domain.payment.client.PaymentResponse;

public class SuccessPaymentClientStub implements PaymentClient {
	@Override
	public PaymentResponse processPayment(PaymentRequest request) {
		String transactionId = "txn_" + ThreadLocalRandom.current().nextLong(1000000000L, 9999999999L);
		return new PaymentResponse("SUCCESS", transactionId, "Payment processed successfully");
	}
}
