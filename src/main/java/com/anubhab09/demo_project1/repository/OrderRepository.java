package com.anubhab09.demo_project1.repository;

import com.anubhab09.demo_project1.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByUserId(Long userId);
}
