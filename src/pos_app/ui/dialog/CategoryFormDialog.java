/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package pos_app.ui.dialog;

import javax.swing.*;
import java.awt.*;
import pos_app.models.Category;

/**
 * CategoryFormDialog là cửa sổ nhập liệu để thêm hoặc cập nhật loại sản phẩm.
 *
 * Cung cấp giao diện gồm ô nhập tên, mô tả, cùng với nút lưu và hủy. Dùng trong
 * CategoryDialog để xử lý CRUD.
 *
 * @author 04dkh
 */
public class CategoryFormDialog extends JDialog {

    /**
     * Ô nhập tên loại sản phẩm
     */
    private final JTextField tfName = new JTextField(20);

    /**
     * Ô nhập mô tả loại sản phẩm
     */
    private final JTextArea taDescription = new JTextArea(3, 20);

    /**
     * Cờ đánh dấu người dùng đã bấm Lưu
     */
    private boolean submitted = false;

    /**
     * Dữ liệu gốc nếu đang chỉnh sửa, null nếu thêm mới
     */
    private final Category originalCategory;

    /**
     * Tạo dialog nhập liệu cho loại sản phẩm.
     *
     * @param owner cửa sổ cha
     * @param title tiêu đề cửa sổ
     * @param category đối tượng Category để chỉnh sửa, null nếu thêm mới
     */
    public CategoryFormDialog(Window owner, String title, Category category) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        this.originalCategory = category;

        setSize(450, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        formPanel.add(createFormRow("Tên loại sản phẩm:", tfName));
        formPanel.add(createTextAreaRow("Mô tả:", taDescription));

        if (category != null) {
            tfName.setText(category.getName());
            taDescription.setText(category.getDescription());
        }

        JButton btnSubmit = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");

        btnSubmit.setPreferredSize(new Dimension(100, 35));
        btnCancel.setPreferredSize(new Dimension(100, 35));

        btnSubmit.addActionListener(e -> handleSubmit());
        btnCancel.addActionListener(e -> dispose());

        JPanel actionPanel = new JPanel();
        actionPanel.add(btnSubmit);
        actionPanel.add(btnCancel);

        add(formPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    /**
     * Tạo một dòng nhập gồm label và JTextField.
     *
     * @param labelText tên trường dữ liệu
     * @param inputField ô nhập liệu
     * @return JPanel chứa dòng nhập
     */
    private JPanel createFormRow(String labelText, JTextField inputField) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(140, 25));

        inputField.setPreferredSize(new Dimension(0, 30));
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel.add(label, BorderLayout.WEST);
        panel.add(inputField, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Tạo một dòng nhập gồm label và JTextArea có scroll.
     *
     * @param labelText tên trường dữ liệu
     * @param textArea ô nhập mô tả
     * @return JPanel chứa dòng nhập
     */
    private JPanel createTextAreaRow(String labelText, JTextArea textArea) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(140, 25));

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(0, 70));

        panel.add(label, BorderLayout.WEST);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Xử lý khi người dùng nhấn nút "Lưu". Kiểm tra dữ liệu đầu vào hợp lệ
     * trước khi đóng dialog.
     */
    private void handleSubmit() {
        String name = tfName.getText().trim();
        String desc = taDescription.getText().trim();

        if (name.isEmpty()) {
            showError("Tên loại sản phẩm không được để trống.", tfName);
            return;
        }

        if (desc.length() < 5) {
            showError("Mô tả nên dài ít nhất 5 ký tự để rõ ràng hơn.", taDescription);
            return;
        }

        submitted = true;
        dispose();
    }

    /**
     * Hiển thị thông báo lỗi và focus vào trường bị lỗi.
     *
     * @param message thông báo lỗi
     * @param component thành phần cần focus lại
     */
    private void showError(String message, JComponent component) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.WARNING_MESSAGE);
        component.requestFocus();
    }

    /**
     * Kiểm tra xem người dùng đã nhấn "Lưu" hay chưa.
     *
     * @return true nếu đã lưu, false nếu hủy
     */
    public boolean isSubmitted() {
        return submitted;
    }

    /**
     * Lấy dữ liệu từ form nhập dưới dạng đối tượng Category.
     *
     * @return đối tượng Category được nhập
     */
    public Category getData() {
        return new Category(
                originalCategory == null ? 0 : originalCategory.getId(),
                tfName.getText().trim(),
                taDescription.getText().trim()
        );
    }
}
