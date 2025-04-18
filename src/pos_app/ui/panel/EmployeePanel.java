package pos_app.ui.panel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import pos_app.ui.components.RoundedButton;
import pos_app.ui.components.RoundedTextField;
import java.util.List;
import pos_app.dao.EmployeeDAO;
import pos_app.models.Employee;
import pos_app.ui.dialog.EmployeeFormDialog;
import pos_app.ui.table.EmployeeButtonEditor;

public class EmployeePanel extends JPanel {

    private final EmployeeDAO dao = new EmployeeDAO();
    private DefaultTableModel model;
    private JTable table;

    public EmployeePanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel control = new JPanel(new GridLayout(1, 3, 10, 0));
        control.setBorder(BorderFactory.createTitledBorder("Thuộc tính nhân viên"));

        String[] attrBtns = {
            "Quản lý chức vụ", "Quản lý phòng ban", "Quản lý ca làm"
        };
        for (String txt : attrBtns) {
            JButton b = new RoundedButton(txt, 10);
            b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            b.setPreferredSize(new Dimension(0, 32));
            control.add(b);
        }

        JPanel wrapper = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = .3;
        wrapper.add(control, gbc);

        model = new DefaultTableModel(
                new String[]{"ID", "Tên", "Username", "Role", "SĐT", "Email", "Hành động"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 6;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        styleTable(table);
        table.getColumn("Hành động").setCellRenderer(new EmployeeButtonEditor.BtnRenderer());
        table.getColumn("Hành động").setCellEditor(
                new EmployeeButtonEditor(model, dao, this::loadTable, this::showForm));

        RoundedTextField tfSearch = new RoundedTextField(20, 15);
        JButton btnSearch = new RoundedButton("Tìm", 15);
        JButton btnAdd = new RoundedButton("Thêm mới", 15);

        btnSearch.addActionListener(e -> doSearch(tfSearch.getText().trim()));
        btnAdd.addActionListener(e -> showForm(null));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách nhân viên"));

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

        add(wrapper, BorderLayout.NORTH);
        add(tableWrap, BorderLayout.CENTER);

        loadTable();
    }

    private void styleTable(JTable t) {
        JTableHeader hd = t.getTableHeader();
        hd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        hd.setBackground(new Color(230, 230, 230));
        hd.setPreferredSize(new Dimension(100, 35));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        DefaultTableCellRenderer ctr = new DefaultTableCellRenderer();
        ctr.setHorizontalAlignment(JLabel.CENTER);
        ctr.setVerticalAlignment(JLabel.CENTER);
        for (int i = 0; i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setCellRenderer(ctr);
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
            if (existing == null) {
                dao.insertEmployee(dlg.getEmployeeData(0));
            } else {
                dao.updateEmployee(dlg.getEmployeeData(existing.getId()));
            }
            loadTable();
        }
    }
}
