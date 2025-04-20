/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.models;

/**
 *
 * @author 04dkh
 */
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Một hóa đơn trong hệ thống POS. Chứa thông tin về khách
 * hàng, nhân viên, tổng tiền, thời gian tạo và danh sách các dòng hàng.
 *
 * @author 04dkh
 */
public class Invoice {

    private int id;
    private int customerId;
    private int employeeId;
    private BigDecimal total; // Tổng tiền của hóa đơn, có thể được tính động
    private LocalDateTime createdAt; // Thời gian tạo hóa đơn

    private List<InvoiceItem> items = new ArrayList<>(); // Danh sách các dòng hàng trong hóa đơn

    /**
     * Khởi tạo một hóa đơn rỗng.
     */
    public Invoice() {
    }

    /**
     * Khởi tạo hóa đơn với các thông tin chi tiết.
     *
     * @param id ID của hóa đơn
     * @param customerId ID của khách hàng
     * @param employeeId ID của nhân viên tạo hóa đơn
     * @param total Tổng tiền ban đầu
     * @param createdAt Ngày tạo hóa đơn
     */
    public Invoice(int id, int customerId, int employeeId,
            BigDecimal total, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.total = total;
        this.createdAt = createdAt;
    }

    /**
     * Trả về ID của hóa đơn.
     *
     * @return ID hóa đơn
     */
    public int getId() {
        return id;
    }

    /**
     * Thiết lập ID cho hóa đơn.
     *
     * @param id ID hóa đơn
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Trả về ID khách hàng.
     *
     * @return ID khách hàng
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Thiết lập ID khách hàng.
     *
     * @param c ID khách hàng
     */
    public void setCustomerId(int c) {
        this.customerId = c;
    }

    /**
     * Trả về ID nhân viên.
     *
     * @return ID nhân viên
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * Thiết lập ID nhân viên.
     *
     * @param e ID nhân viên
     */
    public void setEmployeeId(int e) {
        this.employeeId = e;
    }

    /**
     * Trả về tổng tiền hóa đơn.
     *
     * @return Tổng tiền
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * Thiết lập tổng tiền hóa đơn.
     *
     * @param total Tổng tiền
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * Trả về ngày tạo hóa đơn.
     *
     * @return Ngày tạo
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Thiết lập ngày tạo hóa đơn.
     *
     * @param created Ngày tạo
     */
    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

    /**
     * Trả về danh sách các dòng hàng trong hóa đơn.
     *
     * @return Danh sách dòng hàng
     */
    public List<InvoiceItem> getItems() {
        return items;
    }

    /**
     * Thiết lập danh sách dòng hàng.
     *
     * @param items Danh sách dòng hàng
     */
    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    /**
     * Tính lại tổng tiền của hóa đơn dựa trên danh sách dòng hàng.
     */
    public void recalcTotal() {
        this.total = items.stream()
                .map(InvoiceItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Thêm một dòng hàng vào hóa đơn và cập nhật tổng tiền.
     *
     * @param item Dòng hàng cần thêm
     */
    public void addItem(InvoiceItem item) {
        items.add(item);
        this.total = this.total.add(item.getLineTotal());
    }
}
