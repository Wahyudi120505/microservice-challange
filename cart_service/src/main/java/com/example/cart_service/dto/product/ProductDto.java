package com.example.cart_service.dto.product;

import lombok.Data;

@Data
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
}