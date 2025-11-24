package com.example.week1.dto;

import com.example.week1.validation.ValidEmail;
import jakarta.validation.constraints.NotBlank;

public record UserRegistrationRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank @ValidEmail String email) {
}
