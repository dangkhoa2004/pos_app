/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.util;

/**
 *
 * @author 04dkh
 */
import java.sql.Connection;

public class TestConnect {

    public static void main(String[] args) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            System.out.println("✅ Kết nối thành công đến CSDL!");
        } else {
            System.out.println("❌ Kết nối thất bại!");
        }
    }
}
