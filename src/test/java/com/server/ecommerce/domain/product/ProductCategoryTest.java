package com.server.ecommerce.domain.product;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.server.ecommerce.support.exception.BusinessException;

class ProductCategoryTest {

	@Test
	@DisplayName("유효한 카테고리 문자열을 파싱한다")
	void parse_valid_category() {
		ProductCategory result = ProductCategory.parseCategory("ELECTRONICS");
		
		assertThat(result).isEqualTo(ProductCategory.ELECTRONICS);
	}

	@Test
	@DisplayName("null 입력시 null을 반환한다")
	void parse_null_category() {
		ProductCategory result = ProductCategory.parseCategory(null);
		
		assertThat(result).isNull();
	}

	@Test
	@DisplayName("잘못된 카테고리 문자열시 예외를 발생시킨다")
	void parse_invalid_category() {
		assertThatThrownBy(() -> ProductCategory.parseCategory("INVALID"))
			.isInstanceOf(BusinessException.class);
	}
}
