# 線上投票系統 (Voting System)

這是一個前後端分離的線上投票系統，包含投票項目管理、使用者多選投票、票數統計、資料庫 DDL/DML 與 Stored Procedure。

## 技術架構

* Frontend: Vue 3 + Vite
* Backend: Java 17 + Spring Boot + Spring JDBC
* Database: MySQL 8.0
* Build Tool: Maven
* Deployment Support: Docker Compose

## 專案結構

```text
VotingSystem
├── DB
│   ├── 01_init_schema_and_data.sql
│   └── 02_stored_procedures.sql
├── backend
│   └── src/main/java/com/fantastic96/voting
│       ├── common
│       ├── controller
│       ├── dto
│       ├── repository
│       └── service
├── frontend
└── docker-compose.yml
```

## 功能

* 顯示所有投票項目與目前累積票數
* 新增投票項目
* 刪除投票項目，並一併刪除相關投票紀錄
* 使用者輸入姓名後，可一次勾選多個項目投票

## 啟動方式

### 1. 啟動資料庫

```bash
docker compose up -d
```

資料庫啟動時會自動執行 `DB` 資料夾內的 DDL、DML 與 Stored Procedure。

### 2. 啟動後端

```bash
cd backend
./mvnw spring-boot:run
```

後端 API 預設啟動於 `http://localhost:8080`。

### 3. 啟動前端

```bash
cd frontend
npm install
npm run dev
```

前端頁面預設啟動於 `http://localhost:5173`。

## API

| Method | Path | Description |
| --- | --- | --- |
| GET | `/api/items` | 查詢投票項目與票數 |
| POST | `/api/items` | 新增投票項目 |
| DELETE | `/api/items/{id}` | 刪除投票項目 |
| POST | `/api/votes` | 送出多選投票 |

### 新增投票項目

```json
{
  "itemName": "鍵盤"
}
```

### 送出投票

```json
{
  "voterName": "Alex",
  "itemIds": [1, 2]
}
```

## 安全性與資料一致性

* 後端使用 `JdbcTemplate` 參數化呼叫 Stored Procedure，避免 SQL Injection。
* 前端使用 Vue template interpolation 顯示資料，預設會 escape HTML，降低 XSS 風險。
* 刪除投票項目時由 Stored Procedure 控制 Transaction，避免主表與子表資料不一致。
* 後端 service 層會檢查必要輸入，空白資料會回傳 HTTP 400。
