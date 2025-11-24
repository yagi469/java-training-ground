# Week 2 Day 1 Drill: Stream API Basics (Filter & Map)

## 目的
- `for` ループや `if` 文のネストから脱却し、Stream API を使って宣言的にロジックを書くことに慣れる。
- 特に頻出する `filter`（抽出）と `map`（変換）をマスターする。

## 演習内容

### 準備
`exercises/week2-stream-api-drills` プロジェクトを使用します。
`src/main/java/com/example/week2/day1` パッケージを作成し、以下のクラスを用意してください。

#### `User.java` (Entity/DTO)
```java
package com.example.week2.day1;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private int age;
    private String department; // "Sales", "Engineering", "HR" etc.
}
```

### 課題

`StreamDrill.java` クラスを作成し、以下のメソッドを実装してください。

#### 1. 20歳以上のユーザーを抽出する
- **メソッド名**: `filterAdults`
- **引数**: `List<User> users`
- **戻り値**: `List<User>`
- **条件**: `age >= 20` のユーザーのみを含むリストを返す。

#### 2. ユーザーの名前リストを作成する
- **メソッド名**: `mapToNames`
- **引数**: `List<User> users`
- **戻り値**: `List<String>`
- **条件**: 全ユーザーの `name` だけを抽出したリストを返す。

#### 3. エンジニア部門のユーザー名を大文字で取得する
- **メソッド名**: `getEngineeringUserNamesInUpperCase`
- **引数**: `List<User> users`
- **戻り値**: `List<String>`
- **条件**:
    1. `department` が "Engineering" のユーザーを抽出
    2. そのユーザーの `name` をすべて大文字 (`toUpperCase()`) に変換
    3. リストとして返す

## ヒント
- `stream()` でストリームを開始し、`collect(Collectors.toList())` でリストに戻すのが基本フロー。
- 中間操作:
    - `.filter(u -> u.getAge() >= 20)`
    - `.map(User::getName)`
    - `.map(String::toUpperCase)`

## 提出物
- `src/main/java/com/example/week2/day1/User.java`
- `src/main/java/com/example/week2/day1/StreamDrill.java`
- `src/test/java/com/example/week2/day1/StreamDrillTest.java` (動作確認用)

---
*実装が完了したら、`week2_day1_walkthrough.md` を作成して記録を残してください。*
