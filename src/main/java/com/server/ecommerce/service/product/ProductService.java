package com.server.ecommerce.service.product;

import static com.server.ecommerce.support.exception.BusinessError.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.product.Product;
import com.server.ecommerce.domain.product.ProductRepository;
import com.server.ecommerce.service.product.info.ProductInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductInfo> getProductList(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductInfo::from);
    }

    public ProductInfo getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        
        return ProductInfo.from(product);
    }
}