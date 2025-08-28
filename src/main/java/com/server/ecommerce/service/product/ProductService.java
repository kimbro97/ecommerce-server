package com.server.ecommerce.service.product;

import static com.server.ecommerce.support.exception.BusinessError.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.product.Product;
import com.server.ecommerce.domain.product.ProductRepository;
import com.server.ecommerce.service.order.OrderProduct;
import com.server.ecommerce.service.product.command.DecreaseStockCommand;
import com.server.ecommerce.service.product.command.ProductSearchCommand;
import com.server.ecommerce.service.product.command.ValidateProductCommand;
import com.server.ecommerce.service.product.info.ProductInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

	private final ProductRepository productRepository;

	public Page<ProductInfo> searchProducts(ProductSearchCommand command) {
		Page<Product> products = productRepository.searchProducts(command.toCondition());
		return products.map(ProductInfo::from);
	}

	public void validateProduct(ValidateProductCommand command) {
		List<Product> products = productRepository.findAllByIds(command.getProductIds());

		if (command.getProductIds().size() != products.size()) {
			throw PRODUCT_NOT_FOUND.exception();
		}
	}

	@Transactional
	public void decreaseStock(DecreaseStockCommand command) {

		List<Long> productIds = command.getProducts().stream().map(OrderProduct::getProductId).toList();

		List<Product> products = productRepository.findAllByIdsWithLock(productIds);

		for (OrderProduct orderProduct : command.getProducts()) {
			Product product = products.stream()
				.filter(p -> p.getId().equals(orderProduct.getProductId()))
				.findFirst()
				.orElseThrow(PRODUCT_NOT_FOUND::exception);

			product.decreaseStock(orderProduct.getQuantity());
		}
	}
}
