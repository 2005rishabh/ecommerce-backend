package com.rishabh.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.UncategorizedDataAccessException;

import com.rishabh.ecommerce.dto.OrderItemRequest;
import com.rishabh.ecommerce.dto.OrderRequest;
import com.rishabh.ecommerce.dto.OrderResponse;
import com.rishabh.ecommerce.entities.Order;
import com.rishabh.ecommerce.entities.Product;
import com.rishabh.ecommerce.entities.Role;
import com.rishabh.ecommerce.entities.Status;
import com.rishabh.ecommerce.entities.User;
import com.rishabh.ecommerce.error.UnauthorizedActionException;
import com.rishabh.ecommerce.repositories.OrderItemRepository;
import com.rishabh.ecommerce.repositories.OrderRepository;
import com.rishabh.ecommerce.repositories.ProductRepository;
import com.rishabh.ecommerce.repositories.UserRepository;
import com.rishabh.ecommerce.services.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @Test
    void createOrder_ShouldCalculateTotalAndReduceStock_WhenValid() {
        Long userId = 1L;
        Long productId = 100L;

        User mockUser = new User();
        mockUser.setId(userId);

        Product mockProduct = new Product();
        mockProduct.setId(productId);
        mockProduct.setPrice(50);
        mockProduct.setStock(10);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(productId);
        itemRequest.setQuantity(2);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(userId);
        orderRequest.setItems(List.of(itemRequest));

        Order savedOrder = new Order();
        savedOrder.setId(500L);
        savedOrder.setTotalAmount(100); // 2 items * $50 = $100

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderResponse result = orderServiceImpl.createOrder(orderRequest);

        assertNotNull(result);

        assertEquals(8, mockProduct.getStock());

        verify(userRepository, times(1)).findById(userId);
        verify(productRepository, times(1)).findById(productId);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_ShouldThrowException_WhenInsufficientStock() {
        Long userId = 1L;
        Long productId = 100L;

        User mockUser = new User();
        mockUser.setId(userId);

        Product mockProduct = new Product();
        mockProduct.setId(productId);
        mockProduct.setStock(2);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(productId);
        itemRequest.setQuantity(5);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(userId);
        orderRequest.setItems(List.of(itemRequest));

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        assertThrows(RuntimeException.class, () -> {
            orderServiceImpl.createOrder(orderRequest);
        });

        verify(orderRepository, never()).save(any(Order.class));

    }

    @Test
    void deleteOrder_ShouldCancelOrder_WhenUserIsOwner() {
        Long orderId = 500L;
        Long ownerId = 1L;

        User owner = new User();
        owner.setId(ownerId);

        Order existOrder = new Order();
        existOrder.setId(orderId);
        existOrder.setUser(owner);
        existOrder.setStatus(com.rishabh.ecommerce.entities.Status.PENDING);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existOrder));

        orderServiceImpl.deleteOrder(orderId, ownerId);

        assertEquals(com.rishabh.ecommerce.entities.Status.CANCELLED, existOrder.getStatus());

        verify(userRepository, times(1)).findById(ownerId);
        verify(orderRepository, times(1)).findById(orderId);

        verify(orderRepository, times(1)).save(existOrder);
    }

    @Test
    void deleteOrder_ShouldThrowException_WhenUserIsNotOwner() {
        Long orderId = 500L;
        Long actualId = 1L;
        Long hackerId = 10L;

        User mockUser = new User();
        mockUser.setId(actualId);

        User hacker = new User();
        hacker.setId(hackerId);
        hacker.setRole(Role.USER);

        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setUser(mockUser);
        mockOrder.setStatus(Status.PENDING);

        when(userRepository.findById(hackerId)).thenReturn(Optional.of(hacker));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        assertThrows(UnauthorizedActionException.class, () -> {
            orderServiceImpl.deleteOrder(orderId, hackerId);
        });

        verify(userRepository, times(1)).findById(hackerId);
        verify(orderRepository, times(1)).findById(orderId);

        verify(orderRepository, never()).save(any(Order.class));

    }

}
