# 🛒 POS_App – Ứng dụng Quản lý Bán hàng

[![🌐 English](https://img.shields.io/badge/Language-English-blue)](./README_EN.md) [![🌐 Tiếng Việt](https://img.shields.io/badge/Ngôn_ngữ-Tiếng_Việt-red)](./README.md)  
![GitHub stars](https://img.shields.io/github/stars/dangkhoa2004/pos_app?style=social)
![GitHub forks](https://img.shields.io/github/forks/dangkhoa2004/pos_app?style=social)

Một ứng dụng quản lý bán hàng toàn diện phát triển bằng **Java Swing**, thiết kế theo mô hình **MVC + UI Layered**, hỗ trợ đầy đủ các tính năng quản lý sản phẩm, hóa đơn, khách hàng, tồn kho và nhân viên. Giao diện hiện đại, dễ sử dụng, có thể mở rộng.

---

## 🧰 Công nghệ & Môi trường phát triển

| 🔧 Thành phần     | ⚙️ Thông tin                                      |
| ----------------- | ------------------------------------------------- |
| 🖥️ **Ngôn ngữ**   | Java 24                                           |
| 🧠 **IDE**        | NetBeans 25 (Apache NetBeans)                     |
| 🎨 **UI Toolkit** | Java Swing + FlatLaf                              |
| 🗃️ **Database**   | MySQL + JDBC (mysql-connector-j)                  |
| 🧩 **Mô hình**    | DAO – Model – View – Component Separation (MVC++) |
| 🖌️ **Theme**      | FlatLaf Light                                     |

---

## 💻 Yêu cầu hệ thống

- ☕ Java JDK 17 trở lên (khuyến nghị Java 21+)
- 💡 NetBeans 15+ hoặc IntelliJ có hỗ trợ Swing
- 🛢️ MySQL Server 5.7+ hoặc 8.x
- 🧑‍💻 Tương thích với Windows / macOS / Linux

---

## 📁 Cấu trúc dự án

```bash
pos_app/
├── dao/              # Truy vấn và xử lý dữ liệu (Database Access)
├── icons/            # Icon SVG/PNG dùng trong giao diện
├── models/           # Lớp mô hình: Product, Invoice, Customer, ...
├── pictures/         # Lưu hình ảnh tĩnh phục vụ giao diện
├── sql/              # Chứa các file SQL hoặc thao tác khởi tạo CSDL
├── ui.components/    # UI tái sử dụng: Button, Sidebar, Header,...
├── ui.dialog/        # Dialog nhập/chỉnh sửa dữ liệu (Form)
├── ui.panel/         # Giao diện từng chức năng: Sản phẩm, Hóa đơn...
├── ui.table/         # Tùy biến bảng: CellRenderer, ButtonEditor
├── util/             # Tiện ích chung: DatabaseConnection, IconUtil,...
├── view/             # Giao diện chính (MainFrame.java)
└── test/             # Gói test đơn vị (unit test)
```

---

## 📦 Thư viện sử dụng

| 📚 Thư viện                                | 📝 Mô tả                                                  |
| ------------------------------------------ | --------------------------------------------------------- |
| `FlatLaf`                                  | Giao diện hiện đại, hỗ trợ SVG, Dark mode, IntelliJ Theme |
| `flatlaf-extras`                           | Tuỳ chỉnh theme và icon từ SVG dễ dàng                    |
| `darklaf-core`                             | (Tuỳ chọn) Hiệu ứng shadow, nền blur                      |
| `darklaf-utils`, `darklaf-property-loader` | Công cụ hỗ trợ cấu hình Dark Theme                        |
| `mysql-connector-j`                        | Kết nối MySQL (JDBC)                                      |
| `protobuf-java`                            | Dự phòng: lưu dữ liệu dạng nhị phân                       |
| `jsvg`                                     | Hiển thị icon SVG cho giao diện                           |
| `java-se` (`core-3.5.3`, `javase-3.5.3`)   | Xử lý webcam, hình ảnh, barcode                           |
| `webcam-capture`                           | Tích hợp webcam, quét mã vạch / QR                        |
| `slf4j-api`, `slf4j-simple`                | Ghi log hệ thống dạng đơn giản                            |
| `bridj`                                    | Gọi native API trên Windows cho webcam                    |
| `gson`                                     | Chuyển đổi giữa JSON và đối tượng Java                    |

---

## 🖥️ Tính năng chính

| 🧩 Chức năng               | 📌 Mô tả                                                            |
| -------------------------- | ------------------------------------------------------------------- |
| 🛒 **Bán hàng**            | Giao diện POS, thêm sản phẩm vào giỏ hàng, thanh toán, in hóa đơn   |
| 📦 **Quản lý sản phẩm**    | CRUD sản phẩm, cập nhật số lượng, giá, trạng thái, loại sản phẩm    |
| 👤 **Khách hàng**          | Quản lý thông tin khách hàng, phân loại nhóm khách hàng             |
| 🧾 **Hóa đơn**             | Danh sách hóa đơn, xem chi tiết, in và tìm kiếm hóa đơn             |
| 📊 **Thống kê**            | Biểu đồ Pie/Bar doanh thu theo ngày, tháng, năm _(đang phát triển)_ |
| 🚚 **Nhập / Xuất kho**     | Quản lý tồn kho, tạo phiếu nhập/xuất hàng                           |
| 👔 **Nhân viên / Chức vụ** | Quản lý tài khoản, phân quyền theo vai trò (Admin, Staff)           |
| 📋 **Bảng điều khiển**     | Tổng hợp các log cập nhật hệ thống từ máy POS                       |
| ⚙️ **Cài đặt hệ thống**    | Cấu hình theme, logo, thông tin cửa hàng, đơn vị tiền tệ, v.v.      |

---

## ✅ Hướng dẫn chạy ứng dụng

1. **Clone dự án:**

   ```bash
   git clone https://github.com/dangkhoa2004/pos_app.git
   ```

2. **Mở bằng IDE:**
   👉 NetBeans 25 _(ưu tiên)_ hoặc IntelliJ (đã cấu hình Maven/Ant)

3. **Cài MySQL & tạo CSDL:**

   ```sql
   CREATE DATABASE pos_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

   Sau đó import `pos_app.sql` từ thư mục `/sql`

4. **Cấu hình kết nối trong `DBConnection.java`:**

   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/pos_app";
   private static final String USER = "root";
   private static final String PASSWORD = ""; // đổi theo máy bạn
   ```

5. **Chạy file `MainFrame.java`** để mở giao diện chính.

6. **Lưu ý:**
   - Kiểm tra thư viện JDBC nếu có lỗi `ClassNotFoundException`
   - Dùng UTF-8 để hiển thị tiếng Việt chính xác

---

## 📌 Ghi chú phát triển

- Tất cả icon dạng `.svg` lưu trong `icons/`
- Module mới → Tạo `Panel` trong `ui.panel` → Gọi từ `SideBarMenu`
- Các chức năng đang phát triển sẽ hiện `JOptionPane`
- Thiết kế mở rộng: Có thể tích hợp in hóa đơn PDF, cloud sync...

---

## 📸 Giao diện minh họa

### 🌙 Giao diện FlatLaf Dark

> ⏳ _Chưa ra mắt_

<!-- ![Dark Mode Sidebar](./src/pos_app/icons/sidebar_dark_preview.png) -->

### ☀️ Giao diện FlatLaf Light

> ⏳ _Chưa ra mắt_

<!-- ![Light Mode Main](./src/pos_app/icons/main_light_preview.png) -->

---

## 🔮 Kế hoạch mở rộng

- [ ] 🧾 In hóa đơn ra file PDF
- [ ] 📤 Tích hợp API Google Sheets
- [ ] 💾 Backup định kỳ tự động
- [ ] 🔐 Login + phân quyền nâng cao
- [ ] ☁️ Đồng bộ dữ liệu Firebase

---

## 🤝 Đóng góp

Chúng tôi hoan nghênh mọi đóng góp!

- 🛠 Fork repo
- 🌱 Tạo nhánh `feature/<ten-chuc-nang>`
- 📥 Gửi Pull Request kèm mô tả rõ ràng

---

## 📚 Tài liệu kỹ thuật

- 🧩 **Cấu trúc & Dữ liệu mẫu CSDL:**

  ```sql
  -- Tạo cơ sở dữ liệu
  CREATE DATABASE IF NOT EXISTS pos_app;
  USE pos_app;

  CREATE TABLE roles (
      id INT AUTO_INCREMENT PRIMARY KEY,
      name VARCHAR(50) NOT NULL
  );

  CREATE TABLE employees (
      id INT AUTO_INCREMENT PRIMARY KEY,
      name VARCHAR(100),
      username VARCHAR(50) UNIQUE,
      password VARCHAR(255),
      role_id INT,
      FOREIGN KEY (role_id) REFERENCES roles(id)
  );

  ... ... ...
  ```

[Xem toàn bộ file SQL](./src/pos_app/sql/pos_app.sql)

- 🧩 **Sơ đồ CSDL trực quan:**

  ![Sơ đồ CSDL](./src/pos_app/sql/db_schema.png)

- 📦 **Cách tạo module mới:** _(sẽ có trong Wiki)_

---

## 📜 Giấy phép

Dự án hiện **chưa công khai giấy phép**. Nếu bạn muốn sử dụng lại mã nguồn, vui lòng liên hệ tác giả để được cấp phép rõ ràng.

---

## 👤 Tác giả & Liên hệ

- 👨‍💻 **Tác giả:** Đăng Khoa
- 📧 **Email:** 04dkhoa04@gmail.com
- 💬 **Facebook:** [Đăng Khoa](https://www.facebook.com/dangkh0a2004)
- ⭐ Nếu thấy hữu ích, hãy để lại một ngôi sao cho repo này nhé!
