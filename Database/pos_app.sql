
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

-- Bảng sản phẩm
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    price DECIMAL(10,2),
    quantity INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Bảng khách hàng
CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT
);

-- Bảng hóa đơn
CREATE TABLE invoices (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    employee_id INT,
    total DECIMAL(12,2),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (employee_id) REFERENCES employees(id)
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

-- Bảng cấu hình hệ thống
CREATE TABLE settings (
    id INT PRIMARY KEY,
    store_name VARCHAR(100),
    address TEXT,
    phone VARCHAR(20),
    email VARCHAR(100)
);

-- Dữ liệu mẫu
INSERT INTO roles(name) VALUES ('Admin'), ('Nhân viên');

INSERT INTO employees(name, username, password, role_id) 
VALUES ('Quản trị viên', 'admin', 'admin123', 1);

INSERT INTO settings(id, store_name, address, phone, email) 
VALUES (1, 'Cửa hàng ABC', '123 Đường A, TP.B', '0909000900', 'info@abcshop.vn');

-- Sản phẩm
INSERT INTO products(name, price, quantity)
VALUES 
  ('Cà phê đen', 25000, 100),
  ('Trà sữa thái xanh', 30000, 80),
  ('Sinh tố bơ', 35000, 50);

-- Khách hàng
INSERT INTO customers(name, phone, email, address)
VALUES
  ('Nguyễn Văn A', '0901234567', 'a@gmail.com', '12 Nguyễn Trãi, Q1'),
  ('Trần Thị B', '0907654321', 'b@gmail.com', '56 Lê Lợi, Q3');

-- Hóa đơn
INSERT INTO invoices(customer_id, employee_id, total)
VALUES (1, 1, 55000);

-- Chi tiết hóa đơn
INSERT INTO invoice_items(invoice_id, product_id, quantity, unit_price)
VALUES 
  (1, 1, 1, 25000),
  (1, 2, 1, 30000);

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
