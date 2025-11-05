package com.example.cart_service.services;

import com.example.cart_service.clients.ProductClient;
import com.example.cart_service.dto.cart.CartItemRequest;
import com.example.cart_service.dto.cart.CartResponse;
import com.example.cart_service.models.Cart;
import com.example.cart_service.repository.CartRepository;
import com.example.cart_service.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @InjectMocks
    CartServiceImpl cartService;

    @Mock
    CartRepository cartRepository;

    @Mock
    ProductClient productClient;

    @Mock
    HttpServletRequest request;

    @Mock
    JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddItem() {
        String userId = "user-123";
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(jwtUtil.extractUserId("token123")).thenReturn(userId);

        Cart cart = Cart.builder().id(UUID.randomUUID().toString()).userId(userId).build();
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        CartItemRequest req = new CartItemRequest();
        req.setProductId("prod-1");
        req.setQuantity(2);

        var productDto = new com.example.cart_service.dto.product.ProductDto();
        productDto.setId("prod-1");
        productDto.setName("Guitar");
        productDto.setPrice(1000.0);
        when(productClient.getProductById("prod-1")).thenReturn(productDto);

        CartResponse response = cartService.addItem(req);

        assertEquals(1, response.getItems().size());
        assertEquals(2000.0, response.getItems().get(0).getTotalPrice());
        verify(cartRepository, times(1)).save(cart);
    }
}
