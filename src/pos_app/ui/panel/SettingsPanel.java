package pos_app.ui.panel;

import pos_app.dao.SettingsDAO;
import pos_app.models.Settings;
import pos_app.util.IconUtil;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.File;

/**
 * Giao diện SettingsPanel được thiết kế lại để hiển thị rõ hơn trên màn hình
 * nhỏ, có cuộn dọc, layout thoáng và dễ nhập liệu.
 */
public class SettingsPanel extends JPanel {

    private JTextField txtStoreName, txtPhone, txtEmail;
    private JTextField txtLogoPath, txtTaxRate, txtCurrency, txtInvoicePrefix;
    private JTextField txtPrinterName, txtLanguage, txtBackupPath;
    private JTextArea txtAreaAddress;
    private JButton btnSave, btnChooseLogo, btnChooseBackup;

    private final SettingsDAO settingsDAO = new SettingsDAO();

    public SettingsPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(wrapFormWithScroll(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        loadSettings();
        setComponentBackgroundWhite(this);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(" CÀI ĐẶT HỆ THỐNG", IconUtil.loadSvg("cog.svg", 24), JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(40, 53, 147));
        lblTitle.setIconTextGap(10);

        panel.add(lblTitle, BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane wrapFormWithScroll() {
        JPanel formPanel = createFormPanel();
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(189, 189, 189), 1, true),
                "Thông tin cấu hình",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(55, 71, 79)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel[] labels = {
            new JLabel("Tên cửa hàng"), new JLabel("Địa chỉ"), new JLabel("Số điện thoại"),
            new JLabel("Email"), new JLabel("Logo (chọn file)"), new JLabel("Thuế (%)"),
            new JLabel("Tiền tệ"), new JLabel("Tiền tố hóa đơn"), new JLabel("Tên máy in"),
            new JLabel("Ngôn ngữ"), new JLabel("Thư mục sao lưu")
        };

        int row = 0;

        // Input Fields
        txtStoreName = buildTextField();
        txtAreaAddress = new JTextArea(3, 20);
        txtAreaAddress.setLineWrap(true);
        txtAreaAddress.setWrapStyleWord(true);
        txtAreaAddress.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtAreaAddress.setBorder(BorderFactory.createLineBorder(new Color(189, 189, 189)));
        JScrollPane scrollAddress = new JScrollPane(txtAreaAddress);
        scrollAddress.setPreferredSize(new Dimension(300, 60));

        txtPhone = buildTextField();
        txtEmail = buildTextField();
        txtLogoPath = buildTextField();
        txtTaxRate = buildTextField();
        txtCurrency = buildTextField();
        txtInvoicePrefix = buildTextField();
        txtPrinterName = buildTextField();
        txtLanguage = buildTextField();
        txtBackupPath = buildTextField();

        btnChooseLogo = createIconButton("Chọn logo", "folder.svg");
        btnChooseLogo.addActionListener(e -> chooseFile(txtLogoPath, JFileChooser.FILES_ONLY));

        btnChooseBackup = createIconButton("Chọn thư mục", "folder.svg");
        btnChooseBackup.addActionListener(e -> chooseFile(txtBackupPath, JFileChooser.DIRECTORIES_ONLY));

        JComponent[] fields = {
            txtStoreName, scrollAddress, txtPhone, txtEmail,
            combineFileField(txtLogoPath, btnChooseLogo),
            txtTaxRate, txtCurrency, txtInvoicePrefix, txtPrinterName,
            txtLanguage, combineFileField(txtBackupPath, btnChooseBackup)
        };

        for (int i = 0; i < labels.length; i++) {
            labels[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            gbc.gridx = 0;
            gbc.gridy = row;
            panel.add(labels[i], gbc);

            gbc.gridx = 1;
            panel.add(fields[i], gbc);
            row++;
        }

        return panel;
    }

    private JTextField buildTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(300, 30));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    private JButton createIconButton(String tooltip, String iconName) {
        JButton button = new JButton(IconUtil.loadSvg(iconName, 16));
        button.setPreferredSize(new Dimension(32, 32));
        button.setToolTipText(tooltip);
        return button;
    }

    private JPanel combineFileField(JTextField textField, JButton button) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(textField, BorderLayout.CENTER);
        panel.add(button, BorderLayout.EAST);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton(" Lưu cài đặt", IconUtil.loadSvg("save.svg", 18));
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(76, 175, 80));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setPreferredSize(new Dimension(180, 40));
        btnSave.setIconTextGap(10);
        btnSave.addActionListener(e -> saveSettings());

        panel.add(btnSave);
        return panel;
    }

    private void chooseFile(JTextField targetField, int mode) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(mode);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            targetField.setText(selected.getAbsolutePath());
        }
    }

    private void loadSettings() {
        Settings s = settingsDAO.getSettings();
        if (s != null) {
            txtStoreName.setText(s.getStoreName());
            txtAreaAddress.setText(s.getAddress());
            txtPhone.setText(s.getPhone());
            txtEmail.setText(s.getEmail());
            txtLogoPath.setText(s.getLogoPath());
            txtTaxRate.setText(String.valueOf(s.getTaxRate()));
            txtCurrency.setText(s.getCurrency());
            txtInvoicePrefix.setText(s.getInvoicePrefix());
            txtPrinterName.setText(s.getPrinterName());
            txtLanguage.setText(s.getDefaultLanguage());
            txtBackupPath.setText(s.getBackupPath());
        } else {
            JOptionPane.showMessageDialog(this, "Không tải được cấu hình!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveSettings() {
        try {
            Settings s = new Settings();
            s.setId(1);
            s.setStoreName(txtStoreName.getText());
            s.setAddress(txtAreaAddress.getText());
            s.setPhone(txtPhone.getText());
            s.setEmail(txtEmail.getText());
            s.setLogoPath(txtLogoPath.getText());
            s.setTaxRate(Double.parseDouble(txtTaxRate.getText()));
            s.setCurrency(txtCurrency.getText());
            s.setInvoicePrefix(txtInvoicePrefix.getText());
            s.setPrinterName(txtPrinterName.getText());
            s.setDefaultLanguage(txtLanguage.getText());
            s.setBackupPath(txtBackupPath.getText());

            boolean success = settingsDAO.updateSettings(s);
            if (success) {
                JOptionPane.showMessageDialog(this, "\u2705 Đã lưu cài đặt thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "\u274c Lưu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❗ Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setComponentBackgroundWhite(Component component) {
        if (component instanceof JPanel || component instanceof JScrollPane || component instanceof JTabbedPane) {
            component.setBackground(Color.WHITE);
        }

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                // Trừ JTextField và JButton
                if (!(child instanceof JTextField) && !(child instanceof JButton)) {
                    setComponentBackgroundWhite(child);
                }
            }
        }
    }
}
