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

    public EmployeeFormDialog(JFrame parent, String title, Employee emp) {
        super(parent, title, true);
        setLayout(new GridBagLayout());
        setSize(450, 320);
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

        addLabelField(g, 0, "Họ tên:", tfName);
        addLabelField(g, 1, "Username:", tfUser);
        addLabelField(g, 2, "Mật khẩu:", pfPass);
        addLabelField(g, 3, "Role ID:", tfRole);
        addLabelField(g, 4, "Số ĐT:", tfPhone);
        addLabelField(g, 5, "Email:", tfEmail);

        JButton ok = new JButton("Xác nhận");
        ok.addActionListener(e -> {
            submitted = true;
            setVisible(false);
        });
        g.gridx = 0;
        g.gridy = 6;
        g.gridwidth = 2;
        g.anchor = GridBagConstraints.CENTER;
        add(ok, g);
    }

    private void addLabelField(GridBagConstraints g, int row, String txt, JComponent field) {
        g.gridx = 0;
        g.gridy = row;
        g.gridwidth = 1;
        add(new JLabel(txt), g);
        g.gridx = 1;
        add(field, g);
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public Employee getEmployeeData(int id) {
        String pwd = new String(pfPass.getPassword());
        return new Employee(
                id,
                tfName.getText().trim(),
                tfUser.getText().trim(),
                pwd,
                Integer.parseInt(tfRole.getText().trim()),
                tfPhone.getText().trim(),
                tfEmail.getText().trim()
        );
    }
}
