# Week 4 Day 4 Drill: 最終タイムアタック（CRUDアプリ完全実装）

## 目的

Week4 Day1-3で学んだリファクタリングの知識を活用して、**完全なCRUDアプリケーションをゼロから実装**します。

**目標タイム**: **30分以内**

これができれば、もう「実装が遅い」とは言わせません。

## 背景

実務では、新しい機能を素早く実装する能力が求められます。
Week1-4で学んだ知識を総合的に活用し、**迷わず実装できる**状態を目指します。

## このドリルでやること

**商品管理API**をゼロから実装します。

### 実装する機能

1. **商品一覧取得** (GET `/api/products`)
2. **商品詳細取得** (GET `/api/products/{id}`)
3. **商品登録** (POST `/api/products`)
4. **商品更新** (PUT `/api/products/{id}`)
5. **商品削除** (DELETE `/api/products/{id}`)

## 実装手順

### ステップ1: プロジェクトの準備（5分）

新しいプロジェクトを作成するか、既存のプロジェクトに新しいパッケージを作成します。

```
exercises/
└── week4-final-time-attack/
    └── src/main/java/com/example/week4/finaltask/
        ├── controller/
        ├── service/
        ├── repository/
        ├── entity/
        ├── dto/
        └── exception/
```

### ステップ2: EntityとDTOの定義（5分）

#### `Product.java` (Entity)

```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
}
```

#### `ProductRequest.java` (DTO)

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @Positive(message = "Stock must be positive")
    private Integer stock;
}
```

#### `ProductResponse.java` (DTO)

```java
@Data
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
}
```

### ステップ3: Repositoryの作成（2分）

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
}
```

### ステップ4: Service層の実装（8分）

Week4 Day1で学んだパターンを適用します。

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        // TODO: Stream APIで実装
    }

    public ProductResponse getProductById(@NonNull Long id) {
        // TODO: カスタム例外を使用
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        // TODO: ヘルパーメソッドを使用
    }

    @Transactional
    public ProductResponse updateProduct(@NonNull Long id, ProductRequest request) {
        // TODO: 更新処理
    }

    @Transactional
    public void deleteProduct(@NonNull Long id) {
        // TODO: 削除処理
    }

    // ヘルパーメソッド
    private ProductResponse toResponse(Product product) {
        // TODO: Entity → DTO変換
    }

    private Product toEntity(ProductRequest request) {
        // TODO: DTO → Entity変換
    }
}
```

### ステップ5: Controller層の実装（5分）

Week4 Day2で学んだパターンを適用します。

```java
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        // TODO: ResponseEntity.ok()を使用
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        // TODO: ResponseEntity.ok()を使用
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        // TODO: 201 Createdを返す
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        // TODO: ResponseEntity.ok()を使用
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        // TODO: 204 No Contentを返す
    }
}
```

### ステップ6: 例外処理の実装（3分）

Week4 Day2で学んだパターンを適用します。

```java
// ProductNotFoundException.java
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}

// GlobalExceptionHandler.java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException e) {
        // TODO: 404 Not Foundを返す
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException e) {
        // TODO: 400 Bad Requestを返す
    }
}
```

### ステップ7: 動作確認（2分）

すべてのエンドポイントが正しく動作するか確認します。

## タイムアタックのコツ

### 1. 実装順序

1. **EntityとDTO** → データ構造を定義
2. **Repository** → 簡単なので最初に
3. **Service層** → ビジネスロジック
4. **Controller層** → APIエンドポイント
5. **例外処理** → 最後にまとめて

### 2. コピー&ペーストの活用

Week4 Day1-3で実装したコードを参考に、パターンをコピーして修正します。

### 3. 迷ったらスキップ

実装に詰まったら、その部分をスキップして先に進み、後で戻ります。

## チェックリスト

実装が完了したら、以下を確認してください：

### 機能確認

- [ ] GET `/api/products` - 一覧取得が動作する
- [ ] GET `/api/products/{id}` - 詳細取得が動作する
- [ ] POST `/api/products` - 作成が動作する（201 Created）
- [ ] PUT `/api/products/{id}` - 更新が動作する（200 OK）
- [ ] DELETE `/api/products/{id}` - 削除が動作する（204 No Content）

### エラーハンドリング確認

- [ ] 存在しないIDで取得 → 404 Not Found
- [ ] 存在しないIDで更新 → 404 Not Found
- [ ] 存在しないIDで削除 → 404 Not Found
- [ ] バリデーションエラー → 400 Bad Request

### コード品質確認

- [ ] 重複コードがない（`toResponse`, `toEntity`を使用）
- [ ] Stream APIを使用している
- [ ] カスタム例外を使用している
- [ ] `ResponseEntity`を適切に使用している
- [ ] `@Valid`でバリデーションしている

## 完了条件

✅ すべてのCRUD機能が実装できる
✅ 30分以内で完成できる（目標）
✅ エラーハンドリングが適切に機能する
✅ リンターエラーがない

## タイム記録

実装開始時刻: _______________
実装完了時刻: _______________
所要時間: _______________ 分

**目標**: 30分以内
**達成**: [ ] はい [ ] いいえ

## 振り返り

実装が完了したら、以下を振り返ってください：

1. **どこで時間がかかったか？**
   - その部分をスニペットに登録する
   - 次回はもっと速く実装できるようにする

2. **迷った部分はどこか？**
   - その部分をドキュメントにまとめる
   - パターンを覚える

3. **改善できる点はあるか？**
   - コードの品質を向上させる
   - より良い実装方法を探す

---

## ヒント

困ったときは、以下を参考にしてください：

- **Week4 Day1**: Service層のリファクタリングパターン
- **Week4 Day2**: Controller層のリファクタリングパターン
- **Week4 Day3**: 更新・削除機能の実装パターン

**重要なのは、完璧を目指さず、まず動くものを作ることです。**

頑張ってください！

