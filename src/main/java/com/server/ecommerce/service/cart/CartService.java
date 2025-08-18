package com.server.ecommerce.service.cart;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.cart.Cart;
import com.server.ecommerce.domain.cart.CartRepository;
import com.server.ecommerce.service.cart.command.AddCartCommand;
import com.server.ecommerce.service.cart.command.UpdateCartCommand;
import com.server.ecommerce.service.cart.info.CartInfo;
import com.server.ecommerce.service.product.ProductService;
import com.server.ecommerce.service.product.info.ProductInfo;
import com.server.ecommerce.support.exception.BusinessError;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final ProductService productService;

	@Transactional
	public CartInfo addCart(AddCartCommand command) {
		ProductInfo productInfo = productService.getProductDetail(command.getProductId());
		
		Optional<Cart> existingCart = cartRepository.findByUserIdAndProductId(
			command.getUserId(), command.getProductId());

		if (existingCart.isPresent()) {
			Cart cart = existingCart.get();
			cart.updateQuantity(cart.getQuantity() + command.getQuantity());
			Cart savedCart = cartRepository.save(cart);
			return CartInfo.from(savedCart, productInfo);
		} else {
			Cart cart = Cart.create(command.getUserId(), command.getProductId(), command.getQuantity());
			Cart savedCart = cartRepository.save(cart);
			return CartInfo.from(savedCart, productInfo);
		}
	}

	@Transactional
	public CartInfo updateCart(UpdateCartCommand command) {
		Cart cart = cartRepository.findById(command.getCartId())
			.orElseThrow(() -> BusinessError.CART_NOT_FOUND.exception());

		cart.updateQuantity(command.getQuantity());
		Cart savedCart = cartRepository.save(cart);
		
		ProductInfo productInfo = productService.getProductDetail(savedCart.getProductId());
		return CartInfo.from(savedCart, productInfo);
	}

	@Transactional
	public void deleteCart(Long cartId) {
		Cart cart = cartRepository.findById(cartId)
			.orElseThrow(() -> BusinessError.CART_NOT_FOUND.exception());

		cartRepository.deleteById(cartId);
	}

	@Transactional(readOnly = true)
	public List<CartInfo> getUserCarts(Long userId) {
		List<Cart> carts = cartRepository.findByUserId(userId);
		return carts.stream()
			.map(cart -> {
				ProductInfo productInfo = productService.getProductDetail(cart.getProductId());
				return CartInfo.from(cart, productInfo);
			})
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CartInfo getCart(Long cartId) {
		Cart cart = cartRepository.findById(cartId)
			.orElseThrow(() -> BusinessError.CART_NOT_FOUND.exception());

		ProductInfo productInfo = productService.getProductDetail(cart.getProductId());
		return CartInfo.from(cart, productInfo);
	}
}