package com.rishabh.ecommerce.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.rishabh.ecommerce.entities.Status;
import com.rishabh.ecommerce.entities.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OrderResponse {
    private long id;
    private String orderNumber;
    private int totalAmount;
    private Status status;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
