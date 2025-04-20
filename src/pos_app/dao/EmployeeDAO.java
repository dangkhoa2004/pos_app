package pos_app.dao;

import com.google.gson.Gson;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pos_app.models.Employee;
import pos_app.models.AuditLog;
import pos_app.util.DatabaseConnection;

/**
 * EmployeeDAO dùng để thao tác với bảng employees trong hệ thống POS.
 *
 * Cung cấp các chức năng: - Lấy danh sách nhân viên - Thêm, sửa, xóa nhân viên
 * (có ghi AuditLog) - Kiểm tra đăng nhập
 *
 * Sử dụng kết nối từ lớp DatabaseConnection. Mặc định AuditLog sử dụng
 * employeeId = 1 (admin)
 *
 * @author 04dkh
 */
public class EmployeeDAO {

    private final int defaultEmployeeId = 1;

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
                Employee e = mapResultSet(rs);
                list.add(e);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /* ===================== CREATE ======================= */
    /**
     * Thêm một nhân viên mới vào hệ thống và ghi nhật ký thao tác.
     *
     * @param e đối tượng Employee cần thêm
     */
    public void insertEmployee(Employee e) {
        String sql = "INSERT INTO employees (name, username, password, role_id, phone, email) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, e.getName());
            stmt.setString(2, e.getUsername());
            stmt.setString(3, e.getPassword());
            stmt.setInt(4, e.getRole_id());
            stmt.setString(5, e.getPhone());
            stmt.setString(6, e.getEmail());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    e.setId(rs.getInt(1));
                }
            }

            // Ghi nhật ký thao tác
            Gson gson = new Gson();
            AuditLog log = new AuditLog(
                    defaultEmployeeId,
                    "INSERT",
                    "employees",
                    e.getId(),
                    null,
                    gson.toJson(e)
            );
            new AuditLogDAO(conn).insertAuditLog(log);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* ===================== UPDATE ======================= */
    /**
     * Cập nhật thông tin nhân viên theo ID và ghi nhật ký thao tác.
     *
     * @param e đối tượng Employee cần cập nhật
     */
    public void updateEmployee(Employee e) {
        String sql = "UPDATE employees SET name = ?, username = ?, password = ?, role_id = ?, phone = ?, email = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Lấy dữ liệu cũ
            Employee old = getEmployeeById(e.getId());

            stmt.setString(1, e.getName());
            stmt.setString(2, e.getUsername());
            stmt.setString(3, e.getPassword());
            stmt.setInt(4, e.getRole_id());
            stmt.setString(5, e.getPhone());
            stmt.setString(6, e.getEmail());
            stmt.setInt(7, e.getId());
            stmt.executeUpdate();

            // Ghi nhật ký thao tác
            Gson gson = new Gson();
            AuditLog log = new AuditLog(
                    defaultEmployeeId,
                    "UPDATE",
                    "employees",
                    e.getId(),
                    gson.toJson(old),
                    gson.toJson(e)
            );
            new AuditLogDAO(conn).insertAuditLog(log);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* ===================== DELETE ======================= */
    /**
     * Xóa nhân viên theo ID và ghi nhật ký thao tác.
     *
     * @param id mã nhân viên cần xóa
     */
    public void deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Lấy dữ liệu cũ
            Employee old = getEmployeeById(id);

            stmt.setInt(1, id);
            stmt.executeUpdate();

            // Ghi nhật ký thao tác
            Gson gson = new Gson();
            AuditLog log = new AuditLog(
                    defaultEmployeeId,
                    "DELETE",
                    "employees",
                    id,
                    gson.toJson(old),
                    null
            );
            new AuditLogDAO(conn).insertAuditLog(log);

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

    /* ===================== SUPPORT ====================== */
    /**
     * Tìm nhân viên theo ID.
     *
     * @param id mã nhân viên
     * @return đối tượng Employee nếu có, null nếu không tìm thấy
     */
    public Employee getEmployeeById(int id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Mapping ResultSet sang đối tượng Employee.
     *
     * @param rs ResultSet từ SQL
     * @return đối tượng Employee
     * @throws SQLException nếu lỗi xảy ra
     */
    private Employee mapResultSet(ResultSet rs) throws SQLException {
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
}
