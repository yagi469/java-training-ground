# Java実装スピード改善 Week 1 Day 3: 例外ハンドリングで統一的なエラー処理を実現

## はじめに

Day 1ではController・Service、Day 2ではEntity・Repositoryを学び、基本的なCRUD操作が実装できるようになりました。しかし、エラーが発生したときのレスポンスはどうでしょうか？

Day 2までは`RuntimeException`を投げるだけで、エラーレスポンスはSpringのデフォルト形式でした。これでは：
- エラー形式がバラバラ
- ステータスコードが適切でない（全部500になる）
- エラーメッセージが不親切

Day 3では、**@RestControllerAdvice**を使った統一的なエラー処理を学びます。

この記事は「Java実装スピード改善リカバリープラン」のWeek 1 Day 3の学習記録です。

## 学習目標

1. **カスタム例外クラス**を2分で書けるようにする
2. **@RestControllerAdvice**を5分で書けるようにする
3. **Service層での例外の使い分け**を理解する

## 実装内容

### 1. カスタム例外クラス（ResourceNotFoundException.java）

まず、リソースが見つからない場合に投げる専用の例外クラスを作成します。

```java
package com.example.week1.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

**鉄板パターン（Custom Exception）:**
1. **RuntimeExceptionを継承**: 非検査例外（try-catchを強制しない）
2. **メッセージを受け取るコンストラクタ**: エラー内容を動的に設定
3. **シンプルに**: 余計な機能は追加しない

**なぜRuntimeException？**
- Springでは非検査例外を推奨（ビジネスロジック内でtry-catchを書く必要がない）
- グローバルハンドラーで統一的にキャッチできる

### 2. グローバル例外ハンドラー（GlobalExceptionHandler.java）

次に、`@RestControllerAdvice`を使って、全コントローラーで共通のエラー処理を実装します。

```java
package com.example.week1.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 404, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}

record ErrorResponse(String message, int status, LocalDateTime timestamp) {
}
```

**鉄板パターン（Global Exception Handler）:**
1. **@RestControllerAdvice**: 全コントローラーに適用されるAOP
2. **@ExceptionHandler(例外クラス.class)**: 特定の例外をキャッチ
3. **ErrorResponse record**: 統一されたエラーレスポンス形式
4. **適切なHTTPステータス**: 404 NOT_FOUNDを返す

**ErrorResponseの構造:**
- `message`: エラーメッセージ
- `status`: HTTPステータスコード（数値）
- `timestamp`: エラー発生時刻

### 3. Service層での使用（UserService.java）

最後に、Service層で`RuntimeException`を`ResourceNotFoundException`に置き換えます。

```java
package com.example.week1.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.week1.dto.UserRegistrationRequest;
import com.example.week1.dto.UserResponse;
import com.example.week1.entity.User;
import com.example.week1.exception.ResourceNotFoundException;
import com.example.week1.repository.UserRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;

    @Transactional
    public UserResponse createUser(UserRegistrationRequest request) {
        User user = new User(null, request.username(), request.password());
        User savedUser = repository.save(user);
        return new UserResponse(savedUser.getId(), savedUser.getUsername());
    }

    public UserResponse getUser(@NonNull Long id) {
        return repository.findById(id)
                .map(user -> new UserResponse(user.getId(), user.getUsername()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
```

**変更点:**
```java
// Before (Day 2)
.orElseThrow(() -> new RuntimeException("User not found"));

// After (Day 3)
.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
```

## エラーレスポンスの変化

### Before（Day 2まで）
存在しないユーザー（ID=999）をGETすると：

```json
{
  "timestamp": "2024-11-24T12:55:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "User not found",
  "path": "/users/999"
}
```

**問題点:**
- ステータスコードが500（サーバー内部エラー）→ 本当は404（リソースが見つからない）
- エラー形式がSpringのデフォルト

### After（Day 3）
同じリクエストで：

```json
{
  "message": "User not found with id: 999",
  "status": 404,
  "timestamp": "2024-11-24T12:55:00"
}
```

**改善点:**
- ✅ ステータスコードが404（正しい）
- ✅ エラーメッセージにIDが含まれる（親切）
- ✅ 統一されたエラー形式

## 学んだポイント

### 1. @RestControllerAdviceの威力

Controller層に例外処理を書く必要が**一切ない**。

```java
// これを書く必要がない
@GetMapping("/{id}")
public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
    try {
        return ResponseEntity.ok(service.getUser(id));
    } catch (ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(...); // 不要！
    }
}
```

`@RestControllerAdvice`が全て処理してくれます。

### 2. カスタム例外の使い分け

実践では、複数のカスタム例外を作成します。

| 例外名 | HTTPステータス | 用途 |
|--------|---------------|------|
| `ResourceNotFoundException` | 404 NOT_FOUND | リソースが見つからない |
| `BadRequestException` | 400 BAD_REQUEST | リクエストが不正 |
| `UnauthorizedException` | 401 UNAUTHORIZED | 認証失敗 |
| `ForbiddenException` | 403 FORBIDDEN | 権限なし |

すべて同じパターンで実装できます。

### 3. よくあるミス

練習中に何度も間違えたポイント：

- **@RestControllerAdvice の忘れ**: これがないとグローバルハンドラーとして機能しない
- **@ExceptionHandler の引数ミス**: `@ExceptionHandler(ResourceNotFoundException.class)` の `.class` を忘れる
- **ResponseEntity の型パラメータ**: `ResponseEntity<ErrorResponse>` の `<ErrorResponse>` を忘れる
- **import忘れ**: `LocalDateTime`, `HttpStatus` などのインポートを忘れる

### 4. 拡張性

`GlobalExceptionHandler`に追加のハンドラーを簡単に追加できます。

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        // ...
    }

    // バリデーションエラー
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        // ...
    }

    // その他すべての例外
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        // ...
    }
}
```

## まとめ

今日の成果：
- ✅ カスタム例外クラスの作成パターン
- ✅ `@RestControllerAdvice`による統一的なエラー処理
- ✅ 適切なHTTPステータスコードの返却
- ✅ エラーレスポンスの統一（ErrorResponse）

次のステップ：
- **Day 4-5**: CRUD Time Attack（これまでの知識で1時間以内にCRUDアプリを完成させる）

Day 1〜3で学んだパターンを組み合わせれば、**堅牢でメンテナブルなREST API**が作れます。
「エラーハンドリングどうしよう...」と悩む時間が、ほぼゼロになりました！

---

**関連リンク:**
- [Day 1: Controller & Service層](./day1_blog_post.md)
- [Day 2: Entity & Repository層](./day2_blog_post.md)
- [Java実装スピード改善リカバリープラン全体](./java_recovery_plan.md)
- [鉄板パターン集（スニペット）](./snippets.md)
