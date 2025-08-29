package com.server.ecommerce.domain.product;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.server.ecommerce.support.exception.BusinessException;

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

	@Test
	@DisplayName("decreaseStock 메서드로 재고를 감소 시킬 수 있다.")
	void decrease_stock() {

		String name = "맥북 프로 16인치";
		BigDecimal price = BigDecimal.valueOf(2000000);
		String description = "최신 맥북 프로";
		ProductCategory category = ProductCategory.ELECTRONICS;
		Integer stock = 10;

		Product product = Product.create(name, price, description, category, stock);
		product.decreaseStock(3);
		assertThat(product.getStock()).isEqualTo(stock - 3);

	}

	@Test
	@DisplayName("decreaseStock 메서드로 재고를 감소 시킬 때 품절된 상품이거나 수량이 부족하면 예외가 던져진다")
	void decrease_stock_exception() {

		String name = "맥북 프로 16인치";
		BigDecimal price = BigDecimal.valueOf(2000000);
		String description = "최신 맥북 프로";
		ProductCategory category = ProductCategory.ELECTRONICS;
		Integer stock = 0;

		Product product = Product.create(name, price, description, category, stock);

		assertThatThrownBy(() -> product.decreaseStock(9)).isInstanceOf(BusinessException.class);

	}
}
