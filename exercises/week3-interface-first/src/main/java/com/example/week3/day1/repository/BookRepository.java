package com.example.week3.day1.repository;

import com.example.week3.day1.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // 著者名で部分一致検索（Spring Data JPAの命名規則を利用）
    List<Book> findByAuthorContaining(String author);

    // 出版年以降の書籍を検索
    List<Book> findByPublishedYearGreaterThanEqual(int year);

    // 著者名と出版年で複合検索（ページング対応）
    Page<Book> findByAuthorContainingAndPublishedYearGreaterThanEqual(
            String author,
            int year,
            Pageable pageable);
}
