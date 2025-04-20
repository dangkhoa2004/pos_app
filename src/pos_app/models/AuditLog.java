package pos_app.models;

import java.time.LocalDateTime;

/**
 * Lớp đại diện cho một bản ghi nhật ký (audit log) trong hệ thống. Dùng để ghi
 * nhận lại các thao tác người dùng thực hiện lên dữ liệu trong hệ thống như:
 * thêm mới, cập nhật, xóa, đăng nhập, đăng xuất.
 *
 * Các trường dữ liệu bao gồm thông tin người thao tác, bảng bị tác động, dữ
 * liệu thay đổi, loại hành động và thời gian ghi nhận.
 *
 * Các hành động phổ biến:
 * <ul>
 * <li>INSERT - Thêm bản ghi mới</li>
 * <li>UPDATE - Cập nhật bản ghi</li>
 * <li>DELETE - Xóa bản ghi</li>
 * <li>LOGIN - Đăng nhập</li>
 * <li>LOGOUT - Đăng xuất</li>
 * </ul>
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
    private LocalDateTime createdAt;

    /**
     * Constructor mặc định. Dùng trong trường hợp tạo log rỗng và set thủ công.
     */
    public AuditLog() {
    }

    /**
     * Constructor khởi tạo log đầy đủ thông tin (trừ ID và thời điểm ghi).
     *
     * @param employeeId ID nhân viên thực hiện hành động
     * @param actionType Loại hành động (INSERT, UPDATE, DELETE, LOGIN, LOGOUT)
     * @param tableName Tên bảng bị tác động
     * @param recordId ID bản ghi bị tác động
     * @param oldData Dữ liệu cũ (dạng JSON, có thể null)
     * @param newData Dữ liệu mới (dạng JSON, có thể null)
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
     * Constructor đầy đủ tất cả các trường (thường dùng khi đọc dữ liệu từ
     * CSDL).
     *
     * @param id ID log
     * @param employeeId ID nhân viên
     * @param actionType Loại hành động
     * @param tableName Tên bảng bị tác động
     * @param recordId ID bản ghi
     * @param actionTime Thời gian thực hiện hành động
     * @param oldData Dữ liệu cũ (JSON)
     * @param newData Dữ liệu mới (JSON)
     * @param createdAt Thời điểm ghi log
     */
    public AuditLog(int id, int employeeId, String actionType, String tableName, int recordId,
            LocalDateTime actionTime, String oldData, String newData, LocalDateTime createdAt) {
        this.id = id;
        this.employeeId = employeeId;
        this.actionType = actionType;
        this.tableName = tableName;
        this.recordId = recordId;
        this.actionTime = actionTime;
        this.oldData = oldData;
        this.newData = newData;
        this.createdAt = createdAt;
    }

    /**
     * @return ID của bản ghi log
     */
    public int getId() {
        return id;
    }

    /**
     * @param id ID bản ghi log
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return ID nhân viên thực hiện hành động
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId ID nhân viên
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return Loại hành động (INSERT, UPDATE, DELETE, LOGIN, LOGOUT)
     */
    public String getActionType() {
        return actionType;
    }

    /**
     * @param actionType Loại hành động
     */
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    /**
     * @return Tên bảng bị tác động
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName Tên bảng bị tác động
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return ID bản ghi bị tác động
     */
    public int getRecordId() {
        return recordId;
    }

    /**
     * @param recordId ID bản ghi bị tác động
     */
    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    /**
     * @return Thời điểm thực hiện hành động (có thể null)
     */
    public LocalDateTime getActionTime() {
        return actionTime;
    }

    /**
     * @param actionTime Thời điểm thực hiện hành động
     */
    public void setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }

    /**
     * @return Dữ liệu cũ (dạng JSON, có thể null)
     */
    public String getOldData() {
        return oldData;
    }

    /**
     * @param oldData Dữ liệu cũ (dạng JSON)
     */
    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    /**
     * @return Dữ liệu mới (dạng JSON, có thể null)
     */
    public String getNewData() {
        return newData;
    }

    /**
     * @param newData Dữ liệu mới (dạng JSON)
     */
    public void setNewData(String newData) {
        this.newData = newData;
    }

    /**
     * @return Thời điểm bản ghi log được tạo (do hệ thống sinh ra)
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt Thời điểm bản ghi log được tạo
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
