package com.anubhab09.demo_project1.repository;

import com.anubhab09.demo_project1.event.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Long>{
    Optional<OrderEvent> findByOrderId(Long orderId);
}
