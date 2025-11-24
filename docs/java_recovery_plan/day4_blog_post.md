# Java実装スピード改善 Week 1 Day 4: カスタムバリデーションとAOPでコード品質を向上

## はじめに

Day 1〜3で基本的なCRUD操作と例外ハンドリングを学びました。しかし、実務では以下のような課題があります：

- **バリデーション**: メールアドレスの形式チェックを毎回書くのは面倒
- **横断的関心事**: ログ出力、パフォーマンス測定などを各メソッドに書くのは非効率

Day 4では、**カスタムバリデーション**と**AOP（Aspect Oriented Programming）**を学び、コードの再利用性と保守性を向上させます。

この記事は「Java実装スピード改善リカバリープラン」のWeek 1 Day 4の学習記録です。

## 学習目標

1. **カスタムバリデーションアノテーション**を5分で書けるようにする
2. **AOPアスペクト**を5分で書けるようにする
3. **テストコード**でバリデーションとAOPの動作を検証する

## 実装内容

### 1. カスタムバリデーションアノテーション（@ValidEmail）

まず、メールアドレスの形式をチェックするカスタムアノテーションを作成します。

#### ValidEmail.java

```java
package com.example.week1.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message() default "Invalid email format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

**鉄板パターン（Custom Validation Annotation）:**
1. **@Constraint(validatedBy = バリデータークラス.class)**: バリデーションロジックを指定
2. **@Target(ElementType.FIELD)**: フィールドに適用
3. **@Retention(RetentionPolicy.RUNTIME)**: 実行時に利用可能
4. **message()**: デフォルトエラーメッセージ
5. **groups()とpayload()**: Bean Validationの標準的なメソッド（必須）

#### EmailValidator.java

```java
package com.example.week1.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true; // @NotNull で別途チェック
        return EMAIL_PATTERN.matcher(value).matches();
    }
}
```

**鉄板パターン（Constraint Validator）:**
1. **ConstraintValidator<アノテーション, 型>**: ジェネリクスでアノテーションと対象型を指定
2. **Pattern.compile()**: 正規表現を事前にコンパイル（パフォーマンス向上）
3. **nullチェック**: `@NotNull`と組み合わせる場合は、nullの場合は`true`を返す

**なぜnullでtrueを返す？**
- `@NotNull`が先にチェックされるため、`EmailValidator`ではnullを許可
- 責任の分離：nullチェックは`@NotNull`、形式チェックは`@ValidEmail`

### 2. エンティティへの適用（User.java）

作成したアノテーションを`User`エンティティの`email`フィールドに適用します。

```java
package com.example.week1.entity;

import com.example.week1.validation.ValidEmail;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ValidEmail
    @Column(nullable = false, unique = true)
    private String email;
}
```

**ポイント:**
- `@ValidEmail`を`email`フィールドに追加
- `@Column(nullable = false, unique = true)`: データベース制約も設定

### 3. DTOへの適用（UserRegistrationRequest.java）

リクエストDTOにも適用します。

```java
package com.example.week1.dto;

import com.example.week1.validation.ValidEmail;
import jakarta.validation.constraints.NotBlank;

public record UserRegistrationRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank @ValidEmail String email) {
}
```

**ポイント:**
- `@NotBlank`: null、空文字、空白のみをチェック
- `@ValidEmail`: メールアドレスの形式をチェック
- 複数のアノテーションを組み合わせ可能

### 4. AOPアスペクト（ExecutionTimeLoggerAspect）

次に、サービス層の全メソッドの実行時間を自動的にログ出力するAOPアスペクトを作成します。

#### pom.xmlにAOP依存関係を追加

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

#### ExecutionTimeLoggerAspect.java

```java
package com.example.week1.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeLoggerAspect {
    private static final Logger logger = LoggerFactory.getLogger(ExecutionTimeLoggerAspect.class);

    // Service 層全メソッドに適用
    @Around("execution(* com.example.week1.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            logger.info("{}#{} executed in {} ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    elapsed);
        }
    }
}
```

**鉄板パターン（AOP Aspect）:**
1. **@Aspect**: AOPアスペクトであることを示す
2. **@Component**: Spring Beanとして登録
3. **@Around("execution(...)")**: ポイントカット式で適用範囲を指定
   - `execution(* com.example.week1.service.*.*(..))`: サービス層の全メソッド
   - `*`: 戻り値の型（任意）
   - `com.example.week1.service.*.*`: パッケージ内の全クラスの全メソッド
   - `(..)`: 引数（任意）
4. **ProceedingJoinPoint**: メソッド実行を制御
5. **proceed()**: 実際のメソッドを実行
6. **finallyブロック**: 例外が発生してもログを出力

**ポイントカット式の例:**
- `execution(* com.example.week1.service.*.*(..))`: サービス層の全メソッド
- `execution(* com.example.week1.controller.*.*(..))`: コントローラー層の全メソッド
- `@annotation(com.example.week1.annotation.LogExecutionTime)`: 特定のアノテーションが付いたメソッド

### 5. テストコード

#### EmailValidatorTest.java

```java
package com.example.week1.validation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {
    private final EmailValidator validator = new EmailValidator();

    @Test
    void validEmails() {
        assertTrue(validator.isValid("test@example.com", null));
        assertTrue(validator.isValid("user.name+tag@sub.domain.co", null));
    }

    @Test
    void invalidEmails() {
        assertFalse(validator.isValid("plainaddress", null));
        assertFalse(validator.isValid("@missinguser.com", null));
        assertFalse(validator.isValid("user@.com", null));
    }
}
```

**テストのポイント:**
- **有効なメールアドレス**: 正常系のテスト
- **無効なメールアドレス**: 異常系のテスト
- `ConstraintValidatorContext`は`null`でOK（単体テストでは使用しない）

#### ExecutionTimeLoggerAspectTest.java

```java
package com.example.week1.aspect;

import com.example.week1.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExecutionTimeLoggerAspectTest {
    @Test
    void logsExecutionTime() throws Throwable {
        ExecutionTimeLoggerAspect aspect = new ExecutionTimeLoggerAspect();
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.proceed()).thenReturn("result");
        when(pjp.getSignature()).thenReturn(Mockito.mock(org.aspectj.lang.Signature.class));
        when(pjp.getSignature().getDeclaringTypeName()).thenReturn(UserService.class.getName());
        when(pjp.getSignature().getName()).thenReturn("dummyMethod");

        Object ret = aspect.logExecutionTime(pjp);
        assertEquals("result", ret);
        // Verify that proceed() was called exactly once
        verify(pjp, times(1)).proceed();
    }
}
```

**テストのポイント:**
- **Mockito**: `ProceedingJoinPoint`をモック化
- **verify()**: `proceed()`が1回呼ばれたことを確認
- **戻り値の検証**: アスペクトが正しく結果を返すことを確認

## 動作確認

### バリデーションの動作

無効なメールアドレスでユーザー登録を試みると：

```json
POST /users
{
  "username": "testuser",
  "password": "password123",
  "email": "invalid-email"
}
```

**レスポンス（400 Bad Request）:**
```json
{
  "timestamp": "2024-11-24T13:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid email format",
  "path": "/users"
}
```

### AOPの動作

サービスメソッドを実行すると、ログに実行時間が出力されます：

```
13:43:16.177 [main] INFO com.example.week1.aspect.ExecutionTimeLoggerAspect -- com.example.week1.service.UserService#createUser executed in 15 ms
13:43:16.200 [main] INFO com.example.week1.aspect.ExecutionTimeLoggerAspect -- com.example.week1.service.UserService#getUser executed in 3 ms
```

**メリット:**
- コードに侵入しない（既存コードを変更不要）
- 自動的に全メソッドに適用
- パフォーマンス問題の早期発見

## 学んだポイント

### 1. カスタムバリデーションの利点

**Before（Day 3まで）:**
```java
// 毎回メソッド内でチェック
public UserResponse createUser(UserRegistrationRequest request) {
    if (!request.email().matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$")) {
        throw new BadRequestException("Invalid email format");
    }
    // ...
}
```

**After（Day 4）:**
```java
// アノテーション1つでOK
@ValidEmail
private String email;
```

**利点:**
- ✅ 再利用可能（どこでも使える）
- ✅ 宣言的（コードが読みやすい）
- ✅ 自動的にバリデーション（`@Valid`と組み合わせ）

### 2. AOPの威力

**Before（Day 3まで）:**
```java
public UserResponse createUser(UserRegistrationRequest request) {
    long start = System.currentTimeMillis();
    try {
        // ビジネスロジック
        User user = new User(...);
        return new UserResponse(...);
    } finally {
        long elapsed = System.currentTimeMillis() - start;
        logger.info("createUser executed in {} ms", elapsed);
    }
}
```

**After（Day 4）:**
```java
// ビジネスロジックだけに集中
public UserResponse createUser(UserRegistrationRequest request) {
    User user = new User(...);
    return new UserResponse(...);
}
// AOPが自動的にログ出力
```

**利点:**
- ✅ 横断的関心事の分離（ログ、トランザクション、セキュリティなど）
- ✅ コードの重複削減
- ✅ 保守性の向上（変更が1箇所で済む）

### 3. よくあるミス

練習中に何度も間違えたポイント：

- **@Constraint(validatedBy = ...)の忘れ**: これがないとバリデーションが動作しない
- **@Aspectと@Componentの両方必要**: `@Aspect`だけではSpring Beanにならない
- **ポイントカット式の間違い**: `execution(* ...)`の`*`を忘れる
- **proceed()の呼び忘れ**: `@Around`では必ず`proceed()`を呼ぶ必要がある
- **テストでのnullチェック**: `ConstraintValidatorContext`は`null`でOK

### 4. 実務での活用例

#### カスタムバリデーションの例

```java
// 電話番号の形式チェック
@ValidPhoneNumber
private String phoneNumber;

// パスワードの強度チェック
@StrongPassword(minLength = 8, requireUppercase = true)
private String password;

// 日付の範囲チェック
@DateRange(min = "2024-01-01", max = "2024-12-31")
private LocalDate startDate;
```

#### AOPの活用例

```java
// トランザクション管理（Springが提供）
@Transactional
public void updateUser(...) { }

// ログ出力
@Around("execution(* com.example.service.*.*(..))")
public Object logExecutionTime(...) { }

// キャッシュ
@Around("@annotation(Cacheable)")
public Object cacheResult(...) { }

// セキュリティチェック
@Around("@annotation(RequiresRole)")
public Object checkRole(...) { }
```

## テスト結果

すべてのテストが成功しました：

```
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] Results:
[INFO]   ExecutionTimeLoggerAspectTest: 1 test passed
[INFO]   EmailValidatorTest: 2 tests passed
```

## まとめ

今日の成果：
- ✅ カスタムバリデーションアノテーションの作成パターン
- ✅ AOPアスペクトによる横断的関心事の分離
- ✅ テストコードによる動作検証
- ✅ コードの再利用性と保守性の向上

**Before（Day 3まで）:**
- バリデーションロジックを各メソッドに書く
- ログ出力を各メソッドに書く
- コードの重複が多い

**After（Day 4）:**
- アノテーション1つでバリデーション
- AOPで自動的にログ出力
- コードがシンプルで保守しやすい

次のステップ：
- **Day 5**: CRUD Time Attack（これまでの知識で1時間以内にCRUDアプリを完成させる）

Day 1〜4で学んだパターンを組み合わせれば、**堅牢でメンテナブルなREST API**が作れます。
「バリデーションどうしよう...」「ログどうしよう...」と悩む時間が、ほぼゼロになりました！

---

**関連リンク:**
- [Day 1: Controller & Service層](./day1_blog_post.md)
- [Day 2: Entity & Repository層](./day2_blog_post.md)
- [Day 3: 例外ハンドリング](./day3_blog_post.md)
- [Java実装スピード改善リカバリープラン全体](./java_recovery_plan.md)
- [鉄板パターン集（スニペット）](./snippets.md)

