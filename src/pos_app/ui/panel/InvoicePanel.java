package pos_app.ui.panel;

import pos_app.dao.InvoiceDAO;
import pos_app.models.Invoice;
import pos_app.ui.dialog.InvoiceDetailDialog;
import pos_app.ui.components.RoundedButton;
import pos_app.ui.components.RoundedTextField;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Giao diện quản lý hóa đơn: tìm kiếm, xem chi tiết, xóa.
 */
public class InvoicePanel extends JPanel {

    private final InvoiceDAO dao = new InvoiceDAO();
    private final DefaultTableModel model;
    private final JTable table;

    public InvoicePanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ==== Model & Table ====
        model = new DefaultTableModel(
                new String[]{"ID", "Mã KH", "Mã NV", "Tổng tiền", "Ngày tạo", "Hành động"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        table = new JTable(model);
        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(230, 230, 230));
        table.getTableHeader().setPreferredSize(new Dimension(100, 35));
        table.setBackground(Color.WHITE);
        styleTable(table);

        table.getColumn("Hành động").setCellRenderer(new BtnRenderer());
        table.getColumn("Hành động").setCellEditor(new BtnEditor(model, dao, this::loadTable));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách hoá đơn"));
        scroll.getViewport().setBackground(Color.WHITE);

        // ==== Tìm kiếm ====
        RoundedTextField tfSearch = new RoundedTextField(20, 15);
        tfSearch.setBackground(new Color(245, 245, 245));
        JButton btnFind = new RoundedButton("Tìm", 15);
        btnFind.addActionListener(e -> search(tfSearch.getText().trim()));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(new JLabel("Tìm ID hoặc KH:"));
        rightPanel.add(tfSearch);
        rightPanel.add(btnFind);

        JPanel searchBar = new JPanel(new BorderLayout());
        searchBar.setBackground(Color.WHITE);
        searchBar.add(rightPanel, BorderLayout.EAST);

        add(searchBar, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        loadTable();
    }

    private void styleTable(JTable table) {
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        center.setVerticalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }

    private void loadTable() {
        model.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        List<Invoice> list = dao.getAllInvoices();
        for (Invoice iv : list) {
            model.addRow(new Object[]{
                iv.getId(), iv.getCustomerId(), iv.getEmployeeId(),
                iv.getTotal(), fmt.format(iv.getCreatedAt()), "Hành động"
            });
        }
    }

    private void search(String kw) {
        if (kw.isEmpty()) {
            loadTable();
            return;
        }
        model.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Invoice iv : dao.getAllInvoices()) {
            if (String.valueOf(iv.getId()).contains(kw)
                    || String.valueOf(iv.getCustomerId()).contains(kw)) {
                model.addRow(new Object[]{
                    iv.getId(), iv.getCustomerId(), iv.getEmployeeId(),
                    iv.getTotal(), fmt.format(iv.getCreatedAt()), "Hành động"
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
        public Component getTableCellRendererComponent(JTable table, Object value, boolean sel, boolean foc, int row, int col) {
            setBackground(sel ? table.getSelectionBackground() : Color.WHITE);
            return this;
        }
    }

    /* ========== Editor ========== */
    class BtnEditor extends AbstractCellEditor implements TableCellEditor {

        enum ClickType {
            NONE, VIEW, DELETE
        }

        private ClickType click = ClickType.NONE;
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        private final JButton view = new JButton("Xem"), del = new JButton("Xóa");
        private final DefaultTableModel model;
        private final InvoiceDAO dao;
        private final Runnable reload;
        private int currentRow;

        BtnEditor(DefaultTableModel model, InvoiceDAO dao, Runnable reload) {
            this.model = model;
            this.dao = dao;
            this.reload = reload;

            view.setBackground(new Color(52, 152, 219));
            del.setBackground(new Color(231, 76, 60));
            view.setForeground(Color.WHITE);
            del.setForeground(Color.WHITE);

            view.addActionListener(e -> {
                click = ClickType.VIEW;
                fireEditingStopped();
            });
            del.addActionListener(e -> {
                click = ClickType.DELETE;
                fireEditingStopped();
            });

            panel.setOpaque(true);
            panel.add(view);
            panel.add(del);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            click = ClickType.NONE;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            int id = (int) model.getValueAt(currentRow, 0);
            switch (click) {
                case VIEW ->
                    new InvoiceDetailDialog(null, dao.findById(id)).setVisible(true);
                case DELETE -> {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "Xoá hoá đơn này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        dao.deleteInvoice(id);
                        reload.run();
                    }
                }
            }
            return "";
        }
    }
}
