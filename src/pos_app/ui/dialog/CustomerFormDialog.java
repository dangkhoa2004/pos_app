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
        setLayout(new GridBagLayout());
        setSize(400, 250);
        setLocationRelativeTo(parent);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        if (existing != null) {
            tfName.setText(existing.getName());
            tfPhone.setText(existing.getPhone());
            tfEmail.setText(existing.getEmail());
            tfAddress.setText(existing.getAddress());
        }

        g.gridx = 0;
        g.gridy = 0;
        add(new JLabel("Tên khách hàng:"), g);
        g.gridx = 1;
        add(tfName, g);

        g.gridx = 0;
        g.gridy = 1;
        add(new JLabel("Số điện thoại:"), g);
        g.gridx = 1;
        add(tfPhone, g);

        g.gridx = 0;
        g.gridy = 2;
        add(new JLabel("Email:"), g);
        g.gridx = 1;
        add(tfEmail, g);

        g.gridx = 0;
        g.gridy = 3;
        add(new JLabel("Địa chỉ:"), g);
        g.gridx = 1;
        add(tfAddress, g);

        JButton btnOK = new JButton("Xác nhận");
        btnOK.addActionListener(e -> {
            submitted = true;
            setVisible(false);
        });

        g.gridx = 0;
        g.gridy = 4;
        g.gridwidth = 2;
        g.anchor = GridBagConstraints.CENTER;
        add(btnOK, g);
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
