package com.example.week1.controller;

import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

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

    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody @Valid UserRegistrationRequest request) {
        return ResponseEntity.created(Objects.requireNonNull(URI.create("/users/1"))).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(new UserResponse(1L, "user"));
    }

    // Define UserRegistrationRequest (username, password)
    public record UserRegistrationRequest(
            @NotBlank String username,
            @NotBlank String password) {
    }

    // Define UserResponse (id, username)
    public record UserResponse(
            Long id,
            String username) {
    }
}
