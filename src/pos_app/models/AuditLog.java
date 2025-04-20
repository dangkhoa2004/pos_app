/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.models;

import java.time.LocalDateTime;

/**
 * Một bản ghi nhật ký thao tác người dùng trong hệ
 * thống. Ghi lại các hành động như thêm, sửa, xóa, đăng nhập, đăng xuất kèm
 * theo thông tin thay đổi.
 *
 * Các hành động được theo dõi bao gồm: - INSERT - UPDATE - DELETE - LOGIN -
 * LOGOUT
 *
 * @author 04dkh
 */
public class AuditLog {

    private int id;
    private int employeeId;
    private String actionType;
    private String tableName;
    private int recordId;
    private LocalDateTime actionTime;
    private String oldData;
    private String newData;

    /**
     * Constructor mặc định.
     */
    public AuditLog() {
    }

    /**
     * Constructor khởi tạo đầy đủ thông tin log (trừ ID và thời gian).
     *
     * @param employeeId ID của nhân viên thực hiện hành động
     * @param actionType Loại hành động (INSERT, UPDATE, DELETE, LOGIN, LOGOUT)
     * @param tableName Tên bảng bị tác động
     * @param recordId ID của bản ghi bị tác động
     * @param oldData Dữ liệu trước khi thay đổi (có thể null)
     * @param newData Dữ liệu sau khi thay đổi (có thể null)
     */
    public AuditLog(int employeeId, String actionType, String tableName, int recordId, String oldData, String newData) {
        this.employeeId = employeeId;
        this.actionType = actionType;
        this.tableName = tableName;
        this.recordId = recordId;
        this.oldData = oldData;
        this.newData = newData;
    }

    /**
     * @return ID bản ghi log (tự động tăng)
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return ID nhân viên thực hiện hành động
     */
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return Loại hành động (INSERT, UPDATE, DELETE, LOGIN, LOGOUT)
     */
    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    /**
     * @return Tên bảng bị tác động
     */
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return ID của bản ghi bị ảnh hưởng trong bảng
     */
    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    /**
     * @return Thời gian thực hiện hành động
     */
    public LocalDateTime getActionTime() {
        return actionTime;
    }

    public void setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }

    /**
     * @return Dữ liệu cũ (trước khi thay đổi), định dạng JSON
     */
    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    /**
     * @return Dữ liệu mới (sau khi thay đổi), định dạng JSON
     */
    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }
}
