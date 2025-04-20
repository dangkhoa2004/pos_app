/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package pos_app.ui.dialog;

import javax.swing.*;
import java.awt.*;
import pos_app.models.Employee;

/**
 * EmployeeFormDialog là cửa sổ nhập liệu để thêm hoặc chỉnh sửa nhân viên.
 *
 * Giao diện cho phép nhập: họ tên, username, mật khẩu, role ID, số điện thoại
 * và email. Có xác thực đầu vào và yêu cầu xác nhận trước khi lưu dữ liệu. Được
 * sử dụng kết hợp với danh sách nhân viên trong hệ thống POS.
 *
 * @author 04dkh
 */
public class EmployeeFormDialog extends JDialog {

    /**
     * Ô nhập họ tên nhân viên
     */
    private final JTextField tfName = new JTextField(20);

    /**
     * Ô nhập tên đăng nhập
     */
    private final JTextField tfUser = new JTextField(20);

    /**
     * Ô nhập mật khẩu
     */
    private final JPasswordField pfPass = new JPasswordField(20);

    /**
     * Ô nhập role ID
     */
    private final JTextField tfRole = new JTextField(20);

    /**
     * Ô nhập số điện thoại
     */
    private final JTextField tfPhone = new JTextField(20);

    /**
     * Ô nhập email
     */
    private final JTextField tfEmail = new JTextField(20);

    /**
     * Cờ xác nhận người dùng đã nhấn "Lưu"
     */
    private boolean submitted = false;

    /**
     * Đối tượng nhân viên gốc (nếu đang chỉnh sửa)
     */
    private final Employee originalEmployee;

    /**
     * Tạo form thêm hoặc sửa thông tin nhân viên.
     *
     * @param parent cửa sổ cha
     * @param title tiêu đề dialog
     * @param emp đối tượng nhân viên hiện tại, null nếu thêm mới
     */
    public EmployeeFormDialog(JFrame parent, String title, Employee emp) {
        super(parent, title, true);
        this.originalEmployee = emp;

        setSize(480, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        formPanel.add(createFormRow("Họ tên:", tfName));
        formPanel.add(createFormRow("Username:", tfUser));
        formPanel.add(createFormRow("Mật khẩu:", pfPass));
        formPanel.add(createFormRow("Role ID:", tfRole));
        formPanel.add(createFormRow("Số điện thoại:", tfPhone));
        formPanel.add(createFormRow("Email:", tfEmail));

        if (emp != null) {
            tfName.setText(emp.getName());
            tfUser.setText(emp.getUsername());
            tfRole.setText(String.valueOf(emp.getRole_id()));
            tfPhone.setText(emp.getPhone());
            tfEmail.setText(emp.getEmail());
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
     * Tạo một dòng nhập gồm label và ô nhập (textfield, passwordfield).
     *
     * @param labelText nhãn của trường
     * @param inputField thành phần nhập liệu
     * @return JPanel chứa dòng nhập
     */
    private JPanel createFormRow(String labelText, JComponent inputField) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 25));

        inputField.setPreferredSize(new Dimension(0, 30));
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel.add(label, BorderLayout.WEST);
        panel.add(inputField, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Xử lý xác nhận khi người dùng nhấn "Lưu". Thực hiện kiểm tra hợp lệ trước
     * khi đóng dialog.
     */
    private void handleSubmit() {
        String name = tfName.getText().trim();
        String user = tfUser.getText().trim();
        String pwd = new String(pfPass.getPassword()).trim();
        String roleText = tfRole.getText().trim();
        String phone = tfPhone.getText().trim();

        if (name.isEmpty()) {
            showError("Vui lòng nhập họ tên.", tfName);
            return;
        }

        if (user.isEmpty()) {
            showError("Vui lòng nhập Username.", tfUser);
            return;
        }

        if (pwd.isEmpty() && originalEmployee == null) {
            showError("Vui lòng nhập mật khẩu.", pfPass);
            return;
        }

        if (roleText.isEmpty() || !roleText.matches("\\d+")) {
            showError("Role ID không hợp lệ.", tfRole);
            return;
        }

        if (phone.isEmpty() || !phone.matches("^0\\d{9}$")) {
            showError("Số điện thoại phải có 10 số và bắt đầu bằng 0.", tfPhone);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn lưu thông tin nhân viên?",
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
     * Hiển thị thông báo lỗi và focus vào thành phần bị lỗi.
     *
     * @param message nội dung lỗi
     * @param fieldToFocus trường cần focus lại
     */
    private void showError(String message, JComponent fieldToFocus) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        fieldToFocus.requestFocus();
    }

    /**
     * Kiểm tra xem form đã được xác nhận hay chưa.
     *
     * @return true nếu đã xác nhận, false nếu chưa
     */
    public boolean isSubmitted() {
        return submitted;
    }

    /**
     * Lấy dữ liệu nhân viên từ form. Nếu đang cập nhật và không nhập mật khẩu
     * mới, giữ nguyên mật khẩu cũ.
     *
     * @return đối tượng Employee chứa dữ liệu từ form
     */
    public Employee getEmployeeData() {
        String password = new String(pfPass.getPassword()).trim();
        if (originalEmployee != null && password.isEmpty()) {
            password = originalEmployee.getPassword(); // giữ lại mật khẩu cũ nếu không thay đổi
        }

        return new Employee(
                originalEmployee == null ? 0 : originalEmployee.getId(),
                tfName.getText().trim(),
                tfUser.getText().trim(),
                password,
                Integer.parseInt(tfRole.getText().trim()),
                tfPhone.getText().trim(),
                tfEmail.getText().trim()
        );
    }
}
