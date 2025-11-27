# Week 4 Day 3 実装完了記録: 統合リファクタリング実践（更新・削除機能の追加）

## 実装概要

Week4 Day3では、Day1（Service層）とDay2（Controller層）で学んだリファクタリングの知識を活用して、**更新・削除機能を追加**し、**完全なCRUD API**を完成させました。

## 実装した内容

### 1. GoodBookService.java - 更新・削除機能の追加

#### 実装したメソッド

1. **`updateBook()`** - 書籍更新メソッド
2. **`deleteBook()`** - 書籍削除メソッド

### 2. GoodBookController.java - 更新・削除エンドポイントの追加

#### 実装したエンドポイント

1. **`PUT /api/books/{id}`** - 書籍更新エンドポイント
2. **`DELETE /api/books/{id}`** - 書籍削除エンドポイント

## 実装の詳細

### 1. 更新機能の実装

#### GoodBookService.java

```java
// 書籍更新メソッド
public BookResponse updateBook(@NonNull Long id, BookRequest request) {
    // 1. 既存のリソースを取得（存在チェック）
    Book book = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

    // 2. フィールドを更新
    book.setTitle(request.getTitle());
    book.setAuthor(request.getAuthor());
    book.setIsbn(request.getIsbn());
    book.setPublishedYear(request.getPublishedYear());
    book.setStockQuantity(request.getStockQuantity());

    // 3. 保存して返す
    Book updatedBook = bookRepository.save(book);
    return toResponse(updatedBook);
}
```

**実装のポイント:**
- `findById().orElseThrow()`で存在チェックと取得を同時に行う
- 既存のEntityを更新するため、`toEntity()`ではなく直接`set`メソッドを使用
- `toResponse()`ヘルパーメソッドで統一された変換処理

#### GoodBookController.java

```java
// 書籍更新エンドポイント
@PutMapping("/{id}")
public ResponseEntity<BookResponse> updateBook(
        @PathVariable Long id,
        @Valid @RequestBody BookRequest request) {
    BookResponse updatedBook = bookService.updateBook(id, request);
    return ResponseEntity.ok(updatedBook);  // 200 OK
}
```

**実装のポイント:**
- `@Valid`アノテーションでバリデーションを有効化
- `ResponseEntity.ok()`で200 OKを返す
- Day2で学んだパターンをそのまま適用

### 2. 削除機能の実装

#### GoodBookService.java

```java
// 書籍削除メソッド
public void deleteBook(@NonNull Long id) {
    // 存在確認
    if (!bookRepository.existsById(id)) {
        throw new BookNotFoundException("Book not found with id: " + id);
    }
    // 削除
    bookRepository.deleteById(id);
}
```

**実装のポイント:**
- `existsById()`で存在確認
- 存在しない場合は`BookNotFoundException`を投げる
- `GlobalExceptionHandler`で自動的に404 Not Foundを返す

#### GoodBookController.java

```java
// 書籍削除エンドポイント
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    bookService.deleteBook(id);
    return ResponseEntity.noContent().build();  // 204 No Content
}
```

**実装のポイント:**
- `ResponseEntity<Void>`でボディなしのレスポンス
- `ResponseEntity.noContent().build()`で204 No Contentを返す
- RESTful APIのベストプラクティスに準拠

## 実装パターンの一貫性

### Day1、Day2で学んだパターンの適用

#### 1. 命名規則の統一

| 機能 | Serviceメソッド | Controllerメソッド | エンドポイント |
|------|---------------|------------------|--------------|
| 一覧取得 | `getAllBooks()` | `getAllBooks()` | `GET /api/books` |
| 詳細取得 | `getBookById()` | `getBookById()` | `GET /api/books/{id}` |
| 作成 | `createBook()` | `createBook()` | `POST /api/books` |
| 更新 | `updateBook()` | `updateBook()` | `PUT /api/books/{id}` |
| 削除 | `deleteBook()` | `deleteBook()` | `DELETE /api/books/{id}` |
| 検索 | `searchBooks()` | `searchBooks()` | `GET /api/books/search` |

#### 2. エラーハンドリングの統一

すべてのメソッドで`BookNotFoundException`を使用し、`GlobalExceptionHandler`で統一処理：

```java
// Service層 - 例外を投げるだけ
Book book = bookRepository.findById(id)
        .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

// Controller層 - try-catch不要
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

#### 3. HTTPステータスコードの統一

| 操作 | HTTPメソッド | 成功時のステータスコード | 実装 |
|------|------------|----------------------|------|
| 一覧取得 | GET | 200 OK | `ResponseEntity.ok()` |
| 詳細取得 | GET | 200 OK | `ResponseEntity.ok()` |
| 作成 | POST | 201 Created | `ResponseEntity.status(HttpStatus.CREATED)` |
| 更新 | PUT | 200 OK | `ResponseEntity.ok()` |
| 削除 | DELETE | 204 No Content | `ResponseEntity.noContent().build()` |
| 検索 | GET | 200 OK | `ResponseEntity.ok()` |

#### 4. バリデーションの統一

作成・更新の両方で`@Valid`を使用：

```java
@PostMapping
public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
    // ...
}

@PutMapping("/{id}")
public ResponseEntity<BookResponse> updateBook(
        @PathVariable Long id,
        @Valid @RequestBody BookRequest request) {
    // ...
}
```

## 完全なCRUD APIの完成

### 実装されたエンドポイント一覧

```
GET    /api/books           - 書籍一覧取得
GET    /api/books/{id}      - 書籍詳細取得
POST   /api/books           - 書籍作成
PUT    /api/books/{id}      - 書籍更新
DELETE /api/books/{id}      - 書籍削除
GET    /api/books/search    - 書籍検索
```

### エラーハンドリング

| エラー | HTTPステータスコード | レスポンス形式 |
|--------|-------------------|-------------|
| リソースが見つからない | 404 Not Found | `ErrorResponse("NOT_FOUND", message)` |
| バリデーションエラー | 400 Bad Request | `ErrorResponse("VALIDATION_ERROR", message)` |

## 実装のポイント

### 1. 更新機能の実装パターン

```java
// パターン: 取得 → 更新 → 保存
Book book = bookRepository.findById(id)
        .orElseThrow(() -> new BookNotFoundException(...));
book.setTitle(request.getTitle());
// ... 他のフィールドも更新
Book updatedBook = bookRepository.save(book);
return toResponse(updatedBook);
```

**重要なポイント:**
- 既存のEntityを更新するため、`toEntity()`は使わない
- 直接`set`メソッドでフィールドを更新
- `save()`で更新を反映

### 2. 削除機能の実装パターン

```java
// パターン: 存在確認 → 削除
if (!bookRepository.existsById(id)) {
    throw new BookNotFoundException(...);
}
bookRepository.deleteById(id);
```

**重要なポイント:**
- 削除前に存在確認を行う
- 存在しない場合は例外を投げる
- 削除成功時は204 No Contentを返す（ボディなし）

### 3. 一貫性の確保

- **命名規則**: すべてのメソッドで統一された命名規則
- **エラーハンドリング**: `GlobalExceptionHandler`で統一処理
- **HTTPステータスコード**: RESTful APIのベストプラクティスに準拠
- **バリデーション**: `@Valid`で統一

## チェックリスト

### 実装完了項目

- [x] `GoodBookService.java` に `updateBook()` を実装
- [x] `GoodBookService.java` に `deleteBook()` を実装
- [x] `GoodBookController.java` に PUT エンドポイントを実装
- [x] `GoodBookController.java` に DELETE エンドポイントを実装
- [x] すべての機能が正しく動作する
- [x] エラーハンドリングが適切に機能する
- [x] リンターエラーがない

### 改善点

- [x] 一貫性: すべてのメソッドで同じパターンを使用
- [x] 命名: 統一された命名規則
- [x] エラーハンドリング: `GlobalExceptionHandler`で統一処理
- [x] HTTPステータスコード: 適切に使用
- [x] バリデーション: `@Valid`を使用

## Week4全体の振り返り

### Day1: Service層のリファクタリング

- 命名の改善
- 重複コードの削除（`toResponse`, `toEntity`）
- Stream APIの活用
- カスタム例外の導入

### Day2: Controller層のリファクタリング

- `ResponseEntity`の適切な使用
- `@RestControllerAdvice`によるグローバル例外処理
- `@Valid`によるバリデーション
- HTTPステータスコードの適切な使用

### Day3: 統合リファクタリング

- 更新・削除機能の追加
- 一貫性の確保
- 完全なCRUD APIの完成

## まとめ

Week4 Day3では、Day1とDay2で学んだリファクタリングの知識を活用して、**更新・削除機能を追加**し、**完全なCRUD API**を完成させました。

### 学んだポイント

1. **一貫性の重要性** - すべてのメソッドで同じパターンを使用することで、コードが読みやすくなる
2. **更新機能の実装パターン** - 取得 → 更新 → 保存の流れ
3. **削除機能の実装パターン** - 存在確認 → 削除の流れ
4. **RESTful APIのベストプラクティス** - 適切なHTTPステータスコードの使用

これらの知識により、**読みやすく保守しやすい完全なCRUD API**を実装できるようになりました。

