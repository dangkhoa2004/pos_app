package pos_app.ui.panel;

import java.awt.*;
import javax.swing.*;
import pos_app.ui.components.RoundedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PaymentPanel extends JPanel {

    private final RoundedTextField tfTotal = new RoundedTextField("0 đ", 10);
    private final RoundedTextField tfPayable = new RoundedTextField("0 đ", 10);
    private final RoundedTextField tfItemCount = new RoundedTextField("0", 10);
    private final RoundedTextField tfVat = new RoundedTextField("0%", 10);
    private final RoundedTextField tfDiscount = new RoundedTextField("0%", 10);
    private final JTextField tfCustomerPay = new JTextField(10);
    private final RoundedTextField tfChange = new RoundedTextField("0 đ", 10);
    private final JComboBox<String> cbPaymentMethod = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản", "Momo"});

    private double currentPayable = 0;

    public PaymentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Thanh toán"));
        setBackground(new Color(250, 252, 255));

        setReadOnly(tfTotal);
        setReadOnly(tfPayable);
        setReadOnly(tfItemCount);
        setReadOnly(tfVat);
        setReadOnly(tfDiscount);
        setReadOnly(tfChange);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 15);
        Font valueFont = new Font("Segoe UI", Font.BOLD, 15);

        content.add(buildRow("Số lượng sản phẩm:", tfItemCount, labelFont, valueFont));
        content.add(Box.createRigidArea(new Dimension(0, 8)));

        content.add(buildRow("Tổng tiền:", tfTotal, labelFont, valueFont));
        content.add(Box.createRigidArea(new Dimension(0, 8)));

        content.add(buildRow("VAT:", tfVat, labelFont, valueFont));
        content.add(Box.createRigidArea(new Dimension(0, 8)));

        content.add(buildRow("Chiết khấu:", tfDiscount, labelFont, valueFont));
        content.add(Box.createRigidArea(new Dimension(0, 8)));

        content.add(buildRow("Khách phải trả:", tfPayable, labelFont, valueFont));
        content.add(Box.createRigidArea(new Dimension(0, 12)));

        content.add(buildRow("Tiền khách đưa:", tfCustomerPay, labelFont));
        content.add(Box.createRigidArea(new Dimension(0, 8)));

        content.add(buildRow("Hình thức thanh toán:", cbPaymentMethod, labelFont));
        content.add(Box.createRigidArea(new Dimension(0, 12)));

        content.add(buildRow("Tiền thừa:", tfChange, labelFont, valueFont));
        content.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnPay = new JButton("Thanh toán");
        btnPay.setBackground(new Color(0, 153, 51));
        btnPay.setForeground(Color.WHITE);
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnPay.setFocusPainted(false);
        btnPay.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPay.setPreferredSize(new Dimension(180, 40));

        content.add(btnPay);
        add(content, BorderLayout.CENTER);
        btnPay.addActionListener(e -> {
            if (onPayment != null) {
                onPayment.run();
            }
        });

        tfCustomerPay.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateChange();
            }

            public void removeUpdate(DocumentEvent e) {
                updateChange();
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    private JPanel buildRow(String labelText, JComponent valueComponent, Font labelFont) {
        return buildRow(labelText, valueComponent, labelFont, labelFont);
    }

    private JPanel buildRow(String labelText, JComponent valueComponent, Font labelFont, Font valueFont) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        valueComponent.setFont(valueFont);
        valueComponent.setPreferredSize(new Dimension(180, 30));

        row.add(label);
        row.add(valueComponent);

        return row;
    }

    private void setReadOnly(RoundedTextField field) {
        field.setEditable(false);
        field.setFocusable(false);
        field.setBackground(new Color(245, 245, 245));
        field.setForeground(Color.DARK_GRAY);
    }

    public void setValues(int itemCount, double total, double vatRate, double discountRate, double payable) {
        currentPayable = payable;
        tfItemCount.setText(String.valueOf(itemCount));
        tfTotal.setText(String.format("%,.0f đ", total));
        tfVat.setText(String.format("%.0f%%", vatRate * 100));
        tfDiscount.setText(String.format("%.0f%%", discountRate * 100));
        tfPayable.setText(String.format("%,.0f đ", payable));
        updateChange();
    }

    private void updateChange() {
        try {
            String customerPayStr = tfCustomerPay.getText().replaceAll("[^\\d.]", "");
            double customerPay = Double.parseDouble(customerPayStr);
            double change = customerPay - currentPayable;
            tfChange.setText(String.format("%,.0f đ", change >= 0 ? change : 0));
        } catch (Exception e) {
            tfChange.setText("0 đ");
        }
    }
    // Getter để dùng ở POSPanel

    public double getPayableAmount() {
        return currentPayable;
    }

    public double getCustomerPaid() {
        try {
            return Double.parseDouble(tfCustomerPay.getText().replaceAll("[^\\d.]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    public String getSelectedPaymentMethod() {
        return cbPaymentMethod.getSelectedItem().toString();
    }

// Bổ sung sự kiện nút "Thanh toán"
    private Runnable onPayment;

    public void setOnPayment(Runnable onPayment) {
        this.onPayment = onPayment;
    }

    public void resetFields() {
        tfItemCount.setText("0");
        tfTotal.setText("0 đ");
        tfVat.setText("0%");
        tfDiscount.setText("0%");
        tfPayable.setText("0 đ");
        tfCustomerPay.setText("");
        tfChange.setText("0 đ");
        currentPayable = 0;
    }

}
