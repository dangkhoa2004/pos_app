package pos_app.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import pos_app.dao.EmployeeDAO;
import pos_app.models.Employee;
import pos_app.util.IconUtil;
import pos_app.util.Session;
import pos_app.ui.Theme;

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
        Theme.install();
        setTitle("Đăng nhập • POS Manager");
        setSize(900, 560);
        setMinimumSize(new Dimension(760, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel brandPanel = new JPanel();
        brandPanel.setBackground(Theme.NAV);
        brandPanel.setPreferredSize(new Dimension(350, 0));
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(BorderFactory.createEmptyBorder(70, 42, 50, 42));
        JLabel logoLabel = new JLabel(IconUtil.loadPng("logo.png", 112));
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel brandTitle = new JLabel("Bán hàng gọn nhẹ.");
        brandTitle.setFont(new Font("Segoe UI", Font.BOLD, 27));
        brandTitle.setForeground(Color.WHITE);
        brandTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel brandSubtitle = new JLabel("Quản lý cửa hàng hiệu quả mỗi ngày.");
        brandSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        brandSubtitle.setForeground(new Color(203, 213, 225));
        brandSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandPanel.add(logoLabel);
        brandPanel.add(Box.createVerticalStrut(38));
        brandPanel.add(brandTitle);
        brandPanel.add(Box.createVerticalStrut(10));
        brandPanel.add(brandSubtitle);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(72, 64, 60, 64));
        rightPanel.setBackground(Theme.CANVAS);

        JLabel eyebrow = new JLabel("CHÀO MỪNG TRỞ LẠI");
        eyebrow.setFont(new Font("Segoe UI", Font.BOLD, 11));
        eyebrow.setForeground(Theme.BRAND);
        eyebrow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblTitle = new JLabel("Đăng nhập tài khoản");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Theme.TEXT);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel hint = new JLabel("Nhập thông tin để tiếp tục vào hệ thống.");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hint.setForeground(Theme.MUTED);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        JPanel usernamePanel = createInputField("Tên đăng nhập", txtUsername);
        JPanel passwordPanel = createInputField("Mật khẩu", txtPassword);

        usernamePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        passwordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        usernamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnLogin.setPreferredSize(new Dimension(300, 46));
        btnLogin.setBackground(Theme.BRAND);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(btnLogin.getFont().deriveFont(Font.BOLD, 14f));
        btnLogin.addActionListener(this::handleLogin);

        txtUsername.setPreferredSize(new Dimension(300, 42));
        txtPassword.setPreferredSize(new Dimension(300, 42));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER), BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER), BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        rightPanel.add(eyebrow);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(lblTitle);
        rightPanel.add(Box.createVerticalStrut(7));
        rightPanel.add(hint);
        rightPanel.add(Box.createVerticalStrut(32));
        rightPanel.add(usernamePanel);
        rightPanel.add(Box.createVerticalStrut(18));
        rightPanel.add(passwordPanel);
        rightPanel.add(Box.createVerticalStrut(26));
        rightPanel.add(btnLogin);
        getRootPane().setDefaultButton(btnLogin);
        add(brandPanel, BorderLayout.WEST);
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
