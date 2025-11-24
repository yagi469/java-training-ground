package com.example.week1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

// Drill 1 - Implement User Registration API Shell
// Requirements:
// 1. @RestController, @RequestMapping("/users"), @RequiredArgsConstructor
// 2. POST /users method
// 3. @Valid @RequestBody for input
// 4. Return ResponseEntity<Void> (or appropriate type)

import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    @PostMapping
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok().build();
    }

    public record UserRegistrationRequest(
            @NotBlank String username,
            @NotBlank String password) {
    }
}
