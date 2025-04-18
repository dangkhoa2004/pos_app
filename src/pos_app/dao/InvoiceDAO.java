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
import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;
import pos_app.models.*;
import pos_app.models.Invoice;
import pos_app.models.InvoiceItem;
import pos_app.util.DatabaseConnection;

public class InvoiceDAO {

    /* --------------------- LẤY DANH SÁCH --------------------- */
    public List<Invoice> getAllInvoices() {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoices ORDER BY created_at DESC";

        try (Connection cn = DatabaseConnection.getConnection(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(toInvoice(rs, false)); // không nạp items
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /* ----------------------- TÌM CHI TIẾT -------------------- */
    public Invoice findById(int id) {
        String sqlInv = "SELECT * FROM invoices WHERE id = ?";
        String sqlItm = "SELECT * FROM invoice_items WHERE invoice_id = ?";

        try (Connection cn = DatabaseConnection.getConnection(); PreparedStatement psInv = cn.prepareStatement(sqlInv); PreparedStatement psItm = cn.prepareStatement(sqlItm)) {

            psInv.setInt(1, id);
            try (ResultSet rsInv = psInv.executeQuery()) {
                if (!rsInv.next()) {
                    return null;
                }

                Invoice inv = toInvoice(rsInv, true);
                psItm.setInt(1, id);
                try (ResultSet rsItm = psItm.executeQuery()) {
                    while (rsItm.next()) {
                        inv.getItems().add(toItem(rsItm));
                    }
                }
                return inv;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /* ------------------------ THÊM MỚI ----------------------- */
    public boolean insertInvoice(Invoice inv) {
        String sqlInv = "INSERT INTO invoices"
                + "(customer_id, employee_id, total, created_at) VALUES (?,?,?,?)";
        String sqlItm = "INSERT INTO invoice_items"
                + "(invoice_id, product_id, quantity, unit_price) VALUES (?,?,?,?)";

        try (Connection cn = DatabaseConnection.getConnection()) {
            cn.setAutoCommit(false);

            try (PreparedStatement psInv = cn.prepareStatement(
                    sqlInv, Statement.RETURN_GENERATED_KEYS)) {

                psInv.setInt(1, inv.getCustomerId());
                psInv.setInt(2, inv.getEmployeeId());
                psInv.setBigDecimal(3, inv.getTotal());
                psInv.setTimestamp(4, Timestamp.valueOf(inv.getCreatedAt()));
                psInv.executeUpdate();

                try (ResultSet gk = psInv.getGeneratedKeys()) {
                    if (gk.next()) {
                        inv.setId(gk.getInt(1));
                    }
                }
            }

            try (PreparedStatement psItm = cn.prepareStatement(sqlItm)) {
                for (InvoiceItem it : inv.getItems()) {
                    psItm.setInt(1, inv.getId());
                    psItm.setInt(2, it.getProductId());
                    psItm.setInt(3, it.getQuantity());
                    psItm.setBigDecimal(4, it.getUnitPrice());
                    psItm.addBatch();
                }
                psItm.executeBatch();
            }

            cn.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /* ------------------------ CẬP NHẬT ----------------------- */
    public boolean updateInvoice(Invoice inv) {
        String sqlUpdInv = "UPDATE invoices SET customer_id=?, employee_id=?, total=? WHERE id=?";
        String sqlDelItem = "DELETE FROM invoice_items WHERE invoice_id=?";
        String sqlAddItem = "INSERT INTO invoice_items(invoice_id,product_id,quantity,unit_price)"
                + " VALUES(?,?,?,?)";

        try (Connection cn = DatabaseConnection.getConnection()) {
            cn.setAutoCommit(false);

            try (PreparedStatement ps = cn.prepareStatement(sqlUpdInv)) {
                ps.setInt(1, inv.getCustomerId());
                ps.setInt(2, inv.getEmployeeId());
                ps.setBigDecimal(3, inv.getTotal());
                ps.setInt(4, inv.getId());
                ps.executeUpdate();
            }

            try (PreparedStatement psDel = cn.prepareStatement(sqlDelItem)) {
                psDel.setInt(1, inv.getId());
                psDel.executeUpdate();
            }

            try (PreparedStatement psAdd = cn.prepareStatement(sqlAddItem)) {
                for (InvoiceItem it : inv.getItems()) {
                    psAdd.setInt(1, inv.getId());
                    psAdd.setInt(2, it.getProductId());
                    psAdd.setInt(3, it.getQuantity());
                    psAdd.setBigDecimal(4, it.getUnitPrice());
                    psAdd.addBatch();
                }
                psAdd.executeBatch();
            }

            cn.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /* ------------------------ XOÁ ----------------------- */
    public boolean deleteInvoice(int id) {
        String sqlItm = "DELETE FROM invoice_items WHERE invoice_id=?";
        String sqlInv = "DELETE FROM invoices WHERE id=?";

        try (Connection cn = DatabaseConnection.getConnection()) {
            cn.setAutoCommit(false);

            try (PreparedStatement psItm = cn.prepareStatement(sqlItm)) {
                psItm.setInt(1, id);
                psItm.executeUpdate();
            }
            try (PreparedStatement psInv = cn.prepareStatement(sqlInv)) {
                psInv.setInt(1, id);
                psInv.executeUpdate();
            }
            cn.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /* ================ MAP RESULTSET → OBJECT ================= */
    private Invoice toInvoice(ResultSet rs, boolean withTotal) throws SQLException {
        return new Invoice(
                rs.getInt("id"),
                rs.getInt("customer_id"),
                rs.getInt("employee_id"),
                withTotal ? rs.getBigDecimal("total") : BigDecimal.ZERO,
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    private InvoiceItem toItem(ResultSet rs) throws SQLException {
        return new InvoiceItem(
                rs.getInt("id"),
                rs.getInt("invoice_id"),
                rs.getInt("product_id"),
                rs.getInt("quantity"),
                rs.getBigDecimal("unit_price")
        );
    }
}
