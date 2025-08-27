package com.server.ecommerce.service.product;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.server.ecommerce.domain.product.Product;
import com.server.ecommerce.domain.product.ProductCategory;
import com.server.ecommerce.domain.product.ProductRepository;
import com.server.ecommerce.domain.product.condition.ProductSearchCondition;
import com.server.ecommerce.service.product.command.ProductSearchCommand;
import com.server.ecommerce.service.product.info.ProductInfo;
import com.server.ecommerce.support.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@Test
	@DisplayName("상품 검색 조건으로 상품 목록을 조회한다")
	void search_products_with_command() {
		String category = "HOME";
		String keyword = "맥북";
		BigDecimal minPrice = BigDecimal.ZERO;
		BigDecimal maxPrice = BigDecimal.valueOf(100000);
		Pageable pageRequest = PageRequest.of(0, 10);

		ProductSearchCommand command = ProductSearchCommand.builder()
			.category(category)
			.keyword(keyword)
			.minPrice(minPrice)
			.maxPrice(maxPrice)
			.pageable(pageRequest)
			.build();

		when(productRepository.searchProducts(any(ProductSearchCondition.class))).thenReturn(Page.empty());

		Page<ProductInfo> result = productService.searchProducts(command);

		verify(productRepository).searchProducts(any(ProductSearchCondition.class));
		assertThat(result).isNotNull();
		assertThat(result.getContent()).isEmpty();
	}

	@Test
	@DisplayName("상품 검색 결과를 ProductInfo로 변환하여 반환한다")
	void search_products_returns_product_info() {
		Product product1 = Product.create("맥북 프로", BigDecimal.valueOf(2000000), "최신 맥북", ProductCategory.ELECTRONICS, 5);
		Product product2 = Product.create("아이패드", BigDecimal.valueOf(800000), "태블릿", ProductCategory.ELECTRONICS, 0); // 품절

		List<Product> products = Arrays.asList(product1, product2);
		Page<Product> productPage = new PageImpl<>(products, PageRequest.of(0, 10), 2);

		ProductSearchCommand command = ProductSearchCommand.builder()
			.category("ELECTRONICS")
			.pageable(PageRequest.of(0, 10))
			.build();

		when(productRepository.searchProducts(any(ProductSearchCondition.class))).thenReturn(productPage);

		Page<ProductInfo> result = productService.searchProducts(command);

		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getTotalElements()).isEqualTo(2);

		ProductInfo info1 = result.getContent().get(0);
		assertThat(info1.getProductName()).isEqualTo("맥북 프로");
		assertThat(info1.getPrice()).isEqualTo(BigDecimal.valueOf(2000000));
		assertThat(info1.getDescription()).isEqualTo("최신 맥북");
		assertThat(info1.getCategory()).isEqualTo("전자제품");
		assertThat(info1.isSoldOut()).isFalse();

		ProductInfo info2 = result.getContent().get(1);
		assertThat(info2.getProductName()).isEqualTo("아이패드");
		assertThat(info2.getPrice()).isEqualTo(BigDecimal.valueOf(800000));
		assertThat(info2.getDescription()).isEqualTo("태블릿");
		assertThat(info2.getCategory()).isEqualTo("전자제품");
		assertThat(info2.isSoldOut()).isTrue();
	}

	@Test
	@DisplayName("잘못된 카테고리 값으로 검색 시 예외가 발생한다")
	void search_products_throws_exception_with_invalid_category() {
		ProductSearchCommand command = ProductSearchCommand.builder()
			.category("INVALID_CATEGORY")
			.pageable(PageRequest.of(0, 10))
			.build();

		assertThatThrownBy(() -> productService.searchProducts(command))
			.isInstanceOf(BusinessException.class);

		verifyNoInteractions(productRepository);
	}
}
