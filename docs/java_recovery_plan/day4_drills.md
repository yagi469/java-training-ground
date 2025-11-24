# Day 4 Drill

## 目的
- Java の高度な機能や Spring Boot の実務的な活用を学ぶ。
- 前日の学習内容（例外ハンドリング、トランザクション管理）を踏まえて、カスタムバリデーションと AOP を実装する。

## 演習内容
1. **カスタムバリデーションアノテーション** を作成し、`User` エンティティの `email` フィールドに適用する。
2. **バリデーションロジック** を実装し、Spring の `Validator` と連携させる。
3. **AOP（Aspect Oriented Programming）** を用いて、サービスメソッドの実行時間をログに記録するアスペクトを作成する。
4. **テストケース** を JUnit と Mockito で作成し、バリデーションと AOP が期待通りに動作することを確認する。

## 提出物
- `src/main/java/com/example/week1/validation/EmailValidator.java`
- `src/main/java/com/example/week1/validation/ValidEmail.java`
- `src/main/java/com/example/week1/aspect/ExecutionTimeLoggerAspect.java`
- `src/test/java/com/example/week1/validation/EmailValidatorTest.java`
- `src/test/java/com/example/week1/aspect/ExecutionTimeLoggerAspectTest.java`

## ヒント
- カスタムアノテーションは `@Target(ElementType.FIELD)` と `@Retention(RetentionPolicy.RUNTIME)` を付与する。
- バリデーションは `ConstraintValidator<ValidEmail, String>` を実装し、`isValid` メソッドで正規表現チェックを行う。
- AOP は `@Aspect` と `@Around` を使用し、`ProceedingJoinPoint` でメソッド実行前後の時間を取得する。
- テストでは `MockMvc` と `@WebMvcTest` を活用するとコントローラ層のバリデーションも簡単に検証できる。

---
*このファイルは Day 4 の演習指示書です。実装が完了したら、同様の形式で `day4_walkthrough.md` を作成し、実装手順と学びを記録してください。*
