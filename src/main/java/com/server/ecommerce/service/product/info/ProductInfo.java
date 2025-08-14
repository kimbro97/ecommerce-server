package com.server.ecommerce.service.product.info;

import com.server.ecommerce.domain.product.Product;

import lombok.Getter;

@Getter
public class ProductInfo {
    
    private final Long productId;
    private final String name;
    private final Integer stock;
    private final Long price;
    private final String content;
    
    public ProductInfo(Long productId, String name, Integer stock, Long price, String content) {
        this.productId = productId;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.content = content;
    }
    
    public static ProductInfo from(Product product) {
        return new ProductInfo(
            product.getId(),
            product.getName(),
            product.getStock(),
            product.getPrice(),
            product.getContent()
        );
    }
}