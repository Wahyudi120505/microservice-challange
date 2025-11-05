package com.example.cart_service.services;

import com.example.cart_service.clients.ProductClient;
import com.example.cart_service.dto.cart.CartItemRequest;
import com.example.cart_service.dto.cart.CartItemResponse;
import com.example.cart_service.dto.cart.CartResponse;
import com.example.cart_service.dto.product.ProductDto;
import com.example.cart_service.jwt.JwtUtil;
import com.example.cart_service.models.Cart;
import com.example.cart_service.models.CartItem;
import com.example.cart_service.repository.CartRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductClient productClient;
    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;

    private String getAuthenticatedUserId() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUserId(token);
        if (userId == null || userId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token: userId missing");
        }
        return userId;
    }

    @Override
    @Transactional
    public CartResponse getCartByAuthenticatedUser() {
        String userId = getAuthenticatedUserId();
        log.info("Fetching cart for userId={}", userId);

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = Cart.builder()
                    .userId(userId)
                    .createdAt(LocalDateTime.now())
                    .build();
            return cartRepository.save(newCart);
        });

        return toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItem(CartItemRequest requestDto) {
        String userId = getAuthenticatedUserId();
        log.info("Add item: product={} qty={} by user={}", requestDto.getProductId(), requestDto.getQuantity(), userId);

        ProductDto product = productClient.getProductById(requestDto.getProductId());
        if (product == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found");

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> Cart.builder()
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build());

        CartItem existing = cart.getCartItems().stream()
                .filter(i -> i.getProductId().equals(requestDto.getProductId()))
                .findFirst().orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + requestDto.getQuantity());
        } else {
            CartItem item = CartItem.builder()
                    .id(UUID.randomUUID().toString())
                    .productId(requestDto.getProductId())
                    .quantity(requestDto.getQuantity())
                    .cart(cart)
                    .build();
            cart.getCartItems().add(item);
        }

        cartRepository.save(cart);
        return toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse updateItemQuantity(String productId, Integer quantity) {
        String userId = getAuthenticatedUserId();
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        CartItem item = cart.getCartItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

        if (quantity <= 0) {
            cart.getCartItems().remove(item);
        } else {
            item.setQuantity(quantity);
        }

        cartRepository.save(cart);
        return toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removeItem(String productId) {
        String userId = getAuthenticatedUserId();
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        cart.getCartItems().removeIf(i -> i.getProductId().equals(productId));
        cartRepository.save(cart);
        return toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse clearCart() {
        String userId = getAuthenticatedUserId();
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        cart.getCartItems().clear();
        cartRepository.save(cart);
        return toResponse(cart);
    }

    private CartResponse toResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .items(cart.getCartItems().stream().map(i -> {
                    ProductDto product = productClient.getProductById(i.getProductId());
                    return CartItemResponse.builder()
                            .id(i.getId())
                            .productId(i.getProductId())
                            .productName(product != null ? product.getName() : null)
                            .productPrice(product != null ? product.getPrice() : null)
                            .quantity(i.getQuantity())
                            .totalPrice(product != null ? product.getPrice() * i.getQuantity() : null)
                            .build();
                }).collect(Collectors.toList()))
                .build();
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void generateMonthlySellingReport() {
        List<Cart> allCarts = cartRepository.findAll();
        double totalRevenue = allCarts.stream()
                .flatMap(c -> c.getCartItems().stream())
                .mapToDouble(i -> {
                    ProductDto product = productClient.getProductById(i.getProductId());
                    return (product != null ? product.getPrice() * i.getQuantity() : 0);
                }).sum();

        log.info("=== Monthly Selling Report ===");
        log.info("Total revenue this month: {}", totalRevenue);
        log.info("Total orders: {}", allCarts.size());
        log.info("==============================");
    }
}
