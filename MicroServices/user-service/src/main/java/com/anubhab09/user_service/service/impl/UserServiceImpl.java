package com.anubhab09.user_service.service.impl;

import com.anubhab09.user_service.dto.UserResponse;
import com.anubhab09.user_service.exception.UserNotFoundException;
import com.anubhab09.user_service.model.User;
import com.anubhab09.user_service.repository.UserRepository;
import com.anubhab09.user_service.service.UserService;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
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
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User doesn't exist with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // DTO-returning methods

    @Override
    public List<UserResponse> getAllUsersAsDto() {
        return userRepository.findAll()
                .stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "userById", key = "#id")
    public UserResponse getUserByIdAsDto(Long id) {
        log.info("DB fetch for user id={}", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return toUserResponse(u);
    }

    // Mapper helper - simplified (no orders)
    public UserResponse toUserResponse(User u) {
        return new UserResponse(u.getId(), u.getName(), u.getEmail());
    }

    // Pagination and Sorting (returns DTO page)
    @Override
    public Page<UserResponse> getAllUsersPaged(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::toUserResponse);
    }

    // Search by name (if repository supports it) - returns DTOs
    @Override
    public List<UserResponse> searchUserByName(String keyword) {
        return userRepository.searchUserByName(keyword)
                .stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }
}
