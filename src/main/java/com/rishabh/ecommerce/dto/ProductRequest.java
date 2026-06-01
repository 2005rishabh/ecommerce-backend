package com.rishabh.ecommerce.dto;

import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductRequest {
    private String productName;
    private String description;
    private int price;
    private int stock;
    private String category;
}
