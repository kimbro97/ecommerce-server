package com.server.ecommerce.domain.product;

import com.server.ecommerce.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Long price;

    @Column(columnDefinition = "TEXT")
    private String content;

    public Product(String name, Integer stock, Long price, String content) {
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.content = content;
    }

    public static Product create(String name, Integer stock, Long price, String content) {
        return new Product(name, stock, price, content);
    }
}