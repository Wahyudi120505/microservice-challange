package com.example.product_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;
import com.example.product_service.models.Product;
import com.example.product_service.repository.ProductRepository;

class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = Product.builder()
                .id("123")
                .name("Guitar")
                .description("Electric guitar")
                .price(1000.0)
                .stock(10)
                .build();
    }

    @Test
    void createProduct_shouldReturnProductResponse() {
        ProductRequest request = new ProductRequest();
        request.setName("Guitar");
        request.setDescription("Electric guitar");
        request.setPrice(1000.0);
        request.setStock(10);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.createProduct(request);
        assertEquals("Guitar", response.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct() {
        ProductRequest request = new ProductRequest();
        request.setName("Guitar Updated");
        request.setDescription("Updated Desc");
        request.setPrice(1200.0);
        request.setStock(5);

        when(productRepository.findById("123")).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.updateProduct("123", request);
        assertEquals("Guitar Updated", response.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_notFound_shouldThrow() {
        when(productRepository.findById("999")).thenReturn(Optional.empty());
        ProductRequest request = new ProductRequest();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> productService.updateProduct("999", request));
        assertEquals("404 NOT_FOUND \"Product not found\"", exception.getMessage());
    }

    @Test
    void deleteProduct_shouldCallDelete() {
        when(productRepository.findById("123")).thenReturn(Optional.of(product));
        productService.deleteProduct("123");
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void getProductById_shouldReturnProduct() {
        when(productRepository.findById("123")).thenReturn(Optional.of(product));
        ProductResponse response = productService.getProductById("123");
        assertEquals("Guitar", response.getName());
    }

    @Test
    void getAllProducts_shouldReturnList() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));
        List<ProductResponse> list = productService.getAllProducts();
        assertEquals(1, list.size());
        assertEquals("Guitar", list.get(0).getName());
    }
}