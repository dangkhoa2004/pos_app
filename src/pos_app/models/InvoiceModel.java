/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.models;

import java.util.Date;
import java.util.List;

/**
 * Lớp mô hình dữ liệu đại diện cho hóa đơn sắp được tạo trong hệ thống POS.
 *
 * Thường được dùng như một DTO (Data Transfer Object) để truyền dữ liệu hóa đơn
 * từ giao diện người dùng tới backend khi tạo hóa đơn mới.
 *
 * Bao gồm thông tin khách hàng, nhân viên, bàn, khuyến mãi, tổng tiền, thời
 * gian tạo và danh sách sản phẩm.
 *
 * @author 04dkh
 */
public class InvoiceModel {

    /**
     * Mã khách hàng
     */
    private int customerId;

    /**
     * Mã nhân viên thực hiện giao dịch
     */
    private int employeeId;

    /**
     * Mã bàn (nếu áp dụng cho mô hình nhà hàng/cafe)
     */
    private int tableId;

    /**
     * Mã khuyến mãi hoặc giảm giá áp dụng
     */
    private int discountId;

    /**
     * Tổng tiền hóa đơn (đã áp dụng giảm giá nếu có)
     */
    private double total;

    /**
     * Thời gian tạo hóa đơn
     */
    private Date createdAt;

    /**
     * Danh sách sản phẩm trong hóa đơn
     */
    private List<Product> items;

    /**
     * Khởi tạo đối tượng InvoiceModel rỗng.
     */
    public InvoiceModel() {
    }

    /**
     * Khởi tạo InvoiceModel với đầy đủ thông tin.
     *
     * @param customerId mã khách hàng
     * @param employeeId mã nhân viên
     * @param tableId mã bàn
     * @param discountId mã giảm giá
     * @param total tổng tiền
     * @param createdAt thời gian tạo
     * @param items danh sách sản phẩm
     */
    public InvoiceModel(int customerId, int employeeId, int tableId, int discountId, double total, Date createdAt, List<Product> items) {
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.tableId = tableId;
        this.discountId = discountId;
        this.total = total;
        this.createdAt = createdAt;
        this.items = items;
    }

    /**
     * @return mã khách hàng
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId mã khách hàng
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @return mã nhân viên
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId mã nhân viên
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return mã bàn
     */
    public int getTableId() {
        return tableId;
    }

    /**
     * @param tableId mã bàn
     */
    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    /**
     * @return mã giảm giá
     */
    public int getDiscountId() {
        return discountId;
    }

    /**
     * @param discountId mã giảm giá
     */
    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    /**
     * @return tổng tiền hóa đơn
     */
    public double getTotal() {
        return total;
    }

    /**
     * @param total tổng tiền hóa đơn
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * @return thời gian tạo hóa đơn
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt thời gian tạo hóa đơn
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return danh sách sản phẩm
     */
    public List<Product> getItems() {
        return items;
    }

    /**
     * @param items danh sách sản phẩm
     */
    public void setItems(List<Product> items) {
        this.items = items;
    }
}
