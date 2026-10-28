package pos_app.view;

import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import pos_app.ui.Theme;
import pos_app.ui.panel.*;
import pos_app.util.IconUtil;
import pos_app.util.Session;

/** Persistent, compact navigation for the POS workspace. */
public class SideBarMenu extends JPanel {
    private JButton selectedButton;

    public SideBarMenu(Consumer<JPanel> onMenuClick, Runnable onLogout) {
        setLayout(new BorderLayout());
        setBackground(Theme.NAV);
        setPreferredSize(new Dimension(252, 0));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 13, 0));
        brand.setOpaque(false);
        brand.setBorder(new EmptyBorder(24, 22, 24, 16));
        JLabel logo = new JLabel(IconUtil.loadPng("logo.png", 38));
        JLabel brandName = new JLabel("POS MANAGER");
        brandName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        brandName.setForeground(Color.WHITE);
        brand.add(logo);
        brand.add(brandName);
        add(brand, BorderLayout.NORTH);

        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(8, 14, 12, 14));
        addMenu(menu, "Bán hàng", "shopping-cart.svg", POSPanel::new, onMenuClick);
        addMenu(menu, "Sản phẩm", "ad_product.svg", ProductPanel::new, onMenuClick);
        addMenu(menu, "Khách hàng", "customer_service_agent.svg", CustomerPanel::new, onMenuClick);
        addMenu(menu, "Hóa đơn", "alternate_file.svg", InvoicePanel::new, onMenuClick);
        addMenu(menu, "Thống kê", "pie_chart.svg", null, onMenuClick);
        addMenu(menu, "Nhập / Xuất kho", "stock.svg", StockPanel::new, onMenuClick);
        addMenu(menu, "Nhân viên", "employee-group-line.svg", EmployeePanel::new, onMenuClick);
        addMenu(menu, "Nhật ký hệ thống", "terminal.svg", TerminalPanel::new, onMenuClick);
        addMenu(menu, "Cài đặt", "cog.svg", SettingsPanel::new, onMenuClick);
        JScrollPane scroll = new JScrollPane(menu);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        JButton logout = new JButton("Đăng xuất");
        logout.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logout.setForeground(new Color(254, 202, 202));
        logout.setBackground(new Color(127, 29, 29));
        logout.setFocusPainted(false);
        logout.setBorder(new EmptyBorder(12, 16, 12, 16));
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(14, 18, 20, 18));
        footer.add(logout, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
        logout.addActionListener(e -> {
            Session.logout();
            onLogout.run();
        });
    }

    private void addMenu(JPanel menu, String title, String iconName,
                         java.util.function.Supplier<JPanel> factory, Consumer<JPanel> onClick) {
        Icon icon = IconUtil.loadSvg(iconName, 19);
        if (icon instanceof FlatSVGIcon svg) {
            svg.setColorFilter(new FlatSVGIcon.ColorFilter(color ->
                    new Color(226, 232, 240, color.getAlpha())));
        }
        JButton item = new JButton(title, icon);
        item.setHorizontalAlignment(SwingConstants.LEFT);
        item.setIconTextGap(14);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setForeground(new Color(203, 213, 225));
        item.setBackground(Theme.NAV);
        item.setBorder(new EmptyBorder(12, 14, 12, 10));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        item.setPreferredSize(new Dimension(220, 46));
        item.setFocusPainted(false);
        item.setContentAreaFilled(true);
        item.setOpaque(true);
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (item != selectedButton) item.setBackground(new Color(31, 41, 55));
            }
            @Override public void mouseExited(MouseEvent e) {
                if (item != selectedButton) item.setBackground(Theme.NAV);
            }
        });
        item.addActionListener(e -> {
            if (selectedButton != null) {
                selectedButton.setBackground(Theme.NAV);
                selectedButton.setForeground(new Color(203, 213, 225));
            }
            selectedButton = item;
            item.setBackground(new Color(30, 64, 175));
            item.setForeground(Color.WHITE);
            if (factory == null) {
                JOptionPane.showMessageDialog(this, "Tính năng thống kê đang được phát triển.",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else onClick.accept(factory.get());
        });
        if (selectedButton == null) {
            selectedButton = item;
            item.setBackground(new Color(30, 64, 175));
            item.setForeground(Color.WHITE);
        }
        menu.add(item);
        menu.add(Box.createVerticalStrut(5));
    }
}
