package pos_app.util;

import java.sql.*;

public class SQLSeeder {

    public static void run(String sqlFilePath) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            dropAllTables(conn); // 💣 Xoá hết bảng trước
            runSQLFromFile(conn, sqlFilePath);
            System.out.println("✅ Đã chạy file SQL seed thành công!");
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi chạy file SQL seed:");
            e.printStackTrace();
        }
    }

    private static void dropAllTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW TABLES");

        java.util.List<String> tables = new java.util.ArrayList<>();
        while (rs.next()) {
            tables.add(rs.getString(1)); // Lưu tên bảng lại trước
        }
        rs.close(); // Đóng ResultSet trước khi dùng lại stmt

        stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
        for (String table : tables) {
            stmt.executeUpdate("DROP TABLE IF EXISTS `" + table + "`");
        }
        stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
        System.out.println("🗑️ Đã xoá toàn bộ bảng");
        stmt.close();
    }

    private static void runSQLFromFile(Connection conn, String sqlFilePath) throws Exception {
        try (Statement stmt = conn.createStatement(); java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(sqlFilePath))) {

            StringBuilder sqlBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("--") || line.isEmpty()) {
                    continue;
                }
                sqlBuilder.append(line);
                if (line.endsWith(";")) {
                    String sql = sqlBuilder.toString();
                    sql = sql.substring(0, sql.length() - 1); // bỏ dấu ;
                    stmt.execute(sql);
                    sqlBuilder.setLength(0);
                }
            }
        }
    }
}
