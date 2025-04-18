package pos_app.util;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class IconUtil {

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

    public static Icon loadSvg(String fileName, int size) {
        return new FlatSVGIcon("pos_app/icons/" + fileName, size, size);
    }
}
