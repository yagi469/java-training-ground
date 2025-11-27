# Week 6: ブログAPI実装 - 日次タスクリスト

## Day 1: 基本エンティティとリレーション

### プロジェクトセットアップ
- [x] `week6-blog-api` プロジェクトの作成
- [x] `pom.xml` の設定
- [x] メインクラス作成
- [x] `application.properties` の設定
- [x] Maven Wrapperのコピー

### Entity層
- [x] `User` エンティティ（@OneToMany → Post）
- [x] `Post` エンティティ（@ManyToOne → User, Category）
- [x] `Category` エンティティ
- [x] コンパイル確認

### DTO層
- [ ] `UserRequest.java`, `UserResponse.java`
- [ ] `PostRequest.java`, `PostResponse.java`
- [ ] `CategoryRequest.java`, `CategoryResponse.java`

### Repository層
- [x] `UserRepository.java`
- [x] `PostRepository.java`
- [x] `CategoryRepository.java`

### Service層（Week 5スニペット活用）
- [x] `UserService.java` - getAllUsers, getUserById, createUser, updateUser, deleteUser
- [x] `PostService.java` - getAllPosts, getPostById, createPost, updatePost, deletePost
- [x] `CategoryService.java` - getAllCategories, getCategoryById, createCategory

### Controller層
- [x] `UserController.java` - 全エンドポイント
- [x] `PostController.java` - 全エンドポイント
- [x] `CategoryController.java` - 全エンドポイント

### Exception層
- [x] `UserNotFoundException.java`
- [x] `PostNotFoundException.java`
- [x] `CategoryNotFoundException.java`
- [x] `GlobalExceptionHandler.java`

### テスト（@DataJpaTest）
- [ ] `UserRepositoryTest.java`
- [ ] `PostRepositoryTest.java`
- [ ] `CategoryRepositoryTest.java`

### 動作確認
- [x] `mvnw spring-boot:run` 起動確認
- [ ] Postmanで各CRUD操作確認
- [ ] テスト実行 `mvnw test`

---

## Day 2: 多対多リレーションとカスタムクエリ

### Entity層（Tagの追加）
- [ ] `Tag.java` エンティティ作成
- [ ] `Post.java` に `@ManyToMany` 追加（Tag関連）
- [ ] コンパイル確認

### DTO層
- [ ] `TagRequest.java`, `TagResponse.java`
- [ ] `PostResponse` にタグリスト追加

### Repository層
- [ ] `TagRepository.java`

### カスタムクエリ（@Query使用）
- [ ] `PostRepository` にカスタムクエリ追加:
  - [ ] `findByCategory(Category category)`
  - [ ] `findByUser(User user)`
  - [ ] `findByTitleContaining(String keyword)`
  - [ ] `findByTagsContaining(Tag tag)`

### Service層
- [ ] `TagService.java` - 基本CRUD
- [ ] `PostService` にカスタム検索メソッド追加:
  - [ ] `getPostsByCategory(Long categoryId)`
  - [ ] `getPostsByUser(Long userId)`
  - [ ] `searchPostsByTitle(String keyword)`
  - [ ] `getPostsByTag(Long tagId)`

### Controller層
- [ ] `TagController.java`
- [ ] `PostController` に検索エンドポイント追加:
  - [ ] `GET /api/posts?category={id}`
  - [ ] `GET /api/posts?user={id}`
  - [ ] `GET /api/posts?title={keyword}`
  - [ ] `GET /api/posts?tag={id}`

### テスト
- [ ] `TagRepositoryTest.java`
- [ ] カスタムクエリのテスト

### 動作確認
- [ ] タグ付き記事の作成・取得確認
- [ ] 各種検索機能の動作確認

---

## Day 3: REST API高度化（ページネーション・検索・ソート）

### Repository層
- [ ] `PostRepository` を `JpaRepository` → `PagingAndSortingRepository` に変更（継承追加）

### Service層
- [ ] `PostService` にページネーション対応メソッド追加:
  - [ ] `getAllPosts(Pageable pageable)` - Pageableを引数に
  - [ ] `searchPosts(String title, Long categoryId, Long tagId, Pageable pageable)` - 複合検索

### Controller層
- [ ] `PostController` にページネーション対応:
  - [ ] `GET /api/posts?page=0&size=10`
  - [ ] `GET /api/posts?sort=createdAt,desc`
  - [ ] `GET /api/posts?title=keyword&category=1&page=0&size=10&sort=createdAt,desc`

### テスト（MockMvc）
- [ ] `PostControllerTest.java`:
  - [ ] ページネーションのテスト
  - [ ] ソートのテスト
  - [ ] 複合検索のテスト

### 動作確認
- [ ] `GET /api/posts?page=0&size=5` - 5件ずつ取得
- [ ] `GET /api/posts?sort=title,asc` - タイトル昇順
- [ ] `GET /api/posts?title=Spring&sort=createdAt,desc&page=0&size=10`

---

## Day 4: Spring Security（JWT認証・認可）

### 依存関係確認
- [ ] `pom.xml` にSpring Security・JWT依存関係があることを確認

### Security設定
- [ ] `SecurityConfig.java` 作成:
  - [ ] パスワードエンコーダー設定（BCrypt）
  - [ ] 認証不要パス設定（/api/auth/**, GET /api/posts）
  - [ ] JWT Filter設定
- [ ] `JwtTokenProvider.java` 作成:
  - [ ] トークン生成メソッド
  - [ ] トークン検証メソッド
  - [ ] ユーザー情報抽出メソッド
- [ ] `JwtAuthenticationFilter.java` 作成

### DTO層
- [ ] `LoginRequest.java` - username, password
- [ ] `LoginResponse.java` - token, username, role
- [ ] `RegisterRequest.java` - username, email, password

### Service層
- [ ] `AuthService.java` 作成:
  - [ ] `register(RegisterRequest)` - パスワードハッシュ化
  - [ ] `login(LoginRequest)` - 認証してJWT返却
- [ ] `UserService` 修正:
  - [ ] パスワードをハッシュ化して保存

### Controller層
- [ ] `AuthController.java` 作成:
  - [ ] `POST /api/auth/register`
  - [ ] `POST /api/auth/login`
- [ ] `PostController` に権限設定:
  - [ ] `@PreAuthorize("hasRole('USER')")` をPOST/PUT/DELETEに追加
  - [ ] 本人確認ロジック実装

### テスト
- [ ] `AuthControllerTest.java`:
  - [ ] 登録テスト
  - [ ] ログインテスト
  - [ ] JWT検証テスト
- [ ] `PostControllerTest.java`:
  - [ ] 未認証でのPOST → 401
  - [ ] 認証済みでのPOST → 201
  - [ ] 他人の記事を編集 → 403

### 動作確認
- [ ] ユーザー登録 → JWTトークン取得
- [ ] トークンなしでPOST → 401エラー
- [ ] トークンありでPOST → 成功
- [ ] 他人の記事を編集 → 403エラー

---

## Day 5: TDD（全機能のテスト実装）

### Repository層テスト（@DataJpaTest）
- [ ] `UserRepositoryTest.java` 充実化
- [ ] `PostRepositoryTest.java` 充実化
- [ ] `CategoryRepositoryTest.java` 充実化
- [ ] `TagRepositoryTest.java` 充実化
- [ ] カスタムクエリの全パターンテスト

### Service層テスト（Mockito）
- [ ] `UserServiceTest.java`:
  - [ ] 全CRUDメソッドのテスト
  - [ ] 例外ケースのテスト
- [ ] `PostServiceTest.java`:
  - [ ] 全CRUDメソッドのテスト
  - [ ] 検索メソッドのテスト
  - [ ] ページネーションのテスト
- [ ] `CategoryServiceTest.java`
- [ ] `TagServiceTest.java`
- [ ] `AuthServiceTest.java`:
  - [ ] 登録・ログインのテスト
  - [ ] JWT生成・検証のテスト

### Controller層テスト（MockMvc + Spring Security Test）
- [ ] `UserControllerTest.java`
- [ ] `PostControllerTest.java`:
  - [ ] 全CRUD操作
  - [ ] ページネーション
  - [ ] 検索機能
  - [ ] 権限チェック
- [ ] `CategoryControllerTest.java`
- [ ] `TagControllerTest.java`
- [ ] `AuthControllerTest.java`

### 統合テスト（@SpringBootTest）
- [ ] `BlogApiIntegrationTest.java`:
  - [ ] ユーザー登録 → ログイン → 記事作成 → 記事取得の一連のフロー
  - [ ] 検索・ページネーションの統合テスト

### テストカバレッジ確認
- [ ] JaCoCo設定（`pom.xml`）
- [ ] `mvnw test jacoco:report`
- [ ] カバレッジ80%以上を確認

### 最終動作確認
- [ ] 全エンドポイントの正常系確認
- [ ] エラーケース（400, 401, 403, 404）確認
- [ ] `mvnw clean test` → 全テスト成功

---

## 完了チェックリスト

### 機能
- [ ] ユーザー登録・ログイン（JWT）
- [ ] 記事のCRUD（全パターン）
- [ ] カテゴリ・タグのCRUD
- [ ] ページネーション（10件ずつ）
- [ ] 検索・フィルタリング（タイトル、カテゴリ、タグ）
- [ ] ソート（作成日順、タイトル順）
- [ ] ロールベース権限制御（USER/ADMIN）

### 品質
- [ ] テストカバレッジ80%以上
- [ ] リンターエラーなし
- [ ] `mvnw clean test` 成功
- [ ] `mvnw spring-boot:run` 正常起動

### ドキュメント
- [ ] 実装したコードの理解を深める
- [ ] わからなかった部分をメモ
- [ ] 次の学習テーマを決める
