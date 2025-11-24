# Week 2 Day 3 Drill: Matching & Finding (anyMatch, findFirst)

## 目的
- 条件判定 (`anyMatch`, `allMatch`, `noneMatch`) と検索 (`findFirst`, `findAny`) をマスターする。
- `Optional` の基本的な扱い方に触れる。

## 演習内容

### 準備
`exercises/week2-stream-api-drills` プロジェクトを使用します。
`src/main/java/com/example/week2/day3` パッケージを作成し、Day 1 で作成した `User` クラスをコピー（または再利用）してください。

#### `User.java` (再利用)
```java
package com.example.week2.day3;

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

`MatchingDrill.java` クラスを作成し、以下のメソッドを実装してください。

#### 1. 未成年が含まれているか判定する
- **メソッド名**: `hasUnderage`
- **引数**: `List<User> users`
- **戻り値**: `boolean`
- **条件**: 20歳未満のユーザーが1人でもいれば `true`、いなければ `false` を返す。

#### 2. 全員が特定の部署に所属しているか判定する
- **メソッド名**: `areAllInDepartment`
- **引数**: `List<User> users`, `String department`
- **戻り値**: `boolean`
- **条件**: 全員の `department` が引数で指定された部署と一致すれば `true`、そうでなければ `false` を返す。

#### 3. 最年少のユーザーを探す
- **メソッド名**: `findYoungestUser`
- **引数**: `List<User> users`
- **戻り値**: `Optional<User>`
- **条件**: 年齢が最も低いユーザーを返す。リストが空の場合は空の `Optional` を返す。
- **ヒント**: `min` または `sorted().findFirst()` を使用。

#### 4. 特定の部署の最初のユーザーを探す
- **メソッド名**: `findFirstUserInDepartment`
- **引数**: `List<User> users`, `String department`
- **戻り値**: `Optional<User>`
- **条件**: 指定された部署に所属する最初のユーザーを返す。見つからない場合は空の `Optional` を返す。

## ヒント
- `anyMatch(u -> u.getAge() < 20)`
- `allMatch(u -> department.equals(u.getDepartment()))`
- `min(Comparator.comparingInt(User::getAge))`
- `filter(...).findFirst()`

## 提出物
- `src/main/java/com/example/week2/day3/User.java`
- `src/main/java/com/example/week2/day3/MatchingDrill.java`
- `src/test/java/com/example/week2/day3/MatchingDrillTest.java` (動作確認用)

---
*実装が完了したら、`week2_day3_walkthrough.md` を作成して記録を残してください。*
