package com.anubhab09.demo_project1.service.impl;

import com.anubhab09.demo_project1.dto.OrderResponse;
import com.anubhab09.demo_project1.exception.OrderNotFoundException;
import com.anubhab09.demo_project1.exception.UserNotFoundException;
import com.anubhab09.demo_project1.model.Order;
import com.anubhab09.demo_project1.model.User;
import com.anubhab09.demo_project1.repository.OrderRepository;
import com.anubhab09.demo_project1.repository.UserRepository;
import com.anubhab09.demo_project1.service.OrderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + userId));
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

    public List<OrderResponse> getOrdersByUserIdAsDto(Long userId){
        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        List<Order> orders= orderRepository.findByUserId(userId);
        if(orders.isEmpty()){
            throw new OrderNotFoundException("Order not found with for this id: " + userId);
        }
        return  orders.stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrdersByOrderIdAsDto(Long orderId){
        Order order= orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        return toOrderResponse(order);
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
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        existingOrder.setProductName(updatedOrder.getProductName());
        existingOrder.setPrice(updatedOrder.getPrice());
        //existingOrder.setStatus(updatedOrder.getStatus()) ...have to be added later
        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long orderId) {
        if(!orderRepository.existsById(orderId)){
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }

    //Paging and Sorting
    @Override
    public Page<OrderResponse> getAllOrdersPaged(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(this::toOrderResponse);
    }

    //Custom Queries
    @Override
    public List<OrderResponse> findOrdersByUserEmail(String email) {
        return orderRepository.findOrdersByUserEmail(email)
                .stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> findLatestOrders(int limit) {
        return orderRepository.findLatestOrders(limit)
                .stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }
}
