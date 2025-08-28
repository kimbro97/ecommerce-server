package com.server.ecommerce.service.product;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.product.Product;
import com.server.ecommerce.domain.product.ProductCategory;
import com.server.ecommerce.infra.cart.CartJpaRepository;
import com.server.ecommerce.infra.product.ProductJpaRepository;
import com.server.ecommerce.support.exception.BusinessException;

@Transactional
@SpringBootTest
public class ProductServiceIntegrationTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private CartJpaRepository cartJpaRepository;

	@Autowired
	private ProductJpaRepository productJpaRepository;


	@Test
	@DisplayName("product_id값들을 받아서 상품을 검증할 때 productIds 수량과 검색된 상품의 수량이 다르면 예외가 발생한다")
	void validate_products_exception() {

		Product product1 = Product.create("맥북 프로", BigDecimal.valueOf(1000000), "최신형 맥북", ProductCategory.ELECTRONICS, 40);

		productJpaRepository.save(product1);

		ValidateProductCommand command = new ValidateProductCommand(List.of(product1.getId(), 10L));
		Assertions.assertThatThrownBy(() ->
			productService.validateProduct(command)).isInstanceOf(BusinessException.class);

	}
}
