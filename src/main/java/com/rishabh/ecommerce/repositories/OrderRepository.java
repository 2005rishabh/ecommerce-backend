package com.rishabh.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rishabh.ecommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}