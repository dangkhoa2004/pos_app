/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.ui.components;

import java.awt.*;
import javax.swing.*;

/**
 * RoundedButton là một phiên bản tùy biến của JButton, có bo góc và giao diện
 * hiện đại.
 *
 * Cung cấp giao diện nút mềm mại (rounded) bằng cách ghi đè các phương thức vẽ.
 * Phù hợp dùng trong giao diện người dùng Swing đẹp mắt.
 *
 * @author 04dkh
 */
public class RoundedButton extends JButton {

    /**
     * Độ bo tròn của nút (pixel)
     */
    private int radius;

    /**
     * Tạo một nút có góc bo tròn với nội dung văn bản.
     *
     * @param text nội dung hiển thị trên nút
     * @param radius độ bo góc của nút (px)
     */
    public RoundedButton(String text, int radius) {
        super(text);
        this.radius = radius;
        setOpaque(false);
        setForeground(Color.BLACK);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setContentAreaFilled(false);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    /**
     * Vẽ nền nút với bo góc bằng Graphics2D.
     *
     * @param g đối tượng Graphics để vẽ
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g);
        g2.dispose();
    }

    /**
     * Vẽ đường viền bo tròn của nút.
     *
     * @param g đối tượng Graphics để vẽ
     */
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 30));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }
}
