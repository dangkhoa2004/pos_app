package pos_app.dao;

import com.google.gson.Gson;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pos_app.models.Category;
import pos_app.models.AuditLog;
import pos_app.util.DatabaseConnection;

/**
 * CategoryDAO dùng để thao tác với bảng categories trong hệ thống POS.
 *
 * Cung cấp các chức năng: - Lấy danh sách danh mục sản phẩm - Thêm, sửa, xóa
 * danh mục (có ghi nhật ký thao tác)
 *
 * Sử dụng kết nối từ lớp DatabaseConnection. Sử dụng employeeId mặc định là 1
 * (Admin)
 *
 * @author 04dkh
 */
public class CategoryDAO {

    private final int defaultEmployeeId = 1;

    /* ===================== READ ALL ===================== */
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
    public void insertCategory(Category c) {
        String sql = "INSERT INTO categories(name, description) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getDescription());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setId(rs.getInt(1));
                }
            }

            // Ghi audit log
            Gson gson = new Gson();
            AuditLog log = new AuditLog(
                    defaultEmployeeId,
                    "INSERT",
                    "categories",
                    c.getId(),
                    null,
                    gson.toJson(c)
            );
            new AuditLogDAO(conn).insertAuditLog(log);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ===================== UPDATE ======================= */
    public void updateCategory(Category c) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Lấy dữ liệu cũ
            Category old = getCategoryById(c.getId());

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getDescription());
            stmt.setInt(3, c.getId());
            stmt.executeUpdate();

            // Ghi audit log
            Gson gson = new Gson();
            AuditLog log = new AuditLog(
                    defaultEmployeeId,
                    "UPDATE",
                    "categories",
                    c.getId(),
                    gson.toJson(old),
                    gson.toJson(c)
            );
            new AuditLogDAO(conn).insertAuditLog(log);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ===================== DELETE ======================= */
    public void deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Lấy dữ liệu cũ
            Category old = getCategoryById(id);

            stmt.setInt(1, id);
            stmt.executeUpdate();

            // Ghi audit log
            Gson gson = new Gson();
            AuditLog log = new AuditLog(
                    defaultEmployeeId,
                    "DELETE",
                    "categories",
                    id,
                    gson.toJson(old),
                    null
            );
            new AuditLogDAO(conn).insertAuditLog(log);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ===================== SUPPORT ======================= */
    /**
     * Lấy thông tin danh mục theo ID.
     *
     * @param id mã danh mục
     * @return đối tượng Category nếu tồn tại
     */
    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM categories WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
