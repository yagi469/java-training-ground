# Week 2 Day 4 Drill: Optionalの正しい使い方

## 目的
- `Optional` を「値が入っているかどうかの判定（`isPresent`）」だけに使わず、メソッドチェーンでスマートに処理する。
- `map`, `orElse`, `orElseGet`, `orElseThrow`, `ifPresent` を使いこなす。
- **禁止事項**: `if (opt.isPresent()) { return opt.get(); }` のような書き方は禁止（アンチパターン）。

## 演習内容

### 準備
`exercises/week2-stream-api-drills` プロジェクトを使用します。
`src/main/java/com/example/week2/day4` パッケージを作成し、`User` クラスを用意してください（Day 1-3 と同じもので可）。

#### `User.java`
```java
package com.example.week2.day4;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private int age;
    private String department;
}
```

### 課題

`OptionalDrill.java` クラスを作成し、以下のメソッドを実装してください。
**全てのメソッドで `isPresent()` と `get()` の使用は禁止です。**

#### 1. 値が存在しない場合のデフォルト値を返す (`orElse`)
- **メソッド名**: `getNameOrDefault`
- **引数**: `Optional<User> user`, `String defaultName`
- **戻り値**: `String`
- **条件**: ユーザーが存在すればその名前を、存在しなければ `defaultName` を返す。

#### 2. 値を変換して返す (`map` + `orElse`)
- **メソッド名**: `getUpperCasedNameOrUnknown`
- **引数**: `Optional<User> user`
- **戻り値**: `String`
- **条件**: ユーザーが存在すればその名前を大文字に変換して返し、存在しなければ "UNKNOWN" を返す。

#### 3. 値が存在しない場合に例外を投げる (`orElseThrow`)
- **メソッド名**: `getDepartmentOrThrow`
- **引数**: `Optional<User> user`
- **戻り値**: `String`
- **条件**: ユーザーが存在すればその部署名を返す。存在しなければ `IllegalArgumentException` を投げる。

#### 4. 値が存在する場合のみ処理を実行する (`ifPresent`)
- **メソッド名**: `printNameIfPresent`
- **引数**: `Optional<User> user`
- **戻り値**: `void`
- **条件**: ユーザーが存在する場合のみ、その名前を `System.out.println` で出力する。

## ヒント
- `user.map(User::getName).orElse(defaultName)`
- `user.map(User::getName).map(String::toUpperCase).orElse("UNKNOWN")`
- `user.map(User::getDepartment).orElseThrow(() -> new IllegalArgumentException("User not found"))`
- `user.map(User::getName).ifPresent(System.out::println)`

## 提出物
- `src/main/java/com/example/week2/day4/User.java`
- `src/main/java/com/example/week2/day4/OptionalDrill.java`
- `src/test/java/com/example/week2/day4/OptionalDrillTest.java` (動作確認用)

---
*実装が完了したら、`week2_day4_walkthrough.md` を作成して記録を残してください。*
