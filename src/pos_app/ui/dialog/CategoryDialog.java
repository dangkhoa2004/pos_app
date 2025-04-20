/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package pos_app.ui.dialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import pos_app.dao.CategoryDAO;
import pos_app.models.Category;

/**
 * CategoryDialog là cửa sổ quản lý danh sách loại sản phẩm.
 *
 * Giao diện cho phép người dùng: - Xem danh sách loại sản phẩm - Thêm, sửa, xóa
 * loại sản phẩm thông qua CategoryFormDialog
 *
 * Sử dụng CategoryDAO để thao tác dữ liệu.
 *
 * @author 04dkh
 */
public class CategoryDialog extends JDialog {

    /**
     * Model bảng hiển thị loại sản phẩm
     */
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Tên", "Mô tả"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Không cho sửa trực tiếp trong bảng
        }
    };

    /**
     * Bảng hiển thị dữ liệu loại sản phẩm
     */
    private final JTable table = new JTable(model);

    /**
     * DAO dùng để thao tác với bảng categories
     */
    private final CategoryDAO dao = new CategoryDAO();

    /**
     * Khởi tạo dialog quản lý loại sản phẩm.
     *
     * @param owner cửa sổ cha để canh giữa dialog
     */
    public CategoryDialog(Window owner) {
        super(owner, "Quản lý loại sản phẩm", ModalityType.APPLICATION_MODAL);
        setSize(600, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Cấu hình bảng
        table.setRowHeight(36);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách loại sản phẩm"));

        // Các nút thao tác
        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");

        // Thêm mới loại sản phẩm
        btnAdd.addActionListener(e -> {
            CategoryFormDialog form = new CategoryFormDialog(this, "Thêm loại sản phẩm", null);
            form.setVisible(true);
            if (form.isSubmitted()) {
                dao.insertCategory(form.getData());
                loadTable();
            }
        });

        // Sửa loại sản phẩm
        btnEdit.addActionListener(e -> handleEdit());

        // Xóa loại sản phẩm
        btnDelete.addActionListener(e -> handleDelete());

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);

        add(scroll, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
        loadTable();
    }

    /**
     * Xử lý sự kiện sửa loại sản phẩm được chọn.
     */
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

    /**
     * Xử lý sự kiện xóa loại sản phẩm được chọn.
     */
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

    /**
     * Tải lại toàn bộ dữ liệu từ database và hiển thị lên bảng.
     */
    private void loadTable() {
        model.setRowCount(0);
        for (Category c : dao.getAllCategories()) {
            model.addRow(new Object[]{c.getId(), c.getName(), c.getDescription()});
        }
    }
}
