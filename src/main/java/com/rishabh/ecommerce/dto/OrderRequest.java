package com.rishabh.ecommerce.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderRequest {
    private Long userId;
    private List<OrderItemRequest> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
