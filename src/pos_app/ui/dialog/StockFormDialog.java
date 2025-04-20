/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package pos_app.ui.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * StockFormDialog là form nhập thông tin phiếu nhập hoặc xuất kho.
 *
 * Cho phép người dùng nhập: - Mã sản phẩm - Số lượng - Ghi chú (nếu có)
 *
 * Sử dụng trong nghiệp vụ quản lý kho của hệ thống POS.
 *
 * Sau khi xác nhận, các giá trị có thể được lấy thông qua getter.
 *
 * @author 04dkh
 */
public class StockFormDialog extends JDialog {

    /**
     * Trường nhập mã sản phẩm
     */
    private final JTextField tfProduct = new JTextField(20);

    /**
     * Trường nhập số lượng
     */
    private final JTextField tfQty = new JTextField(20);

    /**
     * Trường nhập ghi chú
     */
    private final JTextField tfNote = new JTextField(20);

    /**
     * Cờ xác định người dùng đã nhấn Lưu hay chưa
     */
    private boolean submitted = false;

    /**
     * Tạo form nhập phiếu kho.
     *
     * @param parent cửa sổ cha
     * @param title tiêu đề (ví dụ: "Nhập kho", "Xuất kho")
     */
    public StockFormDialog(JFrame parent, String title) {
        super(parent, title, true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Panel chứa các dòng nhập liệu
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        formPanel.add(createFormRow("Mã sản phẩm:", tfProduct));
        formPanel.add(createFormRow("Số lượng:", tfQty));
        formPanel.add(createFormRow("Ghi chú:", tfNote));

        // Nút lưu
        JButton btnSave = new JButton("Lưu");
        btnSave.setPreferredSize(new Dimension(100, 35));
        btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSave.addActionListener(e -> {
            submitted = true;
            setVisible(false);
        });

        JPanel footer = new JPanel();
        footer.add(btnSave);

        add(formPanel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    /**
     * Tạo dòng nhập liệu đơn gồm label và textfield.
     *
     * @param labelText nhãn hiển thị
     * @param textField trường nhập dữ liệu
     * @return JPanel chứa dòng
     */
    private JPanel createFormRow(String labelText, JTextField textField) {
        JPanel row = new JPanel(new BorderLayout(5, 5));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 25));

        textField.setPreferredSize(new Dimension(0, 30));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        row.add(label, BorderLayout.WEST);
        row.add(textField, BorderLayout.CENTER);
        return row;
    }

    /**
     * Kiểm tra người dùng đã xác nhận lưu hay chưa.
     *
     * @return true nếu đã nhấn Lưu, ngược lại là false
     */
    public boolean isSubmitted() {
        return submitted;
    }

    /**
     * Lấy mã sản phẩm được nhập.
     *
     * @return ID sản phẩm dưới dạng số nguyên
     */
    public int getProductId() {
        return Integer.parseInt(tfProduct.getText().trim());
    }

    /**
     * Lấy số lượng được nhập.
     *
     * @return số lượng dưới dạng số nguyên
     */
    public int getQuantity() {
        return Integer.parseInt(tfQty.getText().trim());
    }

    /**
     * Lấy ghi chú được nhập.
     *
     * @return nội dung ghi chú
     */
    public String getNote() {
        return tfNote.getText().trim();
    }
}
