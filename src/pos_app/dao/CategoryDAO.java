/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pos_app.models.Category;
import pos_app.util.DatabaseConnection;

/**
 * CategoryDAO dùng để thao tác với bảng categories trong hệ thống POS.
 *
 * Cung cấp các chức năng: - Lấy danh sách danh mục sản phẩm - Thêm, sửa, xóa
 * danh mục
 *
 * Sử dụng kết nối từ lớp DatabaseConnection.
 *
 * @author 04dkh
 */
public class CategoryDAO {

    /* ===================== READ ALL ===================== */
    /**
     * Lấy toàn bộ danh sách danh mục sản phẩm.
     *
     * @return danh sách Category
     */
    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Category c = new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
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
     * Thêm một danh mục mới vào hệ thống.
     *
     * @param c đối tượng Category cần thêm
     */
    public void insertCategory(Category c) {
        String sql = "INSERT INTO categories(name, description) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getDescription());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ===================== UPDATE ======================= */
    /**
     * Cập nhật thông tin danh mục sản phẩm.
     *
     * @param c đối tượng Category cần cập nhật
     */
    public void updateCategory(Category c) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getDescription());
            stmt.setInt(3, c.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ===================== DELETE ======================= */
    /**
     * Xóa danh mục theo ID.
     *
     * @param id mã danh mục cần xóa
     */
    public void deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
