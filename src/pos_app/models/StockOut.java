/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.models;

import java.time.LocalDateTime;

/**
 *
 * @author 04dkh
 */
public class StockOut {

    private int id;
    private int productId;
    private int quantity;
    private String note;
    private LocalDateTime createdAt;

    public StockOut() {
    }

    public StockOut(int id, int productId, int quantity,
            String note, LocalDateTime createdAt) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.note = note;
        this.createdAt = createdAt;
    }

    /* --- getters / setters --- (giống StockIn) */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int p) {
        this.productId = p;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int q) {
        this.quantity = q;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String n) {
        this.note = n;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime c) {
        this.createdAt = c;
    }
}
