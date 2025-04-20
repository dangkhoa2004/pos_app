/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection cung cấp phương thức kết nối tới cơ sở dữ liệu MySQL.
 *
 * Dùng trong toàn hệ thống POS để lấy đối tượng {@link Connection} cho truy vấn
 * SQL.
 *
 * Cấu hình kết nối (host, user, password) được cấu hình sẵn cho XAMPP và MySQL
 * 8 trở lên.
 *
 * Đảm bảo driver MySQL đã được thêm vào classpath
 * (mysql-connector-j-x.x.xx.jar).
 *
 * Ví dụ sử dụng:
 * <pre>
 *     try (Connection conn = DatabaseConnection.getConnection()) {
 *         // Thực hiện truy vấn
 *     }
 * </pre>
 *
 * @author 04dkh
 */
public class DatabaseConnection {

    /**
     * URL kết nối đến CSDL MySQL
     */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pos_app";

    /**
     * Tên đăng nhập MySQL (thường là root với XAMPP)
     */
    private static final String DB_USER = "root";

    /**
     * Mật khẩu MySQL (thường để trống nếu chưa đặt)
     */
    private static final String DB_PASSWORD = "";

    /**
     * Tạo và trả về kết nối đến cơ sở dữ liệu.
     *
     * @return đối tượng {@link Connection} nếu kết nối thành công, null nếu lỗi
     */
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Driver cho MySQL 8+
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); // In lỗi nếu có
            return null;
        }
    }
}
