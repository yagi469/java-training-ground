# CRUD API 実装スニペット集

このドキュメントには、Spring Boot CRUD APIを高速実装するためのスニペット（コードテンプレート）が含まれています。
**太字の部分**を置換して使用してください。

---

## 目次
1. [Entity定義](#1-entity定義)
2. [DTO定義](#2-dto定義)
3. [Repository定義](#3-repository定義)
4. [Service層](#4-service層)
5. [Controller層](#5-controller層)
6. [Exception定義](#6-exception定義)
7. [GlobalExceptionHandler](#7-globalexceptionhandler)

---

## 1. Entity定義

### 基本テンプレート（String + 数値フィールド）

```java
package com.example.PACKAGE_NAME.entity;

import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ENTITY_NAME {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String FIELD_NAME_1;
    private String FIELD_NAME_2;
    private BigDecimal FIELD_NAME_3;
    private Integer FIELD_NAME_4;
}
```

**置換リスト:**
- `PACKAGE_NAME`: パッケージ名（例: `week4.finaltask`）
- `ENTITY_NAME`: エンティティ名（例: `Product`, `Book`, `Order`）
- `FIELD_NAME_X`: フィールド名（例: `name`, `description`, `price`, `stock`）

---

## 2. DTO定義

### 2-1. Request DTO（バリデーション付き）

```java
package com.example.PACKAGE_NAME.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ENTITY_NAMERequest {
    @NotBlank(message = "FIELD_NAME_1 is required")
    private String FIELD_NAME_1;

    private String FIELD_NAME_2;

    @NotNull(message = "FIELD_NAME_3 is required")
    @Positive(message = "FIELD_NAME_3 must be positive")
    private BigDecimal FIELD_NAME_3;

    @Positive(message = "FIELD_NAME_4 must be positive")
    private Integer FIELD_NAME_4;
}
```

### 2-2. Response DTO

```java
package com.example.PACKAGE_NAME.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ENTITY_NAMEResponse {
    private Long id;
    private String FIELD_NAME_1;
    private String FIELD_NAME_2;
    private BigDecimal FIELD_NAME_3;
    private Integer FIELD_NAME_4;
}
```

**置換リスト:**
- `ENTITY_NAME`: エンティティ名（例: `Product` → `ProductRequest`, `ProductResponse`）
- `FIELD_NAME_X`: フィールド名（Entityと同じ）

---

## 3. Repository定義

```java
package com.example.PACKAGE_NAME.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.PACKAGE_NAME.entity.ENTITY_NAME;

public interface ENTITY_NAMERepository extends JpaRepository<ENTITY_NAME, Long> {
}
```

**置換リスト:**
- `ENTITY_NAME`: エンティティ名（例: `Product` → `ProductRepository`）

---

## 4. Service層

### 完全なCRUD Service

```java
package com.example.PACKAGE_NAME.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

import com.example.PACKAGE_NAME.dto.ENTITY_NAMERequest;
import com.example.PACKAGE_NAME.dto.ENTITY_NAMEResponse;
import com.example.PACKAGE_NAME.entity.ENTITY_NAME;
import com.example.PACKAGE_NAME.exception.ENTITY_NAMENotFoundException;
import com.example.PACKAGE_NAME.repository.ENTITY_NAMERepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ENTITY_NAMEService {
    private final ENTITY_NAMERepository REPOSITORY_FIELD_NAME;

    // 一覧取得
    public List<ENTITY_NAMEResponse> getAll() {
        return REPOSITORY_FIELD_NAME.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ID指定取得
    public ENTITY_NAMEResponse getById(Long id) {
        return toResponse(REPOSITORY_FIELD_NAME.findById(id)
                .orElseThrow(() -> new ENTITY_NAMENotFoundException("ENTITY_NAME not found with id: " + id)));
    }

    // 新規作成
    @Transactional
    public ENTITY_NAMEResponse create(ENTITY_NAMERequest request) {
        ENTITY_NAME entity = toEntity(request);
        ENTITY_NAME saved = REPOSITORY_FIELD_NAME.save(entity);
        return toResponse(saved);
    }

    // 更新
    @Transactional
    public ENTITY_NAMEResponse update(Long id, ENTITY_NAMERequest request) {
        ENTITY_NAME entity = REPOSITORY_FIELD_NAME.findById(id)
                .orElseThrow(() -> new ENTITY_NAMENotFoundException("ENTITY_NAME not found with id: " + id));
        entity.setFIELD_NAME_1(request.getFIELD_NAME_1());
        entity.setFIELD_NAME_2(request.getFIELD_NAME_2());
        entity.setFIELD_NAME_3(request.getFIELD_NAME_3());
        entity.setFIELD_NAME_4(request.getFIELD_NAME_4());
        ENTITY_NAME updated = REPOSITORY_FIELD_NAME.save(entity);
        return toResponse(updated);
    }
    
    // 削除
    @Transactional
    public void delete(Long id) {
        REPOSITORY_FIELD_NAME.deleteById(id);
    }

    // ヘルパー: Entity → Response
    private ENTITY_NAMEResponse toResponse(ENTITY_NAME entity) {
        return new ENTITY_NAMEResponse(
                entity.getId(),
                entity.getFIELD_NAME_1(),
                entity.getFIELD_NAME_2(),
                entity.getFIELD_NAME_3(),
                entity.getFIELD_NAME_4()
        );
    }
    
    // ヘルパー: Request → Entity
    private ENTITY_NAME toEntity(ENTITY_NAMERequest request) {
        return new ENTITY_NAME(
                null,
                request.getFIELD_NAME_1(),
                request.getFIELD_NAME_2(),
                request.getFIELD_NAME_3(),
                request.getFIELD_NAME_4()
        );
    }
}
```

**置換リスト:**
- `ENTITY_NAME`: エンティティ名（例: `Product`）
- `REPOSITORY_FIELD_NAME`: リポジトリのフィールド名（例: `productRepository`）
- `FIELD_NAME_X`: フィールド名（例: `Name`, `Description`, `Price`, `Stock`）

---

## 5. Controller層

### 完全なCRUD Controller

```java
package com.example.PACKAGE_NAME.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.example.PACKAGE_NAME.dto.ENTITY_NAMERequest;
import com.example.PACKAGE_NAME.dto.ENTITY_NAMEResponse;
import com.example.PACKAGE_NAME.service.ENTITY_NAMEService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ENDPOINT_PATH")
@RequiredArgsConstructor
public class ENTITY_NAMEController {
    private final ENTITY_NAMEService SERVICE_FIELD_NAME;

    @GetMapping
    public ResponseEntity<List<ENTITY_NAMEResponse>> getAll() {
        return ResponseEntity.ok(SERVICE_FIELD_NAME.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ENTITY_NAMEResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(SERVICE_FIELD_NAME.getById(id));
    }

    @PostMapping
    public ResponseEntity<ENTITY_NAMEResponse> create(@Valid @RequestBody ENTITY_NAMERequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(SERVICE_FIELD_NAME.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ENTITY_NAMEResponse> update(@PathVariable Long id, @Valid @RequestBody ENTITY_NAMERequest request) {
        return ResponseEntity.ok(SERVICE_FIELD_NAME.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        SERVICE_FIELD_NAME.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

**置換リスト:**
- `ENTITY_NAME`: エンティティ名（例: `Product`）
- `ENDPOINT_PATH`: エンドポイントパス（例: `products`, `books`, `orders`）
- `SERVICE_FIELD_NAME`: サービスのフィールド名（例: `productService`）

---

## 6. Exception定義

### カスタム例外クラス

```java
package com.example.PACKAGE_NAME.exception;

public class ENTITY_NAMENotFoundException extends RuntimeException {
    public ENTITY_NAMENotFoundException(String message) {
        super(message);
    }
}
```

**置換リスト:**
- `ENTITY_NAME`: エンティティ名（例: `Product` → `ProductNotFoundException`）

---

## 7. GlobalExceptionHandler

### 共通例外ハンドラー

```java
package com.example.PACKAGE_NAME.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ENTITY_NAMENotFoundException.class)
    public ResponseEntity<String> handleNotFound(ENTITY_NAMENotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationError(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().build();
    }
}
```

**置換リスト:**
- `ENTITY_NAME`: エンティティ名（例: `Product`）

---

## 使い方ガイド

### ステップ1: Entityから開始
1. Entity定義をコピー
2. `ENTITY_NAME`, `FIELD_NAME_X` を置換
3. 保存 → コンパイル確認

### ステップ2: DTO作成
1. Request/Response DTOをコピー
2. 同じフィールド名で置換
3. バリデーションルール調整

### ステップ3: Repository作成
1. Repository定義をコピー
2. `ENTITY_NAME` を置換（1箇所のみ）

### ステップ4: Service作成
1. Service全体をコピー
2. すべての `ENTITY_NAME` を一括置換
3. フィールド名の `set/get` メソッドを調整

### ステップ5: Controller作成
1. Controller全体をコピー
2. `ENTITY_NAME`, `ENDPOINT_PATH` を置換
3. アノテーション確認（`@Valid` など）

### ステップ6: Exception作成
1. Exception定義をコピー
2. `ENTITY_NAME` を置換
3. GlobalExceptionHandlerに追加

---

## 時間短縮のコツ

1. **IDEの一括置換機能を使う**
   - `Ctrl+H` (Windows) / `Cmd+R` (Mac)
   - `ENTITY_NAME` → `Product` などを一括置換

2. **フィールド数が違う場合**
   - 不要なフィールドは削除
   - 追加フィールドはコピー&ペースト

3. **import文の自動補完**
   - IDEの自動import機能を活用
   - 赤線が出たら `Alt+Enter` で修正

4. **コンパイルエラーは後回し**
   - まず全ファイルを作成
   - 最後に一括でエラー修正

---

## 次のステップ

このスニペットを使って、以下のドメインで練習しましょう：
- Book（書籍）: title, author, isbn, price
- Order（注文）: customerName, orderDate, totalAmount, status
- Employee（従業員）: name, email, department, salary

**目標**: 15-20分で完成させる
