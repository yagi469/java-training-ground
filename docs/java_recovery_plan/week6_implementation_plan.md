# Week 6 実装計画: 実践的なブログAPIシステム

## プロジェクト概要

Week 1-5で習得した基礎スキルを活かし、以下の4つの発展的要素を統合した**実務レベルのブログAPI**を構築します。

### 統合する技術要素

1. **データベース応用（テーブル関連）**
2. **Spring Security（認証・認可）**
3. **REST API設計の高度化**
4. **テスト駆動開発（TDD）**

---

## 学習目標

Week 6完了時点で以下ができるようになります：

- ✅ 複数のテーブルを持つデータベース設計（1対多、多対多）
- ✅ JWT認証を使った安全なAPI実装
- ✅ ページネーション・検索・ソート機能の実装
- ✅ テストファーストでの開発（TDD）
- ✅ ロールベースのアクセス制御（RBAC）

---

## データモデル設計

### ER図（概念）

```
┌─────────────┐       ┌──────────────┐
│   User      │1    N │    Post      │
│─────────────│◄──────│──────────────│
│ id          │       │ id           │
│ username    │       │ title        │
│ email       │       │ content      │
│ password    │       │ userId       │
│ role        │       │ categoryId   │
└─────────────┘       │ createdAt    │
                      │ updatedAt    │
                      └──────────────┘
                            │ N
                            │
                            │ N
                      ┌──────────────┐
                      │   Tag        │
                      │──────────────│
                      │ id           │
                      │ name         │
                      └──────────────┘
                            ▲
                            │ N
                            │
                      ┌──────────────┐
                      │  post_tag    │
                      │  (中間表)    │
                      │──────────────│
                      │ postId       │
                      │ tagId        │
                      └──────────────┘

┌─────────────┐
│  Category   │1
│─────────────│
│ id          │
│ name        │
└─────────────┘
```

### テーブル定義

#### 1. User（ユーザー）
| フィールド | 型 | 制約 | 説明 |
|-----------|-----|------|------|
| id | Long | PK, AUTO | ユーザーID |
| username | String | UNIQUE, NOT NULL | ユーザー名 |
| email | String | UNIQUE, NOT NULL | メールアドレス |
| password | String | NOT NULL | ハッシュ化パスワード |
| role | Enum | NOT NULL | ADMIN / USER |

#### 2. Post（記事）
| フィールド | 型 | 制約 | 説明 |
|-----------|-----|------|------|
| id | Long | PK, AUTO | 記事ID |
| title | String | NOT NULL | タイトル |
| content | Text | NOT NULL | 本文 |
| userId | Long | FK → User | 著者ID |
| categoryId | Long | FK → Category | カテゴリID |
| createdAt | LocalDateTime | NOT NULL | 作成日時 |
| updatedAt | LocalDateTime | NOT NULL | 更新日時 |

#### 3. Category（カテゴリ）
| フィールド | 型 | 制約 | 説明 |
|-----------|-----|------|------|
| id | Long | PK, AUTO | カテゴリID |
| name | String | UNIQUE, NOT NULL | カテゴリ名 |

#### 4. Tag（タグ）
| フィールド | 型 | 制約 | 説明 |
|-----------|-----|------|------|
| id | Long | PK, AUTO | タグID |
| name | String | UNIQUE, NOT NULL | タグ名 |

#### 5. post_tag（中間テーブル）
| フィールド | 型 | 制約 | 説明 |
|-----------|-----|------|------|
| postId | Long | FK → Post | 記事ID |
| tagId | Long | FK → Tag | タグID |

**複合主キー**: (postId, tagId)

---

## 実装スケジュール（5日間）

### Day 1-2: データベース応用（テーブル関連）

#### Day 1: 基本エンティティとリレーション
- ✅ プロジェクトセットアップ (`week6-blog-api`)
- ✅ Entity定義:
  - `User`（@OneToMany → Post）
  - `Post`（@ManyToOne → User, @ManyToOne → Category）
  - `Category`
- ✅ Repository作成
- ✅ 基本的なCRUD実装（Week 5スニペット活用）
- ✅ **テスト**: Repository層のテスト（@DataJpaTest）

#### Day 2: 多対多リレーションとカスタムクエリ
- ✅ `Tag` エンティティ
- ✅ `Post` ⇔ `Tag` 多対多関係（@ManyToMany）
- ✅ カスタムクエリ実装:
  - カテゴリ別記事取得
  - タグ別記事取得
  - 著者別記事取得
  - `@Query` アノテーション使用
- ✅ **テスト**: カスタムクエリのテスト

---

### Day 3: REST API高度化

#### ページネーション・検索・ソート
- ✅ `Pageable` を使ったページネーション
  - `GET /api/posts?page=0&size=10`
- ✅ 検索機能:
  - タイトル検索: `GET /api/posts?title=keyword`
  - タグ検索: `GET /api/posts?tag=java`
  - カテゴリ検索: `GET /api/posts?category=tech`
- ✅ ソート機能:
  - `GET /api/posts?sort=createdAt,desc`
  - `GET /api/posts?sort=title,asc`
- ✅ 複合条件: `GET /api/posts?category=tech&sort=createdAt,desc&page=0&size=10`
- ✅ **テスト**: Controller層のテスト（MockMvc）

---

### Day 4: Spring Security（認証・認可）

#### JWT認証の実装
- ✅ 依存関係追加（Spring Security, JWT）
- ✅ SecurityConfig作成
- ✅ JwtTokenProvider実装
- ✅ ユーザー登録・ログインエンドポイント:
  - `POST /api/auth/register`
  - `POST /api/auth/login` → JWTトークン返却
- ✅ **テスト**: 認証フローのテスト

#### ロールベースアクセス制御
- ✅ 権限設定:
  - **未認証**: GET（記事閲覧）のみ
  - **USER**: 自分の記事のCRUD
  - **ADMIN**: すべての記事のCRUD、ユーザー管理
- ✅ `@PreAuthorize` アノテーション使用
- ✅ **テスト**: 権限別のアクセステスト

---

### Day 5: テスト駆動開発（TDD）

#### 全機能のテスト実装
- ✅ **Repository層**:
  - @DataJpaTest
  - カスタムクエリのテスト
- ✅ **Service層**:
  - @ExtendWith(MockitoExtension.class)
  - Mockitoでリポジトリをモック
  - ビジネスロジックのテスト
- ✅ **Controller層**:
  - MockMvc
  - 認証状態を含むテスト
- ✅ **統合テスト**:
  - @SpringBootTest
  - 実際のDBを使った全体フローテスト

#### テストカバレッジ確認
- ✅ JaCoCo設定
- ✅ カバレッジ80%以上を目標

---

## API仕様書

### 認証関連

| Method | Path | 説明 | 認証 |
|--------|------|------|------|
| POST | /api/auth/register | ユーザー登録 | 不要 |
| POST | /api/auth/login | ログイン（JWT取得） | 不要 |

### 記事（Post）管理

| Method | Path | 説明 | 認証 |
|--------|------|------|------|
| GET | /api/posts | 記事一覧取得（ページネーション） | 不要 |
| GET | /api/posts/{id} | 記事詳細取得 | 不要 |
| POST | /api/posts | 記事作成 | USER |
| PUT | /api/posts/{id} | 記事更新 | 本人またはADMIN |
| DELETE | /api/posts/{id} | 記事削除 | 本人またはADMIN |
| GET | /api/posts/search | 検索・フィルタリング | 不要 |

**クエリパラメータ例:**
- `?page=0&size=10` - ページネーション
- `?title=Spring` - タイトル検索
- `?category=tech` - カテゴリフィルタ
- `?tag=java` - タグフィルタ
- `?sort=createdAt,desc` - ソート

### カテゴリ・タグ管理

| Method | Path | 説明 | 認証 |
|--------|------|------|------|
| GET | /api/categories | カテゴリ一覧 | 不要 |
| POST | /api/categories | カテゴリ作成 | ADMIN |
| GET | /api/tags | タグ一覧 | 不要 |
| POST | /api/tags | タグ作成 | USER |

---

## 技術スタック

### 必須依存関係

```xml
<dependencies>
    <!-- Spring Boot基本 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- Security & JWT -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Database -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

---

## 検証計画

### 機能テスト
- [ ] 記事のCRUD（全パターン）
- [ ] ページネーション（10件、20件）
- [ ] 検索・フィルタリング（複合条件）
- [ ] ソート（昇順・降順）
- [ ] JWT認証フロー
- [ ] 権限別アクセス制御

### 非機能テスト
- [ ] テストカバレッジ80%以上
- [ ] エラーハンドリング（400, 401, 403, 404）
- [ ] バリデーション（不正な入力）

---

## 完了条件

✅ すべてのエンドポイントが正常動作  
✅ テストカバレッジ80%以上  
✅ JWT認証が正しく機能  
✅ ロールベースのアクセス制御が動作  
✅ ページネーション・検索・ソートが動作  
✅ リンターエラーなし

---

## 次のステップ

Week 6完了後は以下を検討：
- フロントエンド（React/Vue）との連携
- PostgreSQL/MySQLへの移行
- Docker化
- CI/CDパイプライン構築
