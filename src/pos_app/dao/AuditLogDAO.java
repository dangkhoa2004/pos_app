/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.dao;

import pos_app.models.AuditLog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * AuditLogDAO dùng để thao tác với bảng audit_logs trong cơ sở dữ liệu. Ghi lại
 * các hành động của người dùng như thêm, sửa, xóa, đăng nhập, đăng xuất, v.v.
 *
 * @author 04dkh
 */
public class AuditLogDAO {

    private Connection conn;

    /**
     * Khởi tạo DAO với kết nối cơ sở dữ liệu.
     *
     * @param conn Đối tượng Connection đang kết nối đến cơ sở dữ liệu.
     */
    public AuditLogDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Ghi một bản ghi vào bảng audit_logs để lưu thông tin thao tác của người
     * dùng.
     *
     * @param log Đối tượng AuditLog chứa thông tin thao tác cần ghi.
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
}
