/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.ui.dialog;

/**
 *
 * @author 04dkh
 */
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import pos_app.dao.CategoryDAO;
import pos_app.models.Category;

public class CategoryDialog extends JDialog {

    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Tên", "Mô tả"}, 0);
    private final JTable table = new JTable(model);
    private final CategoryDAO dao = new CategoryDAO();

    public CategoryDialog(Window owner) {
        super(owner, "Quản lý loại sản phẩm", ModalityType.APPLICATION_MODAL);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");

        btnAdd.addActionListener(e -> {
            CategoryFormDialog form = new CategoryFormDialog(null, "Thêm loại sản phẩm", null);
            form.setVisible(true);
            if (form.isSubmitted()) {
                dao.insertCategory(form.getData());
                loadTable();
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Category old = new Category(
                        (int) model.getValueAt(row, 0),
                        (String) model.getValueAt(row, 1),
                        (String) model.getValueAt(row, 2)
                );

                CategoryFormDialog form = new CategoryFormDialog(null, "Cập nhật loại sản phẩm", old);
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
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn một dòng để sửa.",
                        "Chưa chọn mục",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Bạn có chắc chắn muốn xóa?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = (int) model.getValueAt(row, 0);
                    dao.deleteCategory(id);
                    loadTable();
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn một dòng để xóa.",
                        "Chưa chọn mục",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách loại sản phẩm"));

        add(scroll, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
        loadTable();
    }

    private void loadTable() {
        model.setRowCount(0);
        for (Category c : dao.getAllCategories()) {
            model.addRow(new Object[]{c.getId(), c.getName(), c.getDescription()});
        }
    }
}
