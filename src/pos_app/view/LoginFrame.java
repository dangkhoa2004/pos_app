package pos_app.view;

import com.formdev.flatlaf.FlatIntelliJLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import pos_app.dao.EmployeeDAO;
import pos_app.models.Employee;
import pos_app.util.IconUtil;
import pos_app.util.Session;

/**
 * Lớp LoginFrame cung cấp giao diện đăng nhập cho hệ thống POS. Người dùng nhập
 * thông tin tài khoản, hệ thống sẽ xác thực và chuyển sang MainFrame nếu thành
 * công.
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    /**
     * Khởi tạo giao diện đăng nhập và các thành phần hiển thị.
     */
    public LoginFrame() {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Đăng nhập hệ thống");
        setSize(750, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(IconUtil.loadPng("logo.png", 200));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(logoLabel, BorderLayout.CENTER);
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(300, 0));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel lblTitle = new JLabel("Đăng nhập hệ thống");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        JPanel usernamePanel = createInputField("Tên đăng nhập", txtUsername);
        JPanel passwordPanel = createInputField("Mật khẩu", txtPassword);

        usernamePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        passwordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnLogin.setBackground(new Color(33, 150, 243));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(btnLogin.getFont().deriveFont(Font.BOLD, 14f));
        btnLogin.addActionListener(this::handleLogin);

        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton forgotBtn = new JButton("Quên mật khẩu?");
        JButton registerBtn = new JButton("Đăng ký");
        forgotBtn.setFocusPainted(false);
        registerBtn.setFocusPainted(false);
        forgotBtn.setContentAreaFilled(false);
        registerBtn.setContentAreaFilled(false);
        forgotBtn.setForeground(Color.BLUE);
        registerBtn.setForeground(Color.BLUE);
        optionsPanel.add(forgotBtn);
        optionsPanel.add(new JLabel("|"));
        optionsPanel.add(registerBtn);
        optionsPanel.setOpaque(false);

        rightPanel.add(lblTitle);
        rightPanel.add(Box.createVerticalStrut(25));
        rightPanel.add(usernamePanel);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(passwordPanel);
        rightPanel.add(Box.createVerticalStrut(25));
        rightPanel.add(btnLogin);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(optionsPanel);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    /**
     * Tạo một hàng nhập liệu gồm nhãn và trường nhập.
     *
     * @param labelText tên nhãn hiển thị
     * @param inputField trường nhập liệu (JTextField hoặc JPasswordField)
     * @return JPanel chứa nhãn và trường nhập
     */
    private JPanel createInputField(String labelText, JComponent inputField) {
        JPanel container = new JPanel(new BorderLayout(5, 5));
        container.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));

        inputField.setPreferredSize(new Dimension(0, 40));
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        inputField.setBackground(Color.WHITE);

        container.add(label, BorderLayout.NORTH);
        container.add(inputField, BorderLayout.CENTER);
        return container;
    }

    /**
     * Xử lý sự kiện khi nhấn nút đăng nhập. Nếu thông tin hợp lệ → ghi nhận
     * phiên làm việc và mở MainFrame. Nếu sai → hiển thị thông báo lỗi.
     *
     * @param e sự kiện ActionEvent
     */
    private void handleLogin(ActionEvent e) {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        Employee emp = EmployeeDAO.checkLogin(username, password);
        if (emp != null) {
            Session.login(emp);

            try {
                String token = java.util.UUID.randomUUID().toString();
                pos_app.models.UserSession session = new pos_app.models.UserSession();
                session.setEmployeeId(emp.getId());
                session.setSessionToken(token);
                session.setIpAddress(Session.getIPAddress());
                session.setDeviceInfo(System.getProperty("os.name"));
                new pos_app.dao.UserSessionDAO().createSession(session);

                Session.saveAccount(username, password);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
            dispose();
            new MainFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu.");
        }
    }

    /**
     * Điểm khởi chạy nếu chạy trực tiếp frame đăng nhập này.
     *
     * @param args không sử dụng
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
