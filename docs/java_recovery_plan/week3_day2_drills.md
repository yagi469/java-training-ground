# Week 3 Day 2 Drill: インターフェースファースト設計の応用

## 目的
Day 1で学んだインターフェースファースト設計を使って、既存のシステムに新機能を追加する練習をします。
**実装詳細は考えず、まずシグネチャだけを書く** ことで、手戻りなく機能を追加できることを体感します。

## 背景
Day 1で作成した書籍管理システムに、以下の新機能を追加します：

1. **著者で検索** - 著者名の部分一致で書籍を検索
2. **出版年で絞り込み** - 指定した年以降に出版された書籍を取得
3. **ページネーション** - 書籍一覧をページング対応
4. **在庫管理** - 書籍の在庫数を管理

## 演習内容

### シナリオ: 書籍管理システムの機能拡張

Day 1で作成した `week3-interface-first` プロジェクトを拡張します。

### ステップ1: Entityに在庫フィールドを追加

#### `Book.java` の更新
```java
package com.example.week3.day1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String author;
    private String isbn;
    private int publishedYear;
    
    // 新規追加
    private Integer stockQuantity;  // 在庫数
}
```

### ステップ2: DTOの更新

#### `BookResponse.java` の更新
```java
package com.example.week3.day1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private int publishedYear;
    private Integer stockQuantity;  // 在庫数を追加
}
```

#### `BookRequest.java` の更新
```java
package com.example.week3.day1.dto;

import lombok.Data;

@Data
public class BookRequest {
    private String title;
    private String author;
    private String isbn;
    private int publishedYear;
    private Integer stockQuantity;  // 在庫数を追加
}
```

#### `BookSearchRequest.java` (新規作成)
```java
package com.example.week3.day1.dto;

import lombok.Data;

@Data
public class BookSearchRequest {
    private String author;           // 著者名（部分一致）
    private Integer publishedYearFrom;  // この年以降
    private Integer page;            // ページ番号（0始まり）
    private Integer size;            // ページサイズ
}
```

### ステップ3: Repositoryにカスタムクエリを追加

#### `BookRepository.java` の更新
```java
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
        Pageable pageable
    );
}
```

### ステップ4: Serviceに新しいメソッドを追加（シグネチャのみ）

#### `BookService.java` の更新
```java
package com.example.week3.day1.service;

import com.example.week3.day1.dto.BookRequest;
import com.example.week3.day1.dto.BookResponse;
import com.example.week3.day1.dto.BookSearchRequest;
import com.example.week3.day1.entity.Book;
import com.example.week3.day1.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    
    private final BookRepository bookRepository;
    
    // 既存のメソッドはそのまま（Day 1で実装済み）
    public List<BookResponse> getAllBooks() {
        // 実装済み
        return null;
    }
    
    public BookResponse getBookById(Long id) {
        // 実装済み
        return null;
    }
    
    public BookResponse createBook(BookRequest request) {
        // 実装済み
        return null;
    }
    
    public BookResponse updateBook(Long id, BookRequest request) {
        // 実装済み
        return null;
    }
    
    public void deleteBook(Long id) {
        // 実装済み
    }
    
    // 新規メソッド（まだ実装しない）
    public List<BookResponse> searchByAuthor(String author) {
        // TODO: 著者名で検索
        return null;
    }
    
    public List<BookResponse> searchByPublishedYear(int year) {
        // TODO: 出版年以降の書籍を検索
        return null;
    }
    
    public Page<BookResponse> searchBooks(BookSearchRequest request) {
        // TODO: 複合検索（ページング対応）
        return null;
    }
    
    public BookResponse updateStock(Long id, int quantity) {
        // TODO: 在庫数を更新
        return null;
    }
    
    // ヘルパーメソッド（既存のまま）
    private BookResponse toResponse(Book book) {
        return null;
    }
    
    private Book toEntity(BookRequest request) {
        return null;
    }
    
    private void updateEntity(Book book, BookRequest request) {
    }
}
```

### ステップ5: Controllerに新しいエンドポイントを追加

#### `BookController.java` の更新
```java
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
    
    // 既存のエンドポイント（Day 1で実装済み）
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
    
    // 新規エンドポイント
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
```

## 課題

1. **上記のコードを実装してください**（まだ中身は書かない）
2. **コンパイルが通ることを確認する**
3. **IDEの自動補完が効くことを確認する**

**重要なルール:**
- Serviceの新規メソッドは `return null;` のまま
- まず「つながる」ことを確認する
- ヘルパーメソッドの更新が必要な場合は、シグネチャだけ更新する

## Day 2 の目標

✅ 既存システムへの機能追加の流れを理解する
✅ Repository → Service → Controller の順でシグネチャを追加する習慣を身につける
✅ コンパイルエラーをゼロにしてから実装に進む感覚をつかむ

---

次のステップ（Day 3）では、これらのメソッドの実装を埋めていきます。
