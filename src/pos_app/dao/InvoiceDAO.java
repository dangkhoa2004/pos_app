package pos_app.dao;

import com.google.gson.Gson;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;
import pos_app.models.*;
import pos_app.util.DatabaseConnection;

/**
 * InvoiceDAO dùng để thao tác dữ liệu hóa đơn trong hệ thống POS.
 *
 * Bao gồm các thao tác: - Lấy danh sách hóa đơn - Tìm chi tiết hóa đơn theo ID
 * - Thêm mới, cập nhật, xóa hóa đơn - Ghi lịch sử thao tác vào bảng audit_logs
 *
 * Sử dụng kết nối thông qua lớp DatabaseConnection. Mặc định employeeId = 1
 * (admin)
 *
 * @author 04dkh
 */
public class InvoiceDAO {

    private final int defaultEmployeeId = 1;

    /* --------------------- LẤY DANH SÁCH --------------------- */
    public List<Invoice> getAllInvoices() {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoices ORDER BY created_at DESC";

        try (Connection cn = DatabaseConnection.getConnection(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(toInvoice(rs, false));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /* --------------------- TÌM CHI TIẾT ---------------------- */
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

    /* ---------------------- THÊM MỚI -------------------------- */
    public boolean insertInvoice(Invoice inv) {
        String sqlInv = "INSERT INTO invoices (customer_id, employee_id, total, created_at) VALUES (?,?,?,?)";
        String sqlItm = "INSERT INTO invoice_items (invoice_id, product_id, quantity, unit_price) VALUES (?,?,?,?)";

        try (Connection cn = DatabaseConnection.getConnection()) {
            cn.setAutoCommit(false);

            // 1. Thêm hóa đơn
            try (PreparedStatement psInv = cn.prepareStatement(sqlInv, Statement.RETURN_GENERATED_KEYS)) {
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

            // 2. Thêm sản phẩm trong hóa đơn
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

            // 3. Ghi AuditLog
            Gson gson = new Gson();
            AuditLog log = new AuditLog(
                    defaultEmployeeId,
                    "INSERT",
                    "invoices",
                    inv.getId(),
                    null,
                    gson.toJson(inv)
            );
            new AuditLogDAO(cn).insertAuditLog(log);

            cn.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /* ------------------------ CẬP NHẬT ------------------------ */
    public boolean updateInvoice(Invoice inv) {
        String sqlUpdInv = "UPDATE invoices SET customer_id=?, employee_id=?, total=? WHERE id=?";
        String sqlDelItem = "DELETE FROM invoice_items WHERE invoice_id=?";
        String sqlAddItem = "INSERT INTO invoice_items(invoice_id, product_id, quantity, unit_price) VALUES(?,?,?,?)";

        try (Connection cn = DatabaseConnection.getConnection()) {
            cn.setAutoCommit(false);

            // 1. Lưu dữ liệu cũ
            Invoice old = findById(inv.getId());

            // 2. Cập nhật hóa đơn
            try (PreparedStatement ps = cn.prepareStatement(sqlUpdInv)) {
                ps.setInt(1, inv.getCustomerId());
                ps.setInt(2, inv.getEmployeeId());
                ps.setBigDecimal(3, inv.getTotal());
                ps.setInt(4, inv.getId());
                ps.executeUpdate();
            }

            // 3. Xóa chi tiết cũ
            try (PreparedStatement psDel = cn.prepareStatement(sqlDelItem)) {
                psDel.setInt(1, inv.getId());
                psDel.executeUpdate();
            }

            // 4. Thêm chi tiết mới
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

            // 5. Ghi AuditLog
            Gson gson = new Gson();
            AuditLog log = new AuditLog(
                    defaultEmployeeId,
                    "UPDATE",
                    "invoices",
                    inv.getId(),
                    gson.toJson(old),
                    gson.toJson(inv)
            );
            new AuditLogDAO(cn).insertAuditLog(log);

            cn.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /* ------------------------ XÓA ---------------------------- */
    public boolean deleteInvoice(int id) {
        String sqlItm = "DELETE FROM invoice_items WHERE invoice_id=?";
        String sqlInv = "DELETE FROM invoices WHERE id=?";

        try (Connection cn = DatabaseConnection.getConnection()) {
            cn.setAutoCommit(false);

            // 1. Lấy dữ liệu cũ
            Invoice old = findById(id);

            // 2. Xóa chi tiết
            try (PreparedStatement psItm = cn.prepareStatement(sqlItm)) {
                psItm.setInt(1, id);
                psItm.executeUpdate();
            }

            // 3. Xóa hóa đơn
            try (PreparedStatement psInv = cn.prepareStatement(sqlInv)) {
                psInv.setInt(1, id);
                psInv.executeUpdate();
            }

            // 4. Ghi AuditLog
            Gson gson = new Gson();
            AuditLog log = new AuditLog(
                    defaultEmployeeId,
                    "DELETE",
                    "invoices",
                    id,
                    gson.toJson(old),
                    null
            );
            new AuditLogDAO(cn).insertAuditLog(log);

            cn.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /* ================ MAPPING ======================= */
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
