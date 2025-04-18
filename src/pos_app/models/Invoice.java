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

public class Invoice {

    private int id;
    private int customerId;
    private int employeeId;
    private BigDecimal total;            // có thể tính động
    private LocalDateTime createdAt;

    private List<InvoiceItem> items = new ArrayList<>();

    /* ---------- Constructors ---------- */
    public Invoice() {
    }

    public Invoice(int id, int customerId, int employeeId,
            BigDecimal total, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.total = total;
        this.createdAt = createdAt;
    }

    /* ---------- Getters / Setters ---------- */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int c) {
        this.customerId = c;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int e) {
        this.employeeId = e;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    /* ---------- Convenience ---------- */
    /**
     * Tính lại total từ items
     */
    public void recalcTotal() {
        this.total = items.stream()
                .map(InvoiceItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Thêm dòng, tự cộng total
     */
    public void addItem(InvoiceItem item) {
        items.add(item);
        this.total = this.total.add(item.getLineTotal());
    }
}
