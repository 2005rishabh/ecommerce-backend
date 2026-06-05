package com.rishabh.ecommerce.services;

import java.util.List;

import com.rishabh.ecommerce.dto.OrderRequest;
import com.rishabh.ecommerce.dto.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(Long id);

    OrderResponse updateOrder(Long id, OrderRequest request, Long reqUserId); // i have to make sure only admin is allowed to update order

    void deleteOrder(Long id);
}
