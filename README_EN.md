# 🛒 POS_App – Sales Management Application

[![🌐 English](https://img.shields.io/badge/Language-English-blue)](./README_EN.md) [![🌐 Vietnamese](https://img.shields.io/badge/Language-Vietnamese-red)](./README.md)

A comprehensive sales management application developed with **Java Swing**, designed following the **MVC + Layered UI** model, supporting full features for managing products, invoices, customers, inventory, and staff. Modern, user-friendly interface with scalability in mind.

---

## 🔧 Technologies & Development Environment

| Component        | Details                                           |
| ---------------- | ------------------------------------------------- |
| **Language**     | Java 24                                           |
| **IDE**          | NetBeans 25 (Apache NetBeans)                     |
| **UI Toolkit**   | Java Swing + FlatLaf                              |
| **Database**     | MySQL + JDBC (mysql-connector-j)                  |
| **Architecture** | DAO – Model – View – Component Separation (MVC++) |
| **Theme**        | FlatLaf Light / IntelliJ / Dark (customizable)    |

---

## 📁 Project Structure

```bash
pos_app/
├── dao/              # Database access layer
├── icons/            # SVG/PNG icons for the interface
├── models/           # Model classes: Product, Invoice, Customer, ...
├── ui.components/    # Reusable UI components: Button, Sidebar, Header,...
├── ui.dialog/        # Dialogs for data input/editing (Forms)
├── ui.panel/         # Functional UIs: Products, Invoices, etc.
├── ui.table/         # Custom tables: CellRenderer, ButtonEditor
├── util/             # Utilities: DateHelper, IconUtil,...
├── view/             # Main interface (MainFrame.java)
```

---

## 🧩 Libraries Used

| Library Name        | Description                                                 |
| ------------------- | ----------------------------------------------------------- |
| `FlatLaf`           | Modern UI with SVG support, dark mode, IntelliJ-like themes |
| `FlatLaf Extras`    | Easy customization of theme and SVG icons                   |
| `darklaf-core`      | (Optional) Shadow and blur effects                          |
| `mysql-connector-j` | JDBC driver for MySQL connection                            |
| `poi-ooxml`, `poi`  | Read/write Excel files (for reports and statistics)         |
| `jfreechart`        | Pie and Bar charts (used in Statistics tab)                 |
| `junit`             | Unit testing for DAOs and critical modules                  |
| `protobuf-java`     | Backup option: binary data storage                          |

---

## 🖥️ Main Features

| Feature                    | Description                                            |
| -------------------------- | ------------------------------------------------------ |
| 📦 Product Management      | CRUD for products, update stock, price, status         |
| 👤 Customer Management     | Add/edit customer info, segment customers              |
| 🧾 Invoice Management      | Create, view details, print, and search invoices       |
| 📊 Sales Statistics        | Summary by date/month, Pie/Bar charts (in development) |
| 🏪 Inventory Management    | Track stock, handle warehouse input/output             |
| 🧑‍💼 Staff & Role Management | Role-based access (Admin, Staff), staff management     |
| ⚙️ System Settings         | Change theme, store info (placeholder)                 |

---

## ✅ How to Run

1. **Clone the repository:**

   ```bash
   git clone https://github.com/dangkhoa2004/pos_app.git
   ```

2. **Open in your IDE:**

   - Recommended: **NetBeans 25**
   - Or use **IntelliJ IDEA** (with proper Maven/Ant setup).

3. **Set up MySQL database:**

   - Make sure **MySQL Server** is running (e.g., `localhost:3306`).
   - Create a database:
     ```sql
     CREATE DATABASE pos_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
     ```
   - Import the `.sql` file from `Database/pos_app.sql` if available.

4. **Update connection info in `DBConnection.java`:**

   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/pos_app";
   private static final String USER = "root";
   private static final String PASSWORD = ""; // update your password
   ```

5. **Run the application:**

   - Run `MainFrame.java` located in the `view/` folder.
   - The main UI with sidebar and modules will launch.

6. ✅ **Notes:**
   - If you encounter `ClassNotFoundException`, ensure the MySQL JDBC driver (`mysql-connector-j`) is added to `libraries`.
   - Use `UTF-8` encoding to support Vietnamese correctly.
   - Some features like statistics and permission settings may still be under development.

---

## 📌 Development Notes

- All icons are stored in `src/pos_app/icons` as SVG, auto-scaled beautifully.
- To add new features:  
  👉 Create a new Panel under `ui.panel` → Register it in `SideBarMenu.java`.
- Incomplete features will display a `JOptionPane` notification.
- **Designed for extensibility** – e.g., add PDF invoice printing, cloud sync, etc.

---

## 📸 Interface Previews

### 🌙 FlatLaf Dark Theme

> ![Dark Mode Sidebar](./src/pos_app/icons/sidebar_dark_preview.png)

### ☀️ FlatLaf Light Theme

> ![Light Mode Main](./src/pos_app/icons/main_light_preview.png)

---

## 🔮 Roadmap

- [ ] Add **PDF invoice printing**
- [ ] Integrate **Google Sheets API** for quick exports
- [ ] Enable periodic automatic backups
- [ ] Build login screen with robust access control
- [ ] Sync data with Firebase (optional)

---

## 👤 Author & Contact

- 👨‍💻 **Author:** Đăng Khoa
- 📧 **Email:** 04dkhoa04@gmail.com
- 💬 **Facebook:** [Đăng Khoa](https://www.facebook.com/dangkh0a2004)
- ☕ Enjoyed it? Leave a ⭐ on the repo!
