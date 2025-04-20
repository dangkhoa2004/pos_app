package pos_app.ui.panel;

import java.awt.*;
import java.io.File;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import pos_app.dao.ProductDAO;
import pos_app.models.InvoiceModel;
import pos_app.models.Product;
import pos_app.ui.components.RoundedButton;
import pos_app.util.BarcodeScanner;
import pos_app.util.IconUtil;
import pos_app.util.Session;

public class POSPanel extends JPanel {

    private final JTabbedPane tabbedPane = new JTabbedPane();
    private int tabIndex = 1;
    private List<Product> allProducts;
    private final PaymentPanel paymentPanel = new PaymentPanel();

    public POSPanel() {
        ProductDAO productDAO = new ProductDAO();
        allProducts = productDAO.getAllProducts();
        setLayout(new BorderLayout());
        add(buildTopBar(), BorderLayout.NORTH);
        add(buildMainArea(), BorderLayout.CENTER);
        add(buildBottomButtons(), BorderLayout.SOUTH);
        addNewOrderTab();
        paymentPanel.setOnPayment(() -> handlePayment());
        setComponentBackgroundWhite(this);
    }

    private JPanel buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topBar.setBackground(new Color(255, 255, 255));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        JTextField tfSearch = new JTextField(30);
        tfSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfSearch.setToolTipText("Thêm sản phẩm vào đơn (F3)");
        JPopupMenu suggestionPopup = new JPopupMenu();
        suggestionPopup.setFocusable(false);

        tfSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                showSuggestions();
            }

            public void removeUpdate(DocumentEvent e) {
                showSuggestions();
            }

            public void changedUpdate(DocumentEvent e) {
            }

            private void showSuggestions() {
                String keyword = tfSearch.getText().trim().toLowerCase();
                suggestionPopup.removeAll();

                if (keyword.isEmpty()) {
                    suggestionPopup.setVisible(false);
                    return;
                }

                ProductDAO productDAO = new ProductDAO();
                List<Product> allProducts = productDAO.getAllProducts();

                List<Product> matched = allProducts.stream()
                        .filter(p -> p.getName().toLowerCase().contains(keyword))
                        .limit(10)
                        .toList();

                if (matched.isEmpty()) {
                    suggestionPopup.setVisible(false);
                    return;
                }

                for (Product p : matched) {
                    JPanel itemPanel = new JPanel();
                    itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
                    itemPanel.setBackground(Color.WHITE);
                    itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                    itemPanel.setPreferredSize(new Dimension(300, 60));

                    JLabel imgLabel = new JLabel();
                    String path = "src/pos_app/pictures/" + p.getImagePath();
                    File file = new File(path);
                    if (file.exists()) {
                        ImageIcon icon = new ImageIcon(path);
                        Image scaled = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                        imgLabel.setIcon(new ImageIcon(scaled));
                    } else {
                        imgLabel.setText("(Không có ảnh)");
                        imgLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                        imgLabel.setForeground(Color.GRAY);
                    }

                    imgLabel.setPreferredSize(new Dimension(40, 40));
                    itemPanel.add(imgLabel);
                    itemPanel.add(Box.createRigidArea(new Dimension(10, 0)));

                    String html = String.format(
                            "<html><div style='line-height: 1.3'><b>%s</b><br/><span style='color:gray;'>%,.0f đ - SL: %d</span></div></html>",
                            p.getName(), p.getPrice(), p.getQuantity()
                    );
                    JLabel infoLabel = new JLabel(html);
                    infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    itemPanel.add(infoLabel);

                    JMenuItem item = new JMenuItem();
                    item.setLayout(new BorderLayout());
                    item.setBackground(Color.WHITE);
                    item.add(itemPanel, BorderLayout.CENTER);
                    item.setPreferredSize(new Dimension(320, 60));
                    item.setBorder(BorderFactory.createEmptyBorder());

                    item.addActionListener(ev -> {
                        Component selected = tabbedPane.getSelectedComponent();
                        if (selected instanceof OrderTabPanel orderTab) {
                            orderTab.addToCart(p);
                            updatePayment(orderTab);
                        }
                        tfSearch.setText("");
                        suggestionPopup.setVisible(false);
                    });

                    suggestionPopup.add(item);
                }

                suggestionPopup.show(tfSearch, 0, tfSearch.getHeight());
            }
        });

        JButton btnScan = new JButton("SCAN");
        btnScan.setIcon(IconUtil.loadSvg("scan-qr-code.svg", 20));

        searchPanel.add(tfSearch);
        searchPanel.add(btnScan);
        btnScan.addActionListener(e -> {
            BarcodeScanner.scan(barcode -> {
                Product product = new ProductDAO().getProductByBarcode(barcode);
                if (product != null) {
                    SwingUtilities.invokeLater(() -> {
                        Component selected = tabbedPane.getSelectedComponent();
                        if (selected instanceof OrderTabPanel orderTab) {
                            orderTab.addToCart(product);
                            updatePayment(orderTab);
                            JOptionPane.showMessageDialog(this, "Đã thêm sản phẩm: " + product.getName());
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(()
                            -> JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với mã: " + barcode)
                    );
                }
            });
        });

        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tabPanel.setOpaque(false);

        JButton btnAddTab = new JButton();
        btnAddTab.setIcon(IconUtil.loadSvg("plus-circle.svg", 20));
        btnAddTab.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddTab.setPreferredSize(new Dimension(40, 30));
        btnAddTab.setForeground(Color.WHITE);
        btnAddTab.setFocusPainted(false);
        btnAddTab.addActionListener(e -> addNewOrderTab());

        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabPanel.add(tabbedPane);
        tabPanel.add(btnAddTab);

        topBar.add(searchPanel, BorderLayout.WEST);
        topBar.add(tabPanel, BorderLayout.CENTER);
        return topBar;
    }

    private JPanel buildMainArea() {
        JPanel main = new JPanel(new GridLayout(1, 2, 10, 10));
        JPanel left = new JPanel(new BorderLayout());
        left.add(tabbedPane, BorderLayout.CENTER);
        main.add(left);
        main.add(paymentPanel);
        left.setBackground(Color.WHITE);
        main.setBackground(Color.WHITE);
        return main;
    }

    private JPanel buildBottomButtons() {
        JPanel panel = new JPanel(new GridLayout(2, 6, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panel.setBackground(Color.WHITE);

        String[] actions = {
            "Thêm dịch vụ", "Chiết khấu đơn", "Khuyến mãi", "Đổi quà",
            "Ghi chú đơn hàng", "Đổi giá bán hàng", "Thông tin khách hàng", "Xóa toàn bộ sản phẩm",
            "Trả hàng", "Xem danh sách đơn hàng", "Xem báo cáo", "Danh sách thao tác"
        };

        for (String act : actions) {
            JButton btn = new RoundedButton(act, 10);
            if (act.contains("Xóa") || act.contains("Trả hàng")) {
                btn.setBackground(new Color(255, 220, 220));
                btn.setForeground(Color.RED);
            }
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setPreferredSize(new Dimension(120, 45));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            btn.addActionListener(e -> {
                switch (act) {
                    case "Xóa toàn bộ sản phẩm" -> {
                        Component selected = tabbedPane.getSelectedComponent();
                        if (selected instanceof OrderTabPanel orderTab) {
                            int confirm = JOptionPane.showConfirmDialog(
                                    this,
                                    "Bạn có chắc chắn muốn xóa toàn bộ sản phẩm trong giỏ hàng?",
                                    "Xác nhận xóa",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE
                            );
                            if (confirm == JOptionPane.YES_OPTION) {
                                orderTab.clearCart();
                                updatePayment(orderTab);
                            }
                        }
                    }
                    case "Trả hàng" -> {
                        int confirm = JOptionPane.showConfirmDialog(
                                this,
                                "Xác nhận thực hiện trả hàng?",
                                "Xác nhận trả hàng",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );
                        if (confirm == JOptionPane.YES_OPTION) {
                            System.out.println(">>> Đã xác nhận trả hàng");
                        }
                    }
                    default ->
                        System.out.println(">>> Clicked: " + act);
                }
            });
            panel.add(btn);
        }
        return panel;
    }

    private void addNewOrderTab() {
        OrderTabPanel orderPanel = new OrderTabPanel();
        orderPanel.setOnCartChanged(() -> updatePayment(orderPanel));
        tabbedPane.addTab("Đơn " + tabIndex++, orderPanel);
    }

    private void updatePayment(OrderTabPanel orderTab) {
        int itemCount = orderTab.getItemCount();
        double total = orderTab.getTotal();
        double vatRate = 0.08;
        double discountRate = 0;
        double vat = total * vatRate;
        double discount = total * discountRate;
        double payable = total + vat - discount;
        paymentPanel.setValues(itemCount, total, vatRate, discountRate, payable);
    }

    private void handlePayment() {
        Component selected = tabbedPane.getSelectedComponent();
        if (!(selected instanceof OrderTabPanel orderTab)) {
            JOptionPane.showMessageDialog(this, "Không có đơn hàng nào được chọn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double customerPaid = paymentPanel.getCustomerPaid();
        double payable = paymentPanel.getPayableAmount();

        if (customerPaid < payable) {
            JOptionPane.showMessageDialog(this, "Khách hàng thanh toán chưa đủ.", "Thiếu tiền", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xác nhận thanh toán đơn hàng?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            List<Product> items = orderTab.getCartItems();
            double total = orderTab.getTotal();
            double vatRate = 0.08;
            double discountRate = 0;
            double vatAmount = total * vatRate;
            double discountAmount = total * discountRate;
            double payable2 = total + vatAmount - discountAmount;
            InvoiceModel invoice = new InvoiceModel(
                    0, // customerId
                    Session.getEmployeeId(),
                    0, // tableId
                    0, // discountId
                    payable2, // tổng cuối cùng cần thanh toán
                    new java.util.Date(),
                    items
            );
            boolean success = new ProductDAO().saveOrder(invoice);
            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Thanh toán thành công!");

                int selectedIndex = tabbedPane.getSelectedIndex();
                if (selectedIndex == 0) {
                    // Reset giỏ hàng nếu là tab đầu tiên
                    orderTab.clearCart();
                    updatePayment(orderTab);
                    paymentPanel.resetFields();
                } else {
                    // Xóa tab đơn hàng hiện tại nếu không phải tab đầu tiên
                    tabbedPane.removeTabAt(selectedIndex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "❌ Có lỗi xảy ra khi lưu hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
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
