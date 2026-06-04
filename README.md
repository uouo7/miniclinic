# MiniClinic 社區診所掛號系統

一個以 Spring Boot 實作的社區診所掛號系統，支援醫師登入、病患掛號、
掛號狀態管理等功能。

## 線上 Demo

https://miniclinic-uouo7.onrender.com

## 技術棧

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Thymeleaf
- SQLite（開發）/ PostgreSQL（部署）
- BCrypt（密碼雜湊）

## 功能清單

- 醫師登入 / 登出
- 醫師個人 Dashboard
- 病患資料管理（CRUD）
- 線上掛號功能
- 掛號狀態變更（booked / completed / cancelled）
- RESTful API（支援第三方整合）

## 本機執行

```bash
git clone https://github.com/uouo7/miniclinic.git
cd miniclinic
./mvnw spring-boot:run
```

開啟瀏覽器訪問 http://localhost:8080

預設醫師帳密：

- D001 / pass1234
- D002 / pass1234
- （其他醫師密碼均為 pass1234）

## 資料初始化

第一次啟動時，`data.sql` 會自動插入：
- 5 位虛構醫師
- 3 位虛構病患（TEST00001, TEST00002, TEST00003）
- 3 筆示範掛號

## Render 部署設定

如果要把這個專案部署到 Render，建議把敏感的資料庫資訊放在 Render 的 Environment Variables，而不是直接寫死在程式碼中。

1. 進入 Render Dashboard，選擇你的 Web Service。
2. 開啟 Environment 頁面，加入以下變數：
   - `SPRING_DATASOURCE_URL`：你的資料庫連線字串
   - `SPRING_DATASOURCE_USERNAME`：你的資料庫帳號
   - `SPRING_DATASOURCE_PASSWORD`：你的資料庫密碼
   - `SPRING_PROFILES_ACTIVE=prod`
3. 若使用 PostgreSQL，請確認 `application-prod.properties` 內有對應的設定，例如：

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
```

如果沒有外部資料庫，則需要另外補上 H2 等測試資料庫依賴；正式環境仍建議使用真實資料庫。

## 專案結構

```
src/
├── main/
│   ├── java/tw/edu/fju/miniclinic/
│   │   ├── controller/     # HTTP 請求處理
│   │   ├── model/          # Entity 與 Repository
│   │   ├── interceptor/    # 登入驗證
│   │   └── config/         # Spring 配置
│   └── resources/
│       ├── templates/      # Thymeleaf 模板
│       ├── static/         # CSS、JS
│       └── application.properties
```

## 作者

2026 年 Java 程式設計課程作業

## 聲明

所有病患資料均為虛構，僅供教學使用。