/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.models;

import java.sql.Timestamp;

/**
 * Một phiên đăng nhập người dùng trong hệ thống POS.
 *
 * Mỗi phiên ghi nhận ai đã đăng nhập, lúc nào, từ thiết bị nào, và khi nào
 * thoát. Dùng để theo dõi hoạt động của người dùng và phục vụ kiểm tra bảo mật,
 * giám sát hệ thống.
 *
 * @author 04dkh
 */
public class UserSession {

    /**
     * Mã định danh của phiên
     */
    private int id;

    /**
     * ID nhân viên đăng nhập phiên này
     */
    private int employeeId;

    /**
     * Mã token phiên duy nhất để xác thực và quản lý
     */
    private String sessionToken;

    /**
     * Thời điểm đăng nhập vào hệ thống
     */
    private Timestamp loginTime;

    /**
     * Thời điểm đăng xuất khỏi hệ thống
     */
    private Timestamp logoutTime;

    /**
     * Địa chỉ IP thiết bị đăng nhập
     */
    private String ipAddress;

    /**
     * Thông tin thiết bị sử dụng để đăng nhập (OS, trình duyệt, v.v.)
     */
    private String deviceInfo;

    /**
     * @return mã phiên đăng nhập
     */
    public int getId() {
        return id;
    }

    /**
     * @param id mã phiên đăng nhập
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return ID nhân viên
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
     * @return mã token phiên
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * @param sessionToken mã token phiên
     */
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    /**
     * @return thời gian đăng nhập
     */
    public Timestamp getLoginTime() {
        return loginTime;
    }

    /**
     * @param loginTime thời gian đăng nhập
     */
    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * @return thời gian đăng xuất
     */
    public Timestamp getLogoutTime() {
        return logoutTime;
    }

    /**
     * @param logoutTime thời gian đăng xuất
     */
    public void setLogoutTime(Timestamp logoutTime) {
        this.logoutTime = logoutTime;
    }

    /**
     * @return địa chỉ IP đăng nhập
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress địa chỉ IP đăng nhập
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return thông tin thiết bị
     */
    public String getDeviceInfo() {
        return deviceInfo;
    }

    /**
     * @param deviceInfo thông tin thiết bị
     */
    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
