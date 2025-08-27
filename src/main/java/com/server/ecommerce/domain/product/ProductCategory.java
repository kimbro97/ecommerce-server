package com.server.ecommerce.domain.product;

import static com.server.ecommerce.support.exception.BusinessError.*;

import lombok.Getter;

@Getter
public enum ProductCategory {
	ELECTRONICS("전자제품"),
	CLOTHING("의류"),
	BOOKS("도서"),
	HOME("홈&가든"),
	SPORTS("스포츠"),
	FOOD("식품");

	private final String displayName;

	ProductCategory(String displayName) {
		this.displayName = displayName;
	}
}
