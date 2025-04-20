package pos_app.view;

import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.*;
import javax.swing.*;
import pos_app.util.SQLSeeder;
import pos_app.util.Session;

/**
 * Lớp MainFrame là cửa sổ chính của ứng dụng POS. Giao diện bao gồm sidebar
 * menu bên trái và vùng nội dung trung tâm động.
 */
public class MainFrame extends JFrame {

    /**
     * Panel hiển thị nội dung chính bên phải.
     */
    private JPanel mainContent;

    /**
     * Khởi tạo giao diện chính. Sidebar bên trái điều hướng các chức năng, vùng
     * nội dung trung tâm hiển thị theo lựa chọn.
     */
    public MainFrame() {
        setTitle("POS - Quản lý bán hàng");
        setBounds(100, 100, 1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        SideBarMenu sideBar = new SideBarMenu(this::showContentPanel, () -> {
            dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setExtendedState(JFrame.NORMAL);
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        });
        add(sideBar, BorderLayout.WEST);

        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Chào mừng đến hệ thống POS", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        welcomeLabel.setForeground(new Color(60, 60, 60));
        mainContent.add(welcomeLabel, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
    }

    /**
     * Hiển thị nội dung tương ứng trong vùng trung tâm.
     *
     * @param panel JPanel cần được hiển thị
     */
    public void showContentPanel(JPanel panel) {
        mainContent.removeAll();
        mainContent.add(panel, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    /**
     * Điểm khởi chạy ứng dụng POS. Nếu đăng nhập tự động thất bại, chuyển sang
     * giao diện đăng nhập.
     *
     * @param args không sử dụng
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
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
}
