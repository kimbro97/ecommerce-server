package com.server.ecommerce.service.payment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.order.Order;
import com.server.ecommerce.domain.order.OrderRepository;
import com.server.ecommerce.domain.order.OrderStatus;
import com.server.ecommerce.domain.payment.Payment;
import com.server.ecommerce.domain.payment.PaymentMethod;
import com.server.ecommerce.domain.payment.PaymentRepository;
import com.server.ecommerce.service.payment.command.ProcessPaymentCommand;
import com.server.ecommerce.service.payment.info.PaymentInfo;
import com.server.ecommerce.service.point.PointService;
import com.server.ecommerce.service.point.command.UsePointCommand;
import com.server.ecommerce.support.exception.BusinessError;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PointService pointService;

    @Transactional
    public PaymentInfo processPayment(ProcessPaymentCommand command) {
        // 1. 주문 조회 및 검증
        Order order = orderRepository.findById(command.getOrderId())
            .orElseThrow(() -> BusinessError.ORDER_NOT_FOUND.exception());

        // 2. 주문 소유자 검증
        if (!order.getUserId().equals(command.getUserId())) {
            throw BusinessError.INVALID_ORDER_ACCESS.exception();
        }

        // 3. 주문 상태 검증 (PENDING 상태만 결제 가능)
        if (order.getStatus() != OrderStatus.PENDING) {
            throw BusinessError.INVALID_ORDER_STATUS.exception();
        }

        // 4. 이미 결제된 주문인지 확인
        paymentRepository.findByOrderId(command.getOrderId())
            .ifPresent(payment -> {
                if (payment.isCompleted()) {
                    throw BusinessError.PAYMENT_ALREADY_COMPLETED.exception();
                }
            });

        // 5. 결제 생성
        Payment payment = Payment.create(
            command.getOrderId(),
            command.getUserId(),
            order.getTotalAmount(),
            command.getPaymentMethod()
        );

        Payment savedPayment = paymentRepository.save(payment);

        try {
            // 6. 결제 처리
            if (command.getPaymentMethod() == PaymentMethod.POINT) {
                processPointPayment(command.getUserId(), order.getTotalAmount());
            } else {
                // 향후 다른 결제 수단 확장
                throw BusinessError.UNSUPPORTED_PAYMENT_METHOD.exception();
            }

            // 7. 결제 완료 처리
            savedPayment.complete();
            order.pay();
            
            paymentRepository.save(savedPayment);
            orderRepository.save(order);

        } catch (Exception e) {
            // 8. 결제 실패 처리
            savedPayment.fail(e.getMessage());
            paymentRepository.save(savedPayment);
            throw e;
        }

        return PaymentInfo.from(savedPayment);
    }

    @Transactional(readOnly = true)
    public List<PaymentInfo> getUserPayments(Long userId) {
        List<Payment> payments = paymentRepository.findByUserId(userId);
        return payments.stream()
            .map(PaymentInfo::from)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaymentInfo getPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> BusinessError.PAYMENT_NOT_FOUND.exception());

        return PaymentInfo.from(payment);
    }

    @Transactional(readOnly = true)
    public PaymentInfo getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> BusinessError.PAYMENT_NOT_FOUND.exception());

        return PaymentInfo.from(payment);
    }

    private void processPointPayment(Long userId, Long amount) {
        UsePointCommand usePointCommand = new UsePointCommand(userId, amount);
        pointService.usePoint(usePointCommand);
    }
}