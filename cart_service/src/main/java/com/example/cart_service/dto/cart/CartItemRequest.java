package com.example.cart_service.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CartItemRequest {
    @NotBlank(message = "productId is required")
    private String productId;

    @Min(value = 1, message = "quantity must be at least 1")
    private Integer quantity = 1;
}