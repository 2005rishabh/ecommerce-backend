package com.rishabh.ecommerce.services;

import java.util.List;

import com.rishabh.ecommerce.dto.ProductRequest;
import com.rishabh.ecommerce.dto.ProductResponse;
import com.rishabh.ecommerce.entities.Product;

public interface ProductService {
    
    ProductResponse createProduct(ProductRequest request);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    ProductResponse updateProductDetails(Long id, ProductRequest productDetails);

    void deleteProduct(Long id);

}
