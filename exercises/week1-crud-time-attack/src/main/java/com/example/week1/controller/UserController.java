package com.example.week1.controller;

import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.week1.dto.UserDeleteResponse;
import com.example.week1.dto.UserRegistrationRequest;
import com.example.week1.dto.UserResponse;
import com.example.week1.dto.UserUpdateRequest;
import com.example.week1.service.UserService;

import jakarta.validation.Valid;

// Drill 3 - Registration & Retrieval (Target: 5 mins)
// Requirements:
// 1. Class: @RestController, @RequestMapping("/users"), @RequiredArgsConstructor
// 2. Method 1: POST /users
//    - Input: @Valid UserRegistrationRequest
//    - Output: 201 Created (ResponseEntity<Void>)
// 3. Method 2: GET /users/{id}
//    - Input: @PathVariable Long id
//    - Output: 200 OK with UserResponse (ResponseEntity<UserResponse>)
//    - Note: Just return a dummy UserResponse for now.

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody @Valid UserRegistrationRequest request) {
        UserResponse user = userService.createUser(request);
        URI location = Objects.requireNonNull(URI.create("/users/" + user.id()));
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
            @RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new UserDeleteResponse(id, "User deleted successfully"));
    }
}
