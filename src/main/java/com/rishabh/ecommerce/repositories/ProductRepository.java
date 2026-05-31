package com.rishabh.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rishabh.ecommerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
