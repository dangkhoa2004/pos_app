package pos_app.ui.panel;

import pos_app.models.Product;
import pos_app.ui.components.RoundedButton;
import pos_app.ui.components.RoundedTextField;
import pos_app.ui.dialog.ProductFormDialog;
import pos_app.ui.table.ProductButtonEditor;
import pos_app.ui.table.ButtonRenderer;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import pos_app.dao.ProductDAO;
import pos_app.ui.dialog.CategoryDialog;

public class ProductPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private final ProductDAO dao = new ProductDAO();

    public ProductPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Thuộc tính khác"));
        String[] btnNames = {
            "Quản lý loại sản phẩm"
        };
        for (String name : btnNames) {
            JButton btn = new RoundedButton(name, 10);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btn.setPreferredSize(new Dimension(0, 32));
            controlPanel.add(btn);
            // Thêm xử lý mở dialog
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

        model = new DefaultTableModel(new String[]{"ID", "Tên", "Giá", "Số lượng", "Hành động"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        table = new JTable(model);
        table.setRowHeight(28);
        styleTable(table);
        table.getColumn("Hành động").setCellRenderer(new ButtonRenderer());
        table.getColumn("Hành động").setCellEditor(
                new ProductButtonEditor(model, dao, this::loadTable)
        );

        RoundedTextField tfSearch = new RoundedTextField(20, 15);
        JButton btnSearch = new RoundedButton("Tìm", 15);
        JButton btnAdd = new RoundedButton("Thêm mới", 15);

        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim().toLowerCase();
            model.setRowCount(0);
            for (Product p : dao.getAllProducts()) {
                if (p.getName().toLowerCase().contains(keyword)) {
                    model.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getQuantity(), "Hành động"});
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

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        leftPanel.add(btnAdd);
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
            model.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getQuantity(), "Hành động"});
        }
    }
}
