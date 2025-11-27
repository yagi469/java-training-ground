# Implementation Plan - Week 4 Day 4 Preparation

## 概要
Week 4 Day 4のタイムアタック用プロジェクト `week4-final-time-attack` をセットアップします。

## 手順

1.  **プロジェクトディレクトリの作成**
    *   `exercises/week4-final-time-attack` を作成。

2.  **`pom.xml` の作成**
    *   `week4-refactoring-practice` の `pom.xml` をベースに、`artifactId` と `name` を `week4-final-time-attack` に変更して作成。

3.  **ソースコード構造の作成**
    *   `src/main/java/com/example/week4/finaltask/` パッケージを作成。
    *   メインクラス `Week4FinalTimeAttackApplication.java` を作成。
    *   以下のサブパッケージを作成:
        *   `controller`
        *   `service`
        *   `repository`
        *   `entity`
        *   `dto`
        *   `exception`

4.  **リソースファイルの作成**
    *   `src/main/resources/application.properties` を作成（空または基本的な設定）。

5.  **Maven Wrapperのセットアップ**
    *   既存のプロジェクトから `.mvn` ディレクトリと `mvnw`, `mvnw.cmd` をコピー。

## 検証
*   プロジェクトがMavenプロジェクトとして認識されること。
*   `mvnw clean compile` が成功すること。
