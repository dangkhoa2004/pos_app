package pos_app.ui.table;

import pos_app.dao.EmployeeDAO;
import pos_app.models.Employee;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class EmployeeButtonEditor extends AbstractCellEditor implements TableCellEditor {

    public static class BtnRenderer extends JPanel implements TableCellRenderer {
        private final JButton edit = new JButton("Sửa");
        private final JButton del = new JButton("Xóa");

        public BtnRenderer() {
            setOpaque(true);
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            edit.setFocusable(false);
            del.setFocusable(false);
            edit.setBackground(new Color(46, 204, 113));
            del.setBackground(new Color(231, 76, 60));
            edit.setForeground(Color.WHITE);
            del.setForeground(Color.WHITE);
            add(edit);
            add(del);
        }

        @Override
        public Component getTableCellRendererComponent(JTable tbl, Object v, boolean sel, boolean f, int r, int c) {
            setBackground(sel ? tbl.getSelectionBackground() : tbl.getBackground());
            return this;
        }
    }

    enum Click { NONE, EDIT, DELETE }

    private Click click = Click.NONE;
    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
    private final JButton edit = new JButton("Sửa");
    private final JButton del = new JButton("Xóa");

    private final DefaultTableModel model;
    private final EmployeeDAO dao;
    private final Runnable reload;
    private final java.util.function.Consumer<Employee> showForm;
    private int row;

    public EmployeeButtonEditor(DefaultTableModel model, EmployeeDAO dao, Runnable reload, java.util.function.Consumer<Employee> showForm) {
        this.model = model;
        this.dao = dao;
        this.reload = reload;
        this.showForm = showForm;

        edit.setBackground(new Color(46, 204, 113));
        del.setBackground(new Color(231, 76, 60));
        edit.setForeground(Color.WHITE);
        del.setForeground(Color.WHITE);

        edit.addActionListener(e -> {
            click = Click.EDIT;
            fireEditingStopped();
        });
        del.addActionListener(e -> {
            click = Click.DELETE;
            fireEditingStopped();
        });

        panel.add(edit);
        panel.add(del);
    }

    @Override
    public Component getTableCellEditorComponent(JTable tbl, Object v, boolean sel, int r, int c) {
        row = r;
        click = Click.NONE;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        int id = (int) model.getValueAt(row, 0);
        String name = (String) model.getValueAt(row, 1);
        String username = (String) model.getValueAt(row, 2);
        int role = (int) model.getValueAt(row, 3);
        String phone = (String) model.getValueAt(row, 4);
        String email = (String) model.getValueAt(row, 5);

        switch (click) {
            case EDIT -> showForm.accept(new Employee(id, name, username, "", role, phone, email));
            case DELETE -> {
                int ok = JOptionPane.showConfirmDialog(null, "Xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) {
                    dao.deleteEmployee(id);
                    reload.run();
                }
            }
        }
        return "";
    }
}

