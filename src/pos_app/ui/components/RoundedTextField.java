package pos_app.ui.components;

import java.awt.*;
import javax.swing.*;

public class RoundedTextField extends JTextField {

    private final int radius;

    public RoundedTextField(int columns, int radius) {
        super(columns);
        this.radius = radius;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setBackground(Color.WHITE);
    }

    public RoundedTextField(String text, int radius) {
        super(text);
        this.radius = radius;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        // Vẽ nền bo tròn
        g2.setColor(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();

        // Sau khi vẽ nền, vẽ nội dung
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(200, 200, 200)); // Màu viền xám nhẹ
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }
}
