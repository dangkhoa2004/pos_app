package pos_app.ui.dialog;

import pos_app.models.Invoice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InvoiceDetailDialog extends JDialog {

    public InvoiceDetailDialog(JFrame p, Invoice iv) {
        super(p, "Chi tiết hoá đơn #" + iv.getId(), true);
        setSize(600, 400);
        setLocationRelativeTo(p);
        setLayout(new BorderLayout(10, 10));

        String[] cols = {"Sản phẩm", "SL", "Đơn giá", "Thành tiền"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        JTable t = new JTable(m);
        t.setRowHeight(24);

        iv.getItems().forEach(it -> {
            m.addRow(new Object[]{
                it.getProductId(), it.getQuantity(), it.getUnitPrice(), it.getLineTotal()
            });
        });
        add(new JScrollPane(t), BorderLayout.CENTER);

        JPanel info = new JPanel(new GridLayout(3, 2, 5, 5));
        info.add(new JLabel("Khách hàng:"));
        info.add(new JLabel(String.valueOf(iv.getCustomerId())));
        info.add(new JLabel("Nhân viên:"));
        info.add(new JLabel(String.valueOf(iv.getEmployeeId())));
        info.add(new JLabel("Tổng:"));
        info.add(new JLabel(iv.getTotal().toString()));
        add(info, BorderLayout.NORTH);
    }
}
