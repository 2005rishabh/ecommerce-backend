package com.rishabh.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rishabh.ecommerce.dto.ProductRequest;
import com.rishabh.ecommerce.dto.ProductResponse;
import com.rishabh.ecommerce.entities.Product;
import com.rishabh.ecommerce.repositories.ProductRepository;
import com.rishabh.ecommerce.services.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)

public class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Test
    public void createProduct_ProductShouldSaveAndResturnProductResponse() {
        ProductRequest request = new ProductRequest();
        request.setProductName("Hp Pavllion");
        request.setDescription("Gaming laptop with long lasting battery");
        request.setCategory("Electronics");
        request.setPrice(343434);
        request.setStock(13);

        Product saveProduct = new Product();
        saveProduct.setProductName("Hp Pavllion");
        saveProduct.setDescription("Gaming laptop with long lasting battery");
        saveProduct.setCategory("Electronics");
        saveProduct.setPrice(343434);
        saveProduct.setStock(13);

        when(productRepository.save(any(Product.class))).thenReturn(saveProduct);

        ProductResponse productResponse = productServiceImpl.createProduct(request);

        assertNotNull(productResponse);
        assertEquals("Hp Pavllion", productResponse.getProductName());
        assertEquals("Gaming laptop with long lasting battery", productResponse.getDescription());
        assertEquals("Electronics", productResponse.getCategory());
        assertEquals(343434, productResponse.getPrice());
        assertEquals(13, productResponse.getStock());

        verify(productRepository, times(1)).save(any(Product.class));

    }

    @Test
    public void getProductById_ShouldReturnTheProductTheResoponse_WhenProductExists() {
        Long productId = 1L;
        Product mockProduct = new Product();
        mockProduct.setId(productId);
        mockProduct.setProductName("mouse");
        mockProduct.setPrice(1500);

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        ProductResponse result = productServiceImpl.getProductById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("mouse", result.getProductName());

        verify(productRepository, times(1)).findById(productId);

    }
}
