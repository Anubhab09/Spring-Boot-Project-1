package com.anubhab09.demo_project1.service.impl;

import com.anubhab09.demo_project1.dto.OrderResponse;
import com.anubhab09.demo_project1.model.Order;
import com.anubhab09.demo_project1.model.User;
import com.anubhab09.demo_project1.repository.OrderRepository;
import com.anubhab09.demo_project1.repository.UserRepository;
import com.anubhab09.demo_project1.service.OrderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Order createOrder(Long userId, @NotNull Order order) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        order.setUser(user);
        return orderRepository.save(order);
    }
    // DTO methods
    public OrderResponse createOrderAsDto(Long userId, Order order) {
        Order saved = createOrder(userId, order);
        return toOrderResponse(saved);
    }

    public List<OrderResponse> getAllOrdersAsDto() {
        return orderRepository.findAll().stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersAsDto(Long userId){
        return orderRepository.findById(userId).stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    // mapper helper
    private OrderResponse toOrderResponse(Order o) {
        return new OrderResponse(o.getId(), o.getProductName(), o.getPrice());
    }

    @Override
    public List<Order> getOrderByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrder(Long orderId, @NotNull Order updatedOrder) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order Not Found!"));
        existingOrder.setProductName(updatedOrder.getProductName());
        existingOrder.setPrice(updatedOrder.getPrice());
        //existingOrder.setStatus(updatedOrder.getStatus()) ...have to be added later
        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long orderId) {
        if(!orderRepository.existsById(orderId)){
            throw new RuntimeException("No Order Found With This ID");
        }
        orderRepository.deleteById(orderId);
    }
}
