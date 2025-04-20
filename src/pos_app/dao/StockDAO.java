package pos_app.dao;

import java.sql.*;
import java.util.*;
import pos_app.models.*;
import pos_app.util.DatabaseConnection;

/**
 * StockDAO dùng để thao tác dữ liệu nhập kho (StockIn) và xuất kho (StockOut)
 * trong hệ thống POS.
 *
 * Cung cấp các phương thức để: - Lấy danh sách phiếu nhập/xuất kho - Thêm phiếu
 * nhập/xuất kho mới - Xóa phiếu nhập/xuất kho
 *
 * Sử dụng kết nối từ lớp DatabaseConnection để thao tác với database.
 *
 * @author 04dkh
 */
public class StockDAO {

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
     * Thêm mới một phiếu nhập kho vào database.
     *
     * @param s đối tượng StockIn cần thêm
     */
    public void insertStockIn(StockIn s) {
        insert(true, s.getProductId(), s.getQuantity(), s.getNote());
    }

    /**
     * Thêm mới một phiếu xuất kho vào database.
     *
     * @param s đối tượng StockOut cần thêm
     */
    public void insertStockOut(StockOut s) {
        insert(false, s.getProductId(), s.getQuantity(), s.getNote());
    }

    /**
     * Xóa một phiếu nhập kho theo ID.
     *
     * @param id mã định danh của phiếu nhập kho
     */
    public void deleteStockIn(int id) {
        delete(true, id);
    }

    /**
     * Xóa một phiếu xuất kho theo ID.
     *
     * @param id mã định danh của phiếu xuất kho
     */
    public void deleteStockOut(int id) {
        delete(false, id);
    }

    /**
     * Lấy danh sách StockIn hoặc StockOut từ bảng tương ứng.
     *
     * @param isIn true nếu lấy bảng stock_in, false nếu lấy bảng stock_out
     * @param <T> kiểu dữ liệu trả về (StockIn hoặc StockOut)
     * @return danh sách các phiếu tương ứng
     */
    @SuppressWarnings("unchecked")
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

    /**
     * Thêm mới bản ghi vào stock_in hoặc stock_out.
     *
     * @param isIn true nếu là nhập kho, false nếu là xuất kho
     * @param productId mã sản phẩm
     * @param qty số lượng nhập/xuất
     * @param note ghi chú kèm theo
     */
    private void insert(boolean isIn, int productId, int qty, String note) {
        String table = isIn ? "stock_in" : "stock_out";
        String sql = "INSERT INTO " + table + " (product_id, quantity, note, created_at) VALUES (?, ?, ?, NOW())";

        try (Connection cn = DatabaseConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ps.setInt(2, qty);
            ps.setString(3, note);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Xóa bản ghi khỏi bảng stock_in hoặc stock_out theo ID.
     *
     * @param isIn true nếu là bảng stock_in, false nếu là stock_out
     * @param id mã bản ghi cần xóa
     */
    private void delete(boolean isIn, int id) {
        String table = isIn ? "stock_in" : "stock_out";
        String sql = "DELETE FROM " + table + " WHERE id = ?";

        try (Connection cn = DatabaseConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
