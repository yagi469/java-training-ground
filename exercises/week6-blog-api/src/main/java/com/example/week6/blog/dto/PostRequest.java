package com.example.week6.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}
