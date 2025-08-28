package com.server.ecommerce.domain.cart;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.server.ecommerce.support.exception.BusinessException;

class CartTest {

	@Test
	@DisplayName("userId, productId, quantity 값을 받아 새로운 장바구니를 만들 수 있다.")
	void add_cart() {
		Long userId = 1L;
		Long productId = 2L;
		Integer quantity = 5;

		Cart cart = Cart.create(userId, productId, quantity);

		assertThat(cart.getUserId()).isEqualTo(userId);
		assertThat(cart.getProductId()).isEqualTo(productId);
		assertThat(cart.getQuantity()).isEqualTo(quantity);
	}

	@Test
	@DisplayName("수량을 받아서 기존 수량을 증가 시킬 수 있다")
	void increase_quantity() {
		Long userId = 1L;
		Long productId = 2L;
		Integer quantity = 5;
		Integer newQuantity = 1;

		Cart cart = Cart.create(userId, productId, quantity);

		cart.increaseQuantity(newQuantity);

		assertThat(cart.getUserId()).isEqualTo(userId);
		assertThat(cart.getProductId()).isEqualTo(productId);
		assertThat(cart.getQuantity()).isEqualTo(6);
	}

	@Test
	@DisplayName("증가하는 수량이 0이거나 0보다 작다면 예외가 발생한다.")
	void increase_quantity_exception() {
		Long userId = 1L;
		Long productId = 2L;
		Integer quantity = 5;
		Integer newQuantity1 = 0;
		Integer newQuantity2 = -1;

		Cart cart = Cart.create(userId, productId, quantity);

		assertThatThrownBy(() -> cart.increaseQuantity(newQuantity1)).isInstanceOf(BusinessException.class);
		assertThatThrownBy(() -> cart.increaseQuantity(newQuantity2)).isInstanceOf(BusinessException.class);
	}

	@Test
	@DisplayName("수량을 받아서 받은 수량으로 업데이트할 수 있다.")
	void update_quantity() {
		Long userId = 1L;
		Long productId = 2L;
		Integer quantity = 5;
		Integer newQuantity = 10;

		Cart cart = Cart.create(userId, productId, quantity);

		cart.updateQuantity(newQuantity);

		assertThat(cart.getQuantity()).isEqualTo(newQuantity);
	}

	@Test
	@DisplayName("변경하는 수량이 1이거나 1보다 작다면 예외가 발생한다.")
	void update_quantity_exception() {
		Long userId = 1L;
		Long productId = 2L;
		Integer quantity = 5;
		Integer newQuantity1 = 1;
		Integer newQuantity2 = -1;

		Cart cart = Cart.create(userId, productId, quantity);

		assertThatThrownBy(() -> cart.updateQuantity(newQuantity1)).isInstanceOf(BusinessException.class);
		assertThatThrownBy(() -> cart.updateQuantity(newQuantity2)).isInstanceOf(BusinessException.class);
	}
}

