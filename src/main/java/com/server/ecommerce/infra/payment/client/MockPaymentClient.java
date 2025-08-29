package com.server.ecommerce.infra.payment.client;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.server.ecommerce.domain.payment.client.PaymentClient;
import com.server.ecommerce.domain.payment.client.PaymentRequest;
import com.server.ecommerce.domain.payment.client.PaymentResponse;

@Component
public class MockPaymentClient implements PaymentClient {

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        // boolean isSuccess = ThreadLocalRandom.current().nextBoolean();
		//
        // if (isSuccess) {
        //     String transactionId = "txn_" + ThreadLocalRandom.current().nextLong(1000000000L, 9999999999L);
        //     return new PaymentResponse("SUCCESS", transactionId, "Payment processed successfully");
        // } else {
        //     return new PaymentResponse("FAILED", null, "Something wrong!");
        // }
		String transactionId = "txn_" + ThreadLocalRandom.current().nextLong(1000000000L, 9999999999L);
		return new PaymentResponse("SUCCESS", transactionId, "Payment processed successfully");
    }
}
