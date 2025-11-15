package com.anubhab09.demo_project1.kafka;

import com.anubhab09.demo_project1.event.OrderCreatedEvent;
import com.anubhab09.demo_project1.model.OrderEvent;
import com.anubhab09.demo_project1.repository.OrderEventRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import org.springframework.messaging.handler.annotation.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

@Service
public class OrderEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);
    private final OrderEventRepository repo;

    public OrderEventConsumer(OrderEventRepository repo) {
        this.repo = repo;
    }

    @KafkaListener(topics = "order-created", groupId = "order-event-consumers", containerFactory = "kafkaListenerContainerFactory")
    public void listen(OrderCreatedEvent event,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                       @Header(KafkaHeaders.OFFSET) long offset,
                       @Header(KafkaHeaders.RECEIVED_KEY) String key,
                       ConsumerRecord<String, OrderCreatedEvent> record) {

        log.info("Consumed order event key={} partition={} offset={}", key, partition, offset);

        // idempotent save: skip if already present
        if (event == null || event.getOrderId() == null) {
            log.warn("Ignored null/invalid event: {}", event);
            return;
        }

        // Option 1: quick check before save
        if (repo.findByOrderId(event.getOrderId()).isPresent()) {
            log.info("Event for order {} already persisted, ignoring (idempotent).", event.getOrderId());
            return;
        }

        OrderEvent e = new OrderEvent();
        e.setOrderId(event.getOrderId());
        e.setUserId(event.getUserId());
        e.setProductName(event.getProductName());
        e.setPrice(event.getPrice());
        e.setCreatedAt(event.getCreatedAt());
        e.setReceivedAt(Instant.now());

        try {
            repo.save(e);
            log.info("Saved order_event for order {}", event.getOrderId());
        } catch (Exception ex) {
            // If unique constraint violation or other transient error, log and decide retry behaviour
            log.error("Failed to persist order event for {}: {}", event.getOrderId(), ex.getMessage());
        }
    }
}
