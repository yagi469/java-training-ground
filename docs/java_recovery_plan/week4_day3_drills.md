# Week 4 Day 3 Drill: 統合リファクタリング実践（更新・削除機能の追加）

## 目的

Week4 Day1でService層、Day2でController層のリファクタリングを学んだので、Day3では**更新・削除機能を追加**し、**全体を統合**してリファクタリングを完成させます。

これまで学んだ知識を総合的に活用し、完全なCRUD APIを実装します。

## 背景

実務では、以下のような機能追加が必要になります：
- 既存リソースの更新（PUT/PATCH）
- リソースの削除（DELETE）
- より複雑なビジネスロジックの実装

これらの機能も、リファクタリングの原則に従って実装する必要があります。

## このドリルでやること

書籍管理APIに**更新・削除機能**を追加し、以下の観点で改善します：

1. **更新機能の実装** - PUTエンドポイントの追加
2. **削除機能の実装** - DELETEエンドポイントの追加
3. **一貫性の確保** - Day1、Day2で学んだパターンの適用
4. **エラーハンドリングの拡張** - 新しい例外の追加
5. **全体の統合確認** - すべての機能が正しく動作するか確認

## 演習内容

### ステップ1: Service層に更新・削除機能を追加

#### `GoodBookService.java` に追加するメソッド

```java
// TODO 1: 書籍更新メソッド
public BookResponse updateBook(@NonNull Long id, BookRequest request) {
    // ヒント:
    // Book book = bookRepository.findById(id)
    //         .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    // book.setTitle(request.getTitle());
    // book.setAuthor(request.getAuthor());
    // ... 他のフィールドも更新
    // Book updatedBook = bookRepository.save(book);
    // return toResponse(updatedBook);
}

// TODO 2: 書籍削除メソッド
public void deleteBook(@NonNull Long id) {
    // ヒント:
    // if (!bookRepository.existsById(id)) {
    //     throw new BookNotFoundException("Book not found with id: " + id);
    // }
    // bookRepository.deleteById(id);
}
```

### ステップ2: Controller層に更新・削除エンドポイントを追加

#### `GoodBookController.java` に追加するメソッド

```java
// TODO 3: 書籍更新エンドポイント
@PutMapping("/{id}")
public ResponseEntity<BookResponse> updateBook(
        @PathVariable Long id,
        @Valid @RequestBody BookRequest request) {
    // ヒント:
    // BookResponse updatedBook = bookService.updateBook(id, request);
    // return ResponseEntity.ok(updatedBook);
}

// TODO 4: 書籍削除エンドポイント
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    // ヒント:
    // bookService.deleteBook(id);
    // return ResponseEntity.noContent().build();  // 204 No Content
}
```

## 実装のポイント

### 1. 更新機能の実装パターン

```java
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

### 2. 削除機能の実装パターン

```java
public void deleteBook(@NonNull Long id) {
    // 方法1: existsByIdで存在確認してから削除
    if (!bookRepository.existsById(id)) {
        throw new BookNotFoundException("Book not found with id: " + id);
    }
    bookRepository.deleteById(id);

    // 方法2: findByIdで取得してから削除（推奨）
    // Book book = bookRepository.findById(id)
    //         .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    // bookRepository.delete(book);
}
```

### 3. HTTPステータスコード

| 操作 | HTTPメソッド | 成功時のステータスコード |
|------|------------|----------------------|
| 更新 | PUT | 200 OK |
| 削除 | DELETE | 204 No Content |

### 4. エラーハンドリング

- 更新・削除時に存在しないIDを指定した場合 → `BookNotFoundException`（既に実装済み）
- `GlobalExceptionHandler`で自動的に404 Not Foundを返す

## リファクタリングのチェックリスト

実装後のコードが以下を満たしているか確認してください：

### 1. 一貫性
- [ ] すべてのメソッドで同じパターンを使用している
- [ ] 命名規則が統一されている
- [ ] エラーハンドリングが統一されている

### 2. コードの品質
- [ ] 重複コードがない
- [ ] ヘルパーメソッド（`toResponse`, `toEntity`）を活用している
- [ ] Stream APIを適切に使用している

### 3. RESTful API
- [ ] 適切なHTTPメソッドを使用している
- [ ] 適切なHTTPステータスコードを返している
- [ ] エンドポイントがRESTfulな設計になっている

### 4. バリデーション
- [ ] `@Valid`アノテーションを使用している
- [ ] バリデーションエラーが適切に処理されている

## 完了条件

✅ `GoodBookService.java` に `updateBook()` と `deleteBook()` を実装できる
✅ `GoodBookController.java` に PUT と DELETE エンドポイントを実装できる
✅ すべての機能が正しく動作する
✅ エラーハンドリングが適切に機能する
✅ リンターエラーがない

## 実装後の確認事項

実装が完了したら、以下を確認してください：

1. **全機能の動作確認**
   - GET `/api/books` - 一覧取得
   - GET `/api/books/{id}` - 詳細取得
   - POST `/api/books` - 作成
   - PUT `/api/books/{id}` - 更新
   - DELETE `/api/books/{id}` - 削除
   - GET `/api/books/search` - 検索

2. **エラーハンドリングの確認**
   - 存在しないIDで取得 → 404 Not Found
   - 存在しないIDで更新 → 404 Not Found
   - 存在しないIDで削除 → 404 Not Found
   - バリデーションエラー → 400 Bad Request

3. **コードの品質確認**
   - 重複コードがないか
   - 命名が統一されているか
   - コメントが適切か

---

## ヒント

困ったときは、以下を意識してください：

- **更新機能**: 既存のリソースを取得 → フィールドを更新 → 保存
- **削除機能**: 存在確認 → 削除 → 204 No Contentを返す
- **一貫性**: Day1、Day2で学んだパターンをそのまま適用
- **エラーハンドリング**: `GlobalExceptionHandler`が自動的に処理してくれる

頑張ってください！

