package pos_app.ui.dialog;

import javax.swing.*;
import java.awt.*;

public class StockFormDialog extends JDialog {

    private final JTextField tfProduct = new JTextField(20);
    private final JTextField tfQty = new JTextField(20);
    private final JTextField tfNote = new JTextField(20);
    private boolean submitted = false;

    public StockFormDialog(JFrame parent, String title) {
        super(parent, title, true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        formPanel.add(createFormRow("Mã sản phẩm:", tfProduct));
        formPanel.add(createFormRow("Số lượng:", tfQty));
        formPanel.add(createFormRow("Ghi chú:", tfNote));

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

    public boolean isSubmitted() {
        return submitted;
    }

    public int getProductId() {
        return Integer.parseInt(tfProduct.getText().trim());
    }

    public int getQuantity() {
        return Integer.parseInt(tfQty.getText().trim());
    }

    public String getNote() {
        return tfNote.getText().trim();
    }
}
