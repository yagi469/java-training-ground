package com.example.week1.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // DTO for the request
    public record UserRegistrationRequest(
            @NotBlank(message = "Username is required") String username,

            @NotBlank(message = "Password is required") String password) {
    }
}
