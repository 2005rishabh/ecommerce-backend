package com.rishabh.ecommerce.services;

import java.util.List;

import com.rishabh.ecommerce.dto.OrderRequest;
import com.rishabh.ecommerce.dto.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(Long id);

    void deleteOrder(Long id);
}
