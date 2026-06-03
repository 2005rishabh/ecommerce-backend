package com.rishabh.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rishabh.ecommerce.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}