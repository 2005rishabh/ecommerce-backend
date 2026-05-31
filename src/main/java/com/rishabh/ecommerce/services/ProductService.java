package com.rishabh.ecommerce.services;

import java.util.List;

import com.rishabh.ecommerce.entities.Product;

public interface ProductService {
    Product createProducts(Product product);

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product updateProductDetails(Long id, Product productDetails);
    
    void deleteProduct(Long id);
}
