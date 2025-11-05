package com.example.cart_service.dto.cart;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartResponse {
    private String id;
    private String userId;
    private List<CartItemResponse> items;
}