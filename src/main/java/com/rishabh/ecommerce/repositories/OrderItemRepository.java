package com.rishabh.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rishabh.ecommerce.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}