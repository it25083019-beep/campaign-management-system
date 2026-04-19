# 🎯 キャンペーン応募管理システム

---

## 📌 概要

本システムは、キャンペーン応募情報を管理するためのWebアプリケーションです。
ユーザーは応募フォームから情報を登録でき、管理者は管理画面からデータの確認・分析・抽選などを行うことができます。

---

## 🛠 使用技術

* Java / Spring Boot
* Spring Data JPA
* MySQL
* HTML / JavaScript
* Bootstrap
* REST API

---

## ✨ 主な機能

### 👤 ユーザー機能

* 応募フォーム入力
* 入力バリデーション（フロントエンド + バックエンド）
* エラーメッセージ日本語対応
* 入力エラーをフォーム下に表示（UX改善）

---

### 🛠 管理者機能（Admin Dashboard）

* 登録者一覧表示
* 統計情報表示

    * 総登録者数
    * 最多地区
* 地区別検索（フィルター機能）
* 抽選機能（ランダム抽出）
* CSVダウンロード
* ローディング・メッセージ表示対応

---

## 📊 システム構成

```
Frontend (HTML / JavaScript)
        ↓
Spring Boot REST API
        ↓
MySQL Database
```

---

## 🔍 API一覧

| Method | URL               | 内容        |
| ------ | ----------------- | --------- |
| POST   | /api/register     | ユーザー登録    |
| GET    | /api/users        | 全ユーザー取得   |
| GET    | /api/stats        | 統計情報取得    |
| POST   | /api/draw         | 抽選実行      |
| GET    | /api/users/filter | 地区別検索     |
| GET    | /api/users/export | CSVダウンロード |
| DELETE | /api/users/{id}   | ユーザー削除    |

---

## 💡 工夫した点（重要ポイント）

* フロントエンドとバックエンドの両方でバリデーションを実装し、入力ミスを防止しました
* バックエンドのエラーメッセージを日本語化し、ユーザーに分かりやすくしました
* 入力エラーをアラートではなく、各入力欄の下に表示することでUXを改善しました
* 管理画面に統計表示・フィルター機能を追加し、実務に近いUIを意識しました
* CSV出力機能を実装し、データの活用を可能にしました
* Google Apps Scriptベースの仕組みを、Spring Boot + MySQL構成に再設計しました

---

## 🚀 実行方法

### 1. データベース作成

```sql
CREATE DATABASE campaign_db;
```

---

### 2. 設定（application.properties）

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/campaign_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

### 3. アプリケーション起動

Spring Boot を起動します。

---

### 4. フロント画面起動

* register.html（ユーザー用）
* admin.html（管理画面）

※ Live Serverなどで起動

---

## 🖥 画面イメージ（※後で追加推奨）

* 応募フォーム画面
* 管理画面（ダッシュボード）

---

## 🧠 開発背景

もともとGoogle Apps Scriptで構築していたシステムを、
より実務に近い構成にするため、Spring Boot + MySQLで再構築しました。

---

## 🔧 今後の改善

* ログイン機能（認証・認可）
* メール通知機能
* UI/UXのさらなる改善
* クラウド環境へのデプロイ
* APIの構造改善（Service層導入）

---

## 📌 今後の予定

* GitHub公開
* Webデプロイ（Railway / Render）
* ポートフォリオとして活用


## 🌐 デモ
- トップページ  
  https://campaign-management-system-production.up.railway.app/

- 管理画面  
  https://campaign-management-system-production.up.railway.app/admin

- 応募フォーム  
  https://campaign-management-system-production.up.railway.app/register

---
