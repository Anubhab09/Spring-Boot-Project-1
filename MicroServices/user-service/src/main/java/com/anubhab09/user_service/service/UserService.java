package com.anubhab09.user_service.service;


import com.anubhab09.user_service.dto.UserResponse;
import com.anubhab09.user_service.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    User createUser(User user);
    List<User> createUser(List<User> users);
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
    UserResponse toUserResponse(User user);
    List<UserResponse> getAllUsersAsDto();
    UserResponse getUserByIdAsDto(Long id);
    Page<UserResponse> getAllUsersPaged(int page, int size, String sortBy, String direction);
    List<UserResponse> searchUserByName(String keyward);
    // List<UserResponse> findUsersWithOrders();
}

