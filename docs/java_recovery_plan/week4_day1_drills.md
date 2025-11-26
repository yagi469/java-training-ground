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

```java
package com.example.week4.day1.service;

import com.example.week4.day1.dto.BookRequest;
import com.example.week4.day1.dto.BookResponse;
import com.example.week4.day1.entity.Book;
import com.example.week4.day1.exception.BookNotFoundException;
import com.example.week4.day1.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodBookService {
    
    private final BookRepository bookRepository;
    
    // 改善1: メソッド名を明確に、Stream APIを使用
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    // 改善2: カスタム例外を使用、わかりやすいエラーメッセージ
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(
                    "Book not found with id: " + id));
        return toResponse(book);
    }
    
    // 改善3: ヘルパーメソッドで重複を削減
    public BookResponse createBook(BookRequest request) {
        Book book = toEntity(request);
        Book savedBook = bookRepository.save(book);
        return toResponse(savedBook);
    }
    
    // 改善4: Stream APIでフィルタリング、責務を分離
    public List<BookResponse> searchBooks(String author, Integer yearFrom) {
        return bookRepository.findAll()
                .stream()
                .filter(book -> matchesAuthor(book, author))
                .filter(book -> matchesYear(book, yearFrom))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    // ヘルパーメソッド: 条件チェックを分離
    private boolean matchesAuthor(Book book, String author) {
        return author == null || book.getAuthor().contains(author);
    }
    
    private boolean matchesYear(Book book, Integer yearFrom) {
        return yearFrom == null || book.getPublishedYear() >= yearFrom;
    }
    
    // ヘルパーメソッド: Entity → DTO変換
    private BookResponse toResponse(Book book) {
        return new BookResponse(
            book.getId(),
            book.getTitle(),
            book.getAuthor(),
            book.getIsbn(),
            book.getPublishedYear(),
            book.getStockQuantity()
        );
    }
    
    // ヘルパーメソッド: DTO → Entity変換
    private Book toEntity(BookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPublishedYear(request.getPublishedYear());
        book.setStockQuantity(request.getStockQuantity());
        return book;
    }
}
```

#### カスタム例外クラス

```java
package com.example.week4.day1.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super(message);
    }
}
```

## 課題: 自分でリファクタリングしてみよう

### ステップ1: 問題点を特定する

`BadBookService.java` を読んで、以下の問題点を見つけてください：

1. ❌ **問題点A**: どこが読みにくいか？
2. ❌ **問題点B**: どこが重複しているか？
3. ❌ **問題点C**: どこが複雑すぎるか？

### ステップ2: 改善計画を立てる

各問題点に対して、どう改善するかを考えます：

- **Stream APIに置き換える** - for文を削除
- **ヘルパーメソッドを抽出** - 重複を削減
- **カスタム例外を作成** - エラーハンドリングを改善
- **メソッド名を改善** - 意図を明確にする

### ステップ3: 実装する

`GoodBookService.java` を作成して、改善版を実装してください。

### ステップ4: 比較する

Before と After を並べて、何が改善されたかを確認してください。

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

## 学習のポイント

### リファクタリングの原則

#### 1. DRY原則（Don't Repeat Yourself）
同じコードを2回書かない。ヘルパーメソッドで共通化。

#### 2. SOLID原則のS（Single Responsibility）
1つのメソッドは1つの責務だけを持つ。

#### 3. 可読性優先
「動く」だけでなく「読みやすい」コードを書く。

### リファクタリングの手順

1. **テストを書く** - リファクタリング前に動作を確認
2. **小さく変更** - 一度に大きく変えない
3. **テストを実行** - 変更後も動作が変わっていないことを確認
4. **繰り返す** - 少しずつ改善を重ねる

## 実行方法

Week3のプロジェクトを使って、新しいパッケージで実装してみましょう：

```
week3-interface-first/
└── src/main/java/com/example/week3/
    └── day4/  ← 新規作成
        ├── service/
        │   ├── BadBookService.java    ← Before版
        │   └── GoodBookService.java   ← After版（課題）
        └── exception/
            └── BookNotFoundException.java
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
