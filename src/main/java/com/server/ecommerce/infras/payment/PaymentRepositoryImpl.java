package com.server.ecommerce.infras.payment;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.server.ecommerce.domain.payment.Payment;
import com.server.ecommerce.domain.payment.PaymentRepository;
import com.server.ecommerce.domain.payment.PaymentStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    @Override
    public Optional<Payment> findById(Long paymentId) {
        return paymentJpaRepository.findById(paymentId);
    }

    @Override
    public Optional<Payment> findByOrderId(Long orderId) {
        return paymentJpaRepository.findByOrderId(orderId);
    }

    @Override
    public List<Payment> findByUserId(Long userId) {
        return paymentJpaRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Payment> findByUserIdAndStatus(Long userId, PaymentStatus status) {
        return paymentJpaRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status);
    }
}