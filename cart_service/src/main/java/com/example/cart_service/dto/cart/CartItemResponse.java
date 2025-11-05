package com.example.cart_service.dto.cart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {
    private String id;
    private String productId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private Double totalPrice;
}