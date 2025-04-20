package pos_app.ui.panel;

import pos_app.models.Product;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Giao diện giỏ hàng trong POS với nền trắng, font đồng bộ và tự động cập nhật
 * khi thay đổi.
 */
public class OrderTabPanel extends JPanel {

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã", "Tên", "SL", "Đơn giá", "Thành tiền"}, 0
    );
    private final JTable table = new JTable(model);
    private Runnable onCartChanged;

    public OrderTabPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Giỏ hàng"));
        setBackground(Color.WHITE);

        // === Cài đặt bảng ===
        table.setRowHeight(38);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setFillsViewportHeight(true);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setIntercellSpacing(new Dimension(10, 10));

        // === Căn giữa nội dung ===
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < model.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        // === Header trắng và nổi bật ===
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setForeground(Color.DARK_GRAY);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setOpaque(true);

        // === ScrollPane ===
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBackground(Color.WHITE);

        add(scroll, BorderLayout.CENTER);
    }

    public void setOnCartChanged(Runnable onCartChanged) {
        this.onCartChanged = onCartChanged;
    }

    public void addToCart(Product p) {
        for (int i = 0; i < model.getRowCount(); i++) {
            int id = (int) model.getValueAt(i, 0);
            if (id == p.getId()) {
                int qty = (int) model.getValueAt(i, 2);
                qty++;
                model.setValueAt(qty, i, 2);
                model.setValueAt(qty * p.getPrice(), i, 4);
                if (onCartChanged != null) {
                    onCartChanged.run();
                }
                return;
            }
        }
        model.addRow(new Object[]{
            p.getId(),
            p.getName(),
            1,
            p.getPrice(),
            p.getPrice()
        });
        if (onCartChanged != null) {
            onCartChanged.run();
        }
    }

    public void clearCart() {
        model.setRowCount(0);
        if (onCartChanged != null) {
            onCartChanged.run();
        }
    }

    public double getTotal() {
        double total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object val = model.getValueAt(i, 4);
            if (val instanceof Number num) {
                total += num.doubleValue();
            }
        }
        return total;
    }

    public int getItemCount() {
        int count = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object val = model.getValueAt(i, 2);
            if (val instanceof Number num) {
                count += num.intValue();
            }
        }
        return count;
    }

    public List<Product> getCartItems() {
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            int id = (int) model.getValueAt(i, 0);
            String name = (String) model.getValueAt(i, 1);
            int quantity = (int) model.getValueAt(i, 2);
            double price = (double) model.getValueAt(i, 3);
            list.add(new Product(id, name, price, quantity));
        }
        return list;
    }
}
