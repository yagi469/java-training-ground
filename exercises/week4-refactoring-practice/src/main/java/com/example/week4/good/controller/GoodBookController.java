package com.example.week4.good.controller;

import com.example.week4.common.dto.BookRequest;
import com.example.week4.common.dto.BookResponse;
import com.example.week4.good.service.GoodBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class GoodBookController {

  private final GoodBookService bookService;

  // getAllBooks() - 適切なHTTPステータスとメソッド名
  @GetMapping
  public ResponseEntity<List<BookResponse>> getAllBooks() {
    // ヒント: List<BookResponse> books = bookService.getAllBooks();
    List<BookResponse> books = bookService.getAllBooks();
    // ヒント: return ResponseEntity.ok(books);
    return ResponseEntity.ok(books);
  }

  // getBookById() - 適切な例外処理
  @GetMapping("/{id}")
  public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
    // ヒント: BookResponse book = bookService.getBookById(id);
    BookResponse book = bookService.getBookById(id);
    // ヒント: return ResponseEntity.ok(book);
    return ResponseEntity.ok(book);
  }

  // createBook() - バリデーションと201ステータス
  @PostMapping
  public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
    // ヒント: BookResponse createdBook = bookService.createBook(request);
    BookResponse createdBook = bookService.createBook(request);
    // ヒント: return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
  }

  // searchBooks() - 明確なメソッド名
  @GetMapping("/search")
  public ResponseEntity<List<BookResponse>> searchBooks(
      @RequestParam(required = false) String author,
      @RequestParam(required = false) Integer yearFrom) {
    // ヒント: List<BookResponse> books = bookService.searchBooks(author, yearFrom);
    List<BookResponse> books = bookService.searchBooks(author, yearFrom);
    // ヒント: return ResponseEntity.ok(books);
    return ResponseEntity.ok(books);
  }
}
