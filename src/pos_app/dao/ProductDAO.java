package pos_app.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pos_app.models.InvoiceModel;
import pos_app.models.Product;
import pos_app.util.DatabaseConnection;

public class ProductDAO {

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (
                Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
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

    public void insertProduct(Product p) {
        String sql = "INSERT INTO products (name, price, quantity, image_path) VALUES (?, ?, ?, ?)";
        try (
                Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getName());
            stmt.setDouble(2, p.getPrice());
            stmt.setInt(3, p.getQuantity());
            stmt.setString(4, p.getImagePath());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(Product p) {
        String sql = "UPDATE products SET name = ?, price = ?, quantity = ?, image_path = ? WHERE id = ?";
        try (
                Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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

    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (
                Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Product getProductByBarcode(String barcode) {
        String sql = "SELECT * FROM products WHERE barcode = ?";
        try (
                Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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

    public boolean saveOrder(InvoiceModel invoice) {
        Connection conn = null;
        PreparedStatement insertInvoice = null;
        PreparedStatement insertItems = null;
        PreparedStatement updateStock = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Hóa đơn
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

            // 2. Chi tiết sản phẩm
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
