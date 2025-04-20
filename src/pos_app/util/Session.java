package pos_app.util;

import pos_app.dao.EmployeeDAO;
import pos_app.models.Employee;

import java.io.*;

public class Session {

    private static Employee currentUser = null;

    // Đường dẫn tương đối – an toàn cho mọi máy
    private static final File ACCOUNT_FILE = new File("session/account.txt");

    public static void login(Employee emp) {
        currentUser = emp;
    }

    public static void logout() {
        currentUser = null;
        if (ACCOUNT_FILE.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_FILE))) {
                writer.write(""); // Xóa nội dung file
                System.out.println("🧹 Đã xóa nội dung file account.txt");
            } catch (IOException e) {
                System.err.println("❌ Không thể xóa nội dung file.");
                e.printStackTrace();
            }
        } else {
            createAccountFile(); // tạo nếu chưa tồn tại
        }
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static Employee getCurrentUser() {
        return currentUser;
    }

    public static void saveAccount(String username, String password) {
        try {
            createAccountFile(); // đảm bảo file tồn tại

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

    public static void autoLoginFromFile() {
        System.out.println("📂 Kiểm tra file: " + ACCOUNT_FILE.getAbsolutePath());
        if (!ACCOUNT_FILE.exists()) {
            createAccountFile();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNT_FILE))) {
            String username = reader.readLine();
            String password = reader.readLine();

            if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
                Employee emp = EmployeeDAO.checkLogin(username, password);
                if (emp != null) {
                    login(emp);
                    System.out.println("✅ Auto-login thành công!");
                } else {
                    System.out.println("❌ Thông tin trong file không hợp lệ.");
                }
            } else {
                System.out.println("⚠️ File account.txt trống hoặc thiếu dữ liệu.");
            }

        } catch (IOException e) {
            System.err.println("❌ Lỗi đọc file account.txt.");
            e.printStackTrace();
        }
    }

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

    public static int getEmployeeId() {
        return (currentUser != null) ? currentUser.getId() : -1;
    }

}
