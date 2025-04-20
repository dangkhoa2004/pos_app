/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package pos_app.ui.dialog;

import javax.swing.*;
import java.awt.*;
import pos_app.models.Customer;

/**
 * CustomerFormDialog là dialog để thêm hoặc chỉnh sửa thông tin khách hàng.
 *
 * Cung cấp các trường nhập liệu gồm: tên, số điện thoại, email và địa chỉ. Có
 * xác thực đầu vào và yêu cầu xác nhận trước khi lưu. Được sử dụng trong các
 * chức năng CRUD khách hàng.
 *
 * @author 04dkh
 */
public class CustomerFormDialog extends JDialog {

    /**
     * Ô nhập tên khách hàng
     */
    private final JTextField tfName = new JTextField(20);

    /**
     * Ô nhập số điện thoại khách hàng
     */
    private final JTextField tfPhone = new JTextField(20);

    /**
     * Ô nhập email khách hàng
     */
    private final JTextField tfEmail = new JTextField(20);

    /**
     * Ô nhập địa chỉ khách hàng
     */
    private final JTextField tfAddress = new JTextField(20);

    /**
     * Cờ đánh dấu form đã được xác nhận
     */
    private boolean submitted = false;

    /**
     * Tạo dialog thêm hoặc chỉnh sửa khách hàng.
     *
     * @param parent cửa sổ cha
     * @param title tiêu đề dialog
     * @param existing khách hàng đang chỉnh sửa (null nếu thêm mới)
     */
    public CustomerFormDialog(JFrame parent, String title, Customer existing) {
        super(parent, title, true);
        setSize(450, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        formPanel.add(createFormRow("Tên khách hàng:", tfName));
        formPanel.add(createFormRow("Số điện thoại:", tfPhone));
        formPanel.add(createFormRow("Email:", tfEmail));
        formPanel.add(createFormRow("Địa chỉ:", tfAddress));

        if (existing != null) {
            tfName.setText(existing.getName());
            tfPhone.setText(existing.getPhone());
            tfEmail.setText(existing.getEmail());
            tfAddress.setText(existing.getAddress());
        }

        JButton btnOK = new JButton("Xác nhận");
        btnOK.setPreferredSize(new Dimension(100, 35));
        btnOK.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnOK.addActionListener(e -> handleSubmit());

        JPanel footer = new JPanel();
        footer.add(btnOK);

        add(formPanel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    /**
     * Tạo dòng nhập liệu gồm label và JTextField.
     *
     * @param labelText tiêu đề trường
     * @param inputField ô nhập liệu
     * @return panel chứa dòng nhập
     */
    private JPanel createFormRow(String labelText, JTextField inputField) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(120, 25));

        inputField.setPreferredSize(new Dimension(0, 30));
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel.add(label, BorderLayout.WEST);
        panel.add(inputField, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Xử lý khi người dùng nhấn "Xác nhận". Kiểm tra hợp lệ tên và số điện
     * thoại trước khi xác nhận.
     */
    private void handleSubmit() {
        String name = tfName.getText().trim();
        String phone = tfPhone.getText().trim();

        if (name.isEmpty()) {
            showError("Vui lòng nhập tên khách hàng.", tfName);
            return;
        }

        if (phone.isEmpty()) {
            showError("Vui lòng nhập số điện thoại.", tfPhone);
            return;
        }

        if (!phone.matches("^0\\d{9}$")) {
            showError("Số điện thoại không hợp lệ. Phải bắt đầu bằng 0 và đủ 10 chữ số.", tfPhone);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn lưu thông tin khách hàng này?",
                "Xác nhận lưu",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            submitted = true;
            setVisible(false);
        }
    }

    /**
     * Hiển thị hộp thoại lỗi và focus vào thành phần bị lỗi.
     *
     * @param message thông báo lỗi
     * @param component trường bị lỗi
     */
    private void showError(String message, JComponent component) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        component.requestFocus();
    }

    /**
     * Kiểm tra xem người dùng đã xác nhận form hay chưa.
     *
     * @return true nếu đã xác nhận, false nếu chưa
     */
    public boolean isSubmitted() {
        return submitted;
    }

    /**
     * Lấy dữ liệu khách hàng được nhập từ form.
     *
     * @param id mã khách hàng (nếu đang cập nhật)
     * @return đối tượng Customer
     */
    public Customer getCustomerData(int id) {
        return new Customer(
                id,
                tfName.getText().trim(),
                tfPhone.getText().trim(),
                tfEmail.getText().trim(),
                tfAddress.getText().trim()
        );
    }
}
