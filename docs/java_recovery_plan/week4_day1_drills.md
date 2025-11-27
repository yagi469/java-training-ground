# Week 4 Day 1 Drill: リファクタリング実践（コードの改善）

## 目的
「動くけど読みにくいコード」を、Week 1-3で学んだ知識を使って**美しく読みやすいコード**に書き直す練習をします。

## 背景
実務では、以下のような理由で読みにくいコードに遭遇します：
- 急いで書いたコード
- 初心者が書いたコード
- 仕様変更を繰り返して複雑化したコード

これらを**リファクタリング（改善）**するスキルは、実装力と同じくらい重要です。

## このドリルでやること

「動くけど読みにくい」書籍管理APIのコードを、以下の観点で改善します：

1. **命名の改善** - 変数名・メソッド名を明確にする
2. **重複コードの削除** - DRY原則（Don't Repeat Yourself）
3. **責務の分離** - 1つのメソッドに詰め込みすぎない
4. **Stream APIの活用** - for文をStream APIに置き換え
5. **例外処理の改善** - カスタム例外クラスの導入

## 演習内容

### Before: 読みにくいコード

以下は、Week1-3で作成した書籍管理システムを「わざと読みにくく」したバージョンです。

#### `BadBookService.java` - リファクタリング前

```java
package com.example.week4.day1.service;

import com.example.week4.day1.dto.BookRequest;
import com.example.week4.day1.dto.BookResponse;
import com.example.week4.day1.entity.Book;
import com.example.week4.day1.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BadBookService {
    
    private final BookRepository bookRepository;
    
    // 悪い例1: メソッド名が不明確、複数の責務が混在
    public List<BookResponse> get() {
        List<Book> list = bookRepository.findAll();
        List<BookResponse> res = new ArrayList<>();
        
        // 悪い例2: for文の使用（Stream APIを使うべき）
        for (int i = 0; i < list.size(); i++) {
            Book b = list.get(i);
            BookResponse r = new BookResponse(
                b.getId(),
                b.getTitle(),
                b.getAuthor(),
                b.getIsbn(),
                b.getPublishedYear(),
                b.getStockQuantity()
            );
            res.add(r);
        }
        return res;
    }
    
    // 悪い例3: 例外処理が不適切
    public BookResponse getById(Long id) {
        Book b = bookRepository.findById(id).orElse(null);
        if (b == null) {
            throw new RuntimeException("Not found");  // メッセージが不親切
        }
        return new BookResponse(
            b.getId(),
            b.getTitle(),
            b.getAuthor(),
            b.getIsbn(),
            b.getPublishedYear(),
            b.getStockQuantity()
        );
    }
    
    // 悪い例4: 重複コード（Entity → DTOの変換が重複）
    public BookResponse create(BookRequest req) {
        Book b = new Book();
        b.setTitle(req.getTitle());
        b.setAuthor(req.getAuthor());
        b.setIsbn(req.getIsbn());
        b.setPublishedYear(req.getPublishedYear());
        b.setStockQuantity(req.getStockQuantity());
        
        Book saved = bookRepository.save(b);
        
        return new BookResponse(
            saved.getId(),
            saved.getTitle(),
            saved.getAuthor(),
            saved.getIsbn(),
            saved.getPublishedYear(),
            saved.getStockQuantity()
        );
    }
    
    // 悪い例5: メソッドが肥大化、複数の処理が混在
    public List<BookResponse> search(String author, Integer year) {
        List<Book> all = bookRepository.findAll();
        List<Book> filtered = new ArrayList<>();
        
        // ネストしたif文
        for (Book b : all) {
            boolean match = true;
            if (author != null) {
                if (!b.getAuthor().contains(author)) {
                    match = false;
                }
            }
            if (year != null) {
                if (b.getPublishedYear() < year) {
                    match = false;
                }
            }
            if (match) {
                filtered.add(b);
            }
        }
        
        // 変換処理も混在
        List<BookResponse> result = new ArrayList<>();
        for (Book b : filtered) {
            result.add(new BookResponse(
                b.getId(),
                b.getTitle(),
                b.getAuthor(),
                b.getIsbn(),
                b.getPublishedYear(),
                b.getStockQuantity()
            ));
        }
        
        return result;
    }
}
```

### After: 改善後のコード（目標）

#### `GoodBookService.java` - リファクタリング後

（解答例は実装後に確認してください）

## 課題: 自分でリファクタリングしてみよう

### ステップ1: 問題点を特定する

`BadBookService.java` を読んで、以下の問題点を見つけてください：

1. ❌ **問題点A**: どこが読みにくいか？（命名、構造）
2. ❌ **問題点B**: どこが重複しているか？（変換処理など）
3. ❌ **問題点C**: どこが複雑すぎるか？（ネスト、条件分岐）

### ステップ2: 実装のヒントと手順

以下の手順で `GoodBookService.java` を実装していきましょう。

#### 1. ヘルパーメソッドから始める
まずは重複している変換処理を共通化します。

```java
// TODO 6: Entity → DTO変換
private BookResponse toResponse(Book book) {
    return new BookResponse(
        book.getId(),
        book.getTitle(),
        // ... 他のフィールド
    );
}

// TODO 7: DTO → Entity変換
private Book toEntity(BookRequest request) {
    Book book = new Book();
    book.setTitle(request.getTitle());
    // ... 他のフィールド
    return book;
}
```

#### 2. 全件取得メソッドの改善
for文をStream APIに置き換えます。

```java
// TODO 1: getAllBooks()
public List<BookResponse> getAllBooks() {
    return bookRepository.findAll()
            .stream()
            .map(this::toResponse) // ヘルパーメソッドを使用
            .collect(Collectors.toList());
}
```

#### 3. 1件取得メソッドの改善
カスタム例外を使ってエラーハンドリングを改善します。

```java
// TODO 2: getBookById()
public BookResponse getBookById(Long id) {
    Book book = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    return toResponse(book);
}
```

#### 4. 作成メソッドの改善
ヘルパーメソッドを使ってシンプルにします。

```java
// TODO 3: createBook()
public BookResponse createBook(BookRequest request) {
    Book book = toEntity(request);
    Book savedBook = bookRepository.save(book);
    return toResponse(savedBook);
}
```

#### 5. 検索メソッドの改善
複雑な条件分岐を分離し、Stream APIでフィルタリングします。

```java
// TODO 5: 条件チェック用メソッド
private boolean matchesAuthor(Book book, String author) {
    return author == null || book.getAuthor().contains(author);
}

// TODO 4: searchBooks()
public List<BookResponse> searchBooks(String author, Integer yearFrom) {
    return bookRepository.findAll()
            .stream()
            .filter(book -> matchesAuthor(book, author))
            .filter(book -> matchesYear(book, yearFrom))
            .map(this::toResponse)
            .collect(Collectors.toList());
}
```

## リファクタリングのチェックリスト

改善後のコードが以下を満たしているか確認してください：

### 1. 命名
- [ ] メソッド名が「何をするか」を明確に表している
- [ ] 変数名が省略されていない（`b`, `r`, `res` など避ける）

### 2. 重複削除
- [ ] Entity → DTO変換が1箇所にまとまっている
- [ ] 同じロジックが複数箇所に書かれていない

### 3. Stream API
- [ ] for文がStream APIに置き換わっている
- [ ] `map()`, `filter()`, `collect()` を適切に使用

### 4. 責務の分離
- [ ] 1つのメソッドが1つのことだけをしている
- [ ] 複雑な条件チェックがヘルパーメソッドに分離されている

### 5. 例外処理
- [ ] カスタム例外クラスを使用
- [ ] わかりやすいエラーメッセージ

## 実行方法

Week4のプロジェクトを使って実装します：

```
exercises/
└── week4-refactoring-practice/
    ├── pom.xml
    └── src/main/java/com/example/week4/
        ├── bad/
        │   └── service/
        │       └── BadBookService.java    ← Before版
        └── good/
            └── service/
                └── GoodBookService.java   ← After版（課題）
```

## 完了条件

✅ `BadBookService.java` の問題点を5つ以上特定できる
✅ `GoodBookService.java` を実装できる
✅ Stream APIを適切に使用している
✅ 重複コードが削減されている
✅ カスタム例外クラスを作成している

---

## ヒント

困ったときは、以下を意識してください：

- **for文を見たら** → Stream APIに置き換えられないか？
- **同じコードを見たら** → ヘルパーメソッドに抽出できないか？
- **複雑な条件を見たら** → 小さなメソッドに分割できないか？
- **あいまいな名前を見たら** → より明確な名前に変更できないか？

頑張ってください！
