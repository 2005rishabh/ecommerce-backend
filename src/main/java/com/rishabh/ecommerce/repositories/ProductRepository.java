package com.rishabh.ecommerce.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rishabh.ecommerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    findAll(Specification <T> spec, Pageable pageable);
}
