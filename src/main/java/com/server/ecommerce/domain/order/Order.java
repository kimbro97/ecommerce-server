package com.server.ecommerce.domain.order;

import static com.server.ecommerce.domain.order.OrderStatus.*;
import static com.server.ecommerce.support.exception.BusinessError.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.server.ecommerce.domain.BaseEntity;
import com.server.ecommerce.support.exception.BusinessError;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
	name = "orders",
	indexes = {
		@Index(name = "idx_order_user_id", columnList = "order_id"),
		@Index(name = "idx_order_order_no", columnList = "order_no"),
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

	@Id
	@Column(name = "order_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private String orderNo;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal totalPrice;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	private LocalDateTime paidAt;

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<OrderLine> orderLines = new ArrayList<>();

	private Order(Long userId) {
		this.userId = userId;
		this.orderNo = generateOrderNo();
		this.totalPrice = BigDecimal.ZERO;
		this.status = PENDING;
	}

	public static Order create(Long userId) {
		return new Order(userId);
	}

	private static String generateOrderNo() {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
		int randomValue = ThreadLocalRandom.current().nextInt(9999);
		return timestamp + String.format("%04d", randomValue);
	}


	public void addOrderLine(OrderLine orderLine) {
		this.orderLines.add(orderLine);
		orderLine.setOrder(this);
	}

	public void calculateTotalPrice() {

		if (this.orderLines.isEmpty()) {
			throw ORDER_LIEN_IS_EMPTY.exception();
		}

		this.totalPrice = this.orderLines.stream()
			.map(OrderLine::getTotalPrice)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public void complete() {
		if (this.status == COMPLETE) {
			throw ORDER_ALREADY_COMPLETED.exception();
		}
		this.status = COMPLETE;
		this.paidAt = LocalDateTime.now();
	}
}
