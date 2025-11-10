package com.anubhab09.demo_project1.service;

import com.anubhab09.demo_project1.dto.OrderResponse;
import com.anubhab09.demo_project1.model.Order;

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
    List<OrderResponse> getOrdersAsDto(Long userId);
}
