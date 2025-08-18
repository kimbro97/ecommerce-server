package com.server.ecommerce.domain.cart;

import com.server.ecommerce.domain.BaseEntity;

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
public class Cart extends BaseEntity {

	@Id
	@Column(name = "cart_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(nullable = false)
	private Integer quantity;

	public Cart(Long userId, Long productId, Integer quantity) {
		this.userId = userId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static Cart create(Long userId, Long productId, Integer quantity) {
		return new Cart(userId, productId, quantity);
	}

	public void updateQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}