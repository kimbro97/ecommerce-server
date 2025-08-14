package com.server.ecommerce.interfaces.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.server.ecommerce.service.product.ProductService;
import com.server.ecommerce.service.product.info.ProductInfo;
import com.server.ecommerce.support.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<Page<ProductInfo>>> getProductList(
        @RequestParam(defaultValue = "0") int page) {
        
        Pageable pageable = PageRequest.of(page, 10);
        Page<ProductInfo> products = productService.getProductList(pageable);
        
        return ApiResponse.OK(products);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductInfo>> getProductDetail(@PathVariable Long productId) {
        ProductInfo product = productService.getProductDetail(productId);
        return ApiResponse.OK(product);
    }
}