/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * QRCodeUtil là lớp tiện ích để tạo mã QR từ chuỗi văn bản.
 *
 * Sử dụng thư viện ZXing để encode nội dung thành hình ảnh mã QR. Có thể trả về
 * đối tượng {@link BufferedImage} hoặc lưu thẳng thành file PNG.
 *
 * Ví dụ sử dụng:
 * <pre>
 *     BufferedImage qr = QRCodeUtil.generateQRCode("https://pos-app.vn", 200, 200);
 *     QRCodeUtil.saveQRCodeToFile("123456789", 250, 250, new File("qr-code.png"));
 * </pre>
 *
 * Ứng dụng: in mã QR cho đơn hàng, barcode sản phẩm, URL dẫn đến trang thanh
 * toán...
 *
 * @author 04dkh
 */
public class QRCodeUtil {

    /**
     * Tạo mã QR dưới dạng BufferedImage từ nội dung chuỗi.
     *
     * @param content chuỗi văn bản cần encode (ví dụ: URL, mã khách hàng)
     * @param width chiều rộng ảnh QR
     * @param height chiều cao ảnh QR
     * @return đối tượng BufferedImage chứa mã QR
     * @throws WriterException nếu có lỗi trong quá trình encode QR
     */
    public static BufferedImage generateQRCode(String content, int width, int height) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    /**
     * Tạo mã QR và lưu dưới dạng file ảnh PNG.
     *
     * @param content nội dung cần mã hóa
     * @param width chiều rộng ảnh
     * @param height chiều cao ảnh
     * @param file đối tượng {@link File} để lưu ảnh PNG
     * @throws IOException nếu có lỗi khi ghi file
     * @throws WriterException nếu có lỗi khi tạo mã QR
     */
    public static void saveQRCodeToFile(String content, int width, int height, File file) throws IOException, WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);
        MatrixToImageWriter.writeToPath(matrix, "PNG", file.toPath());
    }
}
