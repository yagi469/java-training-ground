# Week 4 Day 2 実装完了記録: Controller層のリファクタリング

## 実装概要

Week4 Day2では、Controller層のリファクタリングを実践しました。
「動くけど読みにくいController」を、RESTful APIのベストプラクティスに沿った美しいコードに書き直しました。

## 実装した内容

### 1. GoodBookController.java

Controller層のリファクタリングを実装しました。

#### 実装したメソッド

1. **`getAllBooks()`** - 書籍一覧取得
2. **`getBookById()`** - 書籍詳細取得
3. **`createBook()`** - 書籍作成
4. **`searchBooks()`** - 書籍検索

### 2. GlobalExceptionHandler.java

グローバル例外ハンドラーを実装しました。

#### 実装したハンドラー

1. **`handleBookNotFound()`** - BookNotFoundExceptionのハンドリング
2. **`handleValidationError()`** - バリデーションエラーのハンドリング

### 3. その他のファイル

- **`ErrorResponse.java`** - エラーレスポンスDTO（recordクラス）
- **`BookRequest.java`** - バリデーションアノテーションを追加

## Before/After 比較

### 1. メソッド名の改善

#### ❌ Before: BadBookController

```java
@GetMapping
public List<BookResponse> get() {
    return service.get();
}
```

#### ✅ After: GoodBookController

```java
@GetMapping
public ResponseEntity<List<BookResponse>> getAllBooks() {
    List<BookResponse> books = bookService.getAllBooks();
    return ResponseEntity.ok(books);
}
```

**改善点:**
- メソッド名が「何をするか」を明確に表現
- `ResponseEntity`を使用してHTTPステータスを明示

### 2. HTTPステータスコードの適切な使用

#### ❌ Before: BadBookController

```java
@PostMapping
public BookResponse create(@RequestBody BookRequest req) {
    return service.create(req);  // 常に200 OK
}
```

#### ✅ After: GoodBookController

```java
@PostMapping
public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
    BookResponse createdBook = bookService.createBook(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);  // 201 Created
}
```

**改善点:**
- POSTリクエストに対して適切な201 Createdを返す
- `ResponseEntity`で明示的にステータスコードを指定

### 3. 例外処理の改善

#### ❌ Before: BadBookController

```java
@GetMapping("/{id}")
public BookResponse getById(@PathVariable Long id) {
    try {
        return service.getById(id);
    } catch (RuntimeException e) {
        return null;  // エラー時にnullを返す（不適切）
    }
}
```

#### ✅ After: GoodBookController + GlobalExceptionHandler

```java
// Controller - try-catch不要
@GetMapping("/{id}")
public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
    BookResponse book = bookService.getBookById(id);
    return ResponseEntity.ok(book);
}

// GlobalExceptionHandler - グローバルに処理
@ExceptionHandler(BookNotFoundException.class)
public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException e) {
    ErrorResponse error = new ErrorResponse("NOT_FOUND", e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
}
```

**改善点:**
- Controllerから例外処理を分離
- `@RestControllerAdvice`でグローバルに処理
- 適切なHTTPステータスコード（404 Not Found）を返す
- 統一されたエラーレスポンス形式

### 4. バリデーションの追加

#### ❌ Before: BadBookController

```java
@PostMapping
public BookResponse create(@RequestBody BookRequest req) {
    return service.create(req);  // バリデーションなし
}
```

#### ✅ After: GoodBookController

```java
@PostMapping
public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
    BookResponse createdBook = bookService.createBook(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
}
```

**改善点:**
- `@Valid`アノテーションでバリデーションを有効化
- `BookRequest`にバリデーションアノテーションを追加
- バリデーションエラーは`GlobalExceptionHandler`で処理

## 実装の詳細

### GoodBookController.java

```java
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class GoodBookController {

    private final GoodBookService bookService;

    // 1. 書籍一覧取得
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);  // 200 OK
    }

    // 2. 書籍詳細取得
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        BookResponse book = bookService.getBookById(id);
        return ResponseEntity.ok(book);  // 200 OK
    }

    // 3. 書籍作成
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        BookResponse createdBook = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);  // 201 Created
    }

    // 4. 書籍検索
    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> searchBooks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer yearFrom) {
        List<BookResponse> books = bookService.searchBooks(author, yearFrom);
        return ResponseEntity.ok(books);  // 200 OK
    }
}
```

### GlobalExceptionHandler.java

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    // BookNotFoundExceptionのハンドリング
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException e) {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);  // 404 Not Found
    }

    // バリデーションエラーのハンドリング
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorResponse error = new ErrorResponse("VALIDATION_ERROR", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);  // 400 Bad Request
    }
}
```

### BookRequest.java（バリデーション追加）

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotNull(message = "Published year is required")
    @Positive(message = "Published year must be positive")
    private Integer publishedYear;

    @Positive(message = "Stock quantity must be positive")
    private Integer stockQuantity;
}
```

### ErrorResponse.java

```java
public record ErrorResponse(
    String code,
    String message
) {}
```

## 学んだポイント

### 1. ResponseEntityの役割

- **HTTPステータスコードを明示的に指定できる**
- **異なるHTTPステータスコードを使い分けられる**（200 OK, 201 Created, 404 Not Foundなど）
- **将来の拡張性**（ヘッダー追加など）

詳細は `response_entity_explanation.md` を参照。

### 2. @Validアノテーションの重要性

- `@Valid`がないと、バリデーションアノテーション（`@NotBlank`、`@NotNull`など）が実行されない
- `@Valid`を付けることで、不正なデータを自動的に拒否できる
- バリデーションエラーは`MethodArgumentNotValidException`として`GlobalExceptionHandler`に渡される

### 3. @RestControllerAdviceによるグローバル例外処理

- Controllerでtry-catchを書く必要がない
- 例外処理を1箇所に集約できる（DRY原則）
- 統一されたエラーレスポンス形式を提供できる
- 適切なHTTPステータスコードを返せる

### 4. 具体的な例外クラスの使用

- `RuntimeException`を直接ハンドリングするのではなく、具体的な例外クラス（`BookNotFoundException`）をハンドリングする
- 例外ごとに適切なエラーレスポンスを返せる
- デバッグが容易になる

## 実装のポイント

### 1. RESTful APIのベストプラクティス

| HTTPメソッド | 成功時のステータスコード | 実装例 |
|------------|----------------------|--------|
| GET | 200 OK | `ResponseEntity.ok(books)` |
| POST | 201 Created | `ResponseEntity.status(HttpStatus.CREATED).body(createdBook)` |
| エラー | 404 Not Found | `ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)` |
| バリデーションエラー | 400 Bad Request | `ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)` |

### 2. エラーハンドリングの流れ

```
1. リクエスト: GET /api/books/999
   ↓
2. Controller: getBookById(999) を呼び出す
   ↓
3. Service: bookRepository.findById(999) → 見つからない
   ↓
4. Service: BookNotFoundException を投げる
   ↓
5. Spring: 例外をキャッチして GlobalExceptionHandler に渡す
   ↓
6. GlobalExceptionHandler: handleBookNotFound() が実行される
   ↓
7. レスポンス: 404 Not Found + ErrorResponse
```

### 3. バリデーションの流れ

```
1. リクエスト: POST /api/books
   {
     "title": "",  // 空文字列
     "author": "John Doe",
     ...
   }
   ↓
2. Spring: @RequestBodyでBookRequestに変換
   ↓
3. Spring: @Validがあるかチェック
   ↓
4. @Validがある場合:
   - BookRequestの各フィールドのバリデーションアノテーションをチェック
   - @NotBlank("Title is required") → titleが空 → エラー
   ↓
5. バリデーションエラーがある場合:
   - MethodArgumentNotValidException が投げられる
   - GlobalExceptionHandler.handleValidationError() が実行される
   ↓
6. レスポンス: 400 Bad Request + ErrorResponse
```

## チェックリスト

### 実装完了項目

- [x] `GoodBookController.java` を実装
- [x] `GlobalExceptionHandler.java` を実装
- [x] `ErrorResponse.java` を作成
- [x] `BookRequest.java` にバリデーションアノテーションを追加
- [x] 適切なHTTPステータスコードを使用
- [x] `@Valid`でバリデーションを実装
- [x] `@RestControllerAdvice`でグローバル例外ハンドリング
- [x] リンターエラーなし

### 改善点

- [x] 命名: メソッド名が明確（`getAllBooks`, `getBookById`, `createBook`, `searchBooks`）
- [x] HTTPステータスコード: 適切に使用（200 OK, 201 Created, 404 Not Found, 400 Bad Request）
- [x] バリデーション: `@Valid`を使用
- [x] エラーハンドリング: `@RestControllerAdvice`でグローバル処理
- [x] レスポンス形式: `ResponseEntity`を適切に使用

## まとめ

Week4 Day2では、Controller層のリファクタリングを通じて以下を学びました：

1. **`ResponseEntity`の重要性** - HTTPステータスコードを明示的に制御
2. **`@Valid`の役割** - バリデーションを有効化
3. **`@RestControllerAdvice`の活用** - グローバル例外処理でコードを簡潔に
4. **RESTful APIのベストプラクティス** - 適切なHTTPステータスコードの使用

これらの知識により、読みやすく保守しやすいController層を実装できるようになりました。

