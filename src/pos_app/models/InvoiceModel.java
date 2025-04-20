/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.models;

/**
 *
 * @author 04dkh
 */
import java.util.Date;
import java.util.List;

public class InvoiceModel {

    private int customerId;
    private int employeeId;
    private int tableId;
    private int discountId;
    private double total;
    private Date createdAt;
    private List<Product> items;

    public InvoiceModel() {
    }

    public InvoiceModel(int customerId, int employeeId, int tableId, int discountId, double total, Date createdAt, List<Product> items) {
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.tableId = tableId;
        this.discountId = discountId;
        this.total = total;
        this.createdAt = createdAt;
        this.items = items;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<Product> getItems() {
        return items;
    }

    public void setItems(List<Product> items) {
        this.items = items;
    }

}
