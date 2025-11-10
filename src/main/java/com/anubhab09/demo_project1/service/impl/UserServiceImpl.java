package com.anubhab09.demo_project1.service.impl;

import com.anubhab09.demo_project1.dto.OrderResponse;
import com.anubhab09.demo_project1.dto.UserResponse;
import com.anubhab09.demo_project1.model.Order;
import com.anubhab09.demo_project1.model.User;
import com.anubhab09.demo_project1.repository.UserRepository;
import com.anubhab09.demo_project1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> createUser(List<User> users) {
        return userRepository.saveAll(users);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setEmail(updatedUser.getEmail());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    //  DTO-returning method:
    public List<UserResponse> getAllUsersAsDto() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }
    public UserResponse getUserByIdAsDto(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toUserResponse(u);
    }

    // Mapper helpers (private)
    public UserResponse toUserResponse(User u) {
        List<OrderResponse> orders = u.getOrders() == null
                ? Collections.emptyList()
                : u.getOrders().stream().map(this::toOrderResponse).collect(Collectors.toList());

        return new UserResponse(u.getId() == 0 ? null : u.getId(), u.getName(), u.getEmail(), orders);
    }

    private OrderResponse toOrderResponse(Order o) {
        return new OrderResponse(o.getId(), o.getProductName(), o.getPrice());
    }

}
