package pos_app.dao;

import pos_app.models.Settings;
import pos_app.util.DatabaseConnection;

import java.sql.*;

/**
 * SettingsDAO dùng để thao tác với bảng settings trong hệ thống POS.
 *
 * Cung cấp các phương thức để: - Lấy thông tin cấu hình hệ thống - Cập nhật
 * thông tin cấu hình hệ thống
 *
 * Sử dụng kết nối từ lớp DatabaseConnection để thao tác với database.
 *
 * Giả định rằng chỉ có một bản ghi cấu hình duy nhất với id = 1.
 *
 * @author 04dkh
 */
public class SettingsDAO {

    /**
     * Lấy thông tin cấu hình hệ thống từ database.
     *
     * @return đối tượng Settings nếu thành công, null nếu thất bại
     */
    public Settings getSettings() {
        String sql = "SELECT * FROM settings WHERE id = 1";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return new Settings(
                        rs.getInt("id"),
                        rs.getString("store_name"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("logo_path"),
                        rs.getDouble("tax_rate"),
                        rs.getString("currency"),
                        rs.getString("invoice_prefix"),
                        rs.getString("printer_name"),
                        rs.getString("default_language"),
                        rs.getString("backup_path"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Cập nhật thông tin cấu hình hệ thống trong database.
     *
     * @param settings đối tượng Settings chứa thông tin mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateSettings(Settings settings) {
        String sql = "UPDATE settings SET "
                + "store_name = ?, address = ?, phone = ?, email = ?, logo_path = ?, "
                + "tax_rate = ?, currency = ?, invoice_prefix = ?, printer_name = ?, "
                + "default_language = ?, backup_path = ?, updated_at = CURRENT_TIMESTAMP "
                + "WHERE id = 1";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, settings.getStoreName());
            stmt.setString(2, settings.getAddress());
            stmt.setString(3, settings.getPhone());
            stmt.setString(4, settings.getEmail());
            stmt.setString(5, settings.getLogoPath());
            stmt.setDouble(6, settings.getTaxRate());
            stmt.setString(7, settings.getCurrency());
            stmt.setString(8, settings.getInvoicePrefix());
            stmt.setString(9, settings.getPrinterName());
            stmt.setString(10, settings.getDefaultLanguage());
            stmt.setString(11, settings.getBackupPath());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
