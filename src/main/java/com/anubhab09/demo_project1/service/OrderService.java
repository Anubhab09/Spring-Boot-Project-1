package com.anubhab09.demo_project1.service;

import com.anubhab09.demo_project1.model.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(Long userId, Order order);
    List<Order> getOrderByUser(Long userId);
    List<Order> getAllOrders();
    Order updateOrder(Long orderId, Order updatedOrder);
    void deleteOrder(Long orderId);
}
