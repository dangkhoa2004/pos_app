/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.util;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * IconUtil là lớp tiện ích để tải icon PNG và SVG từ thư mục resources.
 *
 * Hỗ trợ cả định dạng ảnh bitmap (PNG) và ảnh vector (SVG) khi sử dụng FlatLaf.
 *
 * Các icon nên được đặt tại: {@code src/main/resources/pos_app/icons/}
 *
 * Ví dụ:
 * <pre>
 *     JButton btn = new JButton(IconUtil.loadPng("edit.png", 24));
 *     JLabel label = new JLabel(IconUtil.loadSvg("search.svg", 20));
 * </pre>
 *
 * @author 04dkh
 */
public class IconUtil {

    /**
     * Tải icon PNG từ thư mục {@code /pos_app/icons/} trong resources và scale
     * theo kích thước chỉ định.
     *
     * @param fileName tên file PNG (ví dụ: "add.png")
     * @param size kích thước chiều rộng/chiều cao mong muốn
     * @return ImageIcon đã được scale, hoặc null nếu không tìm thấy
     */
    public static ImageIcon loadPng(String fileName, int size) {
        URL url = IconUtil.class.getResource("/pos_app/icons/" + fileName);
        if (url == null) {
            System.err.println("Không tìm thấy icon PNG: " + fileName);
            return null;
        }
        Image img = new ImageIcon(url).getImage()
                .getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    /**
     * Tải icon SVG từ thư mục {@code /pos_app/icons/} sử dụng FlatLaf.
     *
     * @param fileName tên file SVG (ví dụ: "save.svg")
     * @param size kích thước chiều rộng/chiều cao mong muốn
     * @return Icon đã được scale
     */
    public static Icon loadSvg(String fileName, int size) {
        return new FlatSVGIcon("pos_app/icons/" + fileName, size, size);
    }
}
