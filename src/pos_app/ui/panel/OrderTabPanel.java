/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.ui.panel;

/**
 *
 * @author 04dkh
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import pos_app.models.Product;

public class OrderTabPanel extends JPanel {

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã", "Tên", "SL", "Đơn giá", "Thành tiền"}, 0
    );
    private final JTable table = new JTable(model);

    public OrderTabPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Giỏ hàng"));
        setBackground(Color.WHITE); // Màu nền chính của panel

        table.setRowHeight(38);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setIntercellSpacing(new Dimension(10, 10));
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230)); // màu viền ô nhẹ
        table.setFillsViewportHeight(true);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);

        // Căn giữa các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Tùy chỉnh header cho đẹp và trắng đồng bộ
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setForeground(Color.DARK_GRAY);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setOpaque(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        scroll.getViewport().setBackground(Color.WHITE); // Nền scroll pane
        scroll.setBackground(Color.WHITE);

        add(scroll, BorderLayout.CENTER);
    }

    public void addProduct(Product p) {
        // thêm logic sản phẩm
    }

    public double getTotal() {
        return 0;
    }
}
