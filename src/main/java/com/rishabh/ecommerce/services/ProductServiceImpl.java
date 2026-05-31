package com.rishabh.ecommerce.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rishabh.ecommerce.entities.Product;
import com.rishabh.ecommerce.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product createProducts(Product product) {
        return productRepository.save(product);

    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("cannot find product by id " + id));
    }

    @Override
    public Product updateProductDetails(Long id, Product productDetails) {
        Product newProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("cannot find product by id " + id));

        newProduct.setPrice(productDetails.getPrice());
        newProduct.setDescription(productDetails.getDescription());
        newProduct.setCategory(productDetails.getCategory());
        newProduct.setPrice(productDetails.getPrice());
        newProduct.setStock(productDetails.getStock());

        return null;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
