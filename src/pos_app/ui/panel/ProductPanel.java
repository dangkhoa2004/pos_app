package pos_app.ui.panel;

import java.awt.*;
import java.io.File;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import pos_app.dao.ProductDAO;
import pos_app.models.Product;
import pos_app.ui.components.RoundedButton;
import pos_app.ui.components.RoundedTextField;
import pos_app.ui.dialog.CategoryDialog;
import pos_app.ui.dialog.ProductFormDialog;
import pos_app.ui.table.ProductButtonEditor;
import pos_app.util.QRCodeUtil;
import com.google.zxing.WriterException;
import java.awt.image.BufferedImage;

public class ProductPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private final ProductDAO dao = new ProductDAO();

    public ProductPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Thuộc tính khác"));
        String[] btnNames = {"Quản lý loại sản phẩm"};
        for (String name : btnNames) {
            JButton btn = new RoundedButton(name, 10);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btn.setPreferredSize(new Dimension(0, 32));
            controlPanel.add(btn);
            if (name.equals("Quản lý loại sản phẩm")) {
                btn.addActionListener(e -> {
                    CategoryDialog dialog = new CategoryDialog(SwingUtilities.getWindowAncestor(this));
                    dialog.setVisible(true);
                });
            }
        }
        JPanel inputWrapper = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.weightx = 0.3;
        inputWrapper.add(controlPanel, gbc);

        model = new DefaultTableModel(new String[]{"ID", "BarCode", "Tên", "Giá", "Số lượng", "Hình ảnh", "Hành động"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        table = new JTable(model);
        table.setRowHeight(60);
        styleTable(table);
        table.getColumn("Hình ảnh").setCellRenderer(new ImageRenderer());
        table.getColumn("Hành động").setCellRenderer(new ProductButtonEditor.BtnRenderer());
        table.getColumn("Hành động").setCellEditor(new ProductButtonEditor(model, dao, this::loadTable));

        RoundedTextField tfSearch = new RoundedTextField(20, 15);
        JButton btnSearch = new RoundedButton("Tìm", 15);
        JButton btnAdd = new RoundedButton("Thêm mới", 15);
        JButton btnExportQR = new RoundedButton("Xuất mã QR", 15); // ✅ Thêm nút xuất QR

        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim().toLowerCase();
            model.setRowCount(0);
            for (Product p : dao.getAllProducts()) {
                if (p.getName().toLowerCase().contains(keyword)) {
                    model.addRow(new Object[]{p.getId(), p.getBarcode(), p.getName(), p.getPrice(), p.getQuantity(), p.getImagePath(), "Hành động"});
                }
            }
        });

        btnAdd.addActionListener(e -> {
            ProductFormDialog dialog = new ProductFormDialog(null, "Thêm sản phẩm", null);
            dialog.setVisible(true);
            if (dialog.isSubmitted()) {
                dao.insertProduct(dialog.getProductData(0));
                loadTable();
            }
        });

        // ✅ Sự kiện xuất mã QR
        btnExportQR.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm để xuất mã QR.");
                return;
            }
            String barcode = model.getValueAt(selectedRow, 1).toString();
            try {
                BufferedImage qrImage = QRCodeUtil.generateQRCode(barcode, 300, 300);
                ImageIcon icon = new ImageIcon(qrImage);
                JLabel qrLabel = new JLabel(icon);
                int choice = JOptionPane.showConfirmDialog(this, qrLabel, "Mã QR cho: " + barcode,
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (choice == JOptionPane.OK_OPTION) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setSelectedFile(new File(barcode + ".png"));
                    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                        QRCodeUtil.saveQRCodeToFile(barcode, 300, 300, fileChooser.getSelectedFile());
                        JOptionPane.showMessageDialog(this, "Đã lưu QR tại: " + fileChooser.getSelectedFile().getAbsolutePath());
                    }
                }
            } catch (WriterException | java.io.IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi tạo/lưu mã QR.");
            }
        });

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        leftPanel.add(btnAdd);
        leftPanel.add(btnExportQR); // ✅ Thêm nút QR vào panel

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        rightPanel.add(new JLabel("Tìm kiếm:"));
        rightPanel.add(tfSearch);
        rightPanel.add(btnSearch);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(leftPanel, BorderLayout.WEST);
        searchPanel.add(rightPanel, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.add(searchPanel, BorderLayout.NORTH);
        tableWrapper.add(scrollPane, BorderLayout.CENTER);

        add(inputWrapper, BorderLayout.NORTH);
        add(tableWrapper, BorderLayout.CENTER);
        loadTable();
    }

    private void styleTable(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(230, 230, 230));
        header.setPreferredSize(new Dimension(100, 35));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setVerticalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void loadTable() {
        model.setRowCount(0);
        List<Product> list = dao.getAllProducts();
        for (Product p : list) {
            model.addRow(new Object[]{p.getId(), p.getBarcode(), p.getName(), p.getPrice(), p.getQuantity(), p.getImagePath(), "Hành động"});
        }
    }

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

    private DefaultTableModel panelModel() {
        return this.model;
    }
}
