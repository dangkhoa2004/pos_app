package pos_app.view;

import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.*;
import java.util.Enumeration;
import javax.swing.*;
import pos_app.util.SQLSeeder;
import pos_app.util.Session;
import pos_app.util.IconUtil;

/**
 * Lớp MainFrame là cửa sổ chính của ứng dụng POS. Giao diện bao gồm sidebar
 * menu bên trái và vùng nội dung trung tâm động, thay đổi theo lựa chọn của
 * người dùng.
 */
public class MainFrame extends JFrame {

    /**
     * Panel hiển thị nội dung trung tâm của ứng dụng.
     */
    private JPanel mainContent;

    /**
     * Khởi tạo giao diện chính của ứng dụng POS. Thiết lập title, icon, layout,
     * sidebar menu và vùng nội dung chính.
     */
    public MainFrame() {
        setTitle("POS - Quản lý bán hàng");
        setBounds(100, 100, 1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Đặt icon cửa sổ bằng tiện ích IconUtil
        ImageIcon icon = IconUtil.loadPng("logo.png", 180);
        setIconImage(icon.getImage());

        // Sidebar điều hướng
        SideBarMenu sideBar = new SideBarMenu(this::showContentPanel, () -> {
            dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setExtendedState(JFrame.NORMAL);
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        });
        add(sideBar, BorderLayout.WEST);

        // Vùng hiển thị nội dung chính
        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Chào mừng đến hệ thống POS", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcomeLabel.setForeground(new Color(60, 60, 60));
        mainContent.add(welcomeLabel, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
    }

    /**
     * Thay đổi nội dung hiển thị ở vùng trung tâm.
     *
     * @param panel JPanel cần hiển thị thay thế nội dung cũ
     */
    public void showContentPanel(JPanel panel) {
        mainContent.removeAll();
        mainContent.add(panel, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    /**
     * Hàm main – Điểm khởi chạy chính của ứng dụng POS. Thiết lập giao diện,
     * font chữ mặc định, chạy script khởi tạo dữ liệu, kiểm tra đăng nhập và
     * hiển thị giao diện tương ứng.
     *
     * @param args đối số dòng lệnh (không sử dụng)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf()); // Giao diện FlatLaf
            setUIFont(new Font("Segoe UI", Font.PLAIN, 14)); // Đặt font mặc định
        } catch (Exception e) {
            e.printStackTrace();
        }

        SQLSeeder.run("src/pos_app/sql/pos_app.sql");

        SwingUtilities.invokeLater(() -> {
            Session.autoLoginFromFile();

            if (!Session.isLoggedIn()) {
                JOptionPane.showMessageDialog(null, "Bạn cần đăng nhập trước khi sử dụng hệ thống.");
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setExtendedState(JFrame.NORMAL);
                loginFrame.setLocationRelativeTo(null);
                loginFrame.setVisible(true);
            } else {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setExtendedState(JFrame.NORMAL);
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setVisible(true);
            }
        });
    }

    /**
     * Thiết lập font mặc định cho toàn bộ giao diện UI Swing. Giúp đảm bảo mọi
     * thành phần đều sử dụng cùng một kiểu font.
     *
     * @param font đối tượng Font cần đặt làm mặc định
     */
    public static void setUIFont(Font font) {
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, new javax.swing.plaf.FontUIResource(font));
            }
        }
    }
}
