/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.ui.panel;

/**
 *
 * @author 04dkh
 */
import java.awt.*;
import javax.swing.*;
import pos_app.ui.components.RoundedButton;
import pos_app.util.IconUtil;

public class POSPanel extends JPanel {

    private final JTabbedPane tabbedPane = new JTabbedPane();
    private int tabIndex = 1;

    public POSPanel() {
        setLayout(new BorderLayout());
        add(buildTopBar(), BorderLayout.NORTH);
        add(buildMainArea(), BorderLayout.CENTER);
        add(buildBottomButtons(), BorderLayout.SOUTH);
        addNewOrderTab(); // Mặc định có 1 đơn
    }

    private JPanel buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topBar.setBackground(new Color(230, 240, 255));

        // Thanh tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        JTextField tfSearch = new JTextField(30);
        tfSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfSearch.setToolTipText("Thêm sản phẩm vào đơn (F3)");
        JButton btnScan = new JButton("SCAN");
        //        JButton btn = new RoundedButton(act, 10);
        btnScan.setIcon(IconUtil.loadSvg("scan-qr-code.svg", 20));

        searchPanel.add(tfSearch);
        searchPanel.add(btnScan);

        // Tabs và nút tạo đơn mới
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tabPanel.setOpaque(false);

        JButton btnAddTab = new JButton();
//        JButton btn = new RoundedButton(act, 10);
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

        // Vùng đơn hàng trái & thanh toán phải
        JPanel left = new JPanel(new BorderLayout());
        left.add(tabbedPane, BorderLayout.CENTER);
        PaymentPanel right = new PaymentPanel(); // Tạo riêng lớp PaymentPanel
        main.add(left);
        main.add(right);
        left.setBackground(new Color(255, 255, 255));
        main.setBackground(new Color(255, 255, 255));
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

            // Gắn ActionListener
            btn.addActionListener(e -> {
                switch (act) {
                    case "Xóa toàn bộ sản phẩm" -> {
                        int confirm = JOptionPane.showConfirmDialog(
                                this,
                                "Bạn có chắc chắn muốn xóa toàn bộ sản phẩm trong giỏ hàng?",
                                "Xác nhận xóa",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE
                        );
                        if (confirm == JOptionPane.YES_OPTION) {
                            // TODO: Xử lý xóa sản phẩm ở đây
                            System.out.println(">>> Đã xác nhận xóa toàn bộ sản phẩm");
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
                            // TODO: Xử lý logic trả hàng
                            System.out.println(">>> Đã xác nhận trả hàng");
                        }
                    }

                    default -> {
                        // TODO: Logic cho các nút còn lại
                        System.out.println(">>> Clicked: " + act);
                    }
                }
            });

            panel.add(btn);
        }

        return panel;
    }

    private void addNewOrderTab() {
        OrderTabPanel orderPanel = new OrderTabPanel(); // bạn sẽ tạo class này
        tabbedPane.addTab("Đơn " + tabIndex++, orderPanel);
    }

}
