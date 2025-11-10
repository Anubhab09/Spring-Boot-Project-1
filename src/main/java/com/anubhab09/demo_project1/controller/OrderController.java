package com.anubhab09.demo_project1.controller;


import com.anubhab09.demo_project1.dto.CreateOrderRequest;
import com.anubhab09.demo_project1.dto.OrderResponse;
import com.anubhab09.demo_project1.model.Order;
import com.anubhab09.demo_project1.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/user/{userId}")
    public OrderResponse createOrder(@PathVariable Long userId, @RequestBody @Valid CreateOrderRequest req) {
        Order o = new Order();
        o.setProductName(req.getProductName());
        o.setPrice(req.getPrice());
        return orderService.createOrderAsDto(userId, o);
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponse> getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUserIdAsDto(userId);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrderByorderId(@PathVariable Long orderId){
        return orderService.getOrdersByOrderIdAsDto(orderId);
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrdersAsDto();
    }

    // Pagination and Sorting
    @GetMapping("/paged")
    public ResponseEntity<Page<OrderResponse>> getAllOrdersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction){
        return ResponseEntity.ok(orderService.getAllOrdersPaged(page, size, sortBy, direction));
    }

    @PutMapping("{orderId}")
    public Order updateOrder(@PathVariable Long orderId, @RequestBody Order updatedOrder){
        return orderService.updateOrder(orderId, updatedOrder);
    }

    @DeleteMapping("{orderId}")
    public void deleteOrder(@PathVariable Long orderId){
        orderService.deleteOrder(orderId);
    }

}
