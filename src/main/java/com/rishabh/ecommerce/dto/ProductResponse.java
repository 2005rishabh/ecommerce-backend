package com.rishabh.ecommerce.dto;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductResponse {
    private Long id;
    private String productName;
    private String description;
    private int price;
    private int stock;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
