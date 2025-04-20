package pos_app.dao;

import com.google.gson.Gson;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pos_app.models.Category;
import pos_app.models.AuditLog;
import pos_app.util.DatabaseConnection;

/**
 * Lớp CategoryDAO dùng để thao tác với bảng <b>categories</b> trong hệ thống
 * POS.
 *
 * Cung cấp các chức năng:
 * <ul>
 * <li>Lấy danh sách danh mục sản phẩm</li>
 * <li>Thêm, cập nhật, xóa danh mục (tự động ghi nhật ký thao tác AuditLog)</li>
 * </ul>
 *
 * Kết nối cơ sở dữ liệu thông qua lớp {@link DatabaseConnection}. Khi ghi log
 * thao tác, mặc định sử dụng employeeId = 1 (admin hệ thống).
 *
 * @author 04dkh
 */
public class CategoryDAO {

    private final int defaultEmployeeId = 1;

    /* ===================== READ ALL ===================== */
    /**
     * Lấy toàn bộ danh mục sản phẩm từ bảng categories.
     *
     * @return Danh sách đối tượng Category.
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
     * Thêm mới một danh mục sản phẩm vào hệ thống và ghi nhật ký thao tác.
     *
     * @param c Đối tượng Category cần thêm.
     */
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
    /**
     * Cập nhật thông tin một danh mục theo ID và ghi nhật ký thao tác.
     *
     * @param c Đối tượng Category chứa thông tin cần cập nhật.
     */
    public void updateCategory(Category c) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            Category old = getCategoryById(c.getId());

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getDescription());
            stmt.setInt(3, c.getId());
            stmt.executeUpdate();

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
    /**
     * Xóa một danh mục sản phẩm theo ID và ghi nhật ký thao tác.
     *
     * @param id Mã danh mục cần xóa.
     */
    public void deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            Category old = getCategoryById(id);

            stmt.setInt(1, id);
            stmt.executeUpdate();

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
     * Lấy thông tin một danh mục theo ID.
     *
     * @param id Mã danh mục cần tìm.
     * @return Đối tượng Category nếu tồn tại, null nếu không tìm thấy.
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
