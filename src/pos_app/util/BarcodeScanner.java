package pos_app.util;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class BarcodeScanner {

    public interface BarcodeCallback {
        void onBarcodeScanned(String barcode);
    }

    public static void scan(BarcodeCallback callback) {
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(new java.awt.Dimension(640, 480));

        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setPreferredSize(new java.awt.Dimension(640, 480));

        JFrame window = new JFrame("Quét barcode");
        window.add(panel);
        window.pack();
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        final int debounceTime = 2000; // thời gian ngăn trùng mã (ms)
        final long[] lastScannedTime = {0};
        final String[] lastScannedCode = {""};

        new Thread(() -> {
            while (webcam.isOpen()) {
                BufferedImage image = webcam.getImage();
                if (image == null) continue;

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    Result result = new MultiFormatReader().decode(bitmap);
                    String barcode = result.getText();

                    long now = System.currentTimeMillis();
                    if (barcode != null &&
                            (!barcode.equals(lastScannedCode[0]) || now - lastScannedTime[0] >= debounceTime)) {

                        lastScannedCode[0] = barcode;
                        lastScannedTime[0] = now;

                        SwingUtilities.invokeLater(() -> callback.onBarcodeScanned(barcode));
                    }

                    Thread.sleep(200); // không nên delay quá dài
                } catch (NotFoundException e) {
                    // Không phát hiện mã
                } catch (InterruptedException ex) {
                    break;
                }
            }
        }).start();

        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                webcam.close();
            }
        });
    }
}
