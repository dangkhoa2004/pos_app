package pos_app.ui.panel;

import pos_app.models.Customer;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import pos_app.dao.CustomerDAO;
import pos_app.ui.components.RoundedButton;
import pos_app.ui.components.RoundedTextField;
import pos_app.ui.dialog.CustomerFormDialog;
import pos_app.ui.table.ButtonRenderer;
import pos_app.ui.table.CustomerButtonEditor;

public class CustomerPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private final CustomerDAO dao = new CustomerDAO();

    public CustomerPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        /* ---------- Thanh bên phải ---------- */
        JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Thuộc tính khách hàng"));
        String[] btnNames = {
            "Quản lý hạng thành viên", "Quản lý nhóm KH",
            "Quản lý khu vực", "Quản lý nguồn KH"
        };
        for (String name : btnNames) {
            JButton btn = new RoundedButton(name, 10);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btn.setPreferredSize(new Dimension(0, 32));
            controlPanel.add(btn);
        }

        JPanel inputWrapper = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        inputWrapper.add(controlPanel, gbc);

        /* ---------- Bảng ---------- */
        model = new DefaultTableModel(
                new String[]{"ID", "Tên KH", "SĐT", "Email", "Hành động"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 4;
            }
        };
        table = new JTable(model);
        table.setRowHeight(28);
        styleTable(table);
        table.getColumn("Hành động").setCellRenderer(new ButtonRenderer());
        table.getColumn("Hành động").setCellEditor(
                new CustomerButtonEditor(model, dao, this::loadTable));

        /* ---------- Tìm kiếm & Thêm ---------- */
        RoundedTextField tfSearch = new RoundedTextField(20, 15);
        JButton btnSearch = new RoundedButton("Tìm", 15);
        JButton btnAdd = new RoundedButton("Thêm mới", 15);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));

        btnSearch.addActionListener(e -> {
            String kw = tfSearch.getText().trim().toLowerCase();
            model.setRowCount(0);
            for (Customer c : dao.getAllCustomers()) {
                if (c.getName().toLowerCase().contains(kw)
                        || c.getPhone().contains(kw)) {
                    model.addRow(new Object[]{
                        c.getId(), c.getName(), c.getPhone(), c.getEmail(), "Hành động"
                    });
                }
            }
        });

        btnAdd.addActionListener(e -> {
            CustomerFormDialog dlg = new CustomerFormDialog(null, "Thêm khách hàng", null);
            dlg.setVisible(true);
            if (dlg.isSubmitted()) {
                dao.insertCustomer(dlg.getCustomerData(0));
                loadTable();
            }
        });

        JPanel searchBar = new JPanel(new BorderLayout());
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        left.add(btnAdd);
        right.add(new JLabel("Tìm kiếm:"));
        right.add(tfSearch);
        right.add(btnSearch);
        searchBar.add(left, BorderLayout.WEST);
        searchBar.add(right, BorderLayout.EAST);

        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.add(searchBar, BorderLayout.NORTH);
        tableWrap.add(scroll, BorderLayout.CENTER);

        add(inputWrapper, BorderLayout.NORTH);
        add(tableWrap, BorderLayout.CENTER);
        loadTable();
    }

    private void styleTable(JTable tbl) {
        JTableHeader hd = tbl.getTableHeader();
        hd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        hd.setBackground(new Color(230, 230, 230));
        hd.setPreferredSize(new Dimension(100, 35));
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        DefaultTableCellRenderer ctr = new DefaultTableCellRenderer();
        ctr.setHorizontalAlignment(JLabel.CENTER);
        ctr.setVerticalAlignment(JLabel.CENTER);
        for (int i = 0; i < tbl.getColumnCount(); i++) {
            tbl.getColumnModel().getColumn(i).setCellRenderer(ctr);
        }
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
