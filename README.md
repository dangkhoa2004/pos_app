# 🛒 POS_App – Ứng dụng Quản lý Bán hàng

[![🌐 English](https://img.shields.io/badge/Language-English-blue)](./README_EN.md) [![🌐 Tiếng Việt](https://img.shields.io/badge/Ngôn_ngữ-Tiếng_Việt-red)](./README.md)

Một ứng dụng quản lý bán hàng toàn diện phát triển bằng **Java Swing**, thiết kế theo mô hình **MVC + UI Layered**, hỗ trợ đầy đủ các tính năng quản lý sản phẩm, hóa đơn, khách hàng, tồn kho và nhân viên. Giao diện hiện đại, dễ sử dụng, có thể mở rộng.

---

## 🔧 Công nghệ & Môi trường phát triển

| Thành phần     | Thông tin                                         |
| -------------- | ------------------------------------------------- |
| **Ngôn ngữ**   | Java 24                                           |
| **IDE**        | NetBeans 25 (Apache NetBeans)                     |
| **UI Toolkit** | Java Swing + FlatLaf                              |
| **Database**   | MySQL + JDBC (mysql-connector-j)                  |
| **Mô hình**    | DAO – Model – View – Component Separation (MVC++) |
| **Theme**      | FlatLaf Light / IntelliJ / Dark tùy chọn          |

---

## 📁 Cấu trúc dự án

```bash
pos_app/
├── dao/              # Truy vấn và xử lý dữ liệu (Database Access)
├── icons/            # Icon SVG/PNG dùng trong giao diện
├── models/           # Lớp mô hình: Product, Invoice, Customer, ...
├── ui.components/    # UI tái sử dụng: Button, Sidebar, Header,...
├── ui.dialog/        # Dialog nhập/chỉnh sửa dữ liệu (Form)
├── ui.panel/         # Giao diện từng chức năng: Sản phẩm, Hóa đơn...
├── ui.table/         # Tùy biến bảng: CellRenderer, ButtonEditor
├── util/             # Tiện ích chung: DateHelper, IconUtil,...
├── view/             # Giao diện chính (MainFrame.java)
```

---

## 🧩 Thư viện sử dụng

| Tên Thư viện        | Mô tả                                                     |
| ------------------- | --------------------------------------------------------- |
| `FlatLaf`           | Giao diện hiện đại, hỗ trợ SVG, Dark mode, IntelliJ Theme |
| `FlatLaf Extras`    | Tuỳ chỉnh theme và icon từ SVG dễ dàng                    |
| `darklaf-core`      | (Tuỳ chọn) Hiệu ứng shadow, nền blur                      |
| `mysql-connector-j` | Kết nối MySQL (JDBC)                                      |
| `poi-ooxml`, `poi`  | Đọc/Ghi file Excel (xuất báo cáo, thống kê)               |
| `jfreechart`        | Biểu đồ Pie, Bar (tab Thống kê)                           |
| `junit`             | Unit test cho DAO và các module quan trọng                |
| `protobuf-java`     | Dự phòng: lưu dữ liệu dạng nhị phân                       |

---

## 🖥️ Tính năng chính

| Chức năng                 | Mô tả                                                                |
| ------------------------- | -------------------------------------------------------------------- |
| 📦 Quản lý sản phẩm       | CRUD sản phẩm, cập nhật số lượng, giá, tình trạng                    |
| 👤 Quản lý khách hàng     | Thêm/sửa thông tin khách, phân loại nhóm khách                       |
| 🧾 Quản lý hóa đơn        | Tạo hóa đơn, xem chi tiết, in và tìm kiếm hóa đơn                    |
| 📊 Thống kê doanh thu     | Tổng hợp bán hàng theo ngày/tháng, biểu đồ Pie/Bar (đang phát triển) |
| 🏪 Quản lý tồn kho        | Theo dõi tồn kho, nhập/xuất kho                                      |
| 🧑‍💼 Nhân viên & phân quyền | Phân quyền theo vai trò (Admin, Staff), quản lý nhân viên            |
| ⚙️ Cài đặt hệ thống       | Đổi theme, thông tin cửa hàng (placeholder)                          |

---

## ✅ Hướng dẫn chạy ứng dụng

1. **Clone dự án về máy:**

   ```bash
   git clone https://github.com/dangkhoa2004/pos_app.git
   ```

2. **Mở bằng IDE:**

   - Sử dụng **NetBeans 25** _(ưu tiên)_ hoặc **IntelliJ IDEA** (nếu đã cấu hình Maven hoặc Ant tương ứng).

3. **Cài đặt cơ sở dữ liệu MySQL:**

   - Đảm bảo bạn đã cài **MySQL Server**, ví dụ: `localhost:3306`.
   - Tạo database có tên phù hợp, ví dụ:
     ```sql
     CREATE DATABASE pos_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
     ```
   - Import file `.sql` (nếu có) từ thư mục `Database/pos_app.sql` hoặc theo hướng dẫn trong repo.

4. **Cập nhật thông tin kết nối MySQL trong `DBConnection.java`:**

   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/pos_app";
   private static final String USER = "root";
   private static final String PASSWORD = ""; // đổi lại mật khẩu phù hợp
   ```

5. **Chạy ứng dụng:**

   - Tìm và chạy file `MainFrame.java` trong thư mục `view/`.
   - Giao diện chính sẽ được khởi tạo cùng sidebar và các module chức năng.

6. ✅ **Lưu ý:**
   - Nếu xuất hiện lỗi `ClassNotFoundException`, hãy kiểm tra lại driver MySQL JDBC (`mysql-connector-j` đã thêm vào `libraries` chưa).
   - Cần dùng `UTF-8` để hiển thị tiếng Việt chính xác.
   - Một số tính năng như thống kê, phân quyền có thể đang trong quá trình phát triển.

---

## 📌 Ghi chú phát triển

- Toàn bộ icon lưu tại `src/pos_app/icons` với định dạng SVG, tự động scale đẹp.
- Để thêm tính năng mới:  
  👉 Thêm Panel mới trong `ui.panel` → Gọi từ `SideBarMenu.java`
- Các tính năng chưa hoàn thiện sẽ hiển thị `JOptionPane` thông báo
- **Thiết kế hướng mở rộng** – có thể tích hợp tính năng in hóa đơn PDF, đồng bộ cloud, v.v.

---

## 📸 Giao diện minh họa

### 🌙 Giao diện FlatLaf Dark

> ![Dark Mode Sidebar](./src/pos_app/icons/sidebar_dark_preview.png)

### ☀️ Giao diện FlatLaf Light

> ![Light Mode Main](./src/pos_app/icons/main_light_preview.png)

---

## 🔮 Kế hoạch mở rộng (Roadmap)

- [ ] Thêm chức năng **in hóa đơn PDF**
- [ ] Tích hợp **API Google Sheets** để xuất dữ liệu nhanh
- [ ] Tự động backup định kỳ
- [ ] Tạo trang Login + phân quyền mạnh mẽ
- [ ] Đồng bộ dữ liệu với Firebase (tùy chọn)

---

## 👤 Tác giả & Liên hệ

- 👨‍💻 **Tác giả:** Đăng Khoa
- 📧 **Email:** 04dkhoa04@gmail.com
- 💬 **Facebook:** [Đăng Khoa](https://www.facebook.com/dangkh0a2004)
- ☕ Bạn thấy hay? Hãy ⭐ cho repo này nhé!
