# Java実装スピード改善 Week 1 Day 2: Entity & Repository層でDB操作を身につける

## はじめに

Day 1では、Controller層とService層の鉄板パターンを学びました。Day 2では、それをさらに一歩進めて、**データベース操作（JPA/Hibernate）** を統合します。

この記事は「Java実装スピード改善リカバリープラン」のWeek 1 Day 2の学習記録です。

## 学習目標

1. **Entity定義**を3分で書けるようにする
2. **Repository定義**を2分で書けるようにする
3. **Service層でRepositoryを使う**パターンを理解する

## 実装内容

### 1. Entity（User.java）

まず、データベースのテーブルにマッピングするEntityクラスを定義します。

```java
package com.example.week1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
}
```

**鉄板パターン（Entity）:**
1. **`@Entity`**: JPA管理のエンティティクラスであることを示す
2. **`@Table(name = "users")`**: テーブル名を明示（省略するとクラス名がテーブル名になる）
3. **`@Id` + `@GeneratedValue`**: 主キーとして自動採番（DBの`AUTO_INCREMENT`に対応）
4. **Lombok 3点セット**: `@Getter`, `@NoArgsConstructor`, `@AllArgsConstructor`

### 2. Repository（UserRepository.java）

Spring Data JPAを使えば、インターフェースを定義するだけでCRUD操作が完成します。

```java
package com.example.week1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.week1.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
```

**鉄板パターン（Repository）:**
1. **`JpaRepository<Entity, ID>`を継承**: これだけで`save()`, `findById()`, `findAll()`, `delete()`などが使える
2. **`@Repository`不要**: Spring Bootが自動検出してくれる
3. **メソッド名規約**: `findByUsername`と書くだけで、Springが自動的に`WHERE username = ?`のSQLを生成

### 3. Service（UserService.java）

Day 1で作成したServiceを、実際にRepositoryを使うように修正します。

```java
package com.example.week1.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.week1.dto.UserRegistrationRequest;
import com.example.week1.dto.UserResponse;
import com.example.week1.entity.User;
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
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
```

**鉄板パターン（Service with Repository）:**
1. **Repository注入**: `private final UserRepository repository;` → `@RequiredArgsConstructor`で自動注入
2. **createUser**:
   - `new User(...)` でEntityインスタンス作成
   - `repository.save()` でDB保存（IDが自動採番される）
   - **重要**: `save()`の戻り値（保存後のEntity）を使う
3. **getUser**:
   - `repository.findById()` → `Optional<User>`が返る
   - `.map()` でEntity→DTO変換
   - `.orElseThrow()` でデータがない場合に例外をスロー（404エラーの元）

## 学んだポイント

### 1. findById + map + orElseThrow パターン

これが最も頻出する「DB検索→DTO変換→404処理」のパターンです。

```java
repository.findById(id)
    .map(entity -> new Response(...))
    .orElseThrow(() -> new NotFoundException("..."));
```

Day 1では「手が止まる原因」として挙がっていたこのパターンが、反復練習で**考えなくても書ける**ようになりました。

### 2. save() の戻り値を使う理由

```java
// ❌ 間違い（IDが取れない可能性）
repository.save(user);
return new UserResponse(user.getId(), ...);

// ⭕ 正解
User savedUser = repository.save(user);
return new UserResponse(savedUser.getId(), ...);
```

`save()`はDBに保存した後のエンティティ（IDが採番済み）を返します。元の`user`オブジェクトではIDが`null`のままの可能性があるため、戻り値を使うのが確実です。

### 3. Spring Data JPAの「魔法」

`findByUsername`というメソッド名だけで、Springが以下のSQLを自動生成します。

```sql
SELECT * FROM users WHERE username = ?
```

この「メソッド名規約」を覚えるだけで、9割以上のクエリは書ける。

### 4. よくあるミス

練習中に何度も間違えたポイント：

- **`@Entity`の忘れ**: エンティティとして認識されない
- **`@Id`の忘れ**: 主キーが定義されず起動時エラー
- **`JpaRepository<Entity, ID>`の型パラメータミス**: `<User, Long>`の順番を逆にしてしまう
- **`@Transactional`の書き忘れ**: 書き込みメソッドなのに`readOnly`が効いてしまう

## まとめ

今日の成果：
- ✅ Entity定義の鉄板パターンを暗記
- ✅ Repository定義（1行で完成！）
- ✅ Service層でのRepository活用パターンを習得
- ✅ `findById` + `map` + `orElseThrow`を体得

次のステップ：
- **Day 3**: 例外ハンドリング（`@RestControllerAdvice`で統一的なエラー処理）
- **Day 4-5**: CRUD Time Attack（1時間以内に完成させる）

「Repository?どう書くんだっけ...」と悩む時間が、ほぼゼロになりました。
このパターンが身につけば、実装スピードは劇的に上がります！

---

**関連リンク:**
- [Day 1: Controller & Service層](./day1_blog_post.md)
- [Java実装スピード改善リカバリープラン全体](./java_recovery_plan.md)
- [鉄板パターン集（スニペット）](./snippets.md)
