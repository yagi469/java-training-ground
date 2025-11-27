package com.example.week4.bad.controller;

import com.example.week4.common.dto.BookRequest;
import com.example.week4.common.dto.BookResponse;
import com.example.week4.bad.service.BadBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BadBookController {

  private final BadBookService service;

  // 悪い例1: メソッド名が不明確、HTTPステータスが不適切
  @GetMapping
  public List<BookResponse> get() {
    return service.get();
  }

  // 悪い例2: 例外処理がControllerに混在
  @GetMapping("/{id}")
  public BookResponse getById(@PathVariable Long id) {
    try {
      return service.getById(id);
    } catch (RuntimeException e) {
      return null; // エラー時にnullを返す（不適切）
    }
  }

  // 悪い例3: バリデーションなし、HTTPステータスが不適切
  @PostMapping
  public BookResponse create(@RequestBody BookRequest req) {
    return service.create(req);
  }

  // 悪い例4: メソッド名が不明確
  @GetMapping("/search")
  public List<BookResponse> search(@RequestParam(required = false) String author,
      @RequestParam(required = false) Integer year) {
    return service.search(author, year);
  }
}
