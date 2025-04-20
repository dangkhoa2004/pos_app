package pos_app.util;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * BarcodeScanner là tiện ích giúp quét mã vạch từ webcam.
 *
 * Sử dụng thư viện: - webcam-capture (sarxos) - zxing (Google ZXing for Java)
 *
 * Khi quét thành công, callback sẽ được gọi với mã barcode đã nhận.
 *
 * Ứng dụng trong hệ thống POS để thêm sản phẩm vào giỏ hoặc tra cứu nhanh.
 *
 * @author 04dkh
 */
public class BarcodeScanner {

    /**
     * Interface callback để xử lý kết quả sau khi quét mã.
     */
    public interface BarcodeCallback {

        /**
         * Được gọi khi phát hiện barcode mới.
         *
         * @param barcode chuỗi mã vạch đã quét được
         */
        void onBarcodeScanned(String barcode);
    }

    /**
     * Bắt đầu quá trình quét barcode bằng webcam mặc định.
     *
     * @param callback hàm callback được gọi khi quét được mã
     */
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

        final int debounceTime = 2000; // thời gian chống lặp mã (ms)
        final long[] lastScannedTime = {0};
        final String[] lastScannedCode = {""};

        // Thread liên tục kiểm tra hình ảnh từ webcam để phát hiện barcode
        new Thread(() -> {
            while (webcam.isOpen()) {
                BufferedImage image = webcam.getImage();
                if (image == null) {
                    continue;
                }

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    Result result = new MultiFormatReader().decode(bitmap);
                    String barcode = result.getText();

                    long now = System.currentTimeMillis();
                    if (barcode != null
                            && (!barcode.equals(lastScannedCode[0]) || now - lastScannedTime[0] >= debounceTime)) {

                        lastScannedCode[0] = barcode;
                        lastScannedTime[0] = now;

                        // Gọi callback trong luồng giao diện
                        SwingUtilities.invokeLater(() -> callback.onBarcodeScanned(barcode));
                    }

                    Thread.sleep(200); // tránh quét quá nhanh
                } catch (NotFoundException e) {
                    // Không phát hiện mã trong khung hình hiện tại
                } catch (InterruptedException ex) {
                    break;
                }
            }
        }).start();

        // Đóng webcam khi đóng cửa sổ
        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                webcam.close();
            }
        });
    }
}
