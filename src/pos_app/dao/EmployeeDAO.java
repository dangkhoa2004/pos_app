/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pos_app.models.Employee;
import pos_app.util.DatabaseConnection;

/**
 * EmployeeDAO dùng để thao tác với bảng employees trong hệ thống POS.
 *
 * Cung cấp các chức năng: - Lấy danh sách nhân viên - Thêm, sửa, xóa nhân viên
 * - Kiểm tra đăng nhập
 *
 * Sử dụng kết nối từ lớp DatabaseConnection.
 *
 * @author 04dkh
 */
public class EmployeeDAO {

    /* ===================== READ ALL ===================== */
    /**
     * Lấy toàn bộ danh sách nhân viên từ bảng employees.
     *
     * @return danh sách Employee
     */
    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employee e = new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("role_id"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
                list.add(e);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /* ===================== CREATE ======================= */
    /**
     * Thêm một nhân viên mới vào hệ thống.
     *
     * @param e đối tượng Employee cần thêm
     */
    public void insertEmployee(Employee e) {
        String sql = "INSERT INTO employees (name, username, password, role_id, phone, email) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, e.getName());
            stmt.setString(2, e.getUsername());
            stmt.setString(3, e.getPassword());
            stmt.setInt(4, e.getRole_id());
            stmt.setString(5, e.getPhone());
            stmt.setString(6, e.getEmail());
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* ===================== UPDATE ======================= */
    /**
     * Cập nhật thông tin nhân viên theo ID.
     *
     * @param e đối tượng Employee cần cập nhật
     */
    public void updateEmployee(Employee e) {
        String sql = "UPDATE employees SET name = ?, username = ?, password = ?, role_id = ?, phone = ?, email = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, e.getName());
            stmt.setString(2, e.getUsername());
            stmt.setString(3, e.getPassword());
            stmt.setInt(4, e.getRole_id());
            stmt.setString(5, e.getPhone());
            stmt.setString(6, e.getEmail());
            stmt.setInt(7, e.getId());
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* ===================== DELETE ======================= */
    /**
     * Xóa nhân viên theo ID.
     *
     * @param id mã nhân viên cần xóa
     */
    public void deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* ===================== AUTH ========================= */
    /**
     * Kiểm tra đăng nhập nhân viên bằng username và password.
     *
     * @param username tên đăng nhập
     * @param password mật khẩu
     * @return đối tượng Employee nếu đăng nhập đúng, null nếu sai
     */
    public static Employee checkLogin(String username, String password) {
        String sql = "SELECT * FROM employees WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("role_id"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
