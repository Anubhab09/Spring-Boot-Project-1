package com.anubhab09.inventory_service.kafka;

import com.anubhab09.inventory_service.event.OrderCreatedEvent;
import com.anubhab09.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryEventConsumer {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(InventoryEventConsumer.class);
    private final InventoryService inventoryService;

    @KafkaListener(topics = "${kafka.topic.order-created:order-created}", groupId = "inventory-service-group")
    public void handleOrderCreated(OrderCreatedEvent event){
        log.info("Received OrderCreatedEvent: orderId={}, product={}, userId={}",
                event.getOrderId(), event.getProductName(), event.getUserId());


        boolean reserved = inventoryService.reserveItem(event.getProductName(), 1);
        if (!reserved) {
            log.warn("Failed to reserve stock for order {}", event.getOrderId());
        }
    }
}
