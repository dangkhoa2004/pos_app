/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.util;

import java.sql.*;

/**
 * SQLSeeder dùng để khởi tạo lại cơ sở dữ liệu từ file SQL.
 *
 * Tính năng: - Xoá toàn bộ bảng hiện có (DROP TABLE IF EXISTS) - Đọc và chạy
 * lệnh SQL từ file `.sql` dòng theo dòng
 *
 * Dùng cho mục đích phát triển, testing hoặc khởi tạo hệ thống POS từ đầu.
 *
 * Ví dụ sử dụng:
 * <pre>
 *     SQLSeeder.run("src/resources/db/seed.sql");
 * </pre>
 *
 * ⚠️ CẢNH BÁO: Phương thức này sẽ xoá toàn bộ dữ liệu hiện có trong database.
 *
 * @author 04dkh
 */
public class SQLSeeder {

    /**
     * Chạy seeder từ file SQL chỉ định. Tự động xoá hết bảng hiện có và tạo mới
     * theo nội dung file.
     *
     * @param sqlFilePath đường dẫn đến file SQL
     */
    public static void run(String sqlFilePath) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            dropAllTables(conn); // 💣 Xoá toàn bộ bảng hiện tại
            runSQLFromFile(conn, sqlFilePath);
            System.out.println("✅ Đã chạy file SQL seed thành công!");
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi chạy file SQL seed:");
            e.printStackTrace();
        }
    }

    /**
     * Xoá toàn bộ bảng trong cơ sở dữ liệu hiện tại.
     *
     * @param conn kết nối SQL đang sử dụng
     * @throws SQLException nếu có lỗi xảy ra khi thực thi câu lệnh SQL
     */
    private static void dropAllTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW TABLES");

        java.util.List<String> tables = new java.util.ArrayList<>();
        while (rs.next()) {
            tables.add(rs.getString(1));
        }
        rs.close(); // đóng ResultSet trước khi tiếp tục

        stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
        for (String table : tables) {
            stmt.executeUpdate("DROP TABLE IF EXISTS `" + table + "`");
        }
        stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
        stmt.close();

        System.out.println("🗑️ Đã xoá toàn bộ bảng");
    }

    /**
     * Đọc nội dung từ file SQL và thực thi từng lệnh (các dòng kết thúc bằng
     * ;).
     *
     * @param conn kết nối SQL đang dùng
     * @param sqlFilePath đường dẫn đến file chứa lệnh SQL
     * @throws Exception nếu có lỗi khi đọc hoặc chạy câu lệnh
     */
    private static void runSQLFromFile(Connection conn, String sqlFilePath) throws Exception {
        try (
                Statement stmt = conn.createStatement(); java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(sqlFilePath))) {
            StringBuilder sqlBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("--") || line.isEmpty()) {
                    continue; // bỏ qua dòng comment và dòng trống
                }
                sqlBuilder.append(line);
                if (line.endsWith(";")) {
                    String sql = sqlBuilder.toString();
                    sql = sql.substring(0, sql.length() - 1); // xoá dấu ;
                    stmt.execute(sql);
                    sqlBuilder.setLength(0);
                }
            }
        }
    }
}
