package com.rishabh.ecommerce.services;

import java.util.List;

import com.rishabh.ecommerce.dto.PageResponse;
import com.rishabh.ecommerce.dto.ProductRequest;
import com.rishabh.ecommerce.dto.ProductResponse;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    PageResponse<ProductResponse> getAllProducts(
            int pageNumber, int pageSize, String sortBy, String sortDir,
            String category, Double minPrice, Double maxPrice, String keyword);

    ProductResponse getProductById(Long id);

    ProductResponse updateProductDetails(Long id, ProductRequest productDetails);

    void deleteProduct(Long id);

}
