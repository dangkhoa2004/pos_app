package pos_app.ui.dialog;

import javax.swing.*;
import java.awt.*;
import pos_app.models.Employee;

public class EmployeeFormDialog extends JDialog {

    private final JTextField tfName = new JTextField(20);
    private final JTextField tfUser = new JTextField(20);
    private final JPasswordField pfPass = new JPasswordField(20);
    private final JTextField tfRole = new JTextField(20);
    private final JTextField tfPhone = new JTextField(20);
    private final JTextField tfEmail = new JTextField(20);
    private boolean submitted = false;

    private final Employee originalEmployee;

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

    private void showError(String message, JComponent fieldToFocus) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        fieldToFocus.requestFocus();
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public Employee getEmployeeData() {
        String password = new String(pfPass.getPassword()).trim();
        if (originalEmployee != null && password.isEmpty()) {
            password = originalEmployee.getPassword(); // đảm bảo bạn load sẵn mật khẩu từ DB nếu cần
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
