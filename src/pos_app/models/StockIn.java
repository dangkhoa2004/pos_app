package pos_app.models;

import java.time.LocalDateTime;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Một phiếu nhập kho trong hệ thống POS.
 *
 * Mỗi lần nhập kho sẽ ghi nhận sản phẩm được nhập, số lượng, ghi chú và thời
 * điểm nhập. Thường được dùng để cập nhật tồn kho sản phẩm.
 *
 * @author 04dkh
 */
public class StockIn {

    /**
     * Mã định danh của lần nhập kho
     */
    private int id;

    /**
     * Mã sản phẩm được nhập vào kho
     */
    private int productId;

    /**
     * Số lượng sản phẩm được nhập
     */
    private int quantity;

    /**
     * Ghi chú thêm về lần nhập kho (nếu có)
     */
    private String note;

    /**
     * Thời gian nhập kho
     */
    private LocalDateTime createdAt;

    /**
     * Khởi tạo một phiếu nhập kho trống.
     */
    public StockIn() {
    }

    /**
     * Khởi tạo một phiếu nhập kho với đầy đủ thông tin.
     *
     * @param id mã nhập kho
     * @param productId mã sản phẩm
     * @param quantity số lượng nhập
     * @param note ghi chú
     * @param createdAt thời gian nhập
     */
    public StockIn(int id, int productId, int quantity, String note, LocalDateTime createdAt) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.note = note;
        this.createdAt = createdAt;
    }

    /**
     * @return mã nhập kho
     */
    public int getId() {
        return id;
    }

    /**
     * @param id mã nhập kho
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return mã sản phẩm
     */
    public int getProductId() {
        return productId;
    }

    /**
     * @param productId mã sản phẩm
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * @return số lượng nhập
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity số lượng nhập
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return ghi chú nhập kho
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note ghi chú nhập kho
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return thời gian nhập kho
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt thời gian nhập kho
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
