# ReLoop

Sàn thương mại điện tử mua bán đồ cũ (secondhand marketplace) theo mô hình C2C, lấy cảm hứng từ Chợ Tốt. Người dùng đăng tin bán đồ đã qua sử dụng, mua hàng, nhắn tin trao đổi trực tiếp và thanh toán online qua PayOS.

## Tech stack

| Tầng | Công nghệ |
|------|-----------|
| Backend | Java 11, Jakarta EE 10 (Servlet, JSP, JSTL) |
| Build | Maven, đóng gói WAR |
| Database | MySQL 8 (`reloop_v3`) |
| Server | Apache Tomcat 10+ |
| Thanh toán | PayOS (QR tạo tức thì, callback) |
| Đăng nhập | Email/mật khẩu, Google OAuth, Facebook OAuth |
| Lưu trữ ảnh | AWS S3 |
| Kiểm duyệt nội dung | Google Vision (ảnh) + Hive Moderation (văn bản) |
| Email | Jakarta Mail (SMTP) |
| Chat | WebSocket (JSR-356) |
| Frontend | JSP, Bootstrap 5, Sass |

## Tính năng

- **Xác thực** — đăng ký, đăng nhập bằng email hoặc Google/Facebook, quên/đặt lại mật khẩu qua email.
- **Đăng tin & mua bán** — đăng sản phẩm kèm ảnh, danh mục phân cấp, tìm kiếm, chi tiết sản phẩm, yêu thích, giỏ hàng, đặt hàng, theo dõi người bán.
- **Kiểm duyệt AI** — bài đăng được quét tự động qua Google Vision (ảnh) và Hive (text) trước khi duyệt; trạng thái: `pending / approved / rejected / blocked / warn`.
- **Phân quyền** — 4 vai trò: `user`, `shopkeeper`, `admin`, `supporter`. Admin/supporter duyệt bài, xử lý tố cáo, quản lý tài khoản (khóa/mở), thống kê.
- **Chat realtime** — nhắn tin 1-1 giữa người mua và người bán theo từng sản phẩm, chặn/bỏ chặn.
- **Thanh toán & ví** — nạp tiền, thanh toán QR PayOS, rút tiền, gói Premium, mã giảm giá (voucher), lịch sử giao dịch, phí vận chuyển.
- **Đánh giá & phản hồi** — đánh giá sản phẩm, gửi feedback.

## Cấu trúc thư mục

```
ReLoop/
├── src/main/java/
│   ├── Controller/        # Servlet (Auth, Admin, Post, Paid, Conversation, ...)
│   ├── Model/DAO/         # Data access (admin, auth, commerce, post, pay, ...)
│   ├── Model/entity/      # Entity ánh xạ bảng DB
│   ├── Service/           # Logic nghiệp vụ (OrderService, PayService, ...)
│   └── Utils/             # DBUtils, AppConfig, S3Util, PasswordUtils, ...
├── src/main/webapp/
│   ├── JSP/               # View (Home, Admin, User, Commerce, Conversation, ...)
│   ├── css/ js/ scss/ img/# Asset tĩnh
│   └── WEB-INF/web.xml    # Cấu hình servlet, welcome page
├── db-reloopv3/           # Script khởi tạo DB và dữ liệu mẫu
│   ├── tb-reloop-v3.sql   # Schema (bảng + quan hệ)
│   ├── trickger-reloop-v3.sql  # Trigger
│   ├── data-setup.sql     # Dữ liệu nền (danh mục, voucher, ...)
│   └── product-data.sql   # Dữ liệu sản phẩm mẫu
├── getProduct.py          # Scraper lấy dữ liệu mẫu từ Chợ Tốt (Selenium + BeautifulSoup)
└── pom.xml
```

## Cài đặt

### Yêu cầu

- JDK 11
- Maven 3.8+
- Apache Tomcat 10+ (hoặc NetBeans có tích hợp Tomcat)
- MySQL 8

### 1. Tạo database

Chạy các script trong `db-reloopv3/` theo thứ tự:

```bash
mysql -u root -p < db-reloopv3/tb-reloop-v3.sql
mysql -u root -p reloop_v3 < db-reloopv3/trickger-reloop-v3.sql
mysql -u root -p reloop_v3 < db-reloopv3/data-setup.sql
mysql -u root -p reloop_v3 < db-reloopv3/product-data.sql
```

### 2. Cấu hình `config.properties`

Tạo file `src/main/resources/config.properties` (AppConfig đọc từ classpath). File này chưa có sẵn và nằm ngoài git:

```properties
# Database
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/reloop_v3?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
db.password=

# Google OAuth
google.client_id=
google.client_secret=
google.redirect_uri=
google.grant_type=authorization_code
google.link_get_token=https://oauth2.googleapis.com/token
google.link_get_user_info=https://www.googleapis.com/oauth2/v1/userinfo

# Facebook OAuth
facebook.app_id=
facebook.app_secret=
facebook.redirect_uri=
facebook.link_get_token=
facebook.link_get_user_info=

# Email (SMTP)
email.from=
email.password=

# AWS S3
aws.accessKeyId=
aws.secretAccessKey=
aws.bucketName=
aws.region=

# PayOS
payos.client_id=
payos.api_key=
payos.checksum_key=

# AI censor
hive.api_key=
vision.api_key=
vision.api_url=
```

### 3. Build & deploy

```bash
mvn clean package
```

File `target/ReLoop-1.0-SNAPSHOT.war` deploy lên Tomcat. Hoặc mở project trong NetBeans và Run (đã cấu hình hint Tomcat trong `nb-configuration.xml`).

Truy cập ứng dụng tại `http://localhost:8080/ReLoop/` (welcome page trỏ về `/home`).

## Các vai trò

| Vai trò | Quyền |
|---------|-------|
| `user` | Đăng tin, mua, chat, thanh toán |
| `shopkeeper` | Người bán chuyên nghiệp, đăng nhiều tin |
| `supporter` | Xử lý tố cáo, hỗ trợ kiểm duyệt |
| `admin` | Toàn quyền: duyệt bài, quản lý tài khoản, thống kê |

## Ghi chú

- `getProduct.py` là script phụ để crawl dữ liệu sản phẩm mẫu từ Chợ Tốt, không phải một phần của ứng dụng chính.
- `fix-moderation-status-final-complete.md` và `db-reloopv3/fix-moderation-status-final.sql` là bản vá riêng cho trạng thái kiểm duyệt.
