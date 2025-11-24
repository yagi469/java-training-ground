# Stream API 100本ノック - 第1弾（問題1-10）

## 目的
Day 1-4 で学んだ Stream API の知識を定着させるため、実践的な問題を解いていきます。

## 準備
`exercises/week2-stream-api-drills/src/main/java/com/example/week2/knockout` パッケージを作成します。

### データモデル

#### `Employee.java`
```java
package com.example.week2.knockout;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Employee {
    private Long id;
    private String name;
    private String department;
    private int age;
    private double salary;
}
```

## 問題セット

`StreamKnockout01.java` クラスを作成し、以下のメソッドを実装してください。

### Level 1: 基礎（問題1-3）

#### 問題1: 30歳以上の従業員を抽出
- **メソッド名**: `findEmployeesOver30`
- **引数**: `List<Employee> employees`
- **戻り値**: `List<Employee>`

#### 問題2: 全従業員の名前リストを作成
- **メソッド名**: `getAllNames`
- **引数**: `List<Employee> employees`
- **戻り値**: `List<String>`

#### 問題3: Engineering部門の従業員数をカウント
- **メソッド名**: `countEngineeringEmployees`
- **引数**: `List<Employee> employees`
- **戻り値**: `long`

### Level 2: 中級（問題4-7）

#### 問題4: 部門ごとに従業員をグループ化
- **メソッド名**: `groupByDepartment`
- **引数**: `List<Employee> employees`
- **戻り値**: `Map<String, List<Employee>>`

#### 問題5: 給与が50000以上の従業員の名前をカンマ区切りで結合
- **メソッド名**: `joinHighEarnerNames`
- **引数**: `List<Employee> employees`
- **戻り値**: `String`

#### 問題6: 部門ごとの平均給与を計算
- **メソッド名**: `averageSalaryByDepartment`
- **引数**: `List<Employee> employees`
- **戻り値**: `Map<String, Double>`
- **ヒント**: `Collectors.groupingBy` + `Collectors.averagingDouble`

#### 問題7: 最も給与の高い従業員を検索
- **メソッド名**: `findHighestPaidEmployee`
- **引数**: `List<Employee> employees`
- **戻り値**: `Optional<Employee>`

### Level 3: 応用（問題8-10）

#### 問題8: 各部門で最も給与の高い従業員を取得
- **メソッド名**: `findTopEarnerPerDepartment`
- **引数**: `List<Employee> employees`
- **戻り値**: `Map<String, Optional<Employee>>`
- **ヒント**: `Collectors.groupingBy` + `Collectors.maxBy`

#### 問題9: 給与上位3名の従業員を取得
- **メソッド名**: `findTop3HighestPaid`
- **引数**: `List<Employee> employees`
- **戻り値**: `List<Employee>`
- **ヒント**: `sorted` + `limit`

#### 問題10: 部門ごとの給与合計が100000を超える部門名のリストを取得
- **メソッド名**: `findDepartmentsWithHighTotalSalary`
- **引数**: `List<Employee> employees`
- **戻り値**: `List<String>`
- **ヒント**: `groupingBy` + `summingDouble` でMapを作り、さらにfilterして部門名だけ抽出

---

**テストファイル `StreamKnockout01Test.java` を用意しますので、実装してテストを通してください。**

完了したら次の10問に進みましょう！
