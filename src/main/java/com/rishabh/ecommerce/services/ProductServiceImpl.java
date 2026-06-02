package com.rishabh.ecommerce.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rishabh.ecommerce.dto.ProductRequest;
import com.rishabh.ecommerce.dto.ProductResponse;
import com.rishabh.ecommerce.entities.Product;
import com.rishabh.ecommerce.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ProductServiceImpl implements ProductService {

        private final ProductRepository productRepository;

        @Override
        public ProductResponse createProduct(ProductRequest request) {
                Product productEntity = Product.builder()
                                .productName(request.getProductName())
                                .description(request.getDescription())
                                .price(request.getPrice())
                                .stock(request.getStock())
                                .category(request.getCategory())
                                .build();

                Product savedEntity = productRepository.save(productEntity);

                return ProductResponse.builder()
                                .id(savedEntity.getId())
                                .productName(savedEntity.getProductName())
                                .description(savedEntity.getDescription())
                                .price(savedEntity.getPrice())
                                .stock(savedEntity.getStock())
                                .category(savedEntity.getCategory())
                                .createdAt(savedEntity.getCreatedAt())
                                .updatedAt(savedEntity.getUpdatedAt())
                                .build();

        }

        @Override
        public List<ProductResponse> getAllProducts() {
                List<Product> products = productRepository.findAll();

                return products.stream()
                                .map(product -> ProductResponse.builder()
                                                .id(product.getId())
                                                .productName(product.getProductName())
                                                .description(product.getDescription())
                                                .price(product.getPrice())
                                                .stock(product.getStock())
                                                .category(product.getCategory())
                                                .createdAt(product.getCreatedAt())
                                                .updatedAt(product.getUpdatedAt())
                                                .build())
                                .toList();
        }

        @Override
        public ProductResponse getProductById(Long id) {
                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("cannot find product by id " + id));

                return ProductResponse.builder()
                                .id(product.getId())
                                .productName(product.getProductName())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .stock(product.getStock())
                                .category(product.getCategory())
                                .createdAt(product.getCreatedAt())
                                .updatedAt(product.getUpdatedAt())
                                .build();
        }

        @Override
        public ProductResponse updateProductDetails(Long id, ProductRequest productDetails) {
                Product newProduct = productRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("cannot find product by id " + id));

                newProduct.setProductName(productDetails.getProductName());
                newProduct.setPrice(productDetails.getPrice());
                newProduct.setDescription(productDetails.getDescription());
                newProduct.setCategory(productDetails.getCategory());
                newProduct.setPrice(productDetails.getPrice());
                newProduct.setStock(productDetails.getStock());

                Product savedEntity = productRepository.save(newProduct);
                return ProductResponse.builder()
                                .id(savedEntity.getId())
                                .productName(savedEntity.getProductName())
                                .description(savedEntity.getDescription())
                                .price(savedEntity.getPrice())
                                .stock(savedEntity.getStock())
                                .category(savedEntity.getCategory())
                                .createdAt(savedEntity.getCreatedAt())
                                .updatedAt(savedEntity.getUpdatedAt())
                                .build();
        }

        @Override
        public void deleteProduct(Long id) {
                Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("There is no product with id " + id));
                productRepository.delete(existingProduct);;

        }
}
