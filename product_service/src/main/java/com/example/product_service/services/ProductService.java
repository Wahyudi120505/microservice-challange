package com.example.product_service.services;

import java.util.List;

import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(String id, ProductRequest request);
    void deleteProduct(String id);
    ProductResponse getProductById(String id);
    List<ProductResponse> getAllProducts();
}
