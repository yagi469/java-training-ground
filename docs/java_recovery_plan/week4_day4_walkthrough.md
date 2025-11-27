# Week 4 Day 4 Walkthrough - 最終タイムアタック（CRUDアプリ完全実装）

## 実装概要

Week 4 Day 4の目標である「30分以内での完全なCRUD API実装」を達成しました。
商品管理API (`Product`) をゼロから実装し、すべての機能が正常に動作することを確認しました。

## 実装した機能

### 1. Entity層

#### [Product.java](file:///c:/Users/user/Dev/java-training-ground/exercises/week4-final-time-attack/src/main/java/com/example/week4/finaltask/entity/Product.java)
- データベーステーブルに対応するエンティティクラス
- フィールド: `id`, `name`, `description`, `price`, `stock`
- JPA アノテーション: `@Entity`, `@Id`, `@GeneratedValue`
- Lombok アノテーション: `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`

### 2. DTO層

#### [ProductRequest.java](file:///c:/Users/user/Dev/java-training-ground/exercises/week4-final-time-attack/src/main/java/com/example/week4/finaltask/dto/ProductRequest.java)
- クライアントからの入力データを受け取るDTO
- バリデーションルール:
  - `name`: `@NotBlank` - 必須項目
  - `price`: `@NotNull`, `@Positive` - 必須かつ正の数
  - `stock`: `@Positive` - 正の数
- **重要**: Spring Boot 3.x では `jakarta.validation` を使用

#### [ProductResponse.java](file:///c:/Users/user/Dev/java-training-ground/exercises/week4-final-time-attack/src/main/java/com/example/week4/finaltask/dto/ProductResponse.java)
- クライアントへの返却データを表すDTO
- すべてのフィールドを含む完全な情報を返す

### 3. Repository層

#### [ProductRepository.java](file:///c:/Users/user/Dev/java-training-ground/exercises/week4-final-time-attack/src/main/java/com/example/week4/finaltask/repository/ProductRepository.java)
- `JpaRepository<Product, Long>` を継承
  - 第1型引数: 管理するEntity (`Product`)
  - 第2型引数: EntityのIDの型 (`Long`)
- 基本的なCRUD操作を自動提供

### 4. Service層

#### [ProductService.java](file:///c:/Users/user/Dev/java-training-ground/exercises/week4-final-time-attack/src/main/java/com/example/week4/finaltask/service/ProductService.java)
- ビジネスロジックの実装
- `@Transactional(readOnly = true)`: クラス全体を読み取り専用に設定
- 更新系メソッドには個別に `@Transactional` を付与

**実装メソッド**:
1. `getAllProducts()`: Stream APIで全商品をDTO変換
2. `getProductById(Long id)`: 商品取得、存在しない場合は例外
3. `createProduct(ProductRequest)`: 新規作成
4. `updateProduct(Long, ProductRequest)`: 既存商品の更新
5. `deleteProduct(Long)`: 商品削除

**ヘルパーメソッド**:
- `toResponse(Product)`: Entity → DTO変換
- `toEntity(ProductRequest)`: DTO → Entity変換 (idは`null`で新規作成)

### 5. Controller層

#### [ProductController.java](file:///c:/Users/user/Dev/java-training-ground/exercises/week4-final-time-attack/src/main/java/com/example/week4/finaltask/controller/ProductController.java)
- RESTエンドポイントの定義
- `@RestController`, `@RequestMapping("/api/products")`

**エンドポイント**:
| Method | Path | 説明 | HTTPステータス |
|--------|------|------|----------------|
| GET | `/api/products` | 商品一覧取得 | 200 OK |
| GET | `/api/products/{id}` | 商品詳細取得 | 200 OK |
| POST | `/api/products` | 商品作成 | 201 Created |
| PUT | `/api/products/{id}` | 商品更新 | 200 OK |
| DELETE | `/api/products/{id}` | 商品削除 | 204 No Content |

**重要ポイント**:
- `@Valid` アノテーションでバリデーションを有効化
- POST/PUTの違い:
  - **POST**: 新規作成、冪等ではない（実行のたびにデータが増える）
  - **PUT**: 更新（置換）、冪等である（何度実行しても結果は同じ）

### 6. Exception層

#### [ProductNotFoundException.java](file:///c:/Users/user/Dev/java-training-ground/exercises/week4-final-time-attack/src/main/java/com/example/week4/finaltask/exception/ProductNotFoundException.java)
- カスタム例外クラス
- `RuntimeException` を継承

#### [GlobalExceptionHandler.java](file:///c:/Users/user/Dev/java-training-ground/exercises/week4-final-time-attack/src/main/java/com/example/week4/finaltask/exception/GlobalExceptionHandler.java)
- `@RestControllerAdvice` で全Controllerの例外をキャッチ
- `ProductNotFoundException` → 404 Not Found
- `MethodArgumentNotValidException` → 400 Bad Request

## 動作確認結果

すべてのテストケースが成功しました：

### ✅ テストケース一覧

1. **商品作成 (POST)**
   ```powershell
   Invoke-RestMethod -Uri "http://localhost:8080/api/products" -Method POST -ContentType "application/json" -Body '{"name":"Test Product", "description":"This is a test", "price":1000, "stock":10}'
   ```
   - ステータス: 201 Created
   - レスポンス: 作成された商品データ（IDが自動採番される）

2. **商品一覧取得 (GET)**
   ```powershell
   Invoke-RestMethod -Uri "http://localhost:8080/api/products" -Method GET
   ```
   - ステータス: 200 OK
   - レスポンス: 全商品のリスト

3. **商品詳細取得 (GET)**
   ```powershell
   Invoke-RestMethod -Uri "http://localhost:8080/api/products/1" -Method GET
   ```
   - ステータス: 200 OK
   - レスポンス: ID=1の商品データ

4. **商品更新 (PUT)**
   ```powershell
   Invoke-RestMethod -Uri "http://localhost:8080/api/products/1" -Method PUT -ContentType "application/json" -Body '{"name":"Updated Product", "description":"Updated description", "price":1500, "stock":20}'
   ```
   - ステータス: 200 OK
   - レスポンス: 更新後の商品データ

5. **商品削除 (DELETE)**
   ```powershell
   Invoke-RestMethod -Uri "http://localhost:8080/api/products/1" -Method DELETE
   ```
   - ステータス: 204 No Content
   - レスポンス: なし

6. **エラー確認: 存在しないID (GET)**
   ```powershell
   Invoke-RestMethod -Uri "http://localhost:8080/api/products/999" -Method GET
   ```
   - ステータス: 404 Not Found
   - `ProductNotFoundException` が正しくハンドリングされた

7. **エラー確認: バリデーション (POST)**
   ```powershell
   Invoke-RestMethod -Uri "http://localhost:8080/api/products" -Method POST -ContentType "application/json" -Body '{"description":"No name", "price":1000, "stock":10}'
   ```
   - ステータス: 400 Bad Request
   - `@Valid` + `@NotBlank` によるバリデーションが機能
   - **注意**: 当初は `@Valid` が抜けており、バリデーションが効いていなかった → 修正により正常動作

## 学んだこと

### 1. JpaRepositoryの型パラメータの意味
```java
public interface ProductRepository extends JpaRepository<Product, Long>
```
- **第1型引数 (`Product`)**: 管理するEntityクラス
- **第2型引数 (`Long`)**: EntityのIDの型（`@Id`フィールドの型）

これにより、`findById(Long id)` や `findAll()` が `Product` 型で動作する。

### 2. @Transactionalの意味
- **目的**: データベース操作の一貫性を保つ（All or Nothing）
- クラスレベルで `@Transactional(readOnly = true)` を設定し、更新系メソッドのみ個別に `@Transactional` を付与するのがベストプラクティス
- メソッド内で例外が発生した場合、変更が自動的にロールバックされる

### 3. POST と PUT の違い
| 特性 | POST | PUT |
|------|------|-----|
| 目的 | 新規作成 | 更新（置換） |
| 冪等性 | ❌ 冪等ではない | ✅ 冪等である |
| 実行のたびに | データが増える | データは変わらない（最終状態が同じ） |

### 4. 更新処理のレスポンス
- REST APIでは「Updated」というHTTPステータスコードは存在しない
- 更新成功時は以下のいずれか:
  - **200 OK**: 更新後のデータを返す場合（今回採用）
  - **204 No Content**: データを返さない場合

### 5. EntityとDTOの分離理由
- **セキュリティ**: Mass Assignment脆弱性の防止（`id`など、変更されてはいけないフィールドを保護）
- **バリデーション**: API入力ルールとDB制約を分離
- **責務の分離**: DBスキーマ変更とAPI仕様変更を独立させる

### 6. Spring Boot 3.x への対応
- バリデーション: `javax.validation` → `jakarta.validation` に変更
- `@Valid`の付け忘れに注意（付けないとバリデーションが動作しない）

## コード品質チェック

- ✅ 重複コードがない（`toResponse`, `toEntity`ヘルパーメソッドを使用）
- ✅ Stream APIを使用している（`getAllProducts`）
- ✅ カスタム例外を使用している（`ProductNotFoundException`）
- ✅ `ResponseEntity`を適切に使用している
- ✅ `@Valid`でバリデーションしている
- ✅ リンターエラーなし

## まとめ

Week 4 Day 1〜3で学んだリファクタリングパターンを活用し、完全なCRUD APIを実装できました。
特に以下のパターンを実践：
- **Day 1**: Service層のリファクタリング（ヘルパーメソッド、Stream API）
- **Day 2**: Controller層のリファクタリング（ResponseEntity、HTTPステータス）
- **Day 3**: 例外処理とグローバルハンドラー

すべての機能が正常に動作し、エラーハンドリングも適切に機能していることを確認しました。
