package pos_app.ui.table;

import pos_app.dao.ProductDAO;
import pos_app.models.Product;
import pos_app.ui.dialog.ProductFormDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.TableCellEditor;

public class ProductButtonEditor extends AbstractCellEditor implements TableCellEditor {

    enum Clicked {
        NONE, EDIT, DELETE
    }
    private Clicked clicked = Clicked.NONE;
    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
    private final JButton btnEdit = new JButton("Sửa");
    private final JButton btnDelete = new JButton("Xóa");
    private final DefaultTableModel model;
    private final ProductDAO dao;
    private final Runnable reloadAction;
    private int row;

    public ProductButtonEditor(DefaultTableModel model, ProductDAO dao, Runnable reloadAction) {
        this.model = model;
        this.dao = dao;
        this.reloadAction = reloadAction;

        btnEdit.setBackground(new Color(46, 204, 113));
        btnEdit.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);

        btnEdit.addActionListener(e -> {
            clicked = Clicked.EDIT;
            fireEditingStopped();
        });
        btnDelete.addActionListener(e -> {
            clicked = Clicked.DELETE;
            fireEditingStopped();
        });

        panel.add(btnEdit);
        panel.add(btnDelete);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        clicked = Clicked.NONE;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        int id = (int) model.getValueAt(row, 0);
        String barcode = (String) model.getValueAt(row, 1);
        String name = (String) model.getValueAt(row, 2);
        double price = (double) model.getValueAt(row, 3);
        int quantity = (int) model.getValueAt(row, 4);
        String imagePath = (String) model.getValueAt(row, 5);

        switch (clicked) {
            case EDIT -> {
                ProductFormDialog dlg = new ProductFormDialog(null, "Sửa sản phẩm",
                        new Product(id, barcode, name, price, quantity, imagePath));
                dlg.setVisible(true);
                if (dlg.isSubmitted()) {
                    dao.updateProduct(dlg.getProductData(id));
                    reloadAction.run();
                }
            }
            case DELETE -> {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Bạn chắc chắn muốn xóa sản phẩm này?", "Xác nhận",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dao.deleteProduct(id);
                    reloadAction.run();
                }
            }
        }
        return "";
    }

    public static class BtnRenderer extends JPanel implements javax.swing.table.TableCellRenderer {

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
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }
}
