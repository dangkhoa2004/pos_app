/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.models;

/**
 * Một khách hàng trong hệ thống POS.
 *
 * Bao gồm các thông tin cơ bản như mã khách hàng, họ tên, số điện thoại, địa
 * chỉ email và nơi ở/giao hàng. Lớp này hỗ trợ việc lưu trữ, truy xuất và xử lý
 * thông tin khách hàng trong hệ thống bán hàng.
 *
 * @author 04dkh
 */
public class Customer {

    /**
     * Mã định danh duy nhất của khách hàng
     */
    private int id;

    /**
     * Tên đầy đủ của khách hàng
     */
    private String name;

    /**
     * Số điện thoại của khách hàng
     */
    private String phone;

    /**
     * Địa chỉ email của khách hàng
     */
    private String email;

    /**
     * Địa chỉ nơi ở hoặc giao hàng của khách hàng
     */
    private String address;

    /**
     * Hàm khởi tạo mặc định cho Customer.
     */
    public Customer() {
    }

    /**
     * Hàm khởi tạo khách hàng với đầy đủ thông tin.
     *
     * @param id mã khách hàng
     * @param name tên khách hàng
     * @param phone số điện thoại
     * @param email địa chỉ email
     * @param address địa chỉ nhà
     */
    public Customer(int id, String name, String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    /**
     * @return id khách hàng
     */
    public int getId() {
        return id;
    }

    /**
     * @param id mã khách hàng
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return tên khách hàng
     */
    public String getName() {
        return name;
    }

    /**
     * @param name tên khách hàng
     */
    public void setName(String name) {
        this.name = name;
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
     * @return địa chỉ email
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

    /**
     * @return địa chỉ khách hàng
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address địa chỉ khách hàng
     */
    public void setAddress(String address) {
        this.address = address;
    }

}
