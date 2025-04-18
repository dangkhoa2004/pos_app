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
        setSize(350, 220);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        addRow(g, 0, "Mã sản phẩm:", tfProduct);
        addRow(g, 1, "Số lượng:", tfQty);
        addRow(g, 2, "Ghi chú:", tfNote);

        JButton btn = new JButton("Lưu");
        btn.addActionListener(e -> {
            submitted = true;
            setVisible(false);
        });

        g.gridx = 0;
        g.gridy = 3;
        g.gridwidth = 2;
        g.anchor = GridBagConstraints.CENTER;
        add(btn, g);
    }

    private void addRow(GridBagConstraints g, int row, String label, JComponent field) {
        g.gridx = 0;
        g.gridy = row;
        g.gridwidth = 1;
        add(new JLabel(label), g);
        g.gridx = 1;
        add(field, g);
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
