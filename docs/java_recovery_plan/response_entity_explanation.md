# ResponseEntityの役割と使い方

## 概要

`ResponseEntity`は、Spring FrameworkでHTTPレスポンスの**ステータスコード、ヘッダー、ボディ**を明示的に制御するためのクラスです。

## なぜ`ResponseEntity`が必要なのか？

### 問題: 直接返す場合の制約

```java
// ❌ 悪い例: 直接返す
@GetMapping
public List<BookResponse> get() {
    return service.get();  // 常に200 OKが返される（暗黙的）
}
```

この方法では以下の問題があります：

1. **HTTPステータスコードが常に200 OK**（変更不可）
2. **意図が不明確**（コードを読んでも何が返されるか分からない）
3. **RESTful APIのベストプラクティスに反する**

### 解決: `ResponseEntity`を使う

```java
// ✅ 良い例: ResponseEntityを使う
@GetMapping
public ResponseEntity<List<BookResponse>> getAllBooks() {
    List<BookResponse> books = bookService.getAllBooks();
    return ResponseEntity.ok(books);  // 明示的に200 OKを指定
}
```

## `ResponseEntity`の主な役割

### 1. HTTPステータスコードを明示的に指定できる

```java
// GET: 200 OK
@GetMapping
public ResponseEntity<List<BookResponse>> getAllBooks() {
    List<BookResponse> books = bookService.getAllBooks();
    return ResponseEntity.ok(books);  // 200 OK
}

// POST: 201 Created
@PostMapping
public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
    BookResponse createdBook = bookService.createBook(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);  // 201 Created
}

// DELETE: 204 No Content
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    bookService.deleteBook(id);
    return ResponseEntity.noContent().build();  // 204 No Content
}
```

### 2. 条件に応じて異なるステータスコードを返せる

```java
@GetMapping
public ResponseEntity<List<BookResponse>> getAllBooks() {
    List<BookResponse> books = bookService.getAllBooks();

    // データが空の場合は204 No Contentを返す
    if (books.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content
    }

    return ResponseEntity.ok(books);  // 200 OK
}
```

### 3. HTTPヘッダーを追加できる（将来の拡張性）

```java
@PostMapping
public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
    BookResponse createdBook = bookService.createBook(request);

    // Locationヘッダーを追加（RESTful APIのベストプラクティス）
    return ResponseEntity.status(HttpStatus.CREATED)
        .header("Location", "/api/books/" + createdBook.getId())
        .body(createdBook);
}
```

## よく使う`ResponseEntity`のメソッド

### 1. `ResponseEntity.ok()`
- **用途**: 200 OKを返す
- **例**: `ResponseEntity.ok(data)`

### 2. `ResponseEntity.status()`
- **用途**: 任意のステータスコードを指定
- **例**: `ResponseEntity.status(HttpStatus.CREATED).body(data)`

### 3. `ResponseEntity.noContent()`
- **用途**: 204 No Contentを返す（ボディなし）
- **例**: `ResponseEntity.noContent().build()`

### 4. `ResponseEntity.badRequest()`
- **用途**: 400 Bad Requestを返す
- **例**: `ResponseEntity.badRequest().body(error)`

### 5. `ResponseEntity.notFound()`
- **用途**: 404 Not Foundを返す
- **例**: `ResponseEntity.notFound().build()`

## RESTful APIでの適切なHTTPステータスコード

| HTTPメソッド | 成功時のステータスコード | 使用例 |
|------------|----------------------|--------|
| GET | 200 OK | リソース取得成功 |
| GET (空の場合) | 204 No Content | データが存在しない |
| POST | 201 Created | リソース作成成功 |
| PUT | 200 OK または 204 No Content | リソース更新成功 |
| DELETE | 204 No Content | リソース削除成功 |

## 比較表

| 項目 | 直接返す (`List<BookResponse>`) | `ResponseEntity`を使う |
|------|------------------------------|------------------------|
| ステータスコード | 常に200 OK（暗黙的） | 明示的に指定可能 |
| 柔軟性 | 低い（変更不可） | 高い（将来の拡張に対応） |
| 可読性 | 低い（意図が不明確） | 高い（意図が明確） |
| RESTful準拠 | ❌ | ✅ |

## 実装例

### Before: `ResponseEntity`を使わない場合

```java
@RestController
@RequestMapping("/api/books")
public class BadBookController {

    @GetMapping
    public List<BookResponse> get() {
        return service.get();  // 常に200 OK
    }

    @PostMapping
    public BookResponse create(@RequestBody BookRequest req) {
        return service.create(req);  // 常に200 OK（本来は201 Created）
    }
}
```

### After: `ResponseEntity`を使う場合

```java
@RestController
@RequestMapping("/api/books")
public class GoodBookController {

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);  // 明示的に200 OK
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        BookResponse createdBook = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);  // 201 Created
    }
}
```

## まとめ

- **`ResponseEntity`を使う理由**: HTTPレスポンスを明示的に制御するため
- **主な利点**:
  1. ステータスコードを明示的に指定できる
  2. 条件に応じて異なるステータスコードを返せる
  3. ヘッダーを追加できる（将来の拡張性）
  4. RESTful APIのベストプラクティスに準拠できる

**結論**: `ResponseEntity`を使うことで、HTTPレスポンスを明示的に制御でき、RESTful APIのベストプラクティスに沿った実装になります。

