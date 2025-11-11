package com.anubhab09.demo_project1.service;

import com.anubhab09.demo_project1.dto.OrderResponse;
import com.anubhab09.demo_project1.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface OrderService {
    Order createOrder(Long userId, Order order);
    List<Order> getOrderByUser(Long userId);
    List<Order> getAllOrders();
    Order updateOrder(Long orderId, Order updatedOrder);
    void deleteOrder(Long orderId);
    OrderResponse createOrderAsDto(Long userId, Order order);
    List<OrderResponse> getAllOrdersAsDto();
    List<OrderResponse> getOrdersByUserIdAsDto(Long userId);
    OrderResponse getOrderByOrderIdAsDto(Long orderId);
    Page<OrderResponse> getAllOrdersPaged(int page, int size, String sortBy, String direction);
    List<OrderResponse> findOrdersByUserEmail(String email);
    List<OrderResponse> findLatestOrders(int limit);
}
