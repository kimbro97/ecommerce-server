package com.server.ecommerce.service.cart;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.cart.Cart;
import com.server.ecommerce.infra.cart.CartJpaRepository;
import com.server.ecommerce.service.cart.command.AddCartCommand;
import com.server.ecommerce.service.cart.info.CartInfo;
import com.server.ecommerce.support.exception.BusinessException;

@Transactional
@SpringBootTest
class CartServiceTest {

	@Autowired
	private CartService cartService;

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
}
