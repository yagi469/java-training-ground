package com.example.week3.day1.dto;

import lombok.Data;

@Data
public class BookSearchRequest {
    private String author; // 著者名（部分一致）
    private Integer publishedYearFrom; // この年以降
    private Integer page; // ページ番号（0始まり）
    private Integer size; // ページサイズ
}
