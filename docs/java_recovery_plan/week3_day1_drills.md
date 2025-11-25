# Week 3 Day 1 Drill: インターフェースファースト設計（Mock Programming）

## 目的
実装詳細を考える前に、**メソッドのシグネチャ（引数と戻り値）だけを先に全部書く** 習慣を身につけます。これにより、実装中の「あ、これ足りない」を防ぎ、手戻りをゼロにします。

## 背景
従来のボトムアップ方式（Repositoryから実装していく）では、途中で「あれ、Controllerでこのデータ必要だった」と気づき、戻って修正することになります。

インターフェースファーストでは、**すべてのクラスのメソッド定義だけを先に書き、中身は `return null;` や `throw new UnsupportedOperationException();` にしておく** ことで、**コンパイルが通る骨組み** を作ります。

## 演習内容

### シナリオ: 書籍管理システム

以下の機能を持つ書籍管理APIを作成します（まだ実装はしません）：

1. **書籍一覧取得** (GET `/books`)
2. **書籍詳細取得** (GET `/books/{id}`)
3. **書籍登録** (POST `/books`)
4. **書籍更新** (PUT `/books/{id}`)
5. **書籍削除** (DELETE `/books/{id}`)

### ステップ1: DTOとEntityを定義

#### `BookRequest.java` (リクエストDTO)
```java
package com.example.week3.day1.dto;

import lombok.Data;

@Data
public class BookRequest {
    private String title;
    private String author;
    private String isbn;
    private int publishedYear;
}
```

#### `BookResponse.java` (レスポンスDTO)
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
}
```

#### `Book.java` (Entity)
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
}
```

### ステップ2: Repository（中身なし）

#### `BookRepository.java`
```java
package com.example.week3.day1.repository;

import com.example.week3.day1.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    // Spring Data JPAが自動実装するため、メソッド定義のみ
}
```

### ステップ3: Service（シグネチャのみ）

#### `BookService.java`
```java
package com.example.week3.day1.service;

import com.example.week3.day1.dto.BookRequest;
import com.example.week3.day1.dto.BookResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {
    
    public List<BookResponse> getAllBooks() {
        // TODO: 実装は後で
        return null;
    }
    
    public BookResponse getBookById(Long id) {
        // TODO: 実装は後で
        return null;
    }
    
    public BookResponse createBook(BookRequest request) {
        // TODO: 実装は後で
        return null;
    }
    
    public BookResponse updateBook(Long id, BookRequest request) {
        // TODO: 実装は後で
        return null;
    }
    
    public void deleteBook(Long id) {
        // TODO: 実装は後で
    }
}
```

### ステップ4: Controller（シグネチャのみ）

#### `BookController.java`
```java
package com.example.week3.day1.controller;

import com.example.week3.day1.dto.BookRequest;
import com.example.week3.day1.dto.BookResponse;
import com.example.week3.day1.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    
    private final BookService bookService;
    
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
}
```

## 課題

上記のコードを `exercises/week3-interface-first` プロジェクトに作成してください。

**重要なルール:**
1. **まだ実装は書かない**（Serviceの中身は `return null;` のまま）
2. **コンパイルが通ることを確認する**
3. IDEの自動補完だけでControllerからServiceまでつながることを体感する

## 提出物
- すべてのクラスファイル（DTO, Entity, Repository, Service, Controller）
- **コンパイルが通る状態**であること

---

完了したら、次は実装を埋めていきます。
