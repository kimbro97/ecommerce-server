package com.server.ecommerce.infra.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.ecommerce.domain.product.Product;

public interface ProductJpaRepository extends JpaRepository<Product, Long>,  ProductJpaRepositoryCustom {
	List<Product> findAllByIdIn(List<Long> ids);
}
