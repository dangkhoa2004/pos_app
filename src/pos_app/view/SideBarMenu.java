/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pos_app.view;

/**
 *
 * @author 04dkh
 */
import pos_app.ui.components.RoundedPanel;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;
import javax.swing.*;
import pos_app.ui.panel.CustomerPanel;
import pos_app.ui.panel.EmployeePanel;
import pos_app.ui.panel.InvoicePanel;
import pos_app.ui.panel.ProductPanel;
import pos_app.ui.panel.StockPanel;
import pos_app.util.IconUtil;
import pos_app.util.Session;

public class SideBarMenu extends JPanel {

    private static final Color HOVER_COLOR = new Color(220, 235, 255);
    private static final Color NORMAL_COLOR = Color.WHITE;

    public SideBarMenu(Consumer<JPanel> onMenuClick, Runnable onLogout) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(230, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // -------- LOGO --------
        JLabel logo = new JLabel();
        logo.setPreferredSize(new Dimension(180, 180));
        logo.setMaximumSize(new Dimension(180, 180));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setIcon(IconUtil.loadPng("logo.png", 180));
        add(logo);
        add(Box.createRigidArea(new Dimension(0, 30)));

        // -------- MENU ITEMS --------
        addMenu("Quản lý sản phẩm", IconUtil.loadSvg("ad_product.svg", 20), new ProductPanel(), onMenuClick);
        addMenu("Khách hàng", IconUtil.loadSvg("customer_service_agent.svg", 20), new CustomerPanel(), onMenuClick);
        addMenu("Hóa đơn", IconUtil.loadSvg("alternate_file.svg", 20), new InvoicePanel(), onMenuClick);
        addMenu("Thống kê", IconUtil.loadSvg("pie_chart.svg", 20), null, onMenuClick);
        addMenu("Nhập / Xuất kho", IconUtil.loadSvg("stock.svg", 20), new StockPanel(), onMenuClick);
        addMenu("Nhân viên / Chức vụ", IconUtil.loadSvg("employee-group-line.svg", 20), new EmployeePanel(), onMenuClick);
        addMenu("Cài đặt hệ thống", IconUtil.loadSvg("cog.svg", 20), null, onMenuClick);

        add(Box.createVerticalGlue());

        // -------- LOGOUT --------
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(180, 40));
        btnLogout.setFocusPainted(false);
        btnLogout.setBackground(new Color(255, 80, 80));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.setFont(btnLogout.getFont().deriveFont(Font.BOLD));
        btnLogout.addActionListener(e -> {
            Session.logout();
            onLogout.run();
        });
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(btnLogout);
    }

    private void addMenu(String title, Icon icon, JPanel target, Consumer<JPanel> onClick) {
        RoundedPanel menuItem = new RoundedPanel(15);
        menuItem.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        menuItem.setMaximumSize(new Dimension(180, 45));
        menuItem.setBackground(NORMAL_COLOR);
        menuItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        menuItem.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel iconLabel = new JLabel(icon);
        JLabel textLabel = new JLabel(title);
        textLabel.setFont(textLabel.getFont().deriveFont(Font.PLAIN, 14));

        menuItem.add(iconLabel);
        menuItem.add(textLabel);

        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menuItem.setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuItem.setBackground(NORMAL_COLOR);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (target == null) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Tính năng này hiện chưa được phát triển!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    onClick.accept(target);
                }
            }
        });
        add(menuItem);
        add(Box.createRigidArea(new Dimension(0, 10)));
    }
}
