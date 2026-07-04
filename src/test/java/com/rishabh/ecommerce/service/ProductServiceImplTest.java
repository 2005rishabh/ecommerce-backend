package com.rishabh.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import com.rishabh.ecommerce.error.ProductNotFoundException;
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

    @Test
    void getProductById_ShouldThrowException_WhenProductDoesNotExists() {
        Long invalidId = 22L;

        when(productRepository.findById(invalidId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> {
                    productServiceImpl.getProductById(invalidId);
                });

        assertEquals("cannot find product by id " + invalidId, exception.getMessage());

        verify(productRepository, times(1)).findById(invalidId);
    }

    @Test
    void updateProduct_ShouldUpdateAndReturnProductResponse_WhenProductExists() {
        Long productId = 1L;
        ProductRequest updateReq = new ProductRequest();
        updateReq.setProductName("Upgarded Laptop");
        updateReq.setPrice(342343);

        Product existProduct = new Product();
        existProduct.setId(productId);
        existProduct.setProductName("Old Laptop");
        existProduct.setPrice(1232);

        Product savedProduct = new Product();
        savedProduct.setId(productId);
        savedProduct.setProductName("Upgraded Laptop");
        savedProduct.setPrice(342343);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existProduct));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponse result = productServiceImpl.updateProductDetails(productId, updateReq);

        assertNotNull(result);
        assertEquals("Upgraded Laptop", result.getProductName());
        assertEquals(342343, result.getPrice());

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(Product.class));

    }

    @Test
    void updateProduct_ShouldThrowException_WhenProductNotExists() {
        Long invalidId = 99L;
        ProductRequest updateReq = new ProductRequest();
        updateReq.setProductName("Fake Laptop");

        when(productRepository.findById(invalidId)).thenReturn(Optional.empty());


        assertThrows(ProductNotFoundException.class, () -> {
            productServiceImpl.updateProductDetails(invalidId, updateReq);
        });

        verify(productRepository, times(1)).findById(invalidId);
        verify(productRepository, never()).save(any(Product.class));

    }

    @Test
    void deleteProduct_ShouldDeleteProduct_WhenProductExists() {
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));



        productServiceImpl.deleteProduct(productId);

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(existingProduct);

    }

    @Test
    void deleteProduct_ShouldThrowException_WhenProductNotExists() {
        Long invalidId = 99L;
    
        when(productRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productServiceImpl.deleteProduct(invalidId);
        });


        verify(productRepository, times(1)).findById(invalidId);
        verify(productRepository, never()).delete(any(Product.class));

    }



}
