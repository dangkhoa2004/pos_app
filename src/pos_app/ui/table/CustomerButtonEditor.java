package pos_app.ui.table;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import pos_app.dao.CustomerDAO;
import pos_app.models.Customer;
import pos_app.ui.dialog.CustomerFormDialog;

public class CustomerButtonEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {

    enum Click { NONE, EDIT, DELETE }

    private Click click = Click.NONE;
    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
    private final JButton btnEdit = new JButton("Sửa");
    private final JButton btnDel = new JButton("Xóa");

    private final DefaultTableModel model;
    private final CustomerDAO dao;
    private final Runnable reload;
    private int row;

    public CustomerButtonEditor(DefaultTableModel model, CustomerDAO dao, Runnable reload) {
        this.model = model;
        this.dao = dao;
        this.reload = reload;

        btnEdit.setBackground(new Color(46, 204, 113));
        btnDel.setBackground(new Color(231, 76, 60));
        btnEdit.setForeground(Color.WHITE);
        btnDel.setForeground(Color.WHITE);

        btnEdit.addActionListener(e -> {
            click = Click.EDIT;
            fireEditingStopped();
        });

        btnDel.addActionListener(e -> {
            click = Click.DELETE;
            fireEditingStopped();
        });

        panel.add(btnEdit);
        panel.add(btnDel);
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable tbl, Object v, boolean sel, int r, int c) {
        row = r;
        click = Click.NONE;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        int id = (int) model.getValueAt(row, 0);
        String name = (String) model.getValueAt(row, 1);
        String phone = (String) model.getValueAt(row, 2);
        String email = (String) model.getValueAt(row, 3);
        String address = ""; // Bạn có thể thêm cột Address vào table nếu cần

        switch (click) {
            case EDIT -> {
                CustomerFormDialog dlg = new CustomerFormDialog(
                        null, "Sửa khách hàng",
                        new Customer(id, name, phone, email, address));
                dlg.setVisible(true);
                if (dlg.isSubmitted()) {
                    dao.updateCustomer(dlg.getCustomerData(id));
                    reload.run();
                }
            }
            case DELETE -> {
                int ok = JOptionPane.showConfirmDialog(
                        null, "Xóa khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) {
                    dao.deleteCustomer(id);
                    reload.run();
                }
            }
        }
        return "";
    }
}
