package com.rishabh.ecommerce.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.rishabh.ecommerce.dto.OrderRequest;
import com.rishabh.ecommerce.dto.OrderResponse;
import com.rishabh.ecommerce.entities.Order;
import com.rishabh.ecommerce.entities.Status;
import com.rishabh.ecommerce.entities.User;
import com.rishabh.ecommerce.error.ProductNotFoundException;
import com.rishabh.ecommerce.repositories.OrderItemRepository;
import com.rishabh.ecommerce.repositories.OrderRepository;
import com.rishabh.ecommerce.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor

public class OrderServiceImpl implements OrderService{

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {

        // check kro ki aisa koi user hai ki nahi validate kro
        User user = userRepository.findById(orderRequest.getUserId())
        .orElseThrow(() -> new ProductNotFoundException("User not found id " + orderRequest.getUserId()));

        //order entity ser karo
        Order order = new Order();
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setStatus(Status.PENDING);
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        //loop through cart items and 
        
        return null;
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return null;

    }

    @Override
    public OrderResponse getOrderById(Long id) {
        return null;
    }

    @Override
    public void deleteOrder(Long id) {

    }
}
