/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.ui.components;

import java.awt.*;
import javax.swing.*;

/**
 * RoundedTextField là phiên bản tùy biến của JTextField có góc bo tròn.
 *
 * Cung cấp giao diện đầu vào văn bản mềm mại, hiện đại, sử dụng đồ họa 2D để vẽ
 * nền và viền bo tròn. Thích hợp cho các giao diện POS thân thiện người dùng.
 *
 * @author 04dkh
 */
public class RoundedTextField extends JTextField {

    /**
     * Độ bo tròn của góc khung nhập liệu
     */
    private final int radius;

    /**
     * Tạo RoundedTextField với số cột cố định và bo góc chỉ định.
     *
     * @param columns số lượng cột ký tự
     * @param radius độ bo góc (px)
     */
    public RoundedTextField(int columns, int radius) {
        super(columns);
        this.radius = radius;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setBackground(Color.WHITE);
    }

    /**
     * Tạo RoundedTextField với nội dung sẵn có và bo góc chỉ định.
     *
     * @param text nội dung mặc định của ô nhập
     * @param radius độ bo góc (px)
     */
    public RoundedTextField(String text, int radius) {
        super(text);
        this.radius = radius;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setBackground(Color.WHITE);
    }

    /**
     * Ghi đè phương thức paintComponent để vẽ nền bo tròn trước khi hiển thị
     * nội dung.
     *
     * @param g đối tượng Graphics để vẽ
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();

        super.paintComponent(g);
    }

    /**
     * Ghi đè phương thức paintBorder để vẽ viền bo góc cho JTextField.
     *
     * @param g đối tượng Graphics để vẽ
     */
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(200, 200, 200)); // Màu viền xám nhẹ
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }
}
