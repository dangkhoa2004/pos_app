package pos_app.view;

import java.awt.*;
import javax.swing.*;
import pos_app.ui.Theme;
import pos_app.ui.panel.POSPanel;
import pos_app.util.IconUtil;

/** Main application shell: persistent navigation, page heading, and workspace. */
public class MainFrame extends JFrame {
    private final JPanel workspace = new JPanel(new BorderLayout());
    private final JLabel pageTitle = new JLabel("Bán hàng");
    private final JLabel pageSubtitle = new JLabel("Tạo đơn hàng và xử lý thanh toán");

    public MainFrame() {
        setTitle("POS • Quản lý bán hàng");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1080, 700));
        setSize(1440, 900);
        setLocationRelativeTo(null);
        setIconImage(IconUtil.loadPng("logo.png", 64).getImage());
        getContentPane().setBackground(Theme.CANVAS);
        setLayout(new BorderLayout());

        SideBarMenu sideBar = new SideBarMenu(this::showContentPanel, () -> {
            dispose();
            LoginFrame login = new LoginFrame();
            login.setLocationRelativeTo(null);
            login.setVisible(true);
        });
        add(sideBar, BorderLayout.WEST);

        JPanel page = new JPanel(new BorderLayout(0, 16));
        page.setBackground(Theme.CANVAS);
        page.setBorder(BorderFactory.createEmptyBorder(26, 30, 24, 30));
        pageTitle.setFont(Theme.TITLE);
        pageTitle.setForeground(Theme.TEXT);
        pageSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pageSubtitle.setForeground(Theme.MUTED);
        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(pageTitle);
        heading.add(Box.createVerticalStrut(4));
        heading.add(pageSubtitle);
        page.add(heading, BorderLayout.NORTH);
        workspace.setOpaque(false);
        page.add(workspace, BorderLayout.CENTER);
        add(page, BorderLayout.CENTER);

        showContentPanel(new POSPanel());
    }

    public void showContentPanel(JPanel panel) {
        workspace.removeAll();
        Theme.polish(panel);
        workspace.add(panel, BorderLayout.CENTER);
        String name = panel.getClass().getSimpleName();
        pageTitle.setText(switch (name) {
            case "POSPanel" -> "Bán hàng";
            case "ProductPanel" -> "Sản phẩm";
            case "CustomerPanel" -> "Khách hàng";
            case "InvoicePanel" -> "Hóa đơn";
            case "StockPanel" -> "Nhập / Xuất kho";
            case "EmployeePanel" -> "Nhân viên";
            case "TerminalPanel" -> "Nhật ký hệ thống";
            case "SettingsPanel" -> "Cài đặt hệ thống";
            default -> "Thống kê";
        });
        pageSubtitle.setText(switch (name) {
            case "POSPanel" -> "Tạo đơn hàng và xử lý thanh toán";
            case "ProductPanel" -> "Quản lý danh mục và thông tin sản phẩm";
            case "CustomerPanel" -> "Theo dõi hồ sơ và lịch sử khách hàng";
            case "InvoicePanel" -> "Tra cứu và quản lý giao dịch bán hàng";
            case "StockPanel" -> "Theo dõi hoạt động nhập và xuất hàng";
            case "EmployeePanel" -> "Quản lý tài khoản và nhân sự cửa hàng";
            case "TerminalPanel" -> "Theo dõi hoạt động của hệ thống";
            case "SettingsPanel" -> "Thông tin và cấu hình cửa hàng";
            default -> "Tổng quan hoạt động kinh doanh";
        });
        workspace.revalidate();
        workspace.repaint();
    }

    public static void main(String[] args) {
        pos_app.App.main(args);
    }
}
