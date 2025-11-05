package com.example.cart_service.controllers;

import com.example.cart_service.dto.GenericResponse;
import com.example.cart_service.dto.cart.CartItemRequest;
import com.example.cart_service.services.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final MessageSource messageSource;
    private final CartService cartService;

    @PostMapping("/items")
    @PreAuthorize("hasAnyRole('STAF','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> addItem(@Valid @RequestBody CartItemRequest request, Locale locale) {
        try {
            var resp = cartService.addItem(request);
            String msg = messageSource.getMessage("cart.item.added", null, locale);
            return ResponseEntity.ok(GenericResponse.success(resp, msg));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.eror(e.getMessage()));
        }
    }

    @PutMapping("/items/{productId}")
    @PreAuthorize("hasAnyRole('STAF','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> updateItemQuantity(
            @PathVariable String productId,
            @RequestParam int quantity,
            Locale locale) {
        try {
            var resp = cartService.updateItemQuantity(productId, quantity);
            String msg = messageSource.getMessage("cart.item.updated", null, locale);
            return ResponseEntity.ok(GenericResponse.success(resp, msg));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.eror(e.getMessage()));
        }

    }

    @DeleteMapping("/items/{productId}")
    @PreAuthorize("hasAnyRole('STAF','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> removeItem(@PathVariable String productId, Locale locale) {
        try {
            var resp = cartService.removeItem(productId);
            String msg = messageSource.getMessage("cart.item.removed", null, locale);
            return ResponseEntity.ok(GenericResponse.success(resp, msg));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.eror(e.getMessage()));
        }
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('STAF','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> clearCart(Locale locale) {
        try {
            var resp = cartService.clearCart();
            String msg = messageSource.getMessage("cart.cleared", null, locale);
            return ResponseEntity.ok(GenericResponse.success(resp, msg));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.eror(e.getMessage()));
        }
    }

}
