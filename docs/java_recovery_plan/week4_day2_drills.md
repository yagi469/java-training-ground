# Week 4 Day 2 Drill: Controller 層のリファクタリング実践

## 目的

Week4 Day1 で Service 層のリファクタリングを学んだので、今度は**Controller 層**のリファクタリングを実践します。
「動くけど読みにくい Controller」を、美しく読みやすいコードに書き直す練習をします。

## 背景

Controller 層でも以下のような問題がよく発生します：

- メソッド名が不明確
- エラーハンドリングが不統一
- レスポンス形式がバラバラ
- バリデーションが適切でない
- 重複コードが多い

## このドリルでやること

「動くけど読みにくい」書籍管理 API の Controller コードを、以下の観点で改善します：

1. **命名の改善** - メソッド名とエンドポイントを明確にする
2. **エラーハンドリングの統一** - `@RestControllerAdvice`の活用
3. **レスポンス形式の統一** - `ResponseEntity`の適切な使用
4. **バリデーションの追加** - `@Valid`アノテーションの活用
5. **HTTP ステータスコードの適切な使用** - 201, 204, 404 など

## 演習内容

### Before: 読みにくいコード

以下は、わざと読みにくくした Controller のバージョンです。

#### `BadBookController.java` - リファクタリング前

```java
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
```

### After: 改善後のコード（目標）

#### `GoodBookController.java` - リファクタリング後

（解答例は実装後に確認してください）

## 課題: 自分でリファクタリングしてみよう

### ステップ 1: 問題点を特定する

`BadBookController.java` を読んで、以下の問題点を見つけてください：

1. ❌ **問題点 A**: どこが読みにくいか？（命名、構造）
2. ❌ **問題点 B**: エラーハンドリングは適切か？
3. ❌ **問題点 C**: HTTP ステータスコードは適切か？
4. ❌ **問題点 D**: バリデーションはあるか？

### ステップ 2: 実装のヒントと手順

以下の手順で `GoodBookController.java` を実装していきましょう。

#### 1. 基本的な CRUD エンドポイントの改善

```java
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class GoodBookController {

    private final GoodBookService bookService;

    // TODO 1: getAllBooks() - 適切なHTTPステータスとメソッド名
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // TODO 2: getBookById() - 適切な例外処理
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        BookResponse book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    // TODO 3: createBook() - バリデーションと201ステータス
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        BookResponse createdBook = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    // TODO 4: searchBooks() - 明確なメソッド名
    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> searchBooks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer yearFrom) {
        List<BookResponse> books = bookService.searchBooks(author, yearFrom);
        return ResponseEntity.ok(books);
    }
}
```

#### 2. グローバル例外ハンドラーの作成

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    // TODO 5: BookNotFoundExceptionのハンドリング
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException e) {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // TODO 6: バリデーションエラーのハンドリング
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException e) {
        // バリデーションエラーのメッセージを取得
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorResponse error = new ErrorResponse("VALIDATION_ERROR", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
```

#### 3. エラーレスポンス DTO の作成

```java
// TODO 7: ErrorResponseクラスを作成
public record ErrorResponse(
    String code,
    String message
) {}
```

## リファクタリングのチェックリスト

改善後のコードが以下を満たしているか確認してください：

### 1. 命名

- [ ] メソッド名が「何をするか」を明確に表している
- [ ] エンドポイントのパスが RESTful な設計になっている

### 2. HTTP ステータスコード

- [ ] GET: 200 OK
- [ ] POST: 201 Created
- [ ] エラー時: 適切なステータスコード（404, 400 など）

### 3. バリデーション

- [ ] `@Valid`アノテーションを使用
- [ ] DTO にバリデーションアノテーションを追加

### 4. エラーハンドリング

- [ ] `@RestControllerAdvice`でグローバル例外ハンドリング
- [ ] 統一されたエラーレスポンス形式

### 5. レスポンス形式

- [ ] `ResponseEntity`を適切に使用
- [ ] レスポンスボディが統一されている

## 実行方法

Week4 のプロジェクトを使って実装します：

```
exercises/
└── week4-refactoring-practice/
    └── src/main/java/com/example/week4/
        ├── bad/
        │   ├── controller/
        │   │   └── BadBookController.java    ← Before版
        │   └── service/
        │       └── BadBookService.java
        └── good/
            ├── controller/
            │   └── GoodBookController.java   ← After版（課題）
            └── service/
                └── GoodBookService.java
```

## 完了条件

✅ `BadBookController.java` の問題点を 4 つ以上特定できる
✅ `GoodBookController.java` を実装できる
✅ `@RestControllerAdvice`でグローバル例外ハンドリングを実装している
✅ 適切な HTTP ステータスコードを使用している
✅ `@Valid`でバリデーションを実装している

---

## ヒント

困ったときは、以下を意識してください：

- **メソッド名を見たら** → RESTful な命名になっているか？
- **例外処理を見たら** → Controller に書くべきか、`@RestControllerAdvice`に書くべきか？
- **HTTP ステータスを見たら** → 適切なステータスコードを使っているか？
- **バリデーションを見たら** → `@Valid`を使っているか？

頑張ってください！
