package com.server.ecommerce.domain.payment;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

	@Id
	@Column(name = "payment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long orderId;

	@Column(nullable = false)
	private String transactionId;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal payAmount;

	private Payment(Long userId, Long orderId, BigDecimal payAmount) {
		this.userId = userId;
		this.orderId = orderId;
		this.payAmount = payAmount;
	}

	public static Payment create(Long userId, Long orderId, BigDecimal payAmount) {
		return new Payment(userId, orderId, payAmount);
	}

	public void pay(String transactionId) {
		this.transactionId =  transactionId;
	}
}
