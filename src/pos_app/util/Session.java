package pos_app.util;

import pos_app.dao.EmployeeDAO;
import pos_app.dao.UserSessionDAO;
import pos_app.models.Employee;
import pos_app.models.UserSession;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.util.UUID;

/**
 * Lớp Session quản lý phiên làm việc của người dùng, hỗ trợ đăng nhập tự động,
 * lưu thông tin đăng nhập và truy xuất các tiện ích hệ thống.
 */
public class Session {

    private static Employee currentUser = null;
    private static final File ACCOUNT_FILE = new File("session/account.txt");
    private static String currentSessionToken;

    /**
     * Thiết lập người dùng hiện tại khi đăng nhập.
     *
     * @param emp nhân viên đăng nhập
     */
    public static void login(Employee emp) {
        currentUser = emp;
    }

    /**
     * Đăng xuất người dùng hiện tại, cập nhật thời gian đăng xuất và xóa thông
     * tin đăng nhập.
     */
    public static void logout() {
        currentUser = null;
        try {
            if (currentSessionToken != null) {
                new UserSessionDAO().closeSession(currentSessionToken);
                currentSessionToken = null;
                System.out.println("🛑 Đã cập nhật logout_time cho session hiện tại.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ACCOUNT_FILE.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_FILE))) {
                writer.write("");
                System.out.println("🧹 Đã xóa nội dung file account.txt");
            } catch (IOException e) {
                System.err.println("❌ Không thể xóa nội dung file.");
                e.printStackTrace();
            }
        } else {
            createAccountFile();
        }
    }

    /**
     * Kiểm tra xem có người dùng nào đang đăng nhập không.
     *
     * @return true nếu có người dùng đang đăng nhập, false nếu không
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Trả về đối tượng nhân viên hiện đang đăng nhập.
     *
     * @return nhân viên hiện tại, hoặc null nếu chưa đăng nhập
     */
    public static Employee getCurrentUser() {
        return currentUser;
    }

    /**
     * Trả về ID của nhân viên hiện tại.
     *
     * @return ID nhân viên hoặc -1 nếu chưa đăng nhập
     */
    public static int getEmployeeId() {
        return (currentUser != null) ? currentUser.getId() : -1;
    }

    /**
     * Trả về mã token của phiên làm việc hiện tại.
     *
     * @return chuỗi token của phiên làm việc
     */
    public static String getCurrentSessionToken() {
        return currentSessionToken;
    }

    /**
     * Thực hiện đăng nhập tự động từ file tài khoản nếu có dữ liệu hợp lệ.
     */
    public static void autoLoginFromFile() {
        System.out.println("📂 Kiểm tra file: " + ACCOUNT_FILE.getAbsolutePath());

        if (!ACCOUNT_FILE.exists()) {
            createAccountFile();
            return;
        }

        String username = null;
        String password = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNT_FILE))) {
            username = reader.readLine();
            password = reader.readLine();
        } catch (IOException e) {
            System.err.println("❌ Lỗi đọc file account.txt.");
            e.printStackTrace();
            return;
        }

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            System.out.println("⚠️ File account.txt trống hoặc thiếu dữ liệu. Bỏ qua auto-login.");
            return;
        }

        Employee emp = EmployeeDAO.checkLogin(username, password);
        if (emp == null) {
            System.out.println("❌ Thông tin đăng nhập trong file không hợp lệ.");
            return;
        }

        try {
            UserSessionDAO sessionDAO = new UserSessionDAO();
            UserSession lastSession = sessionDAO.getLastActiveSession(emp.getId());

            if (lastSession != null) {
                long loginTimeMillis = lastSession.getLoginTime().getTime();
                long currentTimeMillis = System.currentTimeMillis();
                long duration = currentTimeMillis - loginTimeMillis;

                if (duration > 2 * 60 * 60 * 1000) {
                    System.out.println("⏰ Phiên đăng nhập đã hết hạn!");

                    if (lastSession.getSessionToken() != null) {
                        sessionDAO.closeSession(lastSession.getSessionToken());
                        System.out.println("🛑 Đã cập nhật logout_time cho phiên cũ.");
                    }

                    logout();

                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null,
                                "Phiên đăng nhập đã hết hạn.",
                                "Thông báo",
                                JOptionPane.WARNING_MESSAGE);
                    });
                    return;
                }
            }

            login(emp);
            currentSessionToken = UUID.randomUUID().toString();
            sessionDAO.openSession(emp.getId(), currentSessionToken, getIPAddress(), getDeviceInfo());
            System.out.println("✅ Auto-login thành công!");
            System.out.println("🔑 Đã tạo session token: " + currentSessionToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ghi thông tin tài khoản vào file để hỗ trợ đăng nhập tự động.
     *
     * @param username tên đăng nhập
     * @param password mật khẩu
     */
    public static void saveAccount(String username, String password) {
        try {
            createAccountFile();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_FILE))) {
                writer.write(username);
                writer.newLine();
                writer.write(password);
                System.out.println("💾 Đã lưu tài khoản vào file: " + ACCOUNT_FILE.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("❌ Không thể lưu tài khoản.");
            e.printStackTrace();
        }
    }

    /**
     * Tạo file lưu tài khoản nếu chưa tồn tại.
     */
    private static void createAccountFile() {
        try {
            File parentDir = ACCOUNT_FILE.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
                System.out.println("📂 Đã tạo thư mục session/");
            }
            if (!ACCOUNT_FILE.exists()) {
                ACCOUNT_FILE.createNewFile();
                System.out.println("📄 Đã tạo file account.txt");
            }
        } catch (IOException e) {
            System.err.println("❌ Không thể tạo file account.txt.");
            e.printStackTrace();
        }
    }

    /**
     * Lấy địa chỉ IP hiện tại của máy.
     *
     * @return địa chỉ IP dưới dạng chuỗi
     */
    public static String getIPAddress() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    /**
     * Lấy thông tin hệ điều hành hiện tại.
     *
     * @return chuỗi mô tả hệ điều hành
     */
    public static String getDeviceInfo() {
        return System.getProperty("os.name");
    }
}
