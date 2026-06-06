package com.rishabh.ecommerce.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import com.rishabh.ecommerce.dto.OrderItemRequest;
import com.rishabh.ecommerce.dto.OrderItemResponse;
import com.rishabh.ecommerce.dto.OrderRequest;
import com.rishabh.ecommerce.dto.OrderResponse;
import com.rishabh.ecommerce.entities.Order;
import com.rishabh.ecommerce.entities.OrderItem;
import com.rishabh.ecommerce.entities.Product;
import com.rishabh.ecommerce.entities.Role;
import com.rishabh.ecommerce.entities.Status;
import com.rishabh.ecommerce.entities.User;
import com.rishabh.ecommerce.error.ProductNotFoundException;
import com.rishabh.ecommerce.error.UnauthorizedActionException;
import com.rishabh.ecommerce.repositories.OrderItemRepository;
import com.rishabh.ecommerce.repositories.OrderRepository;
import com.rishabh.ecommerce.repositories.ProductRepository;
import com.rishabh.ecommerce.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class OrderServiceImpl implements OrderService {

	private final OrderItemRepository orderItemRepository;
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public OrderResponse createOrder(OrderRequest orderRequest) {

		// check kro ki aisa koi user hai ki nahi validate kro
		User user = userRepository.findById(orderRequest.getUserId())
				.orElseThrow(() -> new ProductNotFoundException(
						"User not found id " + orderRequest.getUserId()));

		// order entity ser karo
		Order order = new Order();
		order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		order.setStatus(Status.PENDING);
		order.setUser(user);
		order.setCreatedAt(LocalDateTime.now());
		order.setUpdatedAt(LocalDateTime.now());

		// loop through cart items to calc cost & build entities
		List<OrderItem> orderItems = new ArrayList<>();
		int totalOrderAmount = 0;

		for (OrderItemRequest itemRequest : orderRequest.getItems()) {
			Product product = productRepository.findById(itemRequest.getProductId())
					.orElseThrow(
							() -> new ProductNotFoundException("No product found by id "
									+ itemRequest.getProductId()));

			int requestedQuantity = itemRequest.getQuantity();

			if (product.getStock() < requestedQuantity) {
				throw new IllegalStateException("Not enough stock for product: " + product.getProductName());
			}
			product.setStock(product.getStock() - requestedQuantity);

			int productPrice = product.getPrice();
			int quantity = itemRequest.getQuantity();
			int subtotal = productPrice * quantity;

			totalOrderAmount += subtotal;

			OrderItem orderItem = OrderItem.builder()
					.product(product)
					.quantity(quantity)
					.price(productPrice)
					.subtotal(subtotal)
					.order(order)
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build();

			orderItems.add(orderItem);
		}

		order.setTotalAmount(totalOrderAmount);
		Order saveOrder = orderRepository.save(order);

		orderItemRepository.saveAll(orderItems);

		return mapToOrderResponse(saveOrder, orderItems);
	}

	private OrderResponse mapToOrderResponse(Order saveOrder, List<OrderItem> orderItems) {
		List<OrderItemResponse> itemResponses = orderItems.stream()
				.map(item -> OrderItemResponse.builder()
						.id(item.getId())
						.productId(item.getProduct().getId())
						.productName(item.getProduct().getProductName())
						.quantity(item.getQuantity())
						.price(item.getPrice())
						.subtotal(item.getSubtotal())
						.build())
				.toList();

		return OrderResponse.builder()
				.id(saveOrder.getId())
				.orderNumber(saveOrder.getOrderNumber())
				.totalAmount(saveOrder.getTotalAmount())
				.status(saveOrder.getStatus())
				.createdAt(saveOrder.getCreatedAt())
				.updatedAt(saveOrder.getUpdatedAt())
				.items(itemResponses)
				.build();
	}

	@Override
	public List<OrderResponse> getAllOrders() {

		List<Order> order = orderRepository.findAll();
		List<OrderResponse> orderResponses = order.stream()
				.map(orderEntity -> mapToOrderResponse(orderEntity, orderEntity.getOrderItems()))
				.toList();

		return orderResponses;

	}

	@Override
	public OrderResponse getOrderById(Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Order not found with id : " + id));
		OrderResponse orderResponse = mapToOrderResponse(order, order.getOrderItems());
		return orderResponse;
	}

	@Override
	public OrderResponse updateOrder(Long id, OrderRequest request, Long reqUserId) {
		User user = userRepository.findById(reqUserId)
				.orElseThrow(() -> new ProductNotFoundException("User not found exception with id: " + reqUserId));

		if (user.getRole() != Role.ADMIN) {
			throw new UnauthorizedActionException("You don't have permission to perform these actions");
		}

		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Order not found with id: " + id));

		if (request.getStatus() != null) {
			order.setStatus(request.getStatus());
		}

		order.setUpdatedAt(LocalDateTime.now());

		Order savedOrderResponse = orderRepository.save(order);

		return mapToOrderResponse(savedOrderResponse, savedOrderResponse.getOrderItems());
	}

	@Override
	public void deleteOrder(Long id, Long reqUserId) {
		User user = userRepository.findById(reqUserId)
				.orElseThrow(() -> new ProductNotFoundException("User not found exception with id: " + reqUserId));

		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Order not found with id: " + id));

		boolean isAdmin = (user.getRole() == Role.ADMIN);
		boolean isOwner = order.getUser().getId().equals(reqUserId);

		if (!isAdmin && !isOwner) {
			throw new UnauthorizedActionException("You do not have permission to cancel this order.");
		}

		if (order.getStatus() != Status.PENDING) {
			throw new IllegalStateException("Order cannot be cancelled. Current status is: " + order.getStatus());
		}

		order.setStatus(Status.CANCELLED);
		order.setUpdatedAt(LocalDateTime.now());

		orderRepository.save(order);
	}
}
