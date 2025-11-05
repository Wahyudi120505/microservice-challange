package com.example.cart_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.cart_service.dto.product.ProductDto;

@FeignClient(name = "product-service", url = "http://localhost:8081/api/products")
public interface ProductClient {
    @GetMapping("/{id}")
    ProductDto getProductById(@PathVariable("id") String id);
}