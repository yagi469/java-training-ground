# Day 4 解答（Custom Validation & AOP）

## 目的
- `@ValidEmail` カスタムバリデーションアノテーションを実装し、`User` エンティティの `email` フィールドに適用する。
- Spring AOP を用いてサービスメソッドの実行時間をログに出力するアスペクトを作成する。
- JUnit と Mockito で単体テストを作成し、期待通りに動作することを検証する。

---

## 1. カスタムバリデーションアノテーション `@ValidEmail`

### `ValidEmail.java`
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

### `EmailValidator.java`
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

## 2. `User` エンティティへの適用
```java
package com.example.week1.entity;

import com.example.week1.validation.ValidEmail;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

## 3. AOP アスペクト `ExecutionTimeLoggerAspect`
```java
package com.example.week1.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
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

## 4. テストコード
### `EmailValidatorTest.java`
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

### `ExecutionTimeLoggerAspectTest.java`
```java
package com.example.week1.aspect;

import com.example.week1.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
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

---

## 5. ビルド手順
```bash
# プロジェクトルートへ移動
cd c:\Users\user\Dev\java-training-ground\exercises\week1-crud-time-attack
# Maven ビルド（テスト含む）
mvn clean test
```
テストがすべて成功すれば、カスタムバリデーションと AOP が正しく組み込まれたことが確認できます。

---

## 6. まとめ
- `@ValidEmail` でメールアドレス形式を統一的に検証できるようになった。
- AOP によりサービス層のパフォーマンス測定が自動化され、コードに侵入せずに実行時間をロギングできる。
- JUnit/Mockito テストでロジックの正当性とアスペクトの呼び出しを検証した。

このファイルを `solutions/week1-crud-time-attack/day4_solution.md` として保存しました。
