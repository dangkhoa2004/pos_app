/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.ui.dialog;

/**
 *
 * @author 04dkh
 */
import javax.swing.*;
import java.awt.*;
import pos_app.models.Category;

public class CategoryFormDialog extends JDialog {

    private final JTextField tfName = new JTextField(20);
    private final JTextArea taDescription = new JTextArea(3, 20);
    private boolean submitted = false;
    private Category originalCategory;

    public CategoryFormDialog(Window owner, String title, Category category) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        this.originalCategory = category;

        setLayout(new BorderLayout(10, 10));
        setSize(400, 250);
        setLocationRelativeTo(owner);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Label và TextField - Tên
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Tên loại sản phẩm:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfName, gbc);

        // Label và TextArea - Mô tả
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1;
        JScrollPane sp = new JScrollPane(taDescription);
        taDescription.setLineWrap(true);
        taDescription.setWrapStyleWord(true);
        formPanel.add(sp, gbc);

        // Nếu là chỉnh sửa
        if (category != null) {
            tfName.setText(category.getName());
            taDescription.setText(category.getDescription());
        }

        // Nút hành động
        JButton btnSubmit = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");

        btnSubmit.addActionListener(e -> {
            String name = tfName.getText().trim();
            String desc = taDescription.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Tên loại sản phẩm không được để trống.",
                        "Thiếu thông tin",
                        JOptionPane.WARNING_MESSAGE
                );
                tfName.requestFocus();
                return;
            }

            if (desc.length() < 5) {
                JOptionPane.showMessageDialog(
                        this,
                        "Mô tả nên dài ít nhất 5 ký tự để rõ ràng hơn.",
                        "Mô tả quá ngắn",
                        JOptionPane.INFORMATION_MESSAGE
                );
                taDescription.requestFocus();
                return;
            }

            submitted = true;
            dispose();
        });

        btnCancel.addActionListener(e -> dispose());

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.add(btnSubmit);
        actionPanel.add(btnCancel);

        add(formPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public Category getData() {
        return new Category(
                originalCategory == null ? 0 : originalCategory.getId(),
                tfName.getText().trim(),
                taDescription.getText().trim()
        );
    }
}
