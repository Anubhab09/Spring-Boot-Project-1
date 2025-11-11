package com.anubhab09.demo_project1.repository;

import com.anubhab09.demo_project1.model.Order;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByUserId(Long userId);

    // JPQL: Get orders by user's email
    @Query("SELECT o FROM Order o WHERE o.user.email = :email")
    List<Order> findOrdersByUserEmail(@Param("email") String email);

    // Native SQL: Get latest N orders
    @Query(value = "SELECT * FROM orders ORDER BY id DESC LIMIT :limit", nativeQuery = true)
    List<Order> findLatestOrders(@Param("limit") int limit);

}
