# Week 3 Day 3 Drill: テスト駆動開発の真似事（Test-First Development）

## 目的
実装を書く前に、**テストコードを先に書く**習慣を身につけます。
テストが「このメソッドはどう使われるべきか」を明確にし、実装の手戻りを防ぎます。

## 背景
通常の開発フロー：
```
実装を書く → テストを書く → バグ発見 → 修正 → また修正...
```

テストファースト（Test-First）：
```
テストを書く → 実装を書く → テストを実行 → 一発で通る！
```

## このドリルでやること

Day 1とDay 2で作成した書籍管理システムに対して、**テストを先に書いて**から実装を確認する練習をします。

### ステップ1: テスト用の依存関係を確認

`pom.xml` に以下が含まれていることを確認してください：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### ステップ2: BookService のユニットテストを書く

#### 課題1: `getAllBooks()` のテストを書く

**テストファイル**: `src/test/java/com/example/week3/day1/service/BookServiceTest.java`

**ヒント:**
- `@ExtendWith(MockitoExtension.class)` を使う
- `@Mock` で `BookRepository` をモック化
- `@InjectMocks` で `BookService` にモックを注入
- `Mockito.when()` でRepositoryの振る舞いを定義
- `Assertions.assertEquals()` で結果を検証

**書くべきテスト:**
1. 書籍が3冊ある場合、3冊のリストが返ることを確認
2. 書籍が0冊の場合、空のリストが返ることを確認

#### 課題2: `getBookById()` のテストを書く

**ヒント:**
- 正常系：IDで書籍が見つかる場合
- 異常系：IDで書籍が見つからない場合、例外がスローされることを確認
- `Assertions.assertThrows()` を使う

#### 課題3: `createBook()` のテストを書く

**ヒント:**
- `BookRequest` を作成
- `save()` が呼ばれたときの戻り値をモック
- 返された `BookResponse` の内容が正しいか検証

#### 課題4: `searchByAuthor()` のテストを書く（Day 2の機能）

**ヒント:**
- 著者名で検索した結果が正しくDTOに変換されているか確認
- 0件の場合も確認

### ステップ3: BookController の統合テストを書く

#### 課題5: Controller のテストを書く

**テストファイル**: `src/test/java/com/example/week3/day1/controller/BookControllerTest.java`

**ヒント:**
- `@WebMvcTest(BookController.class)` を使う
- `@MockBean` で `BookService` をモック化
- `MockMvc` を使ってHTTPリクエストをシミュレート
- `.andExpect(status().isOk())` でステータスコードを検証
- `.andExpect(jsonPath("$[0].title").value("期待値"))` でJSONの内容を検証

**書くべきテスト:**
1. `GET /books` が200を返すことを確認
2. `GET /books/{id}` が正しい書籍情報を返すことを確認
3. `POST /books` が201を返すことを確認
4. `GET /books/search/author?author=太郎` が動作することを確認（Day 2の機能）

## 実装のヒント

### BookServiceTest.java の基本構造

```java
package com.example.week3.day1.service;

import com.example.week3.day1.dto.BookRequest;
import com.example.week3.day1.dto.BookResponse;
import com.example.week3.day1.entity.Book;
import com.example.week3.day1.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void getAllBooks_書籍が存在する場合_全書籍のリストが返る() {
        // given (準備)
        // TODO: テストデータを作成
        // Book型のリストを作成し、3冊分のBookオブジェクトを追加
        
        // TODO: when(bookRepository.findAll()).thenReturn(...)でモックの振る舞いを定義
        
        // when (実行)
        // TODO: bookService.getAllBooks() を呼び出し
        
        // then (検証)
        // TODO: 結果が3件であることを確認
        // TODO: 各BookResponseの内容が正しいことを確認
    }

    @Test
    void getBookById_存在するIDを指定_書籍が返る() {
        // TODO: 実装してみてください
    }

    @Test
    void getBookById_存在しないIDを指定_例外がスローされる() {
        // TODO: 実装してみてください
        // ヒント: assertThrows(RuntimeException.class, () -> {...})
    }

    @Test
    void createBook_正しいリクエスト_書籍が作成される() {
        // TODO: 実装してみてください
    }

    @Test
    void searchByAuthor_著者名で検索_該当書籍のリストが返る() {
        // TODO: Day 2の機能のテスト
    }
}
```

### BookControllerTest.java の基本構造

```java
package com.example.week3.day1.controller;

import com.example.week3.day1.dto.BookRequest;
import com.example.week3.day1.dto.BookResponse;
import com.example.week3.day1.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllBooks_GET_200とリストが返る() throws Exception {
        // given
        // TODO: BookResponseのリストを作成
        // TODO: when(bookService.getAllBooks()).thenReturn(...)
        
        // when & then
        // TODO: mockMvc.perform(get("/books"))
        //           .andExpect(status().isOk())
        //           .andExpect(jsonPath("$").isArray())
        //           .andExpect(jsonPath("$[0].title").value("期待値"));
    }

    @Test
    void getBookById_存在するID_200と書籍情報が返る() throws Exception {
        // TODO: 実装してみてください
    }

    @Test
    void createBook_正しいリクエスト_201が返る() throws Exception {
        // TODO: 実装してみてください
        // ヒント: post("/books")
        //        .contentType(MediaType.APPLICATION_JSON)
        //        .content(objectMapper.writeValueAsString(request))
    }

    @Test
    void searchByAuthor_著者名を指定_該当書籍が返る() throws Exception {
        // TODO: Day 2の機能のテスト
        // ヒント: get("/books/search/author").param("author", "太郎")
    }
}
```

## チャレンジ課題

余裕があれば、以下のテストも書いてみてください：

1. **`updateBook()` のテスト**
   - 正常系：更新が成功する
   - 異常系：存在しないIDで更新しようとして例外

2. **`deleteBook()` のテスト**
   - 正常系：削除が成功する
   - 異常系：存在しないIDで削除しようとして例外

3. **`searchBooks()` のテスト（ページング対応）**
   - ページ番号とサイズが正しく設定される
   - 検索条件が正しく適用される

## 学習のポイント

### 1. テストの3段階（AAA パターン）
- **Arrange（準備）**: テストデータを準備
- **Act（実行）**: テスト対象のメソッドを実行
- **Assert（検証）**: 結果が期待通りか確認

### 2. Mockitoの基本
- `@Mock`: モックオブジェクトを作成
- `@InjectMocks`: モックを注入した実オブジェクトを作成
- `when(...).thenReturn(...)`: メソッドの振る舞いを定義
- `verify(...)`: メソッドが呼ばれたか確認

### 3. MockMvcの基本
- `mockMvc.perform(...)`: HTTPリクエストをシミュレート
- `.andExpect(status()...)`: HTTPステータスコードを検証
- `.andExpect(jsonPath()...)`: JSONレスポンスの内容を検証

## 完了条件

✅ `BookServiceTest.java` に最低4つのテストメソッドを実装
✅ `BookControllerTest.java` に最低4つのテストメソッドを実装
✅ すべてのテストがグリーン（成功）になる
✅ `mvn test` でテストが実行できる

## 実行方法

```bash
# テストを実行
./mvnw.cmd test

# 特定のテストクラスだけ実行
./mvnw.cmd test -Dtest=BookServiceTest

# 特定のテストメソッドだけ実行
./mvnw.cmd test -Dtest=BookServiceTest#getAllBooks_書籍が存在する場合_全書籍のリストが返る
```

---

## 困ったときは

1. **テストの書き方がわからない**
   - 上記のヒントを参考に、1つずつコメントアウトしている部分を埋めてみる
   
2. **Mockitoの使い方がわからない**
   - `when(repository.findAll()).thenReturn(リスト)` の形で、メソッドの戻り値を定義する
   
3. **テストが失敗する**
   - エラーメッセージをよく読む
   - `System.out.println()` でデバッグ
   - 私に質問してください！

頑張ってください！わからないところがあれば、いつでも質問してくださいね。
