package com.server.ecommerce.domain.product;

import java.math.BigDecimal;

import com.server.ecommerce.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

	@Id
	@Column(name = "product_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal price;

	@Column(nullable = false)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProductCategory category;

	@Column(nullable = false)
	private Integer stock;

	@Builder
	private Product(String name, BigDecimal price, String description, ProductCategory category, Integer stock) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.category = category;
		this.stock = stock;
	}

	public static Product create(String name, BigDecimal price, String description, ProductCategory category, Integer stock) {
		return Product.builder()
			.name(name)
			.price(price)
			.description(description)
			.category(category)
			.stock(stock)
			.build();
	}
}
