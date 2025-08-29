package com.server.ecommerce.service.product;

import static org.assertj.core.api.Assertions.*;

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
import com.server.ecommerce.service.order.OrderProduct;
import com.server.ecommerce.service.product.command.DecreaseStockCommand;
import com.server.ecommerce.service.product.command.ValidateProductCommand;
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
		assertThatThrownBy(() ->
			productService.validateProduct(command)).isInstanceOf(BusinessException.class);

	}

	@Test
	@DisplayName("주문 상품 정보를 받아서 주문한 수량만큼 재고를 차감할 수 있다.")
	void decrease_stock() {

		Product product1 = Product.create("맥북 프로1", BigDecimal.valueOf(1000000), "최신형 맥북", ProductCategory.ELECTRONICS, 40);
		Product product2 = Product.create("맥북 프로2", BigDecimal.valueOf(1000000), "최신형 맥북", ProductCategory.ELECTRONICS, 30);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);

		OrderProduct orderProduct1 = new OrderProduct(product1.getId(), 5, BigDecimal.valueOf(1000000));
		OrderProduct orderProduct2 = new OrderProduct(product2.getId(), 2, BigDecimal.valueOf(1000000));

		productService.decreaseStock(new DecreaseStockCommand(List.of(orderProduct1, orderProduct2)));

		product1 = productJpaRepository.findById(product1.getId()).orElseThrow();
		product2 = productJpaRepository.findById(product2.getId()).orElseThrow();

		assertThat(product1.getStock()).isEqualTo(35);
		assertThat(product2.getStock()).isEqualTo(28);
	}

	@Test
	@DisplayName("재고 차감시 품절이면 예외가 발생한다")
	void decrease_stock_exception() {

		Product product1 = Product.create("맥북 프로1", BigDecimal.valueOf(1000000), "최신형 맥북", ProductCategory.ELECTRONICS, 0);
		Product product2 = Product.create("맥북 프로2", BigDecimal.valueOf(1000000), "최신형 맥북", ProductCategory.ELECTRONICS, 30);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);

		OrderProduct orderProduct1 = new OrderProduct(product1.getId(), 5, BigDecimal.valueOf(1000000));
		OrderProduct orderProduct2 = new OrderProduct(product2.getId(), 2, BigDecimal.valueOf(1000000));

		assertThatThrownBy(() -> productService.decreaseStock(
			new DecreaseStockCommand(List.of(orderProduct1, orderProduct2))))
			.isInstanceOf(BusinessException.class);
	}

	@Test
	@DisplayName("재고 차감시 주문한 수량보다 재고가 적으면 예외가 발생한다")
	void decrease_stock_exception2() {

		Product product1 = Product.create("맥북 프로1", BigDecimal.valueOf(1000000), "최신형 맥북", ProductCategory.ELECTRONICS, 4);
		Product product2 = Product.create("맥북 프로2", BigDecimal.valueOf(1000000), "최신형 맥북", ProductCategory.ELECTRONICS, 30);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);

		OrderProduct orderProduct1 = new OrderProduct(product1.getId(), 5, BigDecimal.valueOf(1000000));
		OrderProduct orderProduct2 = new OrderProduct(product2.getId(), 2, BigDecimal.valueOf(1000000));

		assertThatThrownBy(() -> productService.decreaseStock(
			new DecreaseStockCommand(List.of(orderProduct1, orderProduct2))))
			.isInstanceOf(BusinessException.class);
	}

}
