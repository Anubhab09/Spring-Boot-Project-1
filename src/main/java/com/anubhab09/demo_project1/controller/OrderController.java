package com.anubhab09.demo_project1.controller;


import com.anubhab09.demo_project1.model.Order;
import com.anubhab09.demo_project1.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/user/{userId}")
    public Order createOrder(@PathVariable Long userId, @RequestBody Order order){
        return orderService.createOrder(userId, order);
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrder(@PathVariable Long userId){
        return orderService.getOrderByUser(userId);
    }

    @GetMapping
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
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
