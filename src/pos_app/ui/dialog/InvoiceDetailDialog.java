package pos_app.ui.dialog;

import pos_app.dao.ProductDAO;
import pos_app.models.Invoice;
import pos_app.models.InvoiceItem;
import pos_app.models.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

/**
 * InvoiceDetailDialog là cửa sổ hiển thị chi tiết một hóa đơn. Bao gồm bảng sản
 * phẩm (có ảnh) và thông tin hóa đơn: khách hàng, nhân viên, tổng tiền.
 *
 * Ảnh sản phẩm sẽ được load từ thư mục: src/pos_app/pictures/
 *
 * @author 04dkh
 */
public class InvoiceDetailDialog extends JDialog {

    private final ProductDAO productDAO = new ProductDAO();

    /**
     * Khởi tạo dialog hiển thị chi tiết hóa đơn.
     *
     * @param parent cửa sổ cha
     * @param invoice đối tượng hóa đơn cần hiển thị
     */
    public InvoiceDetailDialog(JFrame parent, Invoice invoice) {
        super(parent, "Chi tiết hoá đơn #" + invoice.getId(), true);
        setSize(720, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Tạo bảng sản phẩm
        String[] columns = {"Ảnh", "Tên sản phẩm", "SL", "Đơn giá", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? ImageIcon.class : Object.class;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(60);
        table.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());

        for (InvoiceItem item : invoice.getItems()) {
            Product product = productDAO.getProductById(item.getProductId());
            model.addRow(new Object[]{
                product != null ? product.getImagePath() : null,
                product != null ? product.getName() : "Không xác định",
                item.getQuantity(),
                item.getUnitPrice(),
                item.getLineTotal()
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));
        add(scrollPane, BorderLayout.CENTER);

        // Panel thông tin hóa đơn
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));
        infoPanel.add(new JLabel("Khách hàng:"));
        infoPanel.add(new JLabel(String.valueOf(invoice.getCustomerId())));
        infoPanel.add(new JLabel("Nhân viên:"));
        infoPanel.add(new JLabel(String.valueOf(invoice.getEmployeeId())));
        infoPanel.add(new JLabel("Tổng tiền:"));
        infoPanel.add(new JLabel(invoice.getTotal().toString()));

        add(infoPanel, BorderLayout.NORTH);
    }

    /**
     * Renderer tùy chỉnh để hiển thị ảnh sản phẩm trong bảng. Ảnh được lấy từ
     * thư mục `src/pos_app/pictures/`
     */
    public static class ImageRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            if (value != null) {
                String path = "src/pos_app/pictures/" + value.toString();
                File file = new File(path);
                if (file.exists()) {
                    ImageIcon icon = new ImageIcon(path);
                    Image scaled = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    label.setIcon(new ImageIcon(scaled));
                } else {
                    label.setText("(Không có ảnh)");
                }
            }
            return label;
        }
    }
}
