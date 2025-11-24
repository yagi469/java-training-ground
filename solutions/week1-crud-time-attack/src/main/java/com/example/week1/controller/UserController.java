package com.example.week1.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody @Valid UserRegistrationRequest request) {
        // In a real app, we would call a service here.
        // For the drill, just returning 201 Created is enough.
        return ResponseEntity.created(Objects.requireNonNull(URI.create("/users/1"))).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(new UserResponse(id, "dummy_user"));
    }

    // DTO for the request
    public record UserRegistrationRequest(
            @NotBlank(message = "Username is required") String username,

            @NotBlank(message = "Password is required") String password) {
    }

    // DTO for the response
    public record UserResponse(
            Long id,
            String username) {
    }
}
