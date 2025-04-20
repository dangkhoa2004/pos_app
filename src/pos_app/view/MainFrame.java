package pos_app.view;

/**
 *
 * @author 04dkh
 */
import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.*;
import javax.swing.*;
import pos_app.util.SQLSeeder;
import pos_app.util.Session;

public class MainFrame extends JFrame {

    private JPanel mainContent;

    public MainFrame() {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Session.autoLoginFromFile();

        if (!Session.isLoggedIn()) {
            JOptionPane.showMessageDialog(null, "Bạn cần đăng nhập trước khi sử dụng hệ thống.");
            dispose();
            new LoginFrame().setVisible(true);
            return;
        }

        setTitle("POS - Quản lý bán hàng");
        setBounds(100, 100, 1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        SideBarMenu sideBar = new SideBarMenu(this::showContentPanel, () -> {
            dispose();
            new LoginFrame().setVisible(true);
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

    public void showContentPanel(JPanel panel) {
        mainContent.removeAll();
        mainContent.add(panel, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    public static void main(String[] args) {
        SQLSeeder.run("src/pos_app/sql/pos_app.sql");
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
