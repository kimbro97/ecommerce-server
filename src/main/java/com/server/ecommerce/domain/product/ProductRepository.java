package com.server.ecommerce.domain.product;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {
    
    Page<Product> findAll(Pageable pageable);
    
    Optional<Product> findById(Long productId);
}