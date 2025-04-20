/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package pos_app.ui.dialog;

import pos_app.models.Product;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * ProductFormDialog là cửa sổ nhập liệu để thêm hoặc chỉnh sửa sản phẩm.
 *
 * Cho phép người dùng nhập mã vạch, tên, giá, số lượng và chọn ảnh sản phẩm.
 * Ảnh được sao chép về thư mục `src/pos_app/pictures` và chỉ lưu tên file vào
 * imagePath.
 *
 * Form sử dụng để tạo mới hoặc cập nhật thông tin sản phẩm trong hệ thống POS.
 *
 * @author 04dkh
 */
public class ProductFormDialog extends JDialog {

    /**
     * Trường nhập mã vạch sản phẩm
     */
    private JTextField tfBarCode;

    /**
     * Trường nhập tên sản phẩm
     */
    private JTextField tfName;

    /**
     * Trường nhập giá sản phẩm
     */
    private JTextField tfPrice;

    /**
     * Trường nhập số lượng sản phẩm
     */
    private JTextField tfQuantity;

    /**
     * Trường hiển thị đường dẫn ảnh đã chọn
     */
    private JTextField tfImage;

    /**
     * Cờ cho biết người dùng đã nhấn "Xác nhận" hay chưa
     */
    private boolean submitted = false;

    /**
     * Tên file ảnh đã chọn
     */
    private String selectedImagePath = null;

    /**
     * Khởi tạo form thêm/sửa sản phẩm.
     *
     * @param parent cửa sổ cha
     * @param title tiêu đề form
     * @param existingProduct sản phẩm hiện tại (null nếu thêm mới)
     */
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

        // Nếu đang chỉnh sửa, nạp dữ liệu vào form
        if (existingProduct != null) {
            tfBarCode.setText(existingProduct.getBarcode());
            tfName.setText(existingProduct.getName());
            tfPrice.setText(String.valueOf(existingProduct.getPrice()));
            tfQuantity.setText(String.valueOf(existingProduct.getQuantity()));
            selectedImagePath = existingProduct.getImagePath();
            tfImage.setText(selectedImagePath);
        }
    }

    /**
     * Tạo một dòng nhập liệu đơn với label và JTextField.
     *
     * @param labelText nhãn hiển thị
     * @param field trường nhập dữ liệu
     * @return JPanel chứa dòng nhập
     */
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

    /**
     * Tạo dòng nhập ảnh với nút "Chọn ảnh".
     *
     * @param labelText nhãn hiển thị
     * @param field ô hiển thị tên ảnh
     * @param button nút chọn ảnh
     * @return JPanel chứa dòng chọn ảnh
     */
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

    /**
     * Kiểm tra xem người dùng đã nhấn nút xác nhận chưa.
     *
     * @return true nếu đã xác nhận, false nếu đóng dialog mà không lưu
     */
    public boolean isSubmitted() {
        return submitted;
    }

    /**
     * Trích xuất dữ liệu từ form thành đối tượng Product.
     *
     * @param existingId ID của sản phẩm (0 nếu là thêm mới)
     * @return Product chứa thông tin người dùng đã nhập
     */
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
