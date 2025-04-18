/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.dao;

/**
 *
 * @author 04dkh
 */
import java.sql.*;
import java.util.*;
import pos_app.models.*;
import pos_app.util.DatabaseConnection;

public class StockDAO {

    /* ------------------- LẤY DANH SÁCH ------------------- */
    public List<StockIn> getAllStockIn() {
        return getInOrOut(true);
    }

    public List<StockOut> getAllStockOut() {
        return getInOrOut(false);
    }

    /* --------------- THÊM MỚI BẢN GHI -------------------- */
    public void insertStockIn(StockIn s) {
        insert(true, s.getProductId(), s.getQuantity(), s.getNote());
    }

    public void insertStockOut(StockOut s) {
        insert(false, s.getProductId(), s.getQuantity(), s.getNote());
    }

    /* --------------- XOÁ 1 BẢN GHI ----------------------- */
    public void deleteStockIn(int id) {
        delete(true, id);
    }

    public void deleteStockOut(int id) {
        delete(false, id);
    }

    /* ===================================================== */
 /*      IMPLEMENTATION – dùng chung cho 2 bảng           */
 /* ===================================================== */
    private List getInOrOut(boolean isIn) {
        String tbl = isIn ? "stock_in" : "stock_out";
        String sql = "SELECT * FROM " + tbl + " ORDER BY created_at DESC";
        List list = new ArrayList();

        try (Connection cn = DatabaseConnection.getConnection(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                if (isIn) {
                    list.add(new StockIn(
                            rs.getInt("id"), rs.getInt("product_id"),
                            rs.getInt("quantity"), rs.getString("note"),
                            rs.getTimestamp("created_at").toLocalDateTime()));
                } else {
                    list.add(new StockOut(
                            rs.getInt("id"), rs.getInt("product_id"),
                            rs.getInt("quantity"), rs.getString("note"),
                            rs.getTimestamp("created_at").toLocalDateTime()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    private void insert(boolean isIn, int productId, int qty, String note) {
        String tbl = isIn ? "stock_in" : "stock_out";
        String sql = "INSERT INTO " + tbl + "(product_id,quantity,note,created_at)"
                + " VALUES(?,?,?,NOW())";
        try (Connection cn = DatabaseConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, qty);
            ps.setString(3, note);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void delete(boolean isIn, int id) {
        String tbl = isIn ? "stock_in" : "stock_out";
        String sql = "DELETE FROM " + tbl + " WHERE id=?";
        try (Connection cn = DatabaseConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
