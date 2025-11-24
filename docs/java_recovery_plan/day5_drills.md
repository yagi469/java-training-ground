# Day 5 Drill: CRUD Time Attack

## 目的
- Day 1〜4で学んだ知識を統合して、完全なCRUDアプリケーションを実装する
- **目標タイム**: 1時間以内で完成させる
- 詰まったらその部分をスニペットに登録し、最初からやり直す

## 現在の実装状況

✅ **実装済み:**
- POST /users (ユーザー登録)
- GET /users/{id} (ユーザー詳細取得)

❌ **未実装（これから実装する）:**
- GET /users (ユーザー一覧取得)
- PUT /users/{id} (ユーザー更新)
- DELETE /users/{id} (ユーザー削除)

## 実装すべき内容

### 1. ユーザー一覧取得 (GET /users)

**エンドポイント:** `GET /users`

**要件:**
- すべてのユーザーを取得
- ステータスコード: 200 OK
- レスポンス: `List<UserResponse>`

**ヒント:**
- Controller層: `@GetMapping` を使用
- Service層: `repository.findAll()` を使用
- Stream APIの `map()` を使って `User` → `UserResponse` に変換
- Day 2で学んだ `findById().map().orElseThrow()` のパターンと似ているが、今回は `findAll()` なので例外処理は不要

**参考パターン（Day 2）:**
```java
// 単一取得のパターン（参考）
repository.findById(id)
    .map(user -> new UserResponse(...))
    .orElseThrow(...)
```

### 2. ユーザー更新 (PUT /users/{id})

**エンドポイント:** `PUT /users/{id}`

**要件:**
- リクエストボディ: `@Valid UserUpdateRequest`
- パス変数: `@PathVariable Long id`
- ステータスコード: 200 OK
- レスポンス: `UserResponse`

**ヒント:**
- Controller層: `@PutMapping("/{id}")` を使用
- Service層:
  1. `findById(id)` でユーザーを取得（存在しない場合は例外）
  2. `UserUpdateRequest` の値でエンティティを更新
  3. `repository.save()` で保存
  4. `UserResponse` に変換して返す
- `@Transactional` を忘れずに（更新処理なので）
- Day 2で学んだ `orElseThrow()` パターンを使用

**参考パターン（Day 2）:**
```java
User user = repository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException(...));
// エンティティのフィールドを更新
user.setUsername(...);
user.setPassword(...);
User updated = repository.save(user);
```

**注意点:**
- `UserUpdateRequest` には `username` と `password` があるが、`email` は含まれていない
- 更新するのは `username` と `password` のみ
- `email` は更新しない（一意制約があるため）

### 3. ユーザー削除 (DELETE /users/{id})

**エンドポイント:** `DELETE /users/{id}`

**要件:**
- パス変数: `@PathVariable Long id`
- ステータスコード: 200 OK
- レスポンス: `UserDeleteResponse`

**ヒント:**
- Controller層: `@DeleteMapping("/{id}")` を使用
- Service層:
  1. `findById(id)` でユーザーを取得（存在しない場合は例外）
  2. `repository.delete(user)` または `repository.deleteById(id)` で削除
  3. `UserDeleteResponse` を作成して返す
- `@Transactional` を忘れずに（削除処理なので）
- Day 2で学んだ `orElseThrow()` パターンを使用

**参考パターン（Day 2）:**
```java
User user = repository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException(...));
repository.delete(user);
// または
repository.deleteById(id);
```

**注意点:**
- 削除前にユーザーの存在確認が必要（存在しない場合は404を返す）
- `UserDeleteResponse` には `id` と `message` がある
- メッセージ例: `"User deleted successfully"` など

## 実装の順序（推奨）

1. **Service層から実装**（ビジネスロジック）
   - `getAllUsers()` メソッド
   - `updateUser(Long id, UserUpdateRequest request)` メソッド
   - `deleteUser(Long id)` メソッド

2. **Controller層を実装**（HTTPエンドポイント）
   - `GET /users` エンドポイント
   - `PUT /users/{id}` エンドポイント
   - `DELETE /users/{id}` エンドポイント

3. **動作確認**
   - 各エンドポイントをテスト
   - エラーハンドリングが正しく動作するか確認

## チェックリスト

### Service層 (UserService.java)
- [ ] `getAllUsers()` メソッドを追加
  - [ ] `@Transactional(readOnly = true)` がクラスレベルで設定されているか確認（既にあるはず）
  - [ ] `repository.findAll()` を使用
  - [ ] Stream APIの `map()` で `User` → `UserResponse` に変換
  - [ ] `List<UserResponse>` を返す

- [ ] `updateUser(Long id, UserUpdateRequest request)` メソッドを追加
  - [ ] `@Transactional` を付与（更新処理なので）
  - [ ] `repository.findById(id).orElseThrow(...)` でユーザーを取得
  - [ ] エンティティのフィールドを更新（`setUsername()`, `setPassword()`）
  - [ ] `repository.save()` で保存
  - [ ] `UserResponse` に変換して返す

- [ ] `deleteUser(Long id)` メソッドを追加
  - [ ] `@Transactional` を付与（削除処理なので）
  - [ ] `repository.findById(id).orElseThrow(...)` でユーザーを取得
  - [ ] `repository.delete(user)` または `repository.deleteById(id)` で削除
  - [ ] `UserDeleteResponse` を作成して返す

### Controller層 (UserController.java)
- [ ] `GET /users` エンドポイントを追加
  - [ ] `@GetMapping` を使用
  - [ ] `ResponseEntity<List<UserResponse>>` を返す
  - [ ] ステータスコード: 200 OK

- [ ] `PUT /users/{id}` エンドポイントを追加
  - [ ] `@PutMapping("/{id}")` を使用
  - [ ] `@PathVariable Long id` と `@RequestBody @Valid UserUpdateRequest` を受け取る
  - [ ] `ResponseEntity<UserResponse>` を返す
  - [ ] ステータスコード: 200 OK

- [ ] `DELETE /users/{id}` エンドポイントを追加
  - [ ] `@DeleteMapping("/{id}")` を使用
  - [ ] `@PathVariable Long id` を受け取る
  - [ ] `ResponseEntity<UserDeleteResponse>` を返す
  - [ ] ステータスコード: 200 OK

## よくあるミスとヒント

### 1. Service層の `@Transactional` の使い分け
- **読み取り専用**: `getAllUsers()`, `getUser()` → `@Transactional(readOnly = true)` がクラスレベルで設定されているので、個別に指定不要
- **更新・削除**: `createUser()`, `updateUser()`, `deleteUser()` → `@Transactional` を個別に付与（`readOnly = false` がデフォルト）

### 2. Stream APIの使い方
```java
// 一覧取得のパターン
List<UserResponse> users = repository.findAll().stream()
    .map(user -> new UserResponse(user.getId(), user.getUsername()))
    .toList(); // Java 16以降
// または
.collect(Collectors.toList()); // Java 8-15
```

### 3. エンティティの更新パターン
```java
// パターン1: エンティティを取得して更新
User user = repository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException(...));
user.setUsername(request.username());
user.setPassword(request.password());
User updated = repository.save(user);

// パターン2: 直接更新（JPAの機能を使用）
// 今回はパターン1で実装
```

### 4. 削除のパターン
```java
// パターン1: エンティティを取得して削除
User user = repository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException(...));
repository.delete(user);

// パターン2: IDで直接削除
repository.deleteById(id);
// ただし、この場合は存在確認ができないので、パターン1を推奨
```

### 5. DTOの確認
- `UserUpdateRequest`: `username` と `password` のみ（`email` は含まれていない）
- `UserDeleteResponse`: `id` と `message` を持つ
- `UserResponse`: `id` と `username` を持つ（`email` は含まれていない）

## タイムアタックのコツ

1. **まずService層を完成させる**（ビジネスロジックが明確になる）
2. **Controller層はService層のメソッドを呼ぶだけ**（Day 1で学んだパターン）
3. **エラーハンドリングは既に実装済み**（Day 3で実装した `GlobalExceptionHandler` が自動的に処理）
4. **詰まったらDay 1〜4のコードを参考にする**

## 完成後の確認事項

- [ ] すべてのエンドポイントが動作する
- [ ] 存在しないIDで更新・削除を試みると404エラーが返る
- [ ] バリデーションエラー（無効なメールアドレスなど）が正しく処理される
- [ ] AOPが動作してログに実行時間が出力される

---

**目標**: 1時間以内で完成させる

**詰まったら**: その部分をスニペットに登録し、最初からやり直す

**完成したら**: `day5_walkthrough.md` を作成し、実装手順と学びを記録してください。

