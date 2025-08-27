package com.server.ecommerce.domain.product;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

	@Test
	@DisplayName("정상적인 파라미터로 상품을 생성한다")
	void create_product_with_parameters() {
		String name = "맥북 프로 16인치";
		BigDecimal price = BigDecimal.valueOf(2000000);
		String description = "최신 맥북 프로";
		ProductCategory category = ProductCategory.ELECTRONICS;
		Integer stock = 10;

		Product product = Product.create(name, price, description, category, stock);

		assertThat(product).isNotNull();
		assertThat(product.getName()).isEqualTo(name);
		assertThat(product.getPrice()).isEqualTo(price);
		assertThat(product.getDescription()).isEqualTo(description);
		assertThat(product.getCategory()).isEqualTo(category);
		assertThat(product.getStock()).isEqualTo(stock);
	}
}
