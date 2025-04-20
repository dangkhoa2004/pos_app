/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pos_app.models.Customer;
import pos_app.util.DatabaseConnection;

/**
 * CustomerDAO dùng để thao tác với bảng customers trong hệ thống POS.
 *
 * Cung cấp các chức năng: - Lấy danh sách khách hàng - Thêm, sửa, xóa khách
 * hàng
 *
 * Sử dụng kết nối từ lớp DatabaseConnection.
 *
 * @author 04dkh
 */
public class CustomerDAO {

    /* ===================== READ ALL ===================== */
    /**
     * Lấy danh sách toàn bộ khách hàng trong hệ thống.
     *
     * @return danh sách Customer
     */
    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer c = new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address")
                );
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ===================== CREATE ======================= */
    /**
     * Thêm mới một khách hàng vào hệ thống.
     *
     * @param c đối tượng Customer cần thêm
     */
    public void insertCustomer(Customer c) {
        String sql = "INSERT INTO customers(name, phone, email, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getPhone());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getAddress());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ===================== UPDATE ======================= */
    /**
     * Cập nhật thông tin khách hàng theo ID.
     *
     * @param c đối tượng Customer cần cập nhật
     */
    public void updateCustomer(Customer c) {
        String sql = "UPDATE customers SET name = ?, phone = ?, email = ?, address = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getPhone());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getAddress());
            stmt.setInt(5, c.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ===================== DELETE ======================= */
    /**
     * Xóa khách hàng theo ID.
     *
     * @param id mã khách hàng cần xóa
     */
    public void deleteCustomer(int id) {
        String sql = "DELETE FROM customers WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
