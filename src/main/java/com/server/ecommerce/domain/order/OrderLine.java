package com.server.ecommerce.domain.order;

import java.math.BigDecimal;

import com.server.ecommerce.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderLine extends BaseEntity {

	@Id
	@Column(name = "order_line_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long productId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "order_id", nullable = false,
		foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Order order;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false)
	private BigDecimal totalPrice;

	private OrderLine(Long productId, Integer quantity, BigDecimal totalPrice) {
		this.productId = productId;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}

	public static OrderLine create(Long productId, Integer quantity, BigDecimal price) {
		return new OrderLine(productId, quantity, price.multiply(new BigDecimal(quantity)));
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
