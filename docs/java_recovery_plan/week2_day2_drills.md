# Week 2 Day 2 Drill: Collectors (Grouping & Joining)

## 目的
- `Stream` の終端操作である `collect` を使いこなす。
- 特に強力な `groupingBy`（グループ化）と `joining`（文字列結合）をマスターする。

## 演習内容

### 準備
`exercises/week2-stream-api-drills` プロジェクトを使用します。
`src/main/java/com/example/week2/day2` パッケージを作成し、以下のクラスを用意してください。

#### `Product.java` (Entity/DTO)
```java
package com.example.week2.day2;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private String category; // "Electronics", "Books", "Food" etc.
    private double price;
}
```

### 課題

`CollectorsDrill.java` クラスを作成し、以下のメソッドを実装してください。

#### 1. カテゴリごとに商品をグループ化する
- **メソッド名**: `groupByCategory`
- **引数**: `List<Product> products`
- **戻り値**: `Map<String, List<Product>>`
- **条件**: キーをカテゴリ名、値をそのカテゴリに属する商品リストとする Map を返す。

#### 2. 商品名をカンマ区切りで結合する
- **メソッド名**: `joinProductNames`
- **引数**: `List<Product> products`
- **戻り値**: `String`
- **条件**: 全商品の名前をカンマ区切り（例: "Apple, Banana, Orange"）の文字列として返す。

#### 3. カテゴリごとの商品数をカウントする
- **メソッド名**: `countByCategory`
- **引数**: `List<Product> products`
- **戻り値**: `Map<String, Long>`
- **条件**: キーをカテゴリ名、値をそのカテゴリに属する商品の個数とする Map を返す。

## ヒント
- `Collectors.groupingBy(Product::getCategory)`
- `Collectors.joining(", ")`
- `Collectors.groupingBy(Product::getCategory, Collectors.counting())`

## 提出物
- `src/main/java/com/example/week2/day2/Product.java`
- `src/main/java/com/example/week2/day2/CollectorsDrill.java`
- `src/test/java/com/example/week2/day2/CollectorsDrillTest.java` (動作確認用)

---
*実装が完了したら、`week2_day2_walkthrough.md` を作成して記録を残してください。*
