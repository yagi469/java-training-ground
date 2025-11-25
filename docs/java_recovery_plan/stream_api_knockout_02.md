# Stream API 100本ノック - 第2弾（問題11-20）

## 目的
第1弾で基礎を固めたので、第2弾ではより実践的で複雑な操作に挑戦します。

## 準備
引き続き `exercises/week2-stream-api-drills/src/main/java/com/example/week2/knockout` パッケージを使用します。

### 追加データモデル

#### `Project.java`
```java
package com.example.week2.knockout;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class Project {
    private Long id;
    private String name;
    private List<Long> memberIds; // 参加メンバーのEmployee ID
}
```

## 問題セット

`StreamKnockout02.java` クラスを作成し、以下のメソッドを実装してください。

### Level 4: 実践（問題11-14）

#### 問題11: 給与の合計を計算
- **メソッド名**: `calculateTotalSalary`
- **引数**: `List<Employee> employees`
- **戻り値**: `double`
- **ヒント**: `mapToDouble` + `sum`

#### 問題12: 部門ごとの最高給与を取得
- **メソッド名**: `maxSalaryByDepartment`
- **引数**: `List<Employee> employees`
- **戻り値**: `Map<String, Double>`
- **ヒント**: `Collectors.toMap` の第3引数（マージ関数）を使用して、高い方の値を採用する (`(s1, s2) -> Math.max(s1, s2)`)

#### 問題13: 名前に特定の文字列を含む従業員をフィルタ
- **メソッド名**: `findEmployeesWithNameContaining`
- **引数**: `List<Employee> employees`, `String keyword`
- **戻り値**: `List<Employee>`
- **ヒント**: `filter` + `contains`

#### 問題14: 年齢で昇順ソートした従業員リストを取得
- **メソッド名**: `sortEmployeesByAge`
- **引数**: `List<Employee> employees`
- **戻り値**: `List<Employee>`
- **ヒント**: `sorted` + `Comparator.comparing`

### Level 5: 発展（問題15-17）

#### 問題15: 各部門の従業員名リストを作成
- **メソッド名**: `employeeNamesByDepartment`
- **引数**: `List<Employee> employees`
- **戻り値**: `Map<String, List<String>>`
- **ヒント**: `groupingBy` + `mapping` + `toList`

#### 問題16: プロジェクトメンバーの平均年齢を計算
- **メソッド名**: `averageAgeOfProjectMembers`
- **引数**: `List<Employee> employees`, `Project project`
- **戻り値**: `double`
- **ヒント**: プロジェクトの `memberIds` に含まれる従業員の年齢平均を計算

#### 問題17: 給与中央値を計算
- **メソッド名**: `calculateMedianSalary`
- **引数**: `List<Employee> employees`
- **戻り値**: `double`
- **ヒント**: ソートして中央値を取得（偶数個の場合は中央2つの平均）

### Level 6: 最難関（問題18-20）

#### 問題18: 部門ごとの給与範囲（最大-最小）を計算
- **メソッド名**: `salaryRangeByDepartment`
- **引数**: `List<Employee> employees`
- **戻り値**: `Map<String, Double>`
- **ヒント**: `groupingBy` で部門ごとに、各部門で max - min を計算

#### 問題19: 年齢層（10歳刻み）ごとの人数を集計
- **メソッド名**: `countByAgeGroup`
- **引数**: `List<Employee> employees`
- **戻り値**: `Map<String, Long>`
- **ヒント**: 年齢を10で割って "20代", "30代" などのキーでグループ化

#### 問題20: 複数プロジェクトに参加している従業員を検索
- **メソッド名**: `findEmployeesInMultipleProjects`
- **引数**: `List<Employee> employees`, `List<Project> projects`
- **戻り値**: `List<Employee>`
- **ヒント**: 全プロジェクトの memberIds を flatMap で結合し、重複カウントして2以上のIDを抽出

---

**テストファイル `StreamKnockout02Test.java` を用意しますので、実装してテストを通してください。**

完了したら次の10問（第3弾）に進みましょう！
