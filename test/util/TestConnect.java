/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import pos_app.util.DatabaseConnection;

/**
 * Lớp TestConnect dùng để kiểm tra kết nối đến cơ sở dữ liệu MySQL.
 *
 * Chạy như một chương trình Java độc lập (có phương thức main).
 *
 * Mục đích: - Kiểm tra cấu hình trong {@link DatabaseConnection} - Xác nhận
 * Driver và chuỗi kết nối hoạt động bình thường
 *
 * Kết quả sẽ in ra:
 * <ul>
 * <li>✅ nếu kết nối thành công</li>
 * <li>❌ nếu kết nối thất bại</li>
 * </ul>
 *
 * Ví dụ sử dụng trong terminal:
 * <pre>
 *     java pos_app.util.TestConnect
 * </pre>
 *
 * @author 04dkh
 */
public class TestConnect {

    /**
     * Điểm vào chương trình để kiểm tra kết nối cơ sở dữ liệu.
     *
     * @param args không sử dụng
     */
    public static void main(String[] args) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            System.out.println("✅ Kết nối thành công đến CSDL!");
        } else {
            System.out.println("❌ Kết nối thất bại!");
        }
    }
}
