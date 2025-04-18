-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS pos_app;
USE pos_app;

-- Bảng chức vụ
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Bảng nhân viên
CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    username VARCHAR(50) UNIQUE,
    password VARCHAR(255),
    role_id INT,
    phone VARCHAR(20),
    email VARCHAR(100),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Bảng phân loại sản phẩm
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng sản phẩm
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    price DECIMAL(10,2),
    quantity INT,
    image_path VARCHAR(255),
    category_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Bảng khách hàng
CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT
);

-- Bảng bàn (tạo TRƯỚC invoices để tránh lỗi foreign key)
CREATE TABLE tables (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    status ENUM('available', 'occupied', 'reserved'),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng khuyến mãi (tạo TRƯỚC invoices để tránh lỗi foreign key)
CREATE TABLE discounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50),
    description TEXT,
    discount_type ENUM('percent', 'amount'),
    value DECIMAL(10,2),
    start_date DATETIME,
    end_date DATETIME,
    active BOOLEAN DEFAULT TRUE
);

-- Bảng hóa đơn
CREATE TABLE invoices (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    employee_id INT,
    table_id INT,
    discount_id INT,
    total DECIMAL(12,2),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    FOREIGN KEY (table_id) REFERENCES tables(id),
    FOREIGN KEY (discount_id) REFERENCES discounts(id)
);

-- Bảng chi tiết hóa đơn
CREATE TABLE invoice_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT,
    product_id INT,
    quantity INT,
    unit_price DECIMAL(10,2),
    FOREIGN KEY (invoice_id) REFERENCES invoices(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Bảng nhập kho
CREATE TABLE stock_in (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    quantity INT,
    note TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Bảng xuất kho
CREATE TABLE stock_out (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    quantity INT,
    note TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Bảng thanh toán
CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT,
    method ENUM('cash', 'card', 'momo', 'zalo', 'vnpay'),
    amount DECIMAL(10,2),
    paid_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    note TEXT,
    FOREIGN KEY (invoice_id) REFERENCES invoices(id)
);

-- Bảng cấu hình hệ thống
CREATE TABLE settings (
    id INT PRIMARY KEY,
    store_name VARCHAR(100),
    address TEXT,
    phone VARCHAR(20),
    email VARCHAR(100)
);

-- -------------------------
-- Dữ liệu mẫu ban đầu
-- -------------------------

-- Vai trò
INSERT INTO roles(name) VALUES ('Admin'), ('Nhân viên');

-- Nhân viên
INSERT INTO employees(name, username, password, role_id) 
VALUES ('Quản trị viên', 'admin', 'admin123', 1);

-- Cài đặt hệ thống
INSERT INTO settings(id, store_name, address, phone, email) 
VALUES (1, 'Cửa hàng ABC', '123 Đường A, TP.B', '0909000900', 'info@abcshop.vn');

-- Phân loại sản phẩm
INSERT INTO categories(name, description)
VALUES 
  ('Trà sữa', 'Các loại trà sữa truyền thống'),
  ('Đồ uống khác', 'Cà phê, sinh tố, nước trái cây');

-- Sản phẩm
INSERT INTO products(name, price, quantity, category_id)
VALUES 
  ('Cà phê đen', 25000, 100, 2),
  ('Trà sữa thái xanh', 30000, 80, 1),
  ('Sinh tố bơ', 35000, 50, 2);

-- Khách hàng
INSERT INTO customers(name, phone, email, address)
VALUES
  ('Nguyễn Văn A', '0901234567', 'a@gmail.com', '12 Nguyễn Trãi, Q1'),
  ('Trần Thị B', '0907654321', 'b@gmail.com', '56 Lê Lợi, Q3');

-- Thêm bàn
INSERT INTO tables(name, status) 
VALUES ('Bàn 1', 'available'), ('Bàn 2', 'occupied');

-- Thêm khuyến mãi
INSERT INTO discounts(code, description, discount_type, value, start_date, end_date)
VALUES ('SALE10', 'Giảm 10% toàn bộ hóa đơn', 'percent', 10, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY));

-- Tạo hóa đơn
INSERT INTO invoices(customer_id, employee_id, table_id, discount_id, total)
VALUES (1, 1, 1, 1, 55000);

-- Chi tiết hóa đơn
INSERT INTO invoice_items(invoice_id, product_id, quantity, unit_price)
VALUES 
  (1, 1, 1, 25000),
  (1, 2, 1, 30000);

-- Giao dịch thanh toán
INSERT INTO payments(invoice_id, method, amount)
VALUES 
  (1, 'cash', 55000);

-- Nhập kho
INSERT INTO stock_in(product_id, quantity, note)
VALUES 
  (1, 50, 'Nhập lô mới tháng 4'),
  (2, 30, 'Nhập thêm do sắp hết');

-- Xuất kho
INSERT INTO stock_out(product_id, quantity, note)
VALUES 
  (1, 5, 'Xuất cho chi nhánh 2'),
  (3, 2, 'Xuất nhầm cần kiểm tra');
