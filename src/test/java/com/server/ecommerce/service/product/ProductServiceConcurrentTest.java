package com.server.ecommerce.service.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.server.ecommerce.domain.product.Product;
import com.server.ecommerce.domain.product.ProductCategory;
import com.server.ecommerce.infra.product.ProductJpaRepository;
import com.server.ecommerce.service.order.OrderProduct;
import com.server.ecommerce.service.product.command.DecreaseStockCommand;

@SpringBootTest
class ProductServiceConcurrentTest {

	@Autowired
	private ProductJpaRepository productJpaRepository;

	@Autowired
	private ProductService productService;

	@Test
	@DisplayName("하나의 상품을 여러명의 사람들이 동시에 재고를 차감하면 정확히 차감되어야한다")
	void decease_stock_concurrent() throws InterruptedException {
		Product product = Product.create("맥북 프로", BigDecimal.valueOf(1000000), "최신형 맥북", ProductCategory.ELECTRONICS, 40);

		productJpaRepository.save(product);

		int threadCount = 39;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		AtomicInteger successCount = new AtomicInteger(0);
		AtomicInteger failCount = new AtomicInteger(0);
		OrderProduct orderProduct = new OrderProduct(product.getId(), 1, BigDecimal.valueOf(1000000));
		List<OrderProduct> products  = List.of(orderProduct);

		// act
		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					productService.decreaseStock(new DecreaseStockCommand(products));
					successCount.incrementAndGet();
				} catch (Exception e) {
					failCount.incrementAndGet();
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		Product product2 = productJpaRepository.findById(product.getId()).orElseThrow();

		Assertions.assertThat(product2.getStock()).isEqualTo(1);


	}
}
