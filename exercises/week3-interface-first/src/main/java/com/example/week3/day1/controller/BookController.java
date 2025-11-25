package com.example.week3.day1.controller;

import com.example.week3.day1.dto.BookRequest;
import com.example.week3.day1.dto.BookResponse;
import com.example.week3.day1.dto.BookSearchRequest;
import com.example.week3.day1.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // === 既存のエンドポイント（Day 1で実装済み） ===

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.createBook(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @RequestBody BookRequest request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // === 新規エンドポイント（Day 2で追加） ===

    @GetMapping("/search/author")
    public ResponseEntity<List<BookResponse>> searchByAuthor(
            @RequestParam String author) {
        return ResponseEntity.ok(bookService.searchByAuthor(author));
    }

    @GetMapping("/search/year")
    public ResponseEntity<List<BookResponse>> searchByPublishedYear(
            @RequestParam int year) {
        return ResponseEntity.ok(bookService.searchByPublishedYear(year));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<BookResponse>> searchBooks(
            @RequestBody BookSearchRequest request) {
        return ResponseEntity.ok(bookService.searchBooks(request));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<BookResponse> updateStock(
            @PathVariable Long id,
            @RequestParam int quantity) {
        return ResponseEntity.ok(bookService.updateStock(id, quantity));
    }
}
