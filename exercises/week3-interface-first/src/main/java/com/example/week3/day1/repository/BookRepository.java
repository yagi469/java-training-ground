package com.example.week3.day1.repository;

import com.example.week3.day1.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    // Spring Data JPAが自動実装するため、メソッド定義のみ
}
