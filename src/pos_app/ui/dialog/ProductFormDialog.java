package pos_app.ui.dialog;

import pos_app.models.Product;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ProductFormDialog extends JDialog {

    private JTextField tfName, tfPrice, tfQuantity;
    private JTextField tfImage;
    private boolean submitted = false;
    private String selectedImagePath = null;

    public ProductFormDialog(JFrame parent, String title, Product existingProduct) {
        super(parent, title, true);
        setLayout(new GridBagLayout());
        setSize(450, 320);
        setLocationRelativeTo(parent);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfName = new JTextField(20);
        tfPrice = new JTextField(20);
        tfQuantity = new JTextField(20);
        tfImage = new JTextField(20);
        tfImage.setEditable(false);

        JButton btnChooseImage = new JButton("Chọn ảnh...");
        btnChooseImage.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                selectedImagePath = selectedFile.getName();
                tfImage.setText(selectedImagePath);

                // Copy file vào thư mục ảnh nếu chưa tồn tại
                File destFolder = new File("src/pos_app/pictures");
                if (!destFolder.exists()) {
                    destFolder.mkdirs();
                }
                Path dest = destFolder.toPath().resolve(selectedImagePath);
                try {
                    Files.copy(selectedFile.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi sao chép ảnh: " + ex.getMessage());
                }
            }
        });

        if (existingProduct != null) {
            tfName.setText(existingProduct.getName());
            tfPrice.setText(String.valueOf(existingProduct.getPrice()));
            tfQuantity.setText(String.valueOf(existingProduct.getQuantity()));
            selectedImagePath = existingProduct.getImagePath();
            tfImage.setText(selectedImagePath);
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

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Ảnh sản phẩm:"), gbc);
        gbc.gridx = 1;
        add(tfImage, gbc);

        gbc.gridx = 2;
        add(btnChooseImage, gbc);

        JButton btnSubmit = new JButton("Xác nhận");
        btnSubmit.addActionListener(e -> {
            submitted = true;
            setVisible(false);
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
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
                Integer.parseInt(tfQuantity.getText()),
                selectedImagePath
        );
    }
}
