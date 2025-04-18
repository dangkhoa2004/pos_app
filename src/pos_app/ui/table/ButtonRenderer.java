package pos_app.ui.table;

import javax.swing.*;
import java.awt.*;

public class ButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
    private final JButton btnEdit = new JButton("Sửa");
    private final JButton btnDel = new JButton("Xóa");

    public ButtonRenderer() {
        setOpaque(true);
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        btnEdit.setFocusable(false);
        btnDel.setFocusable(false);
        btnEdit.setBackground(new Color(46, 204, 113));
        btnDel.setBackground(new Color(231, 76, 60));
        btnEdit.setForeground(Color.WHITE);
        btnDel.setForeground(Color.WHITE);
        add(btnEdit);
        add(btnDel);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable tbl, Object v, boolean sel, boolean focus, int r, int c) {
        setBackground(sel ? tbl.getSelectionBackground() : tbl.getBackground());
        return this;
    }
}

