package pos_app.ui.panel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import pos_app.dao.StockDAO;
import pos_app.models.StockIn;
import pos_app.models.StockOut;
import pos_app.ui.components.RoundedButton;
import pos_app.ui.dialog.StockFormDialog;

/**
 * Lớp StockPanel là giao diện quản lý hoạt động nhập kho và xuất kho của hệ
 * thống. Giao diện được chia thành 2 tab "Nhập kho" và "Xuất kho", mỗi tab là
 * một bảng hiển thị dữ liệu tương ứng. Cho phép người dùng thêm mới và xóa bản
 * ghi nhập/xuất kho.
 */
public class StockPanel extends JPanel {

    private final StockDAO dao = new StockDAO();
    private final DefaultTableModel mdlIn = buildModel();
    private final DefaultTableModel mdlOut = buildModel();
    private final JTable tblIn = buildTable(mdlIn);
    private final JTable tblOut = buildTable(mdlOut);

    /**
     * Khởi tạo giao diện quản lý nhập/xuất kho. Bao gồm các bảng dữ liệu, các
     * nút thao tác và xử lý sự kiện thêm/xóa.
     */
    public StockPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Nhập kho", buildTabPanel(tblIn, true));
        tabs.addTab("Xuất kho", buildTabPanel(tblOut, false));
        add(tabs, BorderLayout.CENTER);

        DeleteEditor delEditor = new DeleteEditor();
        tblIn.getColumn("Hành động").setCellEditor(delEditor);
        tblOut.getColumn("Hành động").setCellEditor(delEditor);

        loadData();
        setComponentBackgroundWhite(this);
    }

    /**
     * Tạo model bảng với các cột cần thiết.
     *
     * @return DefaultTableModel chứa cấu trúc cột mặc định
     */
    private DefaultTableModel buildModel() {
        return new DefaultTableModel(
                new String[]{"ID", "Mã SP", "Số lượng", "Ghi chú", "Ngày tạo", "Hành động"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 5;
            }
        };
    }

    /**
     * Tạo bảng hiển thị với kiểu dáng và renderer hành động.
     *
     * @param m Model bảng cần hiển thị
     * @return JTable đã được cấu hình
     */
    private JTable buildTable(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setRowHeight(26);
        styleTable(t);
        t.getColumn("Hành động").setCellRenderer(new DeleteRenderer());
        return t;
    }

    /**
     * Xây dựng giao diện từng tab (Nhập kho / Xuất kho).
     *
     * @param table Bảng cần hiển thị
     * @param isIn true nếu là tab nhập kho, false nếu là xuất kho
     * @return JPanel chứa bảng và nút thêm
     */
    private JPanel buildTabPanel(JTable table, boolean isIn) {
        JButton btnAdd = new RoundedButton("Thêm mới", 15);
        btnAdd.addActionListener(e -> showForm(isIn));

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        bar.add(btnAdd);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createTitledBorder(isIn ? "Lịch sử nhập kho" : "Lịch sử xuất kho"));

        JPanel p = new JPanel(new BorderLayout());
        p.add(bar, BorderLayout.NORTH);
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    /**
     * Thiết lập style bảng: font, màu, căn giữa.
     *
     * @param t Bảng cần cấu hình
     */
    private void styleTable(JTable t) {
        JTableHeader hd = t.getTableHeader();
        hd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        hd.setPreferredSize(new Dimension(100, 30));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        DefaultTableCellRenderer ctr = new DefaultTableCellRenderer();
        ctr.setHorizontalAlignment(JLabel.CENTER);
        ctr.setVerticalAlignment(JLabel.CENTER);
        for (int i = 0; i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setCellRenderer(ctr);
        }
    }

    /**
     * Load dữ liệu nhập/xuất kho từ cơ sở dữ liệu và hiển thị lên bảng.
     */
    private void loadData() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        mdlIn.setRowCount(0);
        for (StockIn s : dao.getAllStockIn()) {
            mdlIn.addRow(new Object[]{s.getId(), s.getProductId(), s.getQuantity(), s.getNote(), fmt.format(s.getCreatedAt()), "Xóa"});
        }

        mdlOut.setRowCount(0);
        for (StockOut s : dao.getAllStockOut()) {
            mdlOut.addRow(new Object[]{s.getId(), s.getProductId(), s.getQuantity(), s.getNote(), fmt.format(s.getCreatedAt()), "Xóa"});
        }
    }

    /**
     * Hiển thị form nhập/xuất kho và lưu bản ghi khi người dùng xác nhận.
     *
     * @param isIn true nếu là nhập kho, false nếu là xuất kho
     */
    private void showForm(boolean isIn) {
        StockFormDialog dlg = new StockFormDialog(null, isIn ? "Thêm nhập kho" : "Thêm xuất kho");
        dlg.setVisible(true);
        if (dlg.isSubmitted()) {
            if (isIn) {
                dao.insertStockIn(new StockIn(0, dlg.getProductId(), dlg.getQuantity(), dlg.getNote(), null));
            } else {
                dao.insertStockOut(new StockOut(0, dlg.getProductId(), dlg.getQuantity(), dlg.getNote(), null));
            }
            loadData();
        }
    }

    /**
     * Renderer cho cột "Hành động" để hiển thị nút Xóa.
     */
    private static class DeleteRenderer extends JPanel implements TableCellRenderer {

        private final JButton btn = new JButton("Xóa");

        DeleteRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            btn.setFocusable(false);
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(231, 76, 60));
            add(btn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                boolean focused, int row, int column) {
            setBackground(selected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }

    /**
     * Editor cho cột "Hành động" để xử lý xóa bản ghi khi người dùng nhấn nút.
     */
    private class DeleteEditor extends AbstractCellEditor implements TableCellEditor {

        private int row;
        private JTable owner;
        private final JButton btnDelete = new JButton("Xóa");

        DeleteEditor() {
            btnDelete.setForeground(Color.WHITE);
            btnDelete.setBackground(new Color(231, 76, 60));
            btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDelete.addActionListener(e -> onDelete());
        }

        /**
         * Thực hiện xóa bản ghi nhập/xuất kho tương ứng.
         */
        private void onDelete() {
            int id = (int) ((DefaultTableModel) owner.getModel()).getValueAt(row, 0);
            boolean isIn = owner == tblIn;
            int ok = JOptionPane.showConfirmDialog(null, "Xóa bản ghi này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                if (isIn) {
                    dao.deleteStockIn(id);
                } else {
                    dao.deleteStockOut(id);
                }
                loadData();
            }
            fireEditingStopped();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.owner = table;
            this.row = row;
            return btnDelete;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    private void setComponentBackgroundWhite(Component component) {
        if (component instanceof JPanel || component instanceof JScrollPane || component instanceof JTabbedPane) {
            component.setBackground(Color.WHITE);
        }

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                // Trừ JTextField và JButton
                if (!(child instanceof JTextField) && !(child instanceof JButton)) {
                    setComponentBackgroundWhite(child);
                }
            }
        }
    }
}
