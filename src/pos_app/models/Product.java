/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.models;

import java.util.Random;

/**
 *
 * @author 04dkh
 */
public class Product {

    private int id;
    private String barcode;
    private String name;
    private double price;
    private int quantity;
    private String imagePath;

    public Product() {
        this.barcode = generateRandomBarcode();
    }

    public Product(int id, String barcode, String name, double price, int quantity, String imagePath) {
        this.id = id;
        this.barcode = (barcode != null && !barcode.isEmpty()) ? barcode : generateRandomBarcode();
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }

    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    private String generateRandomBarcode() {
        Random rand = new Random();
        int randomNum = 100000000 + rand.nextInt(900000000);
        return "BC" + randomNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
