package pos_app.ui.dialog;

import pos_app.models.Product;

import javax.swing.*;
import java.awt.*;

public class ProductFormDialog extends JDialog {

    private JTextField tfName, tfPrice, tfQuantity;
    private boolean submitted = false;

    public ProductFormDialog(JFrame parent, String title, Product existingProduct) {
        super(parent, title, true);
        setLayout(new GridBagLayout());
        setSize(400, 250);
        setLocationRelativeTo(parent);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfName = new JTextField(20);
        tfPrice = new JTextField(20);
        tfQuantity = new JTextField(20);

        if (existingProduct != null) {
            tfName.setText(existingProduct.getName());
            tfPrice.setText(String.valueOf(existingProduct.getPrice()));
            tfQuantity.setText(String.valueOf(existingProduct.getQuantity()));
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Tên sản phẩm:"), gbc);
        gbc.gridx = 1;
        add(tfName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Giá:"), gbc);
        gbc.gridx = 1;
        add(tfPrice, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Số lượng:"), gbc);
        gbc.gridx = 1;
        add(tfQuantity, gbc);

        JButton btnSubmit = new JButton("Xác nhận");
        btnSubmit.addActionListener(e -> {
            submitted = true;
            setVisible(false);
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnSubmit, gbc);
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public Product getProductData(int existingId) {
        return new Product(
                existingId,
                tfName.getText(),
                Double.parseDouble(tfPrice.getText()),
                Integer.parseInt(tfQuantity.getText())
        );
    }
}
