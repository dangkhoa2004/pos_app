/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.models;

import java.time.LocalDateTime;

/**
 * Một phiếu xuất kho trong hệ thống POS.
 *
 * Mỗi lần xuất kho ghi nhận sản phẩm được xuất, số lượng, ghi chú và thời điểm
 * xuất. Dùng để giảm số lượng tồn kho tương ứng và theo dõi lịch sử xuất hàng.
 *
 * @author 04dkh
 */
public class StockOut {

    /**
     * Mã định danh của lần xuất kho
     */
    private int id;

    /**
     * Mã sản phẩm được xuất khỏi kho
     */
    private int productId;

    /**
     * Số lượng sản phẩm được xuất
     */
    private int quantity;

    /**
     * Ghi chú thêm về lần xuất kho (nếu có)
     */
    private String note;

    /**
     * Thời gian xuất kho
     */
    private LocalDateTime createdAt;

    /**
     * Khởi tạo một phiếu xuất kho trống.
     */
    public StockOut() {
    }

    /**
     * Khởi tạo một phiếu xuất kho với đầy đủ thông tin.
     *
     * @param id mã xuất kho
     * @param productId mã sản phẩm
     * @param quantity số lượng xuất
     * @param note ghi chú
     * @param createdAt thời gian xuất
     */
    public StockOut(int id, int productId, int quantity, String note, LocalDateTime createdAt) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.note = note;
        this.createdAt = createdAt;
    }

    /**
     * @return mã xuất kho
     */
    public int getId() {
        return id;
    }

    /**
     * @param id mã xuất kho
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
     * @param p mã sản phẩm
     */
    public void setProductId(int p) {
        this.productId = p;
    }

    /**
     * @return số lượng xuất
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param q số lượng xuất
     */
    public void setQuantity(int q) {
        this.quantity = q;
    }

    /**
     * @return ghi chú xuất kho
     */
    public String getNote() {
        return note;
    }

    /**
     * @param n ghi chú xuất kho
     */
    public void setNote(String n) {
        this.note = n;
    }

    /**
     * @return thời gian xuất kho
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @param c thời gian xuất kho
     */
    public void setCreatedAt(LocalDateTime c) {
        this.createdAt = c;
    }
}
