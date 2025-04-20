package pos_app.ui.dialog;

import javax.swing.*;
import java.awt.*;
import pos_app.models.Customer;

public class CustomerFormDialog extends JDialog {

    private final JTextField tfName = new JTextField(20);
    private final JTextField tfPhone = new JTextField(20);
    private final JTextField tfEmail = new JTextField(20);
    private final JTextField tfAddress = new JTextField(20);
    private boolean submitted = false;

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

    private void showError(String message, JComponent component) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        component.requestFocus();
    }

    public boolean isSubmitted() {
        return submitted;
    }

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
