package pos_app.ui.panel;

import pos_app.dao.AuditLogDAO;
import pos_app.models.AuditLog;
import pos_app.util.DatabaseConnection;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Lớp TerminalPanel cung cấp giao diện dòng lệnh đơn giản trong hệ thống POS để
 * theo dõi log hành vi người dùng. Hiển thị các bản ghi log từ bảng audit_log
 * với định dạng có màu sắc theo hành động (INSERT/UPDATE/DELETE), cho phép xuất
 * toàn bộ log ra file .log.
 */
public class TerminalPanel extends JPanel {

    private final JTextPane consoleArea;
    private final StyledDocument doc;

    /**
     * Khởi tạo panel Terminal hiển thị log hành vi từ hệ thống. Thiết lập giao
     * diện hiển thị log có màu sắc, thêm nút xuất file .log và tự động tải dữ
     * liệu log.
     */
    public TerminalPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        consoleArea = new JTextPane();
        consoleArea.setEditable(false);
        consoleArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        consoleArea.setBackground(new Color(30, 30, 30));
        consoleArea.setForeground(Color.WHITE);

        doc = consoleArea.getStyledDocument();
        setupStyles(doc);

        JScrollPane scrollPane = new JScrollPane(consoleArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JButton exportLogButton = new JButton("Export to .log");
        exportLogButton.addActionListener(e -> exportLogsToLogFile());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(30, 30, 30));
        bottomPanel.add(exportLogButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadAndShowLogs();
    }

    /**
     * Tải tất cả log từ cơ sở dữ liệu và hiển thị lên bảng điều khiển.
     */
    private void loadAndShowLogs() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            AuditLogDAO dao = new AuditLogDAO(conn);
            List<AuditLog> logs = dao.getAllAuditLogs();
            showLogs(logs);
        } catch (Exception e) {
            log("Không thể tải audit log: " + e.getMessage(), "info");
        }
    }

    /**
     * Thiết lập các style màu sắc cho từng loại hành động log (INSERT, UPDATE,
     * DELETE, INFO).
     *
     * @param doc StyledDocument cần cấu hình.
     */
    private void setupStyles(StyledDocument doc) {
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setForeground(regular, Color.WHITE);

        Style insert = doc.addStyle("INSERT", regular);
        StyleConstants.setForeground(insert, new Color(0, 255, 128));

        Style update = doc.addStyle("UPDATE", regular);
        StyleConstants.setForeground(update, Color.YELLOW);

        Style delete = doc.addStyle("DELETE", regular);
        StyleConstants.setForeground(delete, Color.PINK);

        Style info = doc.addStyle("info", regular);
        StyleConstants.setForeground(info, Color.CYAN);
    }

    /**
     * In một dòng log ra màn hình với style tương ứng theo loại hành động.
     *
     * @param message Nội dung cần hiển thị
     * @param actionType Loại hành động (INSERT, UPDATE, DELETE, info...)
     */
    public void log(String message, String actionType) {
        try {
            Style style = doc.getStyle(actionType != null ? actionType : "info");
            doc.insertString(doc.getLength(), message + "\n", style);
            consoleArea.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Xóa toàn bộ nội dung trong terminal.
     */
    public void clear() {
        consoleArea.setText("");
    }

    /**
     * Hiển thị danh sách các bản ghi log ra terminal.
     *
     * @param logs Danh sách các log lấy từ database.
     */
    public void showLogs(List<AuditLog> logs) {
        clear();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (AuditLog log : logs) {
            String timestamp = log.getCreatedAt() != null ? log.getCreatedAt().format(dtf) : "N/A";
            insertText("[" + timestamp + "] ", new Color(140, 140, 140));
            insertText("[MAIN/INFO]: ", new Color(100, 180, 255));
            insertText(" | ", Color.GRAY);
            insertBoldText("Table: ", new Color(200, 200, 255));
            insertText(log.getTableName() + "\n", new Color(0, 255, 255));

            insertBoldText(" | Old: ", new Color(255, 160, 130));
            insertText((log.getOldData() != null ? formatJsonAsKeyValue(log.getOldData()) : "null") + "\n", Color.WHITE);

            insertBoldText(" | New: ", new Color(150, 255, 150));
            insertText((log.getNewData() != null ? formatJsonAsKeyValue(log.getNewData()) : "null") + "\n\n", Color.WHITE);
        }
    }

    /**
     * Thêm đoạn văn bản có màu vào terminal.
     *
     * @param text Văn bản cần hiển thị
     * @param color Màu chữ
     */
    private void insertText(String text, Color color) {
        Style style = doc.addStyle("custom_" + color.getRGB(), null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), text, style);
            consoleArea.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Thêm đoạn văn bản **in đậm** với màu vào terminal.
     *
     * @param text Văn bản cần in
     * @param color Màu chữ
     */
    private void insertBoldText(String text, Color color) {
        Style style = doc.addStyle("bold_" + color.getRGB(), null);
        StyleConstants.setForeground(style, color);
        StyleConstants.setBold(style, true);
        try {
            doc.insertString(doc.getLength(), text, style);
            consoleArea.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy màu tương ứng với loại hành động (không còn dùng nếu đã dùng
     * StyledDocument).
     *
     * @param actionType Loại hành động
     * @return Màu phù hợp
     */
    private Color getActionColor(String actionType) {
        switch (actionType) {
            case "INSERT":
                return new Color(0, 255, 128);
            case "UPDATE":
                return new Color(255, 255, 0);
            case "DELETE":
                return new Color(255, 100, 100);
            default:
                return Color.CYAN;
        }
    }

    /**
     * Chuyển chuỗi JSON dạng key-value sang chuỗi dễ đọc hơn để hiển thị trong
     * terminal.
     *
     * @param json Chuỗi JSON đầu vào
     * @return Chuỗi đã format
     */
    private String formatJsonAsKeyValue(String json) {
        if (json == null || json.trim().isEmpty()) {
            return "null";
        }

        try {
            com.google.gson.JsonObject obj = com.google.gson.JsonParser.parseString(json).getAsJsonObject();
            StringBuilder sb = new StringBuilder("[");
            for (String key : obj.keySet()) {
                sb.append(String.format("%s=%s, ", key, obj.get(key).getAsString().replace("\"", "")));
            }
            if (sb.length() >= 2) {
                sb.setLength(sb.length() - 2);
            }
            sb.append("]");
            return sb.toString();
        } catch (Exception e) {
            return json;
        }
    }

    /**
     * Xuất toàn bộ bản ghi log ra file định dạng `.log`. Người dùng sẽ được
     * chọn vị trí lưu file.
     */
    private void exportLogsToLogFile() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            AuditLogDAO dao = new AuditLogDAO(conn);
            List<AuditLog> logs = dao.getAllAuditLogs();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file LOG");
            fileChooser.setSelectedFile(new File("audit_terminal.log"));

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                FileWriter writer = new FileWriter(fileToSave);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                for (AuditLog log : logs) {
                    String timestamp = log.getCreatedAt() != null ? log.getCreatedAt().format(dtf) : "N/A";
                    StringBuilder line = new StringBuilder();

                    line.append("[").append(timestamp).append("] ")
                            .append("[MAIN/INFO]: ")
                            .append(log.getActionType()).append(" | ")
                            .append("Table: ").append(log.getTableName()).append("\n");

                    line.append(" | Old: ")
                            .append(log.getOldData() != null ? log.getOldData() : "null").append("\n");
                    line.append(" | New: ")
                            .append(log.getNewData() != null ? log.getNewData() : "null").append("\n\n");

                    writer.write(line.toString());
                }

                writer.flush();
                writer.close();

                JOptionPane.showMessageDialog(this, "Đã xuất log ra file:\n" + fileToSave.getAbsolutePath(),
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất file .log: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
