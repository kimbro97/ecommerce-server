package com.server.ecommerce.service.product.info;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.server.ecommerce.domain.product.Product;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductInfo {

	private Long productId;
	private String productName;
	private BigDecimal price;
	private String description;
	private String category;
	private boolean isSoldOut;
	private LocalDateTime createdAt;

	@Builder
	public ProductInfo(Long productId, String productName, BigDecimal price, String description, String category,
		boolean isSoldOut, LocalDateTime createdAt) {
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.description = description;
		this.category = category;
		this.isSoldOut = isSoldOut;
		this.createdAt = createdAt;
	}

	public static ProductInfo from(Product product) {
		return ProductInfo.builder()
			.productId(product.getId())
			.productName(product.getName())
			.price(product.getPrice())
			.description(product.getDescription())
			.category(product.getCategory().getDisplayName())
			.isSoldOut(product.getStock() <= 0)
			.createdAt(product.getCreatedAt())
			.build();
	}
}
