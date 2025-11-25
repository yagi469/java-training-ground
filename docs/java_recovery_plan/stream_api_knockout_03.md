# Stream API 100本ノック - 第3弾（問題21-30）

## 目的
第1弾・第2弾で身につけた技術を使い、より実践的で複雑なシナリオに挑戦します。

## 準備
引き続き `exercises/week2-stream-api-drills/src/main/java/com/example/week2/knockout` パッケージを使用します。

### 追加データモデル

#### `Order.java`
```java
package com.example.week2.knockout;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Order {
    private Long id;
    private Long employeeId; // 担当者のEmployee ID
    private String product;
    private int quantity;
    private double price;
    private LocalDate orderDate;
}
```

## 問題セット

`StreamKnockout03.java` クラスを作成し、以下のメソッドを実装してください。

### Level 7: 実務レベル（問題21-24）

#### 問題21: 注文の合計金額を計算
- **メソッド名**: `calculateTotalOrderAmount`
- **引数**: `List<Order> orders`
- **戻り値**: `double`
- **ヒント**: `price * quantity` の合計

#### 問題22: 従業員ごとの注文総額を計算
- **メソッド名**: `totalOrderAmountByEmployee`
- **引数**: `List<Order> orders`
- **戻り値**: `Map<Long, Double>`
- **ヒント**: `groupingBy` + 各注文の金額合計

#### 問題23: 特定期間の注文をフィルタ
- **メソッド名**: `findOrdersBetweenDates`
- **引数**: `List<Order> orders`, `LocalDate startDate`, `LocalDate endDate`
- **戻り値**: `List<Order>`
- **ヒント**: `filter` で日付範囲チェック

#### 問題24: 商品ごとの販売数量を集計
- **メソッド名**: `totalQuantityByProduct`
- **引数**: `List<Order> orders`
- **戻り値**: `Map<String, Integer>`
- **ヒント**: `groupingBy` + `summingInt`

### Level 8: エキスパート（問題25-27）

#### 問題25: 最も売上の高い従業員を検索
- **メソッド名**: `findTopSalesEmployee`
- **引数**: `List<Employee> employees`, `List<Order> orders`
- **戻り値**: `Optional<Employee>`
- **ヒント**: 注文データから従業員IDごとの売上を計算し、最大値の従業員を取得

#### 問題26: 部門ごとの売上トップ3商品を取得
- **メソッド名**: `top3ProductsByDepartment`
- **引数**: `List<Employee> employees`, `List<Order> orders`
- **戻り値**: `Map<String, List<String>>`
- **ヒント**: 従業員と注文を結合 → 部門ごとにグループ化 → 商品売上を計算 → トップ3抽出

#### 問題27: 月ごとの売上推移を計算
- **メソッド名**: `monthlySales`
- **引数**: `List<Order> orders`
- **戻り値**: `Map<String, Double>`
- **ヒント**: 注文日から「YYYY-MM」形式のキーを作成してグループ化

### Level 9: マスター（問題28-30）

#### 問題28: 全従業員の平均売上を超える従業員を抽出
- **メソッド名**: `findAboveAverageSalesEmployees`
- **引数**: `List<Employee> employees`, `List<Order> orders`
- **戻り値**: `List<Employee>`
- **ヒント**: 2段階 - まず平均を計算、次にそれを超える従業員をフィルタ

#### 問題29: 最も注文が集中した日を特定
- **メソッド名**: `findBusiestOrderDate`
- **引数**: `List<Order> orders`
- **戻り値**: `Optional<LocalDate>`
- **ヒント**: 日付ごとの注文数をカウント → 最大値の日付を取得

#### 問題30: 従業員の売上ランキングを作成
- **メソッド名**: `createSalesRanking`
- **引数**: `List<Employee> employees`, `List<Order> orders`
- **戻り値**: `List<Employee>`
- **ヒント**: 従業員ごとの売上を計算 → 売上降順でソート → 従業員リスト化

---

**テストファイル `StreamKnockout03Test.java` を用意しますので、実装してテストを通してください。**

これで30問完了です！お疲れ様でした。
