package pos_app.ui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.JTableHeader;

/** Shared visual system for the desktop POS application. */
public final class Theme {
    public static final Color BRAND = new Color(37, 99, 235);
    public static final Color BRAND_DARK = new Color(29, 78, 216);
    public static final Color NAV = new Color(17, 24, 39);
    public static final Color CANVAS = new Color(246, 248, 251);
    public static final Color SURFACE = Color.WHITE;
    public static final Color TEXT = new Color(31, 41, 55);
    public static final Color MUTED = new Color(107, 114, 128);
    public static final Color BORDER = new Color(229, 233, 240);
    public static final Color DANGER = new Color(220, 38, 38);
    public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font TITLE = new Font("Segoe UI", Font.BOLD, 22);

    private Theme() { }

    public static void install() {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception ignored) { }
        UIManager.put("defaultFont", BODY);
        UIManager.put("Panel.background", CANVAS);
        UIManager.put("Label.foreground", TEXT);
        UIManager.put("Button.arc", 10);
        UIManager.put("Component.arc", 10);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("Component.focusColor", BRAND);
        UIManager.put("Component.borderColor", BORDER);
        UIManager.put("Button.background", BRAND);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", BODY_BOLD);
        UIManager.put("TextField.background", SURFACE);
        UIManager.put("TextField.foreground", TEXT);
        UIManager.put("TextField.margin", new java.awt.Insets(8, 10, 8, 10));
        UIManager.put("PasswordField.margin", new java.awt.Insets(8, 10, 8, 10));
        UIManager.put("Table.background", SURFACE);
        UIManager.put("Table.foreground", TEXT);
        UIManager.put("Table.selectionBackground", new Color(219, 234, 254));
        UIManager.put("Table.selectionForeground", TEXT);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("TableHeader.background", new Color(249, 250, 251));
        UIManager.put("TableHeader.foreground", MUTED);
        UIManager.put("TabbedPane.selectedBackground", SURFACE);
        UIManager.put("TabbedPane.underlineColor", BRAND);
        UIManager.put("ScrollBar.width", 10);
        UIManager.put("ScrollBar.track", CANVAS);
        UIManager.put("ScrollBar.thumb", new Color(203, 213, 225));
        UIManager.put("ScrollBar.thumbArc", 8);
    }

    /** Applies consistent table, input, and button metrics to legacy screens. */
    public static void polish(Component component) {
        if (component instanceof JTable table) {
            table.setRowHeight(Math.max(table.getRowHeight(), 38));
            table.setShowVerticalLines(false);
            table.setShowHorizontalLines(true);
            table.setGridColor(BORDER);
            table.setIntercellSpacing(new java.awt.Dimension(0, 1));
            table.setFillsViewportHeight(true);
            JTableHeader header = table.getTableHeader();
            if (header != null) {
                header.setPreferredSize(new java.awt.Dimension(header.getPreferredSize().width, 42));
                header.setFont(BODY_BOLD);
                header.setReorderingAllowed(false);
            }
        } else if (component instanceof JTextField field) {
            field.setFont(BODY);
            field.setPreferredSize(new java.awt.Dimension(Math.max(160, field.getPreferredSize().width), 38));
        } else if (component instanceof JComboBox<?> combo) {
            combo.setFont(BODY);
            combo.setPreferredSize(new java.awt.Dimension(Math.max(140, combo.getPreferredSize().width), 38));
        } else if (component instanceof JButton button) {
            button.setFont(BODY_BOLD);
            button.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
            button.setFocusPainted(false);
        }
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) polish(child);
        }
    }
}
