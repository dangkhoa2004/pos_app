package pos_app.ui.panel;

import pos_app.dao.InvoiceDAO;
import pos_app.models.Invoice;
import pos_app.ui.dialog.InvoiceDetailDialog;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import pos_app.ui.components.RoundedButton;
import pos_app.ui.components.RoundedTextField;

public class InvoicePanel extends JPanel {

    private final InvoiceDAO dao = new InvoiceDAO();
    private DefaultTableModel model;
    private JTable table;

    public InvoicePanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        model = new DefaultTableModel(
                new String[]{"ID", "Mã KH", "Mã NV", "Tổng tiền", "Ngày tạo", "Hành động"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 5;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        styleTable(table);
        table.getColumn("Hành động")
                .setCellRenderer(new BtnRenderer());
        table.getColumn("Hành động")
                .setCellEditor(new BtnEditor(model, dao, this::loadTable));

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createTitledBorder("Danh sách hoá đơn"));

        RoundedTextField tf = new RoundedTextField(20, 15);
        JButton btnFind = new RoundedButton("Tìm", 15);
        btnFind.addActionListener(e -> search(tf.getText().trim()));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        right.add(new JLabel("Tìm ID hoặc KH:"));
        right.add(tf);
        right.add(btnFind);

        JPanel searchBar = new JPanel(new BorderLayout());
        searchBar.add(right, BorderLayout.EAST);

        add(searchBar, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
        loadTable();
    }

    private void styleTable(JTable t) {
        JTableHeader h = t.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 13));
        h.setBackground(new Color(230, 230, 230));
        h.setPreferredSize(new Dimension(100, 35));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        DefaultTableCellRenderer c = new DefaultTableCellRenderer();
        c.setHorizontalAlignment(JLabel.CENTER);
        c.setVerticalAlignment(JLabel.CENTER);
        for (int i = 0; i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setCellRenderer(c);
        }
    }

    private void loadTable() {
        model.setRowCount(0);
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        List<Invoice> list = dao.getAllInvoices();
        for (Invoice iv : list) {
            model.addRow(new Object[]{
                iv.getId(), iv.getCustomerId(), iv.getEmployeeId(),
                iv.getTotal(), f.format(iv.getCreatedAt()), "Hành động"
            });
        }
    }

    private void search(String kw) {
        if (kw.isEmpty()) {
            loadTable();
            return;
        }
        model.setRowCount(0);
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Invoice iv : dao.getAllInvoices()) {
            if (String.valueOf(iv.getId()).contains(kw)
                    || String.valueOf(iv.getCustomerId()).contains(kw)) {
                model.addRow(new Object[]{
                    iv.getId(), iv.getCustomerId(), iv.getEmployeeId(),
                    iv.getTotal(), f.format(iv.getCreatedAt()), "Hành động"
                });
            }
        }
    }

    /* ========== Renderer ========== */
    class BtnRenderer extends JPanel implements TableCellRenderer {

        final JButton view = new JButton("Xem");
        final JButton del = new JButton("Xóa");

        BtnRenderer() {
            setOpaque(true);
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            view.setFocusable(false);
            del.setFocusable(false);
            view.setBackground(new Color(52, 152, 219));
            del.setBackground(new Color(231, 76, 60));
            view.setForeground(Color.WHITE);
            del.setForeground(Color.WHITE);
            add(view);
            add(del);
        }

        @Override
        public Component getTableCellRendererComponent(JTable tbl, Object v, boolean sel, boolean f, int r, int c) {
            setBackground(sel ? tbl.getSelectionBackground() : tbl.getBackground());
            return this;
        }
    }

    /* ========== Editor ========== */
    class BtnEditor extends AbstractCellEditor implements TableCellEditor {

        enum Click {
            NONE, VIEW, DELETE
        }

        Click click = Click.NONE;
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        JButton view = new JButton("Xem"), del = new JButton("Xóa");
        DefaultTableModel m;
        InvoiceDAO dao;
        Runnable reload;
        int row;

        BtnEditor(DefaultTableModel m, InvoiceDAO dao, Runnable reload) {
            this.m = m;
            this.dao = dao;
            this.reload = reload;
            view.setBackground(new Color(52, 152, 219));
            del.setBackground(new Color(231, 76, 60));
            view.setForeground(Color.WHITE);
            del.setForeground(Color.WHITE);
            view.addActionListener(e -> {
                click = Click.VIEW;
                fireEditingStopped();
            });
            del.addActionListener(e -> {
                click = Click.DELETE;
                fireEditingStopped();
            });
            panel.add(view);
            panel.add(del);
        }

        @Override
        public Component getTableCellEditorComponent(JTable tbl, Object v, boolean sel, int r, int c) {
            row = r;
            click = Click.NONE;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            int id = (int) m.getValueAt(row, 0);
            switch (click) {
                case VIEW ->
                    new InvoiceDetailDialog(null, dao.findById(id)).setVisible(true);
                case DELETE -> {
                    int ok = JOptionPane.showConfirmDialog(null,
                            "Xoá hoá đơn này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (ok == JOptionPane.YES_OPTION) {
                        dao.deleteInvoice(id);
                        reload.run();
                    }
                }
            }
            return "";
        }
    }
}
