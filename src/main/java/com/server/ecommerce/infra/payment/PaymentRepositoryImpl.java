package com.server.ecommerce.infra.payment;

import org.springframework.stereotype.Repository;

import com.server.ecommerce.domain.payment.Payment;
import com.server.ecommerce.domain.payment.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

	private final PaymentJpaRepository paymentJpaRepository;

	@Override
	public Payment save(Payment payment) {
		return paymentJpaRepository.save(payment);
	}
}
