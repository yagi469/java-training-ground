# Java実装スピード改善 Week 1 Day 1: Controller & Service層の鉄板パターンを身につける

## はじめに

「Spring Bootでどう書けばいいか分からない」「毎回ググってしまう」...そんな悩みを解決するために、**迷わず書ける鉄板パターン**を反復練習しました。

この記事は「Java実装スピード改善リカバリープラン」のWeek 1 Day 1の学習記録です。

## 学習目標

1. **Controller層の基本形**を3分で書けるようにする
2. **Service層の基本形**を5分で書けるようにする
3. **Controller → Service**の連携を理解する

## 実装内容

### 1. DTO（Data Transfer Object）

まず、リクエストとレスポンスのデータ構造を定義します。Java 14以降の`record`を使うと簡潔に書けます。

#### UserRegistrationRequest.java
```java
package com.example.week1.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRegistrationRequest(
        @NotBlank String username,
        @NotBlank String password) {
}
```

#### UserResponse.java
```java
package com.example.week1.dto;

public record UserResponse(
        Long id,
        String username) {
}
```

**ポイント:**
- `record`は自動的にコンストラクタ、getter、equals、hashCode、toStringを生成してくれます
- `@NotBlank`でバリデーションを宣言的に追加できます

### 2. Controller層

REST APIのエンドポイントを定義します。

#### UserController.java
```java
package com.example.week1.controller;

import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.week1.dto.UserRegistrationRequest;
import com.example.week1.dto.UserResponse;
import com.example.week1.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody @Valid UserRegistrationRequest request) {
        UserResponse user = userService.createUser(request);
        URI location = Objects.requireNonNull(URI.create("/users/" + user.id()));
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }
}
```

**鉄板パターン（Controller）:**
1. **`@RestController`**: JSON/XMLを返すREST APIのコントローラー
2. **`@RequestMapping("/users")`**: クラスレベルで共通のパスを定義
3. **`@RequiredArgsConstructor`**: Lombokで`final`フィールドのコンストラクタを自動生成（依存性注入に使用）
4. **`@RequestBody @Valid`**: リクエストボディをJavaオブジェクトにマッピング＋バリデーション
5. **`ResponseEntity<T>`**: ステータスコードとボディを明示的に返す

### 3. Service層

ビジネスロジックを実装します（今回はダミーデータを返すだけ）。

#### UserService.java
```java
package com.example.week1.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.week1.dto.UserRegistrationRequest;
import com.example.week1.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    @Transactional
    public UserResponse createUser(UserRegistrationRequest request) {
        return new UserResponse(1L, request.username());
    }

    public UserResponse getUser(Long id) {
        return new UserResponse(id, "dummyUser");
    }
}
```

**鉄板パターン（Service）:**
1. **`@Service`**: Spring管理のServiceクラスであることを示す
2. **`@RequiredArgsConstructor`**: 同上（依存性注入用）
3. **`@Transactional(readOnly = true)`**: クラス全体をデフォルトで読み取り専用トランザクションにする（安全性向上）
4. **書き込みメソッドには`@Transactional`**: クラスレベルの`readOnly`をオーバーライドして書き込み可能にする

## 学んだポイント

### 1. 「外側から書く」思考法
実装の詳細から書き始めると、詳細にハマって手が止まります。
今回は以下の順番で実装しました：

1. **DTO定義** → データの形を決める
2. **Controllerのシグネチャ** → 「何を受け取って何を返すか」だけ書く（中身は後）
3. **Serviceのシグネチャ** → 同上
4. **接続** → ControllerからServiceを呼ぶ
5. **中身を埋める** → 最後にロジックを実装

この順番なら、常に「コンパイルが通る状態」を保てます。

### 2. よくあるミス
練習中に何度も間違えたポイント：

- **`@RequestBody`の忘れ** → JSONがマッピングされない
- **`@Valid`の忘れ** → バリデーションが動かない
- **`ResponseEntity<Void>`の`<Void>`** → 先頭大文字（クラス型）じゃないとコンパイルエラー
- **Service書き込みメソッドの`@Transactional`忘れ** → クラスレベルの`readOnly`が効いてしまう

### 3. タイムアタックの効果
同じコードを5回繰り返し書いたことで、「考えなくても指が動く」状態になりました。
これが「パターンのストック」です。

## まとめ

今日の成果：
- ✅ Controller層の基本形を暗記
- ✅ Service層の基本形を暗記
- ✅ Controller ↔ Service の連携パターンを理解

次のステップ：
- **Day 2**: Repository層（JPA）とStream APIの練習
- **Day 3**: 例外ハンドリング（`@RestControllerAdvice`）
- **Day 4-5**: これまでの知識でCRUDアプリを1時間以内に作る

「考える時間」が「作業する時間」に変わっていく実感がありました。
引き続きがんばります！

---

**関連リンク:**
- [Java実装スピード改善リカバリープラン全体](./java_recovery_plan.md)
- [Week 1 タスクリスト](./week1_drills.md)
- [鉄板パターン集（スニペット）](./snippets.md)
