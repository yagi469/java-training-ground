package com.example.week1.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRegistrationRequest(
        @NotBlank String username,
        @NotBlank String password) {
}
