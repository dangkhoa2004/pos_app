/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.models;

import java.util.Random;

/**
 * Một sản phẩm trong hệ thống POS.
 *
 * Mỗi sản phẩm bao gồm mã định danh, mã barcode, tên, giá bán, số lượng tồn
 * kho, và đường dẫn đến hình ảnh. Nếu không có mã barcode được cung cấp, hệ
 * thống sẽ tự động sinh mã barcode ngẫu nhiên.
 *
 * Lớp này hỗ trợ quản lý thông tin sản phẩm và tích hợp với hóa đơn, tồn kho.
 *
 * @author 04dkh
 */
public class Product {

    /**
     * Mã định danh duy nhất của sản phẩm
     */
    private int id;

    /**
     * Mã vạch sản phẩm (barcode)
     */
    private String barcode;

    /**
     * Tên sản phẩm
     */
    private String name;

    /**
     * Giá bán của sản phẩm
     */
    private double price;

    /**
     * Số lượng tồn kho của sản phẩm
     */
    private int quantity;

    /**
     * Đường dẫn đến hình ảnh sản phẩm
     */
    private String imagePath;

    /**
     * Khởi tạo một sản phẩm mới và tự động sinh mã barcode.
     */
    public Product() {
        this.barcode = generateRandomBarcode();
    }

    /**
     * Khởi tạo sản phẩm với đầy đủ thông tin. Nếu barcode là null hoặc rỗng, hệ
     * thống sẽ tự sinh mã mới.
     *
     * @param id mã sản phẩm
     * @param barcode mã barcode
     * @param name tên sản phẩm
     * @param price giá bán
     * @param quantity số lượng tồn
     * @param imagePath đường dẫn hình ảnh
     */
    public Product(int id, String barcode, String name, double price, int quantity, String imagePath) {
        this.id = id;
        this.barcode = (barcode != null && !barcode.isEmpty()) ? barcode : generateRandomBarcode();
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }

    /**
     * Khởi tạo sản phẩm với thông tin cơ bản (không có barcode và hình ảnh).
     *
     * @param id mã sản phẩm
     * @param name tên sản phẩm
     * @param price giá bán
     * @param quantity số lượng tồn
     */
    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Sinh mã barcode ngẫu nhiên dạng BCXXXXXXXXX (9 chữ số).
     *
     * @return mã barcode ngẫu nhiên
     */
    private String generateRandomBarcode() {
        Random rand = new Random();
        int randomNum = 100000000 + rand.nextInt(900000000);
        return "BC" + randomNum;
    }

    /**
     * @return mã sản phẩm
     */
    public int getId() {
        return id;
    }

    /**
     * @param id mã sản phẩm
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return mã barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * @param barcode mã barcode
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * @return tên sản phẩm
     */
    public String getName() {
        return name;
    }

    /**
     * @param name tên sản phẩm
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return giá sản phẩm
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price giá sản phẩm
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return số lượng tồn
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity số lượng tồn
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return đường dẫn hình ảnh
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath đường dẫn hình ảnh
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
