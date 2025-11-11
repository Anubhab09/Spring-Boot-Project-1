package com.anubhab09.demo_project1.service.impl;

import com.anubhab09.demo_project1.dto.OrderResponse;
import com.anubhab09.demo_project1.dto.UserResponse;
import com.anubhab09.demo_project1.exception.UserNotFoundException;
import com.anubhab09.demo_project1.model.Order;
import com.anubhab09.demo_project1.model.User;
import com.anubhab09.demo_project1.repository.UserRepository;
import com.anubhab09.demo_project1.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    @CacheEvict(value = {"userById", "usersPage"}, allEntries = true)
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
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    @CacheEvict(value = "userById", key = "#id")
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setEmail(updatedUser.getEmail());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    @CacheEvict(value = "userById", key = "#id")
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException("User doesn't even exists with this id: " + id);
        }
        userRepository.deleteById(id);
    }
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    //  DTO-returning method:
    public List<UserResponse> getAllUsersAsDto() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }
    @Cacheable(value = "userById", key = "#id")
    public UserResponse getUserByIdAsDto(Long id) {
        log.info("DB fetch for user id={}", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
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
    //Pagination and Sorting
    @Override
    public Page<UserResponse> getAllUsersPaged(int page, int size, String sortBy, String direction) {
        Sort sort= direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::toUserResponse);
    }

    //Custom Queries
    @Override
    public List<UserResponse> searchUserByName(String keyward) {
        return userRepository.searchUserByName(keyward)
                .stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> findUsersWithOrders() {
        return userRepository.findUsersWithOrders()
                .stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }
}
