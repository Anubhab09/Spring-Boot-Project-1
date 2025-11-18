package com.anubhab09.order_service.kafka;

import com.anubhab09.order_service.event.OrderCreatedEvent;
import com.anubhab09.order_service.service.impl.OrderServiceImpl;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
@Service
public class OrderEventProducer {

//  private static final org.slf4j.Logger log= org.slf4j.LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private final String topic;

    public OrderEventProducer(
            KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate,
            @Value("${kafka.topic.order-created:order-created}") String topic
    ){
        this.kafkaTemplate=kafkaTemplate;
        this.topic=topic;
    }

    public void PublishOrderCreated(OrderCreatedEvent event){
        String key= event.getOrderId() == null ? null : event.getOrderId().toString();

        if(kafkaTemplate == null){
            log.debug("KafkaTemplate is null, skipping event publishing");
            return;
        }

        CompletableFuture<SendResult<String, OrderCreatedEvent>> future = kafkaTemplate.send(topic, key, event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                // preserve the exact style of the original snippet
                log.error("Failed to produce order event for {}: {}", key, ex.getMessage(), ex);
                return;
            }
            RecordMetadata meta = result.getRecordMetadata();
            log.info(
                    "Produced event for order {} to topic={} partition={} offset={}",
                    key,
                    meta.topic(),
                    meta.partition(),
                    meta.offset()
            );
        });
    }
}
