package com.server.ecommerce.service.cart;

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
import com.server.ecommerce.infra.cart.CartJpaRepository;
import com.server.ecommerce.infra.product.ProductJpaRepository;
import com.server.ecommerce.service.cart.command.AddCartCommand;
import com.server.ecommerce.service.cart.info.CartInfo;
import com.server.ecommerce.service.cart.info.CartWithProductInfo;
import com.server.ecommerce.support.exception.BusinessException;

@Transactional
@SpringBootTest
class CartServiceTest {

	@Autowired
	private CartService cartService;

	@Autowired
	private ProductJpaRepository productJpaRepository;

	@Autowired
	private CartJpaRepository cartJpaRepository;

	@Test
	@DisplayName("기존에 없는 상품을 장바구니에 추가하면 새로운 장바구니가 만들어진다.")
	void add_cart_first() {

		Long userId = 1L;
		Long productId = 1L;
		Integer quantity = 4;

		AddCartCommand command = AddCartCommand.builder()
			.userId(userId)
			.productId(productId)
			.quantity(quantity)
			.build();

		CartInfo cartInfo = cartService.addCart(command);

		assertThat(cartInfo.getUserId()).isEqualTo(userId);
		assertThat(cartInfo.getProductId()).isEqualTo(productId);
		assertThat(cartInfo.getQuantity()).isEqualTo(quantity);
	}

	@Test
	@DisplayName("기존에 수량이 4인 장바구니에 2를 추가하면 총 수량은 6이된다.")
	void add_cart_second() {

		Long userId = 1L;
		Long productId = 1L;
		Integer quantity = 4;

		Cart cart = Cart.create(userId, productId, quantity);

		cartJpaRepository.save(cart);

		Integer updateQuantity = 2;
		AddCartCommand command = AddCartCommand.builder()
			.userId(userId)
			.productId(productId)
			.quantity(updateQuantity)
			.build();

		CartInfo cartInfo = cartService.addCart(command);

		assertThat(cartInfo.getUserId()).isEqualTo(userId);
		assertThat(cartInfo.getProductId()).isEqualTo(productId);
		assertThat(cartInfo.getQuantity()).isEqualTo(quantity + updateQuantity);
	}

	@Test
	@DisplayName("기존에 수량이 4인 장바구니에 -2를 추가하면 예외가 발생한다.")
	void add_cart_exception() {

		Long userId = 1L;
		Long productId = 1L;
		Integer quantity = 4;

		Cart cart = Cart.create(userId, productId, quantity);

		cartJpaRepository.save(cart);

		Integer updateQuantity = -2;
		AddCartCommand command = AddCartCommand.builder()
			.userId(userId)
			.productId(productId)
			.quantity(updateQuantity)
			.build();

		assertThatThrownBy(() -> cartService.addCart(command)).isInstanceOf(BusinessException.class)
			.hasMessageContaining("유효하지 않은 수량입니다");
	}

	@Test
	@DisplayName("userId로 장바구니를 상품정보와함께 조회할 수 있다")
	void find_carts() {
		Long userId = 1L;

		Product product1 = Product.create("맥북 에어", BigDecimal.valueOf(1000000), "최신형 맥북", ProductCategory.ELECTRONICS, 0);
		Product product2 = Product.create("맥북 프로", BigDecimal.valueOf(1000000), "최신형 맥북", ProductCategory.ELECTRONICS, 40);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);

		Cart cart1 = Cart.create(userId, product1.getId(), 3);
		Cart cart2 = Cart.create(userId, product2.getId(), 4);

		cartJpaRepository.save(cart1);
		cartJpaRepository.save(cart2);

		List<CartWithProductInfo> carts = cartService.findCarts(userId);

		assertThat(carts).hasSize(2);
		assertThat(carts.get(0).getUserId()).isEqualTo(userId);
		assertThat(carts.get(0).getProductId()).isEqualTo(product1.getId());
		assertThat(carts.get(0).getQuantity()).isEqualTo(3);
		assertThat(carts.get(0).isSoldOut()).isEqualTo(true);
		assertThat(carts.get(1).getUserId()).isEqualTo(userId);
		assertThat(carts.get(1).getProductId()).isEqualTo(product2.getId());
		assertThat(carts.get(1).getQuantity()).isEqualTo(4);
		assertThat(carts.get(1).isSoldOut()).isEqualTo(false);

	}

	@Test
	@DisplayName("userId와 cartId를 받아서 장바구니를 삭제할 수 있다.")
	void delete_cart() {
		Long userId = 1L;

		Product product1 = Product.create("맥북 에어", BigDecimal.valueOf(1000000), "최신형 맥북", ProductCategory.ELECTRONICS, 0);
		Product product2 = Product.create("맥북 프로", BigDecimal.valueOf(1000000), "최신형 맥북", ProductCategory.ELECTRONICS, 40);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);

		Cart cart1 = Cart.create(userId, product1.getId(), 3);
		Cart cart2 = Cart.create(userId, product2.getId(), 4);

		cartJpaRepository.save(cart1);
		cartJpaRepository.save(cart2);

		List<CartWithProductDto> carts1 = cartJpaRepository.findCartListWithProductByUserId(userId);

		assertThat(carts1).hasSize(2);

		DeleteCartCommand command = new DeleteCartCommand(userId, cart1.getId());
		cartService.deleteCart(command);


		List<CartWithProductDto> carts2 = cartJpaRepository.findCartListWithProductByUserId(userId);

		assertThat(carts2).hasSize(1);
	}

	@Test
	@DisplayName("장바구니 정보가 없다면 예외가 발생한다")
	void delete_cart_exception() {
		Long userId = 1L;
		Long cartId = 1L;

		DeleteCartCommand command = new DeleteCartCommand(userId, cartId);

		assertThatThrownBy(() -> cartService.deleteCart(command))
			.isInstanceOf(BusinessException.class)
			.hasMessageContaining("해당 장바구니를 찾을 수 없습니다")
		;
	}
}
