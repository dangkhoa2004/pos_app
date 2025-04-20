/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pos_app.ui.components;

import java.awt.*;
import javax.swing.JPanel;

/**
 * RoundedPanel là một JPanel tùy biến có góc bo tròn và viền mềm mại.
 *
 * Thích hợp dùng làm container cho các thành phần giao diện trong hệ thống POS.
 * Sử dụng Graphics2D để vẽ nền và viền bo góc.
 *
 * @author 04dkh
 */
public class RoundedPanel extends JPanel {

    /**
     * Độ bo tròn góc của panel (tính theo pixel)
     */
    private int radius;

    /**
     * Tạo một RoundedPanel với bán kính bo tròn chỉ định.
     *
     * @param radius độ bo góc của panel (px)
     */
    public RoundedPanel(int radius) {
        this.radius = radius;
        setOpaque(false); // Cho phép vẽ thủ công để hiển thị bo góc
    }

    /**
     * Ghi đè phương thức paintComponent để vẽ panel bo góc.
     *
     * @param g đối tượng Graphics để vẽ
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(radius, radius);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Nền
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcs.width, arcs.height);

        // Viền bo ngoài
        g2.setColor(new Color(200, 200, 200)); // màu viền
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcs.width, arcs.height);
    }
}
