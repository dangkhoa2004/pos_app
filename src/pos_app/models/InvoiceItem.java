/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.models;

import java.math.BigDecimal;

/**
 * Một dòng sản phẩm (mục chi tiết) trong hóa đơn.
 *
 * Mỗi dòng thể hiện một sản phẩm cụ thể được mua với số lượng và đơn giá cụ
 * thể. Bao gồm các thông tin như ID dòng hàng, ID hóa đơn, ID sản phẩm, số
 * lượng, đơn giá và có thể tính được tổng tiền cho dòng hàng đó.
 *
 * @author 04dkh
 */
public class InvoiceItem {

    /**
     * Mã định danh của dòng hàng trong hóa đơn
     */
    private int id;

    /**
     * Mã hóa đơn mà dòng hàng này thuộc về
     */
    private int invoiceId;

    /**
     * Mã sản phẩm của dòng hàng
     */
    private int productId;

    /**
     * Số lượng sản phẩm trong dòng hàng
     */
    private int quantity;

    /**
     * Đơn giá của sản phẩm tại thời điểm mua
     */
    private BigDecimal unitPrice;

    /**
     * Khởi tạo dòng hàng trống.
     */
    public InvoiceItem() {
    }

    /**
     * Khởi tạo dòng hàng với đầy đủ thông tin.
     *
     * @param id mã dòng hàng
     * @param invoiceId mã hóa đơn
     * @param productId mã sản phẩm
     * @param quantity số lượng sản phẩm
     * @param unitPrice đơn giá sản phẩm
     */
    public InvoiceItem(int id, int invoiceId, int productId, int quantity, BigDecimal unitPrice) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    /**
     * @return mã dòng hàng
     */
    public int getId() {
        return id;
    }

    /**
     * @param id mã dòng hàng
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return mã hóa đơn
     */
    public int getInvoiceId() {
        return invoiceId;
    }

    /**
     * @param invoiceId mã hóa đơn
     */
    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
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
     * @return số lượng sản phẩm
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity số lượng sản phẩm
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return đơn giá sản phẩm
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice đơn giá sản phẩm
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * Tính tổng tiền của dòng hàng = đơn giá * số lượng.
     *
     * @return tổng tiền dòng hàng
     */
    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
