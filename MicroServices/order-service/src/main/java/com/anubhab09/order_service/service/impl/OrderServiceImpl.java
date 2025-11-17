package com.anubhab09.order_service.service.impl;

import com.anubhab09.order_service.dto.OrderResponse;
import com.anubhab09.order_service.exception.OrderNotFoundException;
import com.anubhab09.order_service.exception.UserNotFoundException;
import com.anubhab09.order_service.exception.UserServiceUnavailableException;
import com.anubhab09.order_service.model.Order;
import com.anubhab09.order_service.repository.OrderRepository;
import com.anubhab09.order_service.service.OrderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestTemplate restTemplate;
    // private static final org.slf4j.Logger log= org.slf4j.LoggerFactory.getLogger(OrderServiceImpl.class);

    private void validateUserExists(Long userId){

        String url = "http://user-service:8080/Users/" + userId;

        try {
            restTemplate.getForEntity(url, Void.class);
        } catch (HttpClientErrorException.NotFound nf) {
            // 404 -> user not found
            throw new UserNotFoundException("User not found with id: " + userId);
        } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.Forbidden authEx) {
            // user-service protected or auth error
            throw new UserServiceUnavailableException("User-service returned auth error while validating userId=" + userId);
        } catch (HttpClientErrorException httpEx) {
            // other 4xx/5xx
            throw new UserServiceUnavailableException("User-service returned HTTP " + httpEx.getStatusCode() + " while validating userId=" + userId);
        } catch (ResourceAccessException rae) {
            // network / connection refused
            throw new UserServiceUnavailableException("Unable to reach user-service: " + rae.getMessage());
        } catch (Exception ex) {
            // fallback
            throw new UserServiceUnavailableException("Error while validating userId=" + userId + ": " + ex.getMessage());
        }
    }

    @Override
    @CacheEvict(value = {"orderById", "orderByUser"}, allEntries = true)
    public Order createOrder(Long userId, @NotNull Order order) {
        // validate user exists in user-service (calls internal Docker hostname + container port)
        try {
            restTemplate.getForEntity("http://user-service:8080/Users/" + userId, Void.class);
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound ex) {
            throw new com.anubhab09.order_service.exception.OrderNotFoundException("User not found with id: " + userId);
        }

        // set the user id on the order before saving so DB user_id is not null
        order.setUserId(userId);   // <-- IMPORTANT: make sure your Order entity in order-service has a `userId` field mapped to column user_id

        Order saved = orderRepository.save(order);
        return saved;
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

    @Cacheable(value = "orderByUser", key = "#userId")
    public List<OrderResponse> getOrdersByUserIdAsDto(Long userId){
        List<Order> orders= orderRepository.findByUserId(userId);
        if(orders.isEmpty()){
            throw new OrderNotFoundException("Order not found with for this id: " + userId);
        }
        return  orders.stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }
    @Cacheable(value = "orderById", key = "#id")
    public OrderResponse getOrderByOrderIdAsDto(Long orderId){
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
    @CacheEvict(value = {"orderByUser", "orderById"}, allEntries = true)
    public Order updateOrder(Long orderId, @NotNull Order updatedOrder) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        existingOrder.setProductName(updatedOrder.getProductName());
        existingOrder.setPrice(updatedOrder.getPrice());
        //existingOrder.setStatus(updatedOrder.getStatus()) ...have to be added later
        return orderRepository.save(existingOrder);
    }

    @Override
    @CacheEvict(value = {"orderById", "orderByUser"}, allEntries = true)
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
    public List<OrderResponse> findLatestOrders(int limit) {
        return orderRepository.findLatestOrders(limit)
                .stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }
}

