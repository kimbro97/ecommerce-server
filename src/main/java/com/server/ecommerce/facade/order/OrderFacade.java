package com.server.ecommerce.facade.order;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.service.cart.CartService;
import com.server.ecommerce.service.cart.command.ClearCartCommand;
import com.server.ecommerce.service.cart.info.CartWithProductInfo;
import com.server.ecommerce.service.order.OrderProduct;
import com.server.ecommerce.service.order.OrderService;
import com.server.ecommerce.service.order.command.CompleteOrderCommand;
import com.server.ecommerce.service.order.command.CreateOrderCommand;
import com.server.ecommerce.service.order.info.OrderInfo;
import com.server.ecommerce.service.payment.PaymentService;
import com.server.ecommerce.service.payment.command.PayCommand;
import com.server.ecommerce.service.payment.info.PaymentInfo;
import com.server.ecommerce.service.product.ProductService;
import com.server.ecommerce.service.product.command.DecreaseStockCommand;
import com.server.ecommerce.service.product.command.ValidateProductCommand;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final CartService cartService;
	private final OrderService orderService;
	private final PaymentService paymentService;
	private final ProductService productService;

	@Transactional
	public OrderResult order(OrderCriteria criteria) {

		// 카트조회
		List<CartWithProductInfo> carts = cartService.findCartsWithoutCache(criteria.getUserId());
		List<Long> productIds = carts.stream()
			.map(CartWithProductInfo::getProductId)
			.toList();

		List<Long> cartIds = carts.stream()
			.map(CartWithProductInfo::getCartId)
			.toList();

		// 상품검증
		ValidateProductCommand validateProductCommand = new ValidateProductCommand(productIds);
		productService.validateProduct(validateProductCommand);

		// 주문생성 PENDING
		List<OrderProduct> products = carts.stream().map(OrderProduct::from).toList();
		CreateOrderCommand createOrderCommand = new CreateOrderCommand(criteria.getUserId(), products);
		OrderInfo orderInfo = orderService.createOrder(createOrderCommand);

		// 결제생성
		PayCommand payCommand = new PayCommand(criteria.getUserId(), orderInfo.getOrderId(), orderInfo.getTotalPrice());
		PaymentInfo paymentInfo = paymentService.pay(payCommand);

		// 재고차감
		DecreaseStockCommand decreaseStockCommand = new DecreaseStockCommand(products);
		productService.decreaseStock(decreaseStockCommand);

		// 카트 clear
		cartService.clearCart(new ClearCartCommand(criteria.getUserId(),cartIds));

		// 주문완료 PAID
		CompleteOrderCommand completeOrderCommand = new CompleteOrderCommand(orderInfo.getOrderId());
		orderInfo = orderService.completeOrder(completeOrderCommand);

		return OrderResult.from(orderInfo, paymentInfo);
	}

}
