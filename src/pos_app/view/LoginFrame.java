/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.view;

/**
 *
 * @author 04dkh
 */
import com.formdev.flatlaf.FlatIntelliJLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import pos_app.dao.EmployeeDAO;
import pos_app.models.Employee;
import pos_app.util.IconUtil;
import pos_app.util.Session;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        // Set Look and Feel
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

        // -------- LEFT: LOGO --------
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(IconUtil.loadPng("logo.png", 200)); // đường dẫn logo
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(logoLabel, BorderLayout.CENTER);
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(300, 0));

        // -------- RIGHT: FORM --------
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel lblTitle = new JLabel("Đăng nhập hệ thống");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tạo input
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        JPanel usernamePanel = createInputField("Tên đăng nhập", txtUsername);
        JPanel passwordPanel = createInputField("Mật khẩu", txtPassword);

        // Căn giữa input
        usernamePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        passwordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nút login
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnLogin.setBackground(new Color(33, 150, 243));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(btnLogin.getFont().deriveFont(Font.BOLD, 14f));
        btnLogin.addActionListener(this::handleLogin);

        // Forgot password & Register
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

        // Add thành phần vào panel
        rightPanel.add(lblTitle);
        rightPanel.add(Box.createVerticalStrut(25));
        rightPanel.add(usernamePanel);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(passwordPanel);
        rightPanel.add(Box.createVerticalStrut(25));
        rightPanel.add(btnLogin);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(optionsPanel);

        // Gộp trái - phải
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    // ---------- HÀM TẠO INPUT ĐẸP ----------
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

    private void handleLogin(ActionEvent e) {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        Employee emp = EmployeeDAO.checkLogin(username, password);
        if (emp != null) {
            Session.login(emp);
            Session.saveAccount(username, password); // ⚠️ GỌI Ở ĐÂY!
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
            this.dispose();
            new MainFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
