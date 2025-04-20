package pos_app.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pos_app.models.Employee;
import pos_app.models.AuditLog;
import pos_app.util.DatabaseConnection;

/**
 * Lớp EmployeeDAO dùng để thao tác với bảng <b>employees</b> trong hệ thống
 * POS.
 *
 * Cung cấp các chức năng:
 * <ul>
 * <li>Lấy danh sách nhân viên</li>
 * <li>Thêm, sửa, xóa nhân viên (có ghi log thao tác qua AuditLog)</li>
 * <li>Kiểm tra đăng nhập nhân viên</li>
 * </ul>
 *
 * Giao tiếp cơ sở dữ liệu thông qua {@link DatabaseConnection}. Mặc định khi
 * ghi log sử dụng employeeId = 1 (admin hệ thống).
 *
 * @author 04dkh
 */
public class EmployeeDAO {

    private final int defaultEmployeeId = 1;

    /* ===================== READ ALL ===================== */
    /**
     * Lấy toàn bộ danh sách nhân viên từ bảng employees.
     *
     * @return Danh sách đối tượng Employee.
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
     * Thêm một nhân viên mới vào hệ thống và tự động ghi lại nhật ký thao tác
     * (AuditLog).
     *
     * @param e Đối tượng Employee cần thêm.
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
     * Cập nhật thông tin nhân viên theo ID và ghi nhật ký thao tác (so sánh dữ
     * liệu trước/sau).
     *
     * @param e Đối tượng Employee cần cập nhật.
     */
    public void updateEmployee(Employee e) {
        String sql = "UPDATE employees SET name = ?, username = ?, password = ?, role_id = ?, phone = ?, email = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            Employee old = getEmployeeById(e.getId());

            stmt.setString(1, e.getName());
            stmt.setString(2, e.getUsername());
            stmt.setString(3, e.getPassword());
            stmt.setInt(4, e.getRole_id());
            stmt.setString(5, e.getPhone());
            stmt.setString(6, e.getEmail());
            stmt.setInt(7, e.getId());
            stmt.executeUpdate();

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
     * Xóa nhân viên theo ID và ghi lại thông tin xóa vào nhật ký thao tác.
     *
     * @param id Mã nhân viên cần xóa.
     */
    public void deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            Employee old = getEmployeeById(id);

            stmt.setInt(1, id);
            stmt.executeUpdate();

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
     * Kiểm tra đăng nhập nhân viên bằng tài khoản và mật khẩu.
     *
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return Đối tượng Employee nếu đăng nhập đúng, null nếu sai
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
     * Tìm thông tin nhân viên theo ID.
     *
     * @param id Mã nhân viên cần tìm.
     * @return Đối tượng Employee nếu tìm thấy, null nếu không.
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
     * Chuyển đổi từ một dòng dữ liệu trong ResultSet thành đối tượng Employee.
     *
     * @param rs Đối tượng ResultSet từ truy vấn SQL
     * @return Đối tượng Employee tương ứng
     * @throws SQLException nếu có lỗi xảy ra khi truy cập dữ liệu
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
