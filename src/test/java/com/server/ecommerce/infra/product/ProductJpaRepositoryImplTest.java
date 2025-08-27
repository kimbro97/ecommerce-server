package com.server.ecommerce.infra.product;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.product.Product;
import com.server.ecommerce.domain.product.ProductCategory;
import com.server.ecommerce.domain.product.condition.ProductSearchCondition;

@Transactional
@SpringBootTest
class ProductJpaRepositoryImplTest {

	@Autowired
	private ProductJpaRepositoryImpl productJpaRepositoryImpl;

	@Autowired
	private ProductJpaRepository productJpaRepository;

	@Test
	@DisplayName("전체 조건으로 상품을 검색한다")
	void search_products_with_all_conditions() {
		Product product1 = Product.create("맥북 프로 16인치", BigDecimal.valueOf(1000000), "최신 맥북", ProductCategory.ELECTRONICS, 50);
		Product product2 = Product.create("맥북 에어 16인치", BigDecimal.valueOf(900000), "최신 맥북", ProductCategory.ELECTRONICS, 50);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);

		ProductSearchCondition condition = new ProductSearchCondition("ELECTRONICS", "맥북", BigDecimal.valueOf(10000), BigDecimal.valueOf(999999), PageRequest.of(0, 10));

		Page<Product> products = productJpaRepositoryImpl.searchProducts(condition);
		assertThat(products.getContent()).hasSize(1);
		assertThat(products.getContent().get(0).getName()).isEqualTo("맥북 에어 16인치");
	}

	@Test
	@DisplayName("카테고리만으로 상품을 검색한다")
	void search_products_by_category_only() {
		Product product1 = Product.create("맥북 프로 16인치", BigDecimal.valueOf(1000000), "최신 맥북", ProductCategory.ELECTRONICS, 50);
		Product product2 = Product.create("운동화", BigDecimal.valueOf(100000), "나이키 운동화", ProductCategory.SPORTS, 30);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);

		ProductSearchCondition condition = new ProductSearchCondition("ELECTRONICS", null, null, null, PageRequest.of(0, 10));

		Page<Product> products = productJpaRepositoryImpl.searchProducts(condition);
		assertThat(products.getContent()).hasSize(1);
		assertThat(products.getContent().get(0).getName()).isEqualTo("맥북 프로 16인치");
	}

	@Test
	@DisplayName("키워드만으로 상품을 검색한다")
	void search_products_by_keyword_only() {
		Product product1 = Product.create("맥북 프로 16인치", BigDecimal.valueOf(1000000), "최신 맥북", ProductCategory.ELECTRONICS, 50);
		Product product2 = Product.create("아이패드 프로", BigDecimal.valueOf(800000), "최신 태블릿", ProductCategory.ELECTRONICS, 30);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);

		ProductSearchCondition condition = new ProductSearchCondition(null, "맥북", null, null, PageRequest.of(0, 10));

		Page<Product> products = productJpaRepositoryImpl.searchProducts(condition);
		assertThat(products.getContent()).hasSize(1);
		assertThat(products.getContent().get(0).getName()).isEqualTo("맥북 프로 16인치");
	}

	@Test
	@DisplayName("가격 범위만으로 상품을 검색한다")
	void search_products_by_price_range_only() {
		Product product1 = Product.create("맥북 프로 16인치", BigDecimal.valueOf(1000000), "최신 맥북", ProductCategory.ELECTRONICS, 50);
		Product product2 = Product.create("아이패드 프로", BigDecimal.valueOf(800000), "최신 태블릿", ProductCategory.ELECTRONICS, 30);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);

		ProductSearchCondition condition = new ProductSearchCondition(null, null, BigDecimal.valueOf(500000), BigDecimal.valueOf(900000), PageRequest.of(0, 10));

		Page<Product> products = productJpaRepositoryImpl.searchProducts(condition);
		assertThat(products.getContent()).hasSize(1);
		assertThat(products.getContent().get(0).getName()).isEqualTo("아이패드 프로");
	}

	@Test
	@DisplayName("카테고리와 키워드로 상품을 검색한다")
	void search_products_by_category_and_keyword() {
		Product product1 = Product.create("맥북 프로 16인치", BigDecimal.valueOf(1000000), "최신 맥북", ProductCategory.ELECTRONICS, 50);
		Product product2 = Product.create("맥북 케이스", BigDecimal.valueOf(50000), "맥북용 케이스", ProductCategory.CLOTHING, 100);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);

		ProductSearchCondition condition = new ProductSearchCondition("ELECTRONICS", "맥북", null, null, PageRequest.of(0, 10));

		Page<Product> products = productJpaRepositoryImpl.searchProducts(condition);
		assertThat(products.getContent()).hasSize(1);
		assertThat(products.getContent().get(0).getName()).isEqualTo("맥북 프로 16인치");
	}

	@Test
	@DisplayName("카테고리와 가격범위로 상품을 검색한다")
	void search_products_by_category_and_price_range() {
		Product product1 = Product.create("맥북 프로 16인치", BigDecimal.valueOf(1000000), "최신 맥북", ProductCategory.ELECTRONICS, 50);
		Product product2 = Product.create("아이패드 프로", BigDecimal.valueOf(800000), "최신 태블릿", ProductCategory.ELECTRONICS, 30);
		Product product3 = Product.create("운동화", BigDecimal.valueOf(700000), "명품 운동화", ProductCategory.SPORTS, 20);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);
		productJpaRepository.save(product3);

		ProductSearchCondition condition = new ProductSearchCondition("ELECTRONICS", null, BigDecimal.valueOf(500000), BigDecimal.valueOf(900000), PageRequest.of(0, 10));

		Page<Product> products = productJpaRepositoryImpl.searchProducts(condition);
		assertThat(products.getContent()).hasSize(1);
		assertThat(products.getContent().get(0).getName()).isEqualTo("아이패드 프로");
	}

	@Test
	@DisplayName("키워드와 가격범위로 상품을 검색한다")
	void search_products_by_keyword_and_price_range() {
		Product product1 = Product.create("맥북 프로 16인치", BigDecimal.valueOf(1000000), "최신 맥북", ProductCategory.ELECTRONICS, 50);
		Product product2 = Product.create("맥북 에어 13인치", BigDecimal.valueOf(800000), "경량 맥북", ProductCategory.ELECTRONICS, 30);
		Product product3 = Product.create("맥북 케이스", BigDecimal.valueOf(50000), "맥북용 케이스", ProductCategory.CLOTHING, 100);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);
		productJpaRepository.save(product3);

		ProductSearchCondition condition = new ProductSearchCondition(null, "맥북", BigDecimal.valueOf(500000), BigDecimal.valueOf(900000), PageRequest.of(0, 10));

		Page<Product> products = productJpaRepositoryImpl.searchProducts(condition);
		assertThat(products.getContent()).hasSize(1);
		assertThat(products.getContent().get(0).getName()).isEqualTo("맥북 에어 13인치");
	}

	@Test
	@DisplayName("조건 없이 전체 상품을 검색한다")
	void search_all_products_without_condition() {
		Product product1 = Product.create("맥북 프로 16인치", BigDecimal.valueOf(1000000), "최신 맥북", ProductCategory.ELECTRONICS, 50);
		Product product2 = Product.create("아이패드 프로", BigDecimal.valueOf(800000), "최신 태블릿", ProductCategory.ELECTRONICS, 30);
		Product product3 = Product.create("운동화", BigDecimal.valueOf(100000), "나이키 운동화", ProductCategory.SPORTS, 20);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);
		productJpaRepository.save(product3);

		ProductSearchCondition condition = new ProductSearchCondition(null, null, null, null, PageRequest.of(0, 10));

		Page<Product> products = productJpaRepositoryImpl.searchProducts(condition);
		assertThat(products.getContent()).hasSize(3);
	}

	@Test
	@DisplayName("페이징이 적용된 상품을 검색한다")
	void search_products_with_paging() {
		for (int i = 1; i <= 5; i++) {
			Product product = Product.create("상품" + i, BigDecimal.valueOf(100000 * i), "상품 설명" + i, ProductCategory.ELECTRONICS, 10);
			productJpaRepository.save(product);
		}

		ProductSearchCondition condition = new ProductSearchCondition(null, null, null, null, PageRequest.of(0, 3));

		Page<Product> products = productJpaRepositoryImpl.searchProducts(condition);
		assertThat(products.getContent()).hasSize(3);
		assertThat(products.getTotalElements()).isEqualTo(5);
		assertThat(products.getTotalPages()).isEqualTo(2);
	}

	@Test
	@DisplayName("생성일시 역순으로 정렬된 상품을 조회한다")
	void search_products_sorted_by_created_at_desc() {
		Product product1 = Product.create("첫번째 상품", BigDecimal.valueOf(100000), "첫번째", ProductCategory.ELECTRONICS, 10);
		Product product2 = Product.create("두번째 상품", BigDecimal.valueOf(200000), "두번째", ProductCategory.ELECTRONICS, 20);
		Product product3 = Product.create("세번째 상품", BigDecimal.valueOf(300000), "세번째", ProductCategory.ELECTRONICS, 30);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);
		productJpaRepository.save(product3);

		ProductSearchCondition condition = new ProductSearchCondition(null, null, null, null, PageRequest.of(0, 10));

		Page<Product> products = productJpaRepositoryImpl.searchProducts(condition);
		assertThat(products.getContent()).hasSize(3);
		assertThat(products.getContent().get(0).getName()).isEqualTo("세번째 상품");
		assertThat(products.getContent().get(1).getName()).isEqualTo("두번째 상품");
		assertThat(products.getContent().get(2).getName()).isEqualTo("첫번째 상품");
	}

	@Test
	@DisplayName("검색 결과가 없는 경우 빈 페이지를 반환한다")
	void search_products_returns_empty_page_when_no_results() {
		Product product = Product.create("맥북 프로", BigDecimal.valueOf(1000000), "최신 맥북", ProductCategory.ELECTRONICS, 50);
		productJpaRepository.save(product);

		ProductSearchCondition condition = new ProductSearchCondition(null, "존재하지않는상품", null, null, PageRequest.of(0, 10));

		Page<Product> products = productJpaRepositoryImpl.searchProducts(condition);
		assertThat(products.getContent()).isEmpty();
		assertThat(products.getTotalElements()).isEqualTo(0);
	}

	@Test
	@DisplayName("null 카테고리는 무시하고 검색한다")
	void search_products_ignores_null_category() {
		Product product1 = Product.create("맥북 프로", BigDecimal.valueOf(1000000), "최신 맥북", ProductCategory.ELECTRONICS, 50);
		Product product2 = Product.create("운동화", BigDecimal.valueOf(100000), "나이키", ProductCategory.SPORTS, 30);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);

		ProductSearchCondition condition = new ProductSearchCondition(null, "맥북", null, null, PageRequest.of(0, 10));

		Page<Product> products = productJpaRepositoryImpl.searchProducts(condition);
		assertThat(products.getContent()).hasSize(1);
		assertThat(products.getContent().get(0).getName()).isEqualTo("맥북 프로");
	}

	@Test
	@DisplayName("빈 키워드는 무시하고 검색한다")
	void search_products_ignores_empty_keyword() {
		Product product1 = Product.create("맥북 프로", BigDecimal.valueOf(1000000), "최신 맥북", ProductCategory.ELECTRONICS, 50);
		Product product2 = Product.create("운동화", BigDecimal.valueOf(100000), "나이키", ProductCategory.SPORTS, 30);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);

		ProductSearchCondition condition = new ProductSearchCondition("ELECTRONICS", "", null, null, PageRequest.of(0, 10));

		Page<Product> products = productJpaRepositoryImpl.searchProducts(condition);
		assertThat(products.getContent()).hasSize(1);
		assertThat(products.getContent().get(0).getName()).isEqualTo("맥북 프로");
	}

	@Test
	@DisplayName("null 가격은 무시하고 검색한다")
	void search_products_ignores_null_price() {
		Product product1 = Product.create("맥북 프로", BigDecimal.valueOf(1000000), "최신 맥북", ProductCategory.ELECTRONICS, 50);
		Product product2 = Product.create("아이패드", BigDecimal.valueOf(800000), "태블릿", ProductCategory.ELECTRONICS, 30);

		productJpaRepository.save(product1);
		productJpaRepository.save(product2);

		ProductSearchCondition condition = new ProductSearchCondition("ELECTRONICS", "맥북", null, null, PageRequest.of(0, 10));

		Page<Product> products = productJpaRepositoryImpl.searchProducts(condition);
		assertThat(products.getContent()).hasSize(1);
		assertThat(products.getContent().get(0).getName()).isEqualTo("맥북 프로");
	}

	@Test
	@DisplayName("페이지 총 개수를 정확히 계산한다")
	void search_products_calculates_total_count() {
		for (int i = 1; i <= 7; i++) {
			Product product = Product.create("상품" + i, BigDecimal.valueOf(100000), "설명" + i, ProductCategory.ELECTRONICS, 10);
			productJpaRepository.save(product);
		}

		ProductSearchCondition condition = new ProductSearchCondition("ELECTRONICS", null, null, null, PageRequest.of(0, 3));

		Page<Product> products = productJpaRepositoryImpl.searchProducts(condition);
		assertThat(products.getContent()).hasSize(3);
		assertThat(products.getTotalElements()).isEqualTo(7);
		assertThat(products.getTotalPages()).isEqualTo(3);
		assertThat(products.hasNext()).isTrue();
	}
}
