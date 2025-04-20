package pos_app.dao;

import com.google.gson.Gson;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pos_app.models.Customer;
import pos_app.models.AuditLog;
import pos_app.util.DatabaseConnection;

/**
 * Lớp CustomerDAO dùng để thao tác với bảng <b>customers</b> trong hệ thống
 * POS.
 *
 * Cung cấp các chức năng:
 * <ul>
 * <li>Lấy danh sách khách hàng</li>
 * <li>Thêm, cập nhật, xóa khách hàng (tự động ghi nhật ký thao tác
 * AuditLog)</li>
 * </ul>
 *
 * Kết nối CSDL thông qua lớp {@link DatabaseConnection}. Mặc định khi ghi log
 * sử dụng employeeId = 1 (admin hệ thống).
 *
 * @author 04dkh
 */
public class CustomerDAO {

    private final int defaultEmployeeId = 1;

    /* ===================== READ ALL ===================== */
    /**
     * Lấy toàn bộ danh sách khách hàng trong hệ thống.
     *
     * @return Danh sách các đối tượng Customer.
     */
    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer c = mapResultSet(rs);
                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /* ===================== CREATE ======================= */
    /**
     * Thêm mới một khách hàng vào hệ thống và ghi log thao tác.
     *
     * @param c Đối tượng Customer cần thêm.
     */
    public void insertCustomer(Customer c) {
        String sql = "INSERT INTO customers(name, phone, email, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getPhone());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getAddress());
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
                    "customers",
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
     * Cập nhật thông tin khách hàng theo ID và ghi log thao tác.
     *
     * @param c Đối tượng Customer chứa thông tin mới.
     */
    public void updateCustomer(Customer c) {
        String sql = "UPDATE customers SET name = ?, phone = ?, email = ?, address = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            Customer old = getCustomerById(c.getId());

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getPhone());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getAddress());
            stmt.setInt(5, c.getId());
            stmt.executeUpdate();

            Gson gson = new Gson();
            AuditLog log = new AuditLog(
                    defaultEmployeeId,
                    "UPDATE",
                    "customers",
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
     * Xóa khách hàng theo ID và ghi log thao tác.
     *
     * @param id Mã khách hàng cần xóa.
     */
    public void deleteCustomer(int id) {
        String sql = "DELETE FROM customers WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            Customer old = getCustomerById(id);

            stmt.setInt(1, id);
            stmt.executeUpdate();

            Gson gson = new Gson();
            AuditLog log = new AuditLog(
                    defaultEmployeeId,
                    "DELETE",
                    "customers",
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
     * Lấy thông tin khách hàng theo ID.
     *
     * @param id Mã khách hàng.
     * @return Đối tượng Customer nếu tồn tại, null nếu không tìm thấy.
     */
    public Customer getCustomerById(int id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Chuyển dữ liệu từ ResultSet thành đối tượng Customer.
     *
     * @param rs Dòng kết quả từ truy vấn SQL.
     * @return Đối tượng Customer tương ứng.
     * @throws SQLException nếu có lỗi trong quá trình truy xuất dữ liệu.
     */
    private Customer mapResultSet(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("address")
        );
    }
}
