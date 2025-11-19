package com.anubhab09.inventory_service.event;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreatedEvent {
    private Long orderId;
    private Long userId;
    private String productName;
    private String price;
    private Instant createdAt;
}

