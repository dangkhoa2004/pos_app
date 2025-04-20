package pos_app.dao;

import pos_app.models.AuditLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp AuditLogDAO dùng để thao tác với bảng <b>audit_logs</b> trong cơ sở dữ
 * liệu.
 *
 * Bảng này ghi lại toàn bộ hành động của người dùng trong hệ thống như: thêm,
 * sửa, xóa dữ liệu; đăng nhập, đăng xuất, và các thao tác khác liên quan đến
 * bảng dữ liệu chính.
 *
 * Các hành động sẽ được hiển thị trong giao diện "TerminalPanel" hoặc xuất ra
 * file log.
 */
public class AuditLogDAO {

    private final Connection conn;

    /**
     * Khởi tạo đối tượng DAO với kết nối cơ sở dữ liệu.
     *
     * @param conn Đối tượng Connection đang mở.
     */
    public AuditLogDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Ghi một bản ghi log vào bảng <b>audit_logs</b>, lưu lại thông tin thao
     * tác của người dùng.
     *
     * @param log Đối tượng AuditLog chứa đầy đủ thông tin hành động cần ghi.
     * @return true nếu ghi thành công, false nếu có lỗi xảy ra.
     */
    public boolean insertAuditLog(AuditLog log) {
        String sql = "INSERT INTO audit_logs (employee_id, action_type, table_name, record_id, old_data, new_data) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, log.getEmployeeId());
            ps.setString(2, log.getActionType());
            ps.setString(3, log.getTableName());
            ps.setInt(4, log.getRecordId());
            ps.setString(5, log.getOldData());
            ps.setString(6, log.getNewData());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi ghi Audit Log: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy danh sách toàn bộ bản ghi từ bảng <b>audit_logs</b>, sắp xếp theo
     * thời gian giảm dần (mới nhất trước).
     *
     * @return Danh sách các đối tượng AuditLog đã ghi nhận.
     */
    public List<AuditLog> getAllAuditLogs() {
        List<AuditLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM audit_logs ORDER BY id DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AuditLog log = new AuditLog();
                log.setId(rs.getInt("id"));
                log.setEmployeeId(rs.getInt("employee_id"));
                log.setActionType(rs.getString("action_type"));
                log.setTableName(rs.getString("table_name"));
                log.setRecordId(rs.getInt("record_id"));
                log.setOldData(rs.getString("old_data"));
                log.setNewData(rs.getString("new_data"));

                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    log.setCreatedAt(createdAt.toLocalDateTime());
                }

                logs.add(log);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách Audit Log: " + e.getMessage());
        }

        return logs;
    }
}
