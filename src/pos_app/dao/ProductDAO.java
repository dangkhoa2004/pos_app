package pos_app.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pos_app.models.InvoiceModel;
import pos_app.models.Product;
import pos_app.util.DatabaseConnection;

/**
 * ProductDAO dùng để thao tác với bảng products và các nghiệp vụ liên quan đến
 * hóa đơn trong hệ thống POS.
 *
 * Cung cấp các phương thức để: - Lấy danh sách sản phẩm - Thêm, sửa, xóa sản
 * phẩm - Tìm kiếm sản phẩm theo barcode - Lưu hóa đơn bán hàng và cập nhật tồn
 * kho
 *
 * Sử dụng kết nối từ lớp DatabaseConnection để thao tác với database.
 *
 * @author 04dkh
 */
public class ProductDAO {

    /**
     * Lấy danh sách toàn bộ sản phẩm từ database.
     *
     * @return danh sách sản phẩm
     */
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("barcode"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getString("image_path")
                );
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Tìm kiếm sản phẩm theo ID.
     *
     * @param id mã định danh sản phẩm
     * @return đối tượng Product nếu tìm thấy, null nếu không có
     */
    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getInt("id"),
                            rs.getString("barcode"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("quantity"),
                            rs.getString("image_path")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Thêm sản phẩm mới vào database.
     *
     * @param p đối tượng sản phẩm cần thêm
     */
    public void insertProduct(Product p) {
        String sql = "INSERT INTO products (name, price, quantity, image_path) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getName());
            stmt.setDouble(2, p.getPrice());
            stmt.setInt(3, p.getQuantity());
            stmt.setString(4, p.getImagePath());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật thông tin sản phẩm theo ID.
     *
     * @param p đối tượng sản phẩm cần cập nhật
     */
    public void updateProduct(Product p) {
        String sql = "UPDATE products SET name = ?, price = ?, quantity = ?, image_path = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getName());
            stmt.setDouble(2, p.getPrice());
            stmt.setInt(3, p.getQuantity());
            stmt.setString(4, p.getImagePath());
            stmt.setInt(5, p.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Xóa sản phẩm theo ID.
     *
     * @param id mã sản phẩm cần xóa
     */
    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tìm kiếm sản phẩm theo barcode.
     *
     * @param barcode mã vạch sản phẩm
     * @return sản phẩm tương ứng hoặc null nếu không tìm thấy
     */
    public Product getProductByBarcode(String barcode) {
        String sql = "SELECT * FROM products WHERE barcode = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, barcode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getInt("id"),
                            rs.getString("barcode"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("quantity"),
                            rs.getString("image_path")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Lưu đơn hàng mới vào hệ thống, bao gồm: - Thêm hóa đơn - Thêm chi tiết
     * sản phẩm (invoice_items) - Trừ số lượng tồn kho tương ứng
     *
     * Tất cả thực hiện trong một giao dịch (transaction).
     *
     * @param invoice đối tượng hóa đơn cần lưu
     * @return true nếu thành công, false nếu lỗi
     */
    public boolean saveOrder(InvoiceModel invoice) {
        Connection conn = null;
        PreparedStatement insertInvoice = null;
        PreparedStatement insertItems = null;
        PreparedStatement updateStock = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Thêm hóa đơn
            String sqlInvoice = "INSERT INTO invoices (customer_id, employee_id, table_id, discount_id, total, created_at) VALUES (?, ?, ?, ?, ?, ?)";
            insertInvoice = conn.prepareStatement(sqlInvoice, Statement.RETURN_GENERATED_KEYS);

            if (invoice.getCustomerId() <= 0) {
                insertInvoice.setNull(1, Types.INTEGER);
            } else {
                insertInvoice.setInt(1, invoice.getCustomerId());
            }

            insertInvoice.setInt(2, invoice.getEmployeeId());

            if (invoice.getTableId() <= 0) {
                insertInvoice.setNull(3, Types.INTEGER);
            } else {
                insertInvoice.setInt(3, invoice.getTableId());
            }

            if (invoice.getDiscountId() <= 0) {
                insertInvoice.setNull(4, Types.INTEGER);
            } else {
                insertInvoice.setInt(4, invoice.getDiscountId());
            }

            insertInvoice.setDouble(5, invoice.getTotal());
            insertInvoice.setDate(6, new java.sql.Date(invoice.getCreatedAt().getTime()));
            insertInvoice.executeUpdate();

            generatedKeys = insertInvoice.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Không lấy được ID hóa đơn.");
            }

            int invoiceId = generatedKeys.getInt(1);

            // 2. Thêm chi tiết sản phẩm
            String sqlItem = "INSERT INTO invoice_items (invoice_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
            insertItems = conn.prepareStatement(sqlItem);

            for (Product p : invoice.getItems()) {
                insertItems.setInt(1, invoiceId);
                insertItems.setInt(2, p.getId());
                insertItems.setInt(3, p.getQuantity());
                insertItems.setDouble(4, p.getPrice());
                insertItems.addBatch();
            }
            insertItems.executeBatch();

            // 3. Trừ tồn kho
            String sqlUpdate = "UPDATE products SET quantity = quantity - ? WHERE id = ?";
            updateStock = conn.prepareStatement(sqlUpdate);

            for (Product p : invoice.getItems()) {
                updateStock.setInt(1, p.getQuantity());
                updateStock.setInt(2, p.getId());
                updateStock.addBatch();
            }
            updateStock.executeBatch();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;

        } finally {
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (insertInvoice != null) {
                    insertInvoice.close();
                }
                if (insertItems != null) {
                    insertItems.close();
                }
                if (updateStock != null) {
                    updateStock.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
