package com.anubhab09.order_service.dto;

public class OrderResponse {
    private Long id;
    private String productName;
    private double price;

    private Long userId;
    private String name;
    private String email;

    public OrderResponse() {
    }

    public OrderResponse(Long id, String productName, double price, Long userId, String name, String email) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

