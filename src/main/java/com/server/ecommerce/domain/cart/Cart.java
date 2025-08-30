package com.server.ecommerce.domain.cart;

import static com.server.ecommerce.support.exception.BusinessError.*;

import com.server.ecommerce.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(indexes = {
	@Index(name = "idx_cart_user_id", columnList = "user_id"),
	@Index(name = "idx_cart_product_id", columnList = "product_id"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

	@Id
	@Column(name = "cart_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	private Long productId;

	private Integer quantity;

	@Builder
	private Cart(Long userId, Long productId, Integer quantity) {
		this.productId = productId;
		this.userId = userId;
		this.quantity = quantity;
	}

	public static Cart create(Long userId, Long productId, Integer quantity) {
		return Cart.builder()
			.userId(userId)
			.productId(productId)
			.quantity(quantity)
			.build();
	}

	public void updateQuantity(Integer quantity) {

		if (quantity <= 1) {
			throw CART_QUANTITY_INVALID.exception();
		}

		this.quantity = quantity;
	}

	public void increaseQuantity(Integer quantity) {

		if (quantity <= 0) {
			throw CART_QUANTITY_INVALID.exception();
		}

		this.quantity = this.quantity + quantity;
	}
}
