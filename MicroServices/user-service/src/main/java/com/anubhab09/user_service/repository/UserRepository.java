package com.anubhab09.user_service.repository;

import com.anubhab09.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // JPQL: Find users whose name contains a keyword (case-insensitive)
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyward, '%'))")
    List<User> searchUserByName(@Param("keyward") String keyward);
}
