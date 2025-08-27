package com.server.ecommerce.infra.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.ecommerce.domain.product.Product;

public interface ProductJpaRepository extends JpaRepository<Product, Long>,  ProductJpaRepositoryCustom {
}
