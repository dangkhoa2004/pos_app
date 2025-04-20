package pos_app.dao;

import java.sql.*;
import java.util.*;
import pos_app.models.*;
import pos_app.util.DatabaseConnection;

public class StockDAO {

    public List<StockIn> getAllStockIn() {
        return getInOrOut(true);
    }

    public List<StockOut> getAllStockOut() {
        return getInOrOut(false);
    }

    public void insertStockIn(StockIn s) {
        insert(true, s.getProductId(), s.getQuantity(), s.getNote());
    }

    public void insertStockOut(StockOut s) {
        insert(false, s.getProductId(), s.getQuantity(), s.getNote());
    }

    public void deleteStockIn(int id) {
        delete(true, id);
    }

    public void deleteStockOut(int id) {
        delete(false, id);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getInOrOut(boolean isIn) {
        String table = isIn ? "stock_in" : "stock_out";
        String sql = "SELECT * FROM " + table + " ORDER BY created_at DESC";
        List<T> list = new ArrayList<>();

        try (Connection cn = DatabaseConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

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

    private void insert(boolean isIn, int productId, int qty, String note) {
        String table = isIn ? "stock_in" : "stock_out";
        String sql = "INSERT INTO " + table + " (product_id, quantity, note, created_at) VALUES (?, ?, ?, NOW())";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ps.setInt(2, qty);
            ps.setString(3, note);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void delete(boolean isIn, int id) {
        String table = isIn ? "stock_in" : "stock_out";
        String sql = "DELETE FROM " + table + " WHERE id = ?";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
