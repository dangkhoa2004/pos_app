package pos_app.view;

/**
 *
 * @author 04dkh
 */
import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.*;
import javax.swing.*;
import pos_app.util.Session;

public class MainFrame extends JFrame {

    private JPanel mainContent;

    public MainFrame() {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Session.autoLoginFromFile();  // <--- PHẢI có dòng này!

        if (!Session.isLoggedIn()) {
            JOptionPane.showMessageDialog(null, "Bạn cần đăng nhập trước khi sử dụng hệ thống.");
            dispose();
            new LoginFrame().setVisible(true);
            return;
        }

        // Cài đặt cơ bản frame
        setTitle("📊 POS - Quản lý bán hàng");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE); // Nền trắng đẹp

        // Sidebar menu
        SideBarMenu sideBar = new SideBarMenu(this::showContentPanel, () -> {
            dispose(); // logout
            new LoginFrame().setVisible(true);
        });
        add(sideBar, BorderLayout.WEST);

        // Nội dung chính
        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);

        // Panel chào mừng
        JLabel welcomeLabel = new JLabel("🏪 Chào mừng đến hệ thống POS", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        welcomeLabel.setForeground(new Color(60, 60, 60));
        mainContent.add(welcomeLabel, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
    }

    public void showContentPanel(JPanel panel) {
        mainContent.removeAll();
        mainContent.add(panel, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
