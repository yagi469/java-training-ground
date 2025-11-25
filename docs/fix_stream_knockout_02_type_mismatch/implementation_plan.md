# 実装計画: StreamKnockout02 修正

## 変更対象ファイル
*   `src/main/java/com/example/week2/knockout/StreamKnockout02.java`
*   `src/test/java/com/example/week2/knockout/StreamKnockout02Test.java`

## 実装ステップ

### 1. `StreamKnockout02.java` の修正と実装
*   **`salaryRangeByDepartment`**:
    *   `Collectors.groupingBy` と `Collectors.summarizingDouble` を使用して部門ごとの統計情報を取得。
    *   `collectingAndThen` を使用して `max - min` を計算し、`Map<String, Double>` を返す。
*   **`countByAgeGroup`**:
    *   年齢を10で割って10倍し、文字列 "代" (または "s") を付与してキーとする。
    *   `Map<String, Long>` を返すようにし、型不一致エラーを解消する。
    *   テストに合わせてサフィックスを "代" とする。
*   **`findEmployeesInMultipleProjects`**:
    *   プロジェクトリストから全メンバーIDをフラット化し、IDごとの出現回数をカウント。
    *   出現回数が1より大きいIDを持つ従業員をフィルタリングしてリスト化する。
*   **`findEmployeesWithNameContaining`**:
    *   テストの期待値に合わせて、大文字小文字を区別しない検索 (`toLowerCase()`) に変更する。

### 2. `StreamKnockout02Test.java` の修正
*   **`problem20_findEmployeesInMultipleProjects`**:
    *   テストデータの構成上、複数プロジェクトに参加している従業員は3名（Alice, Charlie, Eve）であるため、期待値を `4` から `3` に修正する。

## 検証計画
*   `mvn -Dtest=StreamKnockout02Test test` を実行し、全てのテストケースが成功することを確認する。
