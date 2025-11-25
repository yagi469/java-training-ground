# タスク: StreamKnockout02の型不一致エラー修正とTODO実装

## 概要
ユーザーが `StreamKnockout02.java` の `countByAgeGroup` メソッド実装中に遭遇した `Type mismatch: cannot convert from Map<Object,Long> to Map<String,Long>` エラーを解決する。また、未実装のTODOメソッド (`salaryRangeByDepartment`, `findEmployeesInMultipleProjects`) を実装し、関連するテストをパスさせる。

## 要求事項
1.  **型不一致エラーの修正**: `countByAgeGroup` メソッドで `Map<String, Long>` を返すように実装を修正する。
2.  **TODOメソッドの実装**:
    *   `salaryRangeByDepartment`: 部門ごとの給与範囲（最大-最小）を計算。
    *   `countByAgeGroup`: 年齢層（10歳刻み）ごとの人数を集計。
    *   `findEmployeesInMultipleProjects`: 複数プロジェクトに参加している従業員を検索。
3.  **テストの修正とパス**: `StreamKnockout02Test` を実行し、全てパスすることを確認する。必要に応じてテスト側の不備も修正する。

## エラー詳細
*   **エラーメッセージ**: `Type mismatch: cannot convert from Map<Object,Long> to Map<String,Long>`
*   **発生箇所**: `StreamKnockout02.java` の `countByAgeGroup` メソッド（推測）
*   **原因**: `Collectors.groupingBy` のキー分類関数が `String` 型として推論されない実装になっていたため、`Map<Object, Long>` として扱われていた。

## 作業計画
1.  `StreamKnockout02.java` のファイル内容を確認。
2.  `countByAgeGroup` を含む未実装メソッドを正しく実装する。
3.  `StreamKnockout02Test` を実行し、失敗するテストを特定・修正する。
    *   `findEmployeesWithNameContaining` の大文字小文字区別の修正。
    *   `findEmployeesInMultipleProjects` の期待値（件数）の修正。
