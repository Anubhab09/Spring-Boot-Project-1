package com.anubhab09.order_service.repository;

import com.anubhab09.order_service.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByUserId(Long userId);

    Page<Order> findByUserId(Long userId, Pageable pageable);

    boolean existsByUserId(Long userId);

    // Native SQL: Get latest N orders
    @Query(value = "SELECT * FROM orders ORDER BY id DESC LIMIT :limit", nativeQuery = true)
    List<Order> findLatestOrders(@Param("limit") int limit);

}

