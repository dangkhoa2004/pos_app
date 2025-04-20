package pos_app.ui.panel;

import pos_app.models.Customer;
import pos_app.dao.CustomerDAO;
import pos_app.ui.components.RoundedButton;
import pos_app.ui.components.RoundedTextField;
import pos_app.ui.dialog.CustomerFormDialog;
import pos_app.ui.table.ButtonRenderer;
import pos_app.ui.table.CustomerButtonEditor;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * Giao diện quản lý khách hàng với nền trắng toàn bộ,
 * các input nổi bật, bố cục rõ ràng và khởi tạo đúng thứ tự.
 */
public class CustomerPanel extends JPanel {

    private final DefaultTableModel model;
    private final JTable table;
    private final CustomerDAO dao = new CustomerDAO();

    public CustomerPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tạo model trước để đảm bảo bảng có cột
        model = new DefaultTableModel(
                new String[]{"ID", "Tên KH", "SĐT", "Email", "Hành động"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        table = new JTable(model);
        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setBackground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(100, 35));
        table.getTableHeader().setBackground(new Color(230, 230, 230));
        styleTable(table);

        add(buildControlPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        loadTable();
    }

    private JPanel buildControlPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);

        JPanel attrButtons = new JPanel(new GridLayout(1, 4, 10, 0));
        attrButtons.setBorder(BorderFactory.createTitledBorder("Thuộc tính khách hàng"));
        attrButtons.setBackground(Color.WHITE);

        String[] btnNames = {
                "Quản lý hạng thành viên", "Quản lý nhóm KH",
                "Quản lý khu vực", "Quản lý nguồn KH"
        };
        for (String name : btnNames) {
            JButton btn = new RoundedButton(name, 10);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btn.setBackground(Color.WHITE);
            btn.setPreferredSize(new Dimension(0, 32));
            attrButtons.add(btn);
        }

        wrapper.add(attrButtons, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // --- Thanh tìm kiếm & thêm mới ---
        RoundedTextField tfSearch = new RoundedTextField(20, 15);
        tfSearch.setBackground(new Color(245, 245, 245));

        JButton btnSearch = new RoundedButton("Tìm", 15);
        JButton btnAdd = new RoundedButton("Thêm mới", 15);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        left.setBackground(Color.WHITE);
        left.add(btnAdd);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        right.setBackground(Color.WHITE);
        right.add(new JLabel("Tìm kiếm:"));
        right.add(tfSearch);
        right.add(btnSearch);

        topBar.add(left, BorderLayout.WEST);
        topBar.add(right, BorderLayout.EAST);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));
        scroll.getViewport().setBackground(Color.WHITE);

        // --- Tìm kiếm action ---
        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim().toLowerCase();
            model.setRowCount(0);
            for (Customer c : dao.getAllCustomers()) {
                if (c.getName().toLowerCase().contains(keyword) || c.getPhone().contains(keyword)) {
                    model.addRow(new Object[]{
                            c.getId(), c.getName(), c.getPhone(), c.getEmail(), "Hành động"
                    });
                }
            }
        });

        // --- Thêm khách hàng ---
        btnAdd.addActionListener(e -> {
            CustomerFormDialog dlg = new CustomerFormDialog(null, "Thêm khách hàng", null);
            dlg.setVisible(true);
            if (dlg.isSubmitted()) {
                dao.insertCustomer(dlg.getCustomerData(0));
                loadTable();
            }
        });

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void styleTable(JTable tbl) {
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        center.setVerticalAlignment(JLabel.CENTER);

        for (int i = 0; i < tbl.getColumnCount(); i++) {
            tbl.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        tbl.getColumn("Hành động").setCellRenderer(new ButtonRenderer());
        tbl.getColumn("Hành động").setCellEditor(
                new CustomerButtonEditor(model, dao, this::loadTable)
        );
    }

    private void loadTable() {
        model.setRowCount(0);
        List<Customer> list = dao.getAllCustomers();
        for (Customer c : list) {
            model.addRow(new Object[]{
                    c.getId(), c.getName(), c.getPhone(), c.getEmail(), "Hành động"
            });
        }
    }
}
