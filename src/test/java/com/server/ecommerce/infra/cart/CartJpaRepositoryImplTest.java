package com.server.ecommerce.infra.cart;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.cart.Cart;
import com.server.ecommerce.domain.cart.dto.CartWithProductDto;
import com.server.ecommerce.domain.product.Product;
import com.server.ecommerce.domain.product.ProductCategory;
import com.server.ecommerce.infra.product.ProductJpaRepository;

@Transactional
@SpringBootTest
class CartJpaRepositoryImplTest {

	@Autowired
	private CartJpaRepository cartJpaRepository;

	@Autowired
	private ProductJpaRepository productJpaRepository;

	@Test
	@DisplayName("장바구니 조회시 상품정보를 포함해 조회할 수 있다.")
	void findAllByUserId() {

		Long userId = 1L;

		Product product1 = Product.create("맥북 에어", BigDecimal.valueOf(1000000), "최신형 맥북", ProductCategory.ELECTRONICS, 0);
		Product product2 = Product.create("맥북 프로", BigDecimal.valueOf(1000000), "최신형 맥북", ProductCategory.ELECTRONICS, 40);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);

		Cart cart1 = Cart.create(userId, product1.getId(), 3);
		Cart cart2 = Cart.create(userId, product2.getId(), 4);

		cartJpaRepository.save(cart1);
		cartJpaRepository.save(cart2);

		List<CartWithProductDto> cartList = cartJpaRepository.findCartListWithProductByUserId(
			userId);

		assertThat(cartList.get(0).getProductId()).isEqualTo(product1.getId());
		assertThat(cartList.get(0).isSoldOut()).isEqualTo(true);
		assertThat(cartList.get(0).getQuantity()).isEqualTo(3);
		assertThat(cartList.get(0).getProductName()).isEqualTo("맥북 에어");
		assertThat(cartList.get(1).getProductId()).isEqualTo(product2.getId());
		assertThat(cartList.get(1).isSoldOut()).isEqualTo(false);
		assertThat(cartList.get(1).getQuantity()).isEqualTo(4);
		assertThat(cartList.get(1).getProductName()).isEqualTo("맥북 프로");
	}

}
