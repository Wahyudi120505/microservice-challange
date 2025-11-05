package com.example.product_service.controllers;

import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.GenericResponse;
import com.example.product_service.services.ProductService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> createProduct(@RequestBody ProductRequest request, Locale locale) {
        try {
            var product = productService.createProduct(request);
            String msg = messageSource.getMessage("product.create.success", null, locale);
            return ResponseEntity.ok(GenericResponse.success(product, msg));
        } catch (ResponseStatusException e) {
            String msg = messageSource.getMessage("product.error", null, locale);
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(msg + ": " + e.getReason()));
        } catch (Exception e) {
            String msg = messageSource.getMessage("product.error.internal", null, locale);
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror(msg));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> updateProduct(@PathVariable String id, @RequestBody ProductRequest request, Locale locale) {
        try {
            var product = productService.updateProduct(id, request);
            String msg = messageSource.getMessage("product.update.success", null, locale);
            return ResponseEntity.ok(GenericResponse.success(product, msg));
        } catch (ResponseStatusException e) {
            String msg = messageSource.getMessage("product.error", null, locale);
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(msg + ": " + e.getReason()));
        } catch (Exception e) {
            String msg = messageSource.getMessage("product.error.internal", null, locale);
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror(msg));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> deleteProduct(@PathVariable String id, Locale locale) {
        try {
            productService.deleteProduct(id);
            String msg = messageSource.getMessage("product.delete.success", null, locale);
            return ResponseEntity.ok(GenericResponse.success(null, msg));
        } catch (ResponseStatusException e) {
            String msg = messageSource.getMessage("product.error", null, locale);
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(msg + ": " + e.getReason()));
        } catch (Exception e) {
            String msg = messageSource.getMessage("product.error.internal", null, locale);
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror(msg));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAF')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getProduct(@PathVariable String id, Locale locale) {
        try {
            var product = productService.getProductById(id);
            String msg = messageSource.getMessage("product.fetch.success", null, locale);
            return ResponseEntity.ok(GenericResponse.success(product, msg));
        } catch (ResponseStatusException e) {
            String msg = messageSource.getMessage("product.error", null, locale);
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(msg + ": " + e.getReason()));
        } catch (Exception e) {
            String msg = messageSource.getMessage("product.error.internal", null, locale);
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror(msg));
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAF')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAllProducts(Locale locale) {
        try {
            var products = productService.getAllProducts();
            String msg = messageSource.getMessage("product.fetch.all.success", null, locale);
            return ResponseEntity.ok(GenericResponse.success(products, msg));
        } catch (ResponseStatusException e) {
            String msg = messageSource.getMessage("product.error", null, locale);
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(msg + ": " + e.getReason()));
        } catch (Exception e) {
            String msg = messageSource.getMessage("product.error.internal", null, locale);
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror(msg));
        }
    }
}
