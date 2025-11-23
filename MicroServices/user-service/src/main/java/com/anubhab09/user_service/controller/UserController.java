package com.anubhab09.user_service.controller;

import com.anubhab09.user_service.dto.CreateUserRequest;
import com.anubhab09.user_service.dto.UserResponse;
import com.anubhab09.user_service.model.User;
import com.anubhab09.user_service.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Users")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @PostMapping
    public UserResponse createUser(@RequestBody @Valid CreateUserRequest req) {
        User u = new User();
        u.setUsername(req.getName());
        u.setEmail(req.getEmail());
        User saved = userService.createUser(u);
        return userService.toUserResponse(saved); // if toUserResponse is private, expose conversion or call getUserByIdAsDto(saved.getId())
    }

    @PostMapping("/bulk")
    public List<User> createUsers(@RequestBody List<User> users){
        return userService.createUser(users);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsersAsDto();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserByIdAsDto(id);
    }

    // Pagination and Sorting
    @GetMapping("/paged")
    public ResponseEntity<Page<UserResponse>> getAllUsersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction){
        return ResponseEntity.ok(userService.getAllUsersPaged(page, size, sortBy, direction));
    }

    // JPQL: Search users by name (case-insensitive)
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsersByName(@RequestParam String keyword) {
        return ResponseEntity.ok(userService.searchUserByName(keyword));
    }


    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

}

