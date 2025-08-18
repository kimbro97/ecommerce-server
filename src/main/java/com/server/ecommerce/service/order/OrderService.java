package com.server.ecommerce.service.order;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.cart.Cart;
import com.server.ecommerce.domain.cart.CartRepository;
import com.server.ecommerce.domain.order.Order;
import com.server.ecommerce.domain.order.OrderLine;
import com.server.ecommerce.domain.order.OrderRepository;
import com.server.ecommerce.domain.product.Product;
import com.server.ecommerce.domain.product.ProductRepository;
import com.server.ecommerce.service.order.command.CreateOrderCommand;
import com.server.ecommerce.service.order.info.OrderInfo;
import com.server.ecommerce.support.exception.BusinessError;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderInfo createOrder(CreateOrderCommand command) {
        // 1. 선택된 장바구니 아이템들 조회
        List<Cart> selectedCarts = command.getCartIds().stream()
            .map(cartId -> cartRepository.findById(cartId)
                .orElseThrow(BusinessError.CART_NOT_FOUND::exception))
            .collect(Collectors.toList());

        // 2. 사용자 검증 (모든 장바구니가 같은 사용자 것인지 확인)
        validateUserCarts(command.getUserId(), selectedCarts);

        // 3. 재고 확인 및 차감 (비관적 락 사용)
        List<OrderLine> orderLines = processStockAndCreateOrderLines(selectedCarts);

        // 4. 총 금액 계산
        Long totalAmount = orderLines.stream()
            .mapToLong(OrderLine::getTotalPrice)
            .sum();

        // 5. 주문 생성
        Order order = Order.create(command.getUserId(), totalAmount);
        orderLines.forEach(order::addOrderLine);

        // 6. 주문 저장
        Order savedOrder = orderRepository.save(order);

        // 7. 주문 완료된 장바구니 아이템들 삭제
        command.getCartIds().forEach(cartRepository::deleteById);

        // 8. 주문 완료 처리
        savedOrder.complete();

        return OrderInfo.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderInfo> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
            .map(OrderInfo::from)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderInfo getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(BusinessError.ORDER_NOT_FOUND::exception);

        return OrderInfo.from(order);
    }

    @Transactional(readOnly = true)
    public OrderInfo getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(BusinessError.ORDER_NOT_FOUND::exception);

        return OrderInfo.from(order);
    }

    private void validateUserCarts(Long userId, List<Cart> carts) {
        boolean hasInvalidCart = carts.stream()
            .anyMatch(cart -> !cart.getUserId().equals(userId));

        if (hasInvalidCart) {
            throw BusinessError.INVALID_CART_ACCESS.exception();
        }
    }

    private List<OrderLine> processStockAndCreateOrderLines(List<Cart> carts) {
        return carts.stream()
            .map(cart -> {
                // 비관적 락으로 상품 조회
                Product product = productRepository.findByIdWithPessimisticLock(cart.getProductId())
                    .orElseThrow(BusinessError.PRODUCT_NOT_FOUND::exception);

                // 재고 확인 및 차감
                if (!product.hasEnoughStock(cart.getQuantity())) {
                    throw BusinessError.INSUFFICIENT_STOCK.exception();
                }

                product.decreaseStock(cart.getQuantity());
                productRepository.save(product);

                // OrderLine 생성
                return OrderLine.create(
                    cart.getProductId(),
                    cart.getQuantity(),
                    product.getPrice()
                );
            })
            .collect(Collectors.toList());
    }
}
