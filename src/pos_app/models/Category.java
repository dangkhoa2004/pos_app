/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos_app.models;

/**
 * Một danh mục sản phẩm trong hệ thống POS.
 *
 * Danh mục dùng để nhóm các sản phẩm có cùng tính chất, loại hoặc mục đích sử
 * dụng. Mỗi danh mục bao gồm mã định danh, tên và mô tả ngắn gọn.
 *
 * @author 04dkh
 */
public class Category {

    /**
     * Mã định danh duy nhất của danh mục
     */
    private int id;

    /**
     * Tên của danh mục
     */
    private String name;

    /**
     * Mô tả ngắn gọn về danh mục
     */
    private String description;

    /**
     * Khởi tạo một danh mục trống.
     */
    public Category() {
    }

    /**
     * Khởi tạo một danh mục với các thông tin được cung cấp.
     *
     * @param id mã định danh của danh mục
     * @param name tên của danh mục
     * @param description mô tả của danh mục
     */
    public Category(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * @return id của danh mục
     */
    public int getId() {
        return id;
    }

    /**
     * @param id mã danh mục
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return tên danh mục
     */
    public String getName() {
        return name;
    }

    /**
     * @param name tên danh mục
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return mô tả danh mục
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description mô tả danh mục
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
