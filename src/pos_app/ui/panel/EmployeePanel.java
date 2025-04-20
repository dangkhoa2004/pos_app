package pos_app.ui.panel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import pos_app.dao.EmployeeDAO;
import pos_app.models.Employee;
import pos_app.ui.components.RoundedButton;
import pos_app.ui.components.RoundedTextField;
import pos_app.ui.dialog.EmployeeFormDialog;
import pos_app.ui.table.EmployeeButtonEditor;

public class EmployeePanel extends JPanel {

    private final EmployeeDAO dao = new EmployeeDAO();
    private final DefaultTableModel model;
    private final JTable table;

    public EmployeePanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ==== Model & Table ====
        model = new DefaultTableModel(new String[]{"ID", "Tên", "Username", "Role", "SĐT", "Email", "Hành động"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        table = new JTable(model);
        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setBackground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(230, 230, 230));
        table.getTableHeader().setPreferredSize(new Dimension(100, 35));
        styleTable(table);

        table.getColumn("Hành động").setCellRenderer(new EmployeeButtonEditor.BtnRenderer());
        table.getColumn("Hành động").setCellEditor(
                new EmployeeButtonEditor(model, dao, this::loadTable, this::showForm));

        // ==== Thuộc tính nhân viên ====
        JPanel attrPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        attrPanel.setBorder(BorderFactory.createTitledBorder("Thuộc tính nhân viên"));
        attrPanel.setBackground(Color.WHITE);
        String[] btnNames = {"Quản lý chức vụ", "Quản lý phòng ban", "Quản lý ca làm"};
        for (String name : btnNames) {
            JButton btn = new RoundedButton(name, 10);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btn.setBackground(Color.WHITE);
            btn.setPreferredSize(new Dimension(0, 32));
            attrPanel.add(btn);
        }

        JPanel attrWrapper = new JPanel(new BorderLayout());
        attrWrapper.setBackground(Color.WHITE);
        attrWrapper.add(attrPanel, BorderLayout.CENTER);

        // ==== Thanh tìm kiếm & thêm mới ====
        RoundedTextField tfSearch = new RoundedTextField(20, 15);
        tfSearch.setBackground(new Color(245, 245, 245));

        JButton btnSearch = new RoundedButton("Tìm", 15);
        JButton btnAdd = new RoundedButton("Thêm mới", 15);

        btnSearch.addActionListener(e -> doSearch(tfSearch.getText().trim()));
        btnAdd.addActionListener(e -> showForm(null));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(btnAdd);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(new JLabel("Tìm kiếm:"));
        rightPanel.add(tfSearch);
        rightPanel.add(btnSearch);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(leftPanel, BorderLayout.WEST);
        searchPanel.add(rightPanel, BorderLayout.EAST);

        // ==== Bảng danh sách nhân viên ====
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách nhân viên"));
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(Color.WHITE);
        tableWrapper.add(searchPanel, BorderLayout.NORTH);
        tableWrapper.add(scrollPane, BorderLayout.CENTER);

        // ==== Gắn các thành phần ====
        add(attrWrapper, BorderLayout.NORTH);
        add(tableWrapper, BorderLayout.CENTER);

        // ==== Load dữ liệu ban đầu ====
        loadTable();
    }

    private void styleTable(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setVerticalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void loadTable() {
        model.setRowCount(0);
        List<Employee> list = dao.getAllEmployees();
        for (Employee e : list) {
            model.addRow(new Object[]{
                e.getId(), e.getName(), e.getUsername(), e.getRole_id(),
                e.getPhone(), e.getEmail(), "Hành động"
            });
        }
    }

    private void doSearch(String kw) {
        model.setRowCount(0);
        kw = kw.toLowerCase();
        for (Employee e : dao.getAllEmployees()) {
            if (e.getName().toLowerCase().contains(kw)
                    || e.getUsername().toLowerCase().contains(kw)
                    || e.getPhone().contains(kw)) {
                model.addRow(new Object[]{
                    e.getId(), e.getName(), e.getUsername(),
                    e.getRole_id(), e.getPhone(), e.getEmail(), "Hành động"
                });
            }
        }
    }

    private void showForm(Employee existing) {
        EmployeeFormDialog dlg = new EmployeeFormDialog(null,
                existing == null ? "Thêm nhân viên" : "Sửa nhân viên", existing);
        dlg.setVisible(true);
        if (dlg.isSubmitted()) {
            Employee emp = dlg.getEmployeeData();
            if (existing == null) {
                dao.insertEmployee(emp);
            } else {
                dao.updateEmployee(emp);
            }
            loadTable();
        }
    }
}
