package com.rishabh.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OrderItemResponse {
    private Long id;

    private Long productId;

    private String productName;

    private Integer quantity;

    private Integer price;

    private Integer subtotal;
    
}
