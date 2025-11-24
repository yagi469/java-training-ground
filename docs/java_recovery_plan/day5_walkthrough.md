# Day 5 Walkthrough: CRUD Time Attack

## 目的
- Day 1〜4の知識を総動員して、1時間以内に完全なCRUD機能を実装する。
- 実装対象:
  - `GET /users` (一覧取得)
  - `PUT /users/{id}` (更新)
  - `DELETE /users/{id}` (削除)

## 実装ログ

### 開始時刻: 14:30

### 1. Service層の実装
- [x] `getAllUsers()`: `repository.findAll()` を使用し、Stream API で `UserResponse` に変換。
- [x] `updateUser()`: `findById().orElseThrow()` で取得後、セッターで値を更新し `save()`。
- [x] `deleteUser()`: `deleteById()` を使用して削除。

### 2. Controller層の実装
- [x] `GET /users`: `userService.getUsers()` を呼び出し、リストを返却。
- [x] `PUT /users/{id}`: `@Valid` 付きの `UserUpdateRequest` を受け取り、更新後のユーザーを返却。
- [x] `DELETE /users/{id}`: 削除成功後にメッセージを含む `UserDeleteResponse` を返却。

### 3. 動作確認
- [x] 全エンドポイントのテスト完了
  - `UserControllerTest` を作成し、`MockMvc` を使用して全エンドポイント（登録、取得、一覧取得、更新、削除）の正常系を検証。

### 終了時刻: 14:48
### 所要時間: 18分

## 学び・気付き
- Service層を先に実装することで、Controller層の実装が非常にスムーズに進んだ。
- `MockMvc` を使ったテストは、コントローラの挙動（ステータスコード、レスポンスボディ、ヘッダー）を網羅的に確認できるため強力。
- DTO (`UserUpdateRequest`, `UserDeleteResponse`) の設計が明確だったので、迷わず実装できた。

## 課題・ハマった点
- `UserController` でクラス定義が重複するミスがあったが、すぐに修正した。
- テストクラスで `null` 警告が出たため `@SuppressWarnings("null")` で対応した（実務では原因を特定して解消すべきだが、今回はスピード優先）。
