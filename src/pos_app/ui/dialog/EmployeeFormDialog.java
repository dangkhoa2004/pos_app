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

        setLayout(new GridBagLayout());
        setSize(450, 340);
        setLocationRelativeTo(parent);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        if (emp != null) {
            tfName.setText(emp.getName());
            tfUser.setText(emp.getUsername());
            tfRole.setText(String.valueOf(emp.getRole_id()));
            tfPhone.setText(emp.getPhone());
            tfEmail.setText(emp.getEmail());
        }

        g.gridx = 0;
        g.gridy = 0;
        add(new JLabel("Họ tên:"), g);
        g.gridx = 1;
        add(tfName, g);

        g.gridx = 0;
        g.gridy = 1;
        add(new JLabel("Username:"), g);
        g.gridx = 1;
        add(tfUser, g);

        g.gridx = 0;
        g.gridy = 2;
        add(new JLabel("Mật khẩu:"), g);
        g.gridx = 1;
        add(pfPass, g);

        g.gridx = 0;
        g.gridy = 3;
        add(new JLabel("Role ID:"), g);
        g.gridx = 1;
        add(tfRole, g);

        g.gridx = 0;
        g.gridy = 4;
        add(new JLabel("Số điện thoại:"), g);
        g.gridx = 1;
        add(tfPhone, g);

        g.gridx = 0;
        g.gridy = 5;
        add(new JLabel("Email:"), g);
        g.gridx = 1;
        add(tfEmail, g);

        JButton btnOK = new JButton("Xác nhận");
        btnOK.addActionListener(e -> {
            String name = tfName.getText().trim();
            String user = tfUser.getText().trim();
            String pwd = new String(pfPass.getPassword()).trim();
            String roleText = tfRole.getText().trim();
            String phone = tfPhone.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                tfName.requestFocus();
                return;
            }

            if (user.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Username.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                tfUser.requestFocus();
                return;
            }

            if (pwd.isEmpty() && originalEmployee == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                pfPass.requestFocus();
                return;
            }

            if (roleText.isEmpty() || !roleText.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Role ID không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                tfRole.requestFocus();
                return;
            }

            if (phone.isEmpty() || !phone.matches("^0\\d{9}$")) {
                JOptionPane.showMessageDialog(this, "Số điện thoại phải có 10 số và bắt đầu bằng 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                tfPhone.requestFocus();
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
        });

        g.gridx = 0;
        g.gridy = 6;
        g.gridwidth = 2;
        g.anchor = GridBagConstraints.CENTER;
        add(btnOK, g);
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public Employee getEmployeeData() {
        String password = new String(pfPass.getPassword()).trim();
        // Nếu đang sửa và mật khẩu không nhập → giữ nguyên
        if (originalEmployee != null && password.isEmpty()) {
            password = originalEmployee.getPassword(); // bạn cần truyền password từ DB vào nếu muốn giữ
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
