package com.anubhab09.demo_project1.service;

import com.anubhab09.demo_project1.dto.UserResponse;
import com.anubhab09.demo_project1.model.User;
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
}
