package com.example.week3.day1.dto;

import lombok.Data;

@Data
public class BookRequest {
    private String title;
    private String author;
    private String isbn;
    private int publishedYear;
}
