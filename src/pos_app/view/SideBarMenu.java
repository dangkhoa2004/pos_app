package pos_app.view;

import pos_app.ui.components.RoundedPanel;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;
import javax.swing.*;
import pos_app.ui.panel.*;
import pos_app.util.IconUtil;
import pos_app.util.Session;

/**
 * Lớp SideBarMenu tạo thanh menu điều hướng bên trái cho giao diện chính của
 * ứng dụng POS. Giao diện này bao gồm logo, danh sách các chức năng như Bán
 * hàng, Quản lý sản phẩm, Khách hàng, Hóa đơn, Thống kê, Kho, Nhân viên, Cài
 * đặt, và nút Đăng xuất.
 */
public class SideBarMenu extends JPanel {

    private static final Color HOVER_COLOR = new Color(220, 235, 255);
    private static final Color NORMAL_COLOR = Color.WHITE;

    /**
     * Khởi tạo menu bên trái của ứng dụng.
     *
     * @param onMenuClick Hàm callback sẽ được gọi khi người dùng chọn một mục
     * menu; truyền vào JPanel tương ứng.
     * @param onLogout Hàm callback sẽ được gọi khi người dùng nhấn "Đăng xuất".
     */
    public SideBarMenu(Consumer<JPanel> onMenuClick, Runnable onLogout) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(230, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel logo = new JLabel();
        logo.setPreferredSize(new Dimension(180, 180));
        logo.setMaximumSize(new Dimension(180, 180));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setIcon(IconUtil.loadPng("logo.png", 180));
        add(logo);
        add(Box.createRigidArea(new Dimension(0, 30)));

        addMenu("Bán Hàng", IconUtil.loadSvg("shopping-cart.svg", 20), new POSPanel(), onMenuClick);
        addMenu("Quản lý sản phẩm", IconUtil.loadSvg("ad_product.svg", 20), new ProductPanel(), onMenuClick);
        addMenu("Khách hàng", IconUtil.loadSvg("customer_service_agent.svg", 20), new CustomerPanel(), onMenuClick);
        addMenu("Hóa đơn", IconUtil.loadSvg("alternate_file.svg", 20), new InvoicePanel(), onMenuClick);
        addMenu("Thống kê", IconUtil.loadSvg("pie_chart.svg", 20), null, onMenuClick);
        addMenu("Nhập / Xuất kho", IconUtil.loadSvg("stock.svg", 20), new StockPanel(), onMenuClick);
        addMenu("Nhân viên / Chức vụ", IconUtil.loadSvg("employee-group-line.svg", 20), new EmployeePanel(), onMenuClick);
        addMenu("Bảng điều khiển", IconUtil.loadSvg("terminal.svg", 20), new TerminalPanel(), onMenuClick);
        addMenu("Cài đặt hệ thống", IconUtil.loadSvg("cog.svg", 20), new SettingsPanel(), onMenuClick);

        add(Box.createVerticalGlue());

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

    /**
     * Thêm một mục menu vào thanh bên.
     *
     * @param title Tên mục hiển thị trong menu.
     * @param icon Biểu tượng hiển thị (SVG hoặc PNG).
     * @param target JPanel sẽ được hiển thị khi click (null nếu chưa phát
     * triển).
     * @param onClick Hàm callback sẽ được gọi khi click mục, truyền vào
     * `target`.
     */
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
                    JOptionPane.showMessageDialog(null, "Tính năng này hiện chưa được phát triển!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    onClick.accept(target);
                }
            }
        });

        add(menuItem);
        add(Box.createRigidArea(new Dimension(0, 10)));
    }
}
