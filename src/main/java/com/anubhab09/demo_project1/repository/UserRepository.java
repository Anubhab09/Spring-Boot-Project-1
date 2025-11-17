package com.anubhab09.demo_project1.repository;

import com.anubhab09.demo_project1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    // JPQL: Find users whose name contains a keyword (case-insensitive)
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyward, '%'))")
    List<User> searchUserByName(@Param("keyward") String keyward);

    // Native SQL: Find all users who have placed at least one order
    @Query(value = "SELECT * FROM users u WHERE u.id IN (SELECT DISTINCT o.user_id FROM orders o)", nativeQuery = true)
    List<User> findUsersWithOrders();
}
