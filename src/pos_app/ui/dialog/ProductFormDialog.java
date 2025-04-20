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

    private JTextField tfBarCode, tfName, tfPrice, tfQuantity, tfImage;
    private boolean submitted = false;
    private String selectedImagePath = null;

    public ProductFormDialog(JFrame parent, String title, Product existingProduct) {
        super(parent, title, true);
        setSize(450, 380);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        tfBarCode = new JTextField(20);
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

                File destFolder = new File("src/pos_app/pictures");
                if (!destFolder.exists()) destFolder.mkdirs();

                Path dest = destFolder.toPath().resolve(selectedImagePath);
                try {
                    Files.copy(selectedFile.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi sao chép ảnh: " + ex.getMessage());
                }
            }
        });

        formPanel.add(createFormRow("Mã vạch:", tfBarCode));
        formPanel.add(createFormRow("Tên sản phẩm:", tfName));
        formPanel.add(createFormRow("Giá:", tfPrice));
        formPanel.add(createFormRow("Số lượng:", tfQuantity));
        formPanel.add(createFormRowWithButton("Ảnh sản phẩm:", tfImage, btnChooseImage));

        JButton btnSubmit = new JButton("Xác nhận");
        btnSubmit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSubmit.setPreferredSize(new Dimension(100, 35));
        btnSubmit.addActionListener(e -> {
            submitted = true;
            setVisible(false);
        });

        JPanel footer = new JPanel();
        footer.add(btnSubmit);

        add(formPanel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        if (existingProduct != null) {
            tfBarCode.setText(existingProduct.getBarcode());
            tfName.setText(existingProduct.getName());
            tfPrice.setText(String.valueOf(existingProduct.getPrice()));
            tfQuantity.setText(String.valueOf(existingProduct.getQuantity()));
            selectedImagePath = existingProduct.getImagePath();
            tfImage.setText(selectedImagePath);
        }
    }

    private JPanel createFormRow(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 25));

        field.setPreferredSize(new Dimension(0, 30));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormRowWithButton(String labelText, JTextField field, JButton button) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 25));

        field.setPreferredSize(new Dimension(0, 30));
        button.setPreferredSize(new Dimension(110, 30));

        JPanel inputGroup = new JPanel(new BorderLayout(5, 5));
        inputGroup.add(field, BorderLayout.CENTER);
        inputGroup.add(button, BorderLayout.EAST);

        panel.add(label, BorderLayout.WEST);
        panel.add(inputGroup, BorderLayout.CENTER);
        return panel;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public Product getProductData(int existingId) {
        return new Product(
                existingId,
                tfBarCode.getText(),
                tfName.getText(),
                Double.parseDouble(tfPrice.getText()),
                Integer.parseInt(tfQuantity.getText()),
                selectedImagePath
        );
    }
}
