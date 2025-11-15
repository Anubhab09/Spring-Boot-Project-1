package com.anubhab09.demo_project1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateOrderRequest {
    @NotBlank(message = "Product name required")
    private String productName;

    @NotNull(message = "Price required")
    @Positive(message = "Price must be positive")
    private double price;

    // @NotNull(message = "userId required")
    private Long userId;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(String productName, double price, Long userId) {
        this.productName = productName;
        this.price = price;
        this.userId = userId;
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
}
