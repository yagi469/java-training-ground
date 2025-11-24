# Week 1 Day 2: Repository & Entity層の実装

## 完了したDrill

- ✅ **Drill 6**: Entity Definition
- ✅ **Drill 7**: Repository Definition  
- ✅ **Drill 8**: Service with Repository

## 実装内容

### 1. User Entity

JPA Entityを定義し、データベーステーブルにマッピングしました。

```java
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
}
```

### 2. User Repository

Spring Data JPAを使用して、シンプルなインターフェースでCRUD操作を実装。

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
```

### 3. UserService (Repository統合版)

Repositoryを注入し、実際のDB操作を行うように修正しました。

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;

    @Transactional
    public UserResponse createUser(UserRegistrationRequest request) {
        User user = new User(null, request.username(), request.password());
        repository.save(user);
        return new UserResponse(user.getId(), user.getUsername());
    }

    public UserResponse getUser(Long id) {
        return repository.findById(id)
                .map(user -> new UserResponse(user.getId(), user.getUsername()))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
```

## 学んだこと

### Entity Pattern
- `@Entity` + `@Table` でテーブルマッピング
- `@Id` + `@GeneratedValue` で主キーの自動採番
- Lombokで定型コードを削減

### Repository Pattern
- `JpaRepository<Entity, ID>` を継承するだけでCRUD完成
- メソッド名規約で自動クエリ生成（`findByUsername`）
- `@Repository` アノテーション不要

### Service with Repository
- `findById` + `orElseThrow` パターンで404エラー処理
- `map` でEntity→DTO変換
- `save` でエンティティを永続化

## 改善ポイント

1. **createUserの改善案**: `save` の戻り値（保存されたエンティティ）を使う方が安全
   ```java
   User savedUser = repository.save(user);
   return new UserResponse(savedUser.getId(), savedUser.getUsername());
   ```

2. **カスタム例外**: `RuntimeException` ではなく、専用の例外クラスを作るとより良い（Day 3で学習予定）

## 次のステップ

- **Day 3**: 例外ハンドリング (`@RestControllerAdvice`)
- **Day 4-5**: CRUD Time Attack（1時間以内に完成させる）
