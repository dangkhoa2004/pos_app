package pos_app.dao;

import com.google.gson.Gson;
import java.sql.*;
import java.util.*;
import pos_app.models.*;
import pos_app.util.DatabaseConnection;

/**
 * StockDAO dùng để thao tác dữ liệu nhập kho (StockIn) và xuất kho (StockOut)
 * trong hệ thống POS.
 *
 * Cung cấp các phương thức để: - Lấy danh sách phiếu nhập/xuất kho - Thêm phiếu
 * nhập/xuất kho mới - Xóa phiếu nhập/xuất kho - Ghi lịch sử thao tác vào
 * audit_logs
 *
 * Sử dụng kết nối từ lớp DatabaseConnection để thao tác với database. Mặc định
 * employeeId = 1 (Admin)
 *
 * @author 04dkh
 */
public class StockDAO {

    private final int defaultEmployeeId = 1;

    /**
     * Lấy danh sách tất cả các phiếu nhập kho, sắp xếp theo thời gian giảm dần.
     *
     * @return danh sách StockIn
     */
    public List<StockIn> getAllStockIn() {
        return getInOrOut(true);
    }

    /**
     * Lấy danh sách tất cả các phiếu xuất kho, sắp xếp theo thời gian giảm dần.
     *
     * @return danh sách StockOut
     */
    public List<StockOut> getAllStockOut() {
        return getInOrOut(false);
    }

    /**
     * Thêm mới một phiếu nhập kho và ghi log.
     *
     * @param s đối tượng StockIn cần thêm
     */
    public void insertStockIn(StockIn s) {
        insert(true, s.getProductId(), s.getQuantity(), s.getNote(), defaultEmployeeId);
    }

    /**
     * Thêm mới một phiếu xuất kho và ghi log.
     *
     * @param s đối tượng StockOut cần thêm
     */
    public void insertStockOut(StockOut s) {
        insert(false, s.getProductId(), s.getQuantity(), s.getNote(), defaultEmployeeId);
    }

    /**
     * Xóa một phiếu nhập kho theo ID và ghi log.
     *
     * @param id mã định danh của phiếu nhập kho
     */
    public void deleteStockIn(int id) {
        delete(true, id, defaultEmployeeId);
    }

    /**
     * Xóa một phiếu xuất kho theo ID và ghi log.
     *
     * @param id mã định danh của phiếu xuất kho
     */
    public void deleteStockOut(int id) {
        delete(false, id, defaultEmployeeId);
    }

    // ---------------------- PHẦN RIÊNG TƯ ----------------------
    private <T> List<T> getInOrOut(boolean isIn) {
        String table = isIn ? "stock_in" : "stock_out";
        String sql = "SELECT * FROM " + table + " ORDER BY created_at DESC";
        List<T> list = new ArrayList<>();

        try (Connection cn = DatabaseConnection.getConnection(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                if (isIn) {
                    list.add((T) new StockIn(
                            rs.getInt("id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            rs.getString("note"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    ));
                } else {
                    list.add((T) new StockOut(
                            rs.getInt("id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            rs.getString("note"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    ));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    private void insert(boolean isIn, int productId, int qty, String note, int employeeId) {
        String table = isIn ? "stock_in" : "stock_out";
        String sql = "INSERT INTO " + table + " (product_id, quantity, note, created_at) VALUES (?, ?, ?, NOW())";

        try (Connection cn = DatabaseConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, productId);
            ps.setInt(2, qty);
            ps.setString(3, note);
            ps.executeUpdate();

            int id = -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }

            // Ghi AuditLog
            Gson gson = new Gson();
            String newData = isIn
                    ? gson.toJson(new StockIn(id, productId, qty, note, null))
                    : gson.toJson(new StockOut(id, productId, qty, note, null));

            AuditLog log = new AuditLog(employeeId, "INSERT", table, id, null, newData);
            new AuditLogDAO(cn).insertAuditLog(log);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void delete(boolean isIn, int id, int employeeId) {
        String table = isIn ? "stock_in" : "stock_out";
        String sqlSelect = "SELECT * FROM " + table + " WHERE id = ?";
        String sqlDelete = "DELETE FROM " + table + " WHERE id = ?";

        try (Connection cn = DatabaseConnection.getConnection(); PreparedStatement selectStmt = cn.prepareStatement(sqlSelect); PreparedStatement deleteStmt = cn.prepareStatement(sqlDelete)) {

            selectStmt.setInt(1, id);
            ResultSet rs = selectStmt.executeQuery();

            String oldData = null;
            if (rs.next()) {
                Gson gson = new Gson();
                if (isIn) {
                    oldData = gson.toJson(new StockIn(
                            rs.getInt("id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            rs.getString("note"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    ));
                } else {
                    oldData = gson.toJson(new StockOut(
                            rs.getInt("id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            rs.getString("note"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    ));
                }

                AuditLog log = new AuditLog(employeeId, "DELETE", table, id, oldData, null);
                new AuditLogDAO(cn).insertAuditLog(log);
            }

            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
