package com.example.cart_service.services;

import com.example.cart_service.dto.cart.CartItemRequest;
import com.example.cart_service.dto.cart.CartResponse;

public interface CartService {
    CartResponse getCartByAuthenticatedUser();
    CartResponse addItem(CartItemRequest request);
    CartResponse clearCart();
    CartResponse removeItem(String productId);
    CartResponse updateItemQuantity(String productId, Integer quantity);
}
