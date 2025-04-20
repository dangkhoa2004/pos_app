/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.models;

/**
 * Một nhân viên trong hệ thống POS. Bao gồm thông tin đăng nhập,
 * thông tin cá nhân và phân quyền.
 *
 * Các trường bao gồm: mã định danh, họ tên, tài khoản, mật khẩu, vai trò, số
 * điện thoại và email. Đây là lớp dùng để thao tác dữ liệu nhân viên.
 *
 * @author 04dkh
 */
public class Employee {

    /**
     * Mã định danh của nhân viên
     */
    private int id;

    /**
     * Tên đầy đủ của nhân viên
     */
    private String name;

    /**
     * Tên tài khoản dùng để đăng nhập
     */
    private String username;

    /**
     * Mật khẩu đăng nhập
     */
    private String password;

    /**
     * ID của vai trò/quyền (ví dụ: quản lý, nhân viên)
     */
    private int role_id;

    /**
     * Số điện thoại của nhân viên
     */
    private String phone;

    /**
     * Email cá nhân của nhân viên
     */
    private String email;

    /**
     * Hàm khởi tạo mặc định.
     */
    public Employee() {
    }

    /**
     * Hàm khởi tạo nhân viên với đầy đủ thông tin.
     *
     * @param id mã nhân viên
     * @param name tên nhân viên
     * @param username tên đăng nhập
     * @param password mật khẩu
     * @param role_id mã vai trò
     * @param phone số điện thoại
     * @param email địa chỉ email
     */
    public Employee(int id, String name, String username, String password, int role_id, String phone, String email) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role_id = role_id;
        this.phone = phone;
        this.email = email;
    }

    /**
     * @return id nhân viên
     */
    public int getId() {
        return id;
    }

    /**
     * @param id mã nhân viên
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return tên nhân viên
     */
    public String getName() {
        return name;
    }

    /**
     * @param name tên nhân viên
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username tên tài khoản
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return mật khẩu
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password mật khẩu
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return role_id
     */
    public int getRole_id() {
        return role_id;
    }

    /**
     * @param role_id mã vai trò
     */
    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    /**
     * @return số điện thoại
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone số điện thoại
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email địa chỉ email
     */
    public void setEmail(String email) {
        this.email = email;
    }

}
