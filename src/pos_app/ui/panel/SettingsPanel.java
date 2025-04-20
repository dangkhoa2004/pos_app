package pos_app.ui.panel;

import pos_app.dao.SettingsDAO;
import pos_app.models.Settings;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.File;
import pos_app.util.IconUtil;

/**
 * Giao diện SettingsPanel nâng cấp, giao diện đẹp và hiện đại hơn.
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
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadSettings();
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

    private JPanel createFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);

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

        JLabel[] labels = {
            new JLabel("Tên cửa hàng"), new JLabel("Địa chỉ"), new JLabel("Số điện thoại"),
            new JLabel("Email"), new JLabel("Logo (chọn file)"), new JLabel("Thuế (%)"),
            new JLabel("Tiền tệ"), new JLabel("Tiền tố hóa đơn"), new JLabel("Tên máy in"),
            new JLabel("Ngôn ngữ"), new JLabel("Thư mục sao lưu")
        };

        txtStoreName = new JTextField(20);

        txtAreaAddress = new JTextArea(3, 20);
        txtAreaAddress.setLineWrap(true);
        txtAreaAddress.setWrapStyleWord(true);
        txtAreaAddress.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtAreaAddress.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 189, 189)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        JScrollPane scrollAddress = new JScrollPane(txtAreaAddress);
        scrollAddress.setBorder(null);
        scrollAddress.setPreferredSize(new Dimension(300, 60));

        txtPhone = new JTextField(20);
        txtEmail = new JTextField(20);
        txtLogoPath = new JTextField(15);
        txtTaxRate = new JTextField(20);
        txtCurrency = new JTextField(20);
        txtInvoicePrefix = new JTextField(20);
        txtPrinterName = new JTextField(20);
        txtLanguage = new JTextField(20);
        txtBackupPath = new JTextField(15);

        btnChooseLogo = new JButton(IconUtil.loadSvg("folder.svg", 16));
        btnChooseLogo.setPreferredSize(new Dimension(30, 30));
        btnChooseLogo.setToolTipText("Chọn logo");
        btnChooseLogo.addActionListener(e -> chooseFile(txtLogoPath, JFileChooser.FILES_ONLY));

        btnChooseBackup = new JButton(IconUtil.loadSvg("folder.svg", 16));
        btnChooseBackup.setPreferredSize(new Dimension(30, 30));
        btnChooseBackup.setToolTipText("Chọn thư mục sao lưu");
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
            gbc.gridy = i;
            panel.add(labels[i], gbc);

            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }

        wrapper.add(panel, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);

        btnSave = new JButton(" Lưu cài đặt", IconUtil.loadSvg("save.svg", 18));
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(76, 175, 80));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setPreferredSize(new Dimension(170, 40));
        btnSave.setIconTextGap(10);

        btnSave.addActionListener(e -> saveSettings());
        panel.add(btnSave);

        return panel;
    }

    private JPanel combineFileField(JTextField textField, JButton button) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBackground(Color.WHITE);
        panel.add(textField, BorderLayout.CENTER);
        panel.add(button, BorderLayout.EAST);
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
}
