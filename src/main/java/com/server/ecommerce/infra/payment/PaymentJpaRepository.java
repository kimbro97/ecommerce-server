package com.server.ecommerce.infra.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.ecommerce.domain.payment.Payment;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
}
