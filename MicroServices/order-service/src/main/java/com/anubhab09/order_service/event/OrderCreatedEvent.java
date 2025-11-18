package com.anubhab09.order_service.event;

// Event DTO

import java.time.Instant;

public class OrderCreatedEvent {
    private Long userId;
    private Long orderId;
    private String productName;
    private double price;
    private Instant createdAt;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(Long userId, Long orderId, String productName, double price, Instant createdAt) {
        this.userId = userId;
        this.orderId = orderId;
        this.productName = productName;
        this.price = price;
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

