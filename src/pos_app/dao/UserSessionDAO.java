package pos_app.dao;

import java.sql.*;
import java.util.*;
import pos_app.models.UserSession;
import pos_app.util.DatabaseConnection;

/**
 * UserSessionDAO dùng để quản lý phiên đăng nhập người dùng trong hệ thống POS.
 *
 * Cung cấp các phương thức để: - Ghi nhận phiên đăng nhập mới - Đánh dấu phiên
 * đã đăng xuất - Lấy danh sách phiên đang hoạt động - Kiểm tra phiên gần nhất
 * còn hiệu lực
 *
 * Sử dụng kết nối từ lớp DatabaseConnection để thao tác với database.
 */
public class UserSessionDAO {

    public boolean createSession(UserSession session) throws SQLException {
        String sql = "INSERT INTO user_sessions (employee_id, session_token, ip_address, device_info) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, session.getEmployeeId());
            ps.setString(2, session.getSessionToken());
            ps.setString(3, session.getIpAddress());
            ps.setString(4, session.getDeviceInfo());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean closeSession(String token) throws SQLException {
        String sql = "UPDATE user_sessions SET logout_time = NOW() WHERE session_token = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            return ps.executeUpdate() > 0;
        }
    }

    public List<UserSession> getActiveSessions() throws SQLException {
        List<UserSession> list = new ArrayList<>();
        String sql = "SELECT * FROM user_sessions WHERE logout_time IS NULL";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                UserSession s = new UserSession();
                s.setId(rs.getInt("id"));
                s.setEmployeeId(rs.getInt("employee_id"));
                s.setSessionToken(rs.getString("session_token"));
                s.setLoginTime(rs.getTimestamp("login_time"));
                s.setLogoutTime(rs.getTimestamp("logout_time"));
                s.setIpAddress(rs.getString("ip_address"));
                s.setDeviceInfo(rs.getString("device_info"));
                list.add(s);
            }
        }

        return list;
    }

    /**
     * Lấy phiên làm việc gần nhất chưa logout của nhân viên.
     */
    public UserSession getLastActiveSession(int employeeId) {
        String sql = "SELECT * FROM user_sessions WHERE employee_id = ? AND logout_time IS NULL ORDER BY login_time DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserSession session = new UserSession();
                    session.setId(rs.getInt("id"));
                    session.setEmployeeId(rs.getInt("employee_id"));
                    session.setSessionToken(rs.getString("session_token"));
                    session.setLoginTime(rs.getTimestamp("login_time"));
                    session.setLogoutTime(rs.getTimestamp("logout_time"));
                    session.setIpAddress(rs.getString("ip_address"));
                    session.setDeviceInfo(rs.getString("device_info"));
                    return session;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mở phiên làm việc mới và ghi vào database.
     */
    public void openSession(int employeeId, String sessionToken, String ip, String deviceInfo) {
        String sql = "INSERT INTO user_sessions (employee_id, session_token, login_time, ip_address, device_info) VALUES (?, ?, NOW(), ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ps.setString(2, sessionToken);
            ps.setString(3, ip);
            ps.setString(4, deviceInfo);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
