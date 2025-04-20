package pos_app.ui.dialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import pos_app.dao.CategoryDAO;
import pos_app.models.Category;

public class CategoryDialog extends JDialog {

    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Tên", "Mô tả"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Không cho sửa trực tiếp trong bảng
        }
    };
    private final JTable table = new JTable(model);
    private final CategoryDAO dao = new CategoryDAO();

    public CategoryDialog(Window owner) {
        super(owner, "Quản lý loại sản phẩm", ModalityType.APPLICATION_MODAL);
        setSize(600, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách loại sản phẩm"));

        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");

        btnAdd.addActionListener(e -> {
            CategoryFormDialog form = new CategoryFormDialog(this, "Thêm loại sản phẩm", null);
            form.setVisible(true);
            if (form.isSubmitted()) {
                dao.insertCategory(form.getData());
                loadTable();
            }
        });

        btnEdit.addActionListener(e -> handleEdit());

        btnDelete.addActionListener(e -> handleDelete());

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);

        add(scroll, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
        loadTable();
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để sửa.", "Chưa chọn mục", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Category old = new Category(
                (int) model.getValueAt(row, 0),
                (String) model.getValueAt(row, 1),
                (String) model.getValueAt(row, 2)
        );

        CategoryFormDialog form = new CategoryFormDialog(this, "Cập nhật loại sản phẩm", old);
        form.setVisible(true);

        if (form.isSubmitted()) {
            Category updated = form.getData();
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn cập nhật loại sản phẩm \"" + updated.getName() + "\"?",
                    "Xác nhận cập nhật",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                dao.updateCategory(updated);
                loadTable();
            }
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa.", "Chưa chọn mục", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa loại sản phẩm này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) model.getValueAt(row, 0);
            dao.deleteCategory(id);
            loadTable();
        }
    }

    private void loadTable() {
        model.setRowCount(0);
        for (Category c : dao.getAllCategories()) {
            model.addRow(new Object[]{c.getId(), c.getName(), c.getDescription()});
        }
    }
}
