package com.example.week6.blog.dto;

import lombok.Data;

@Data
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private Long categoryId;
}
