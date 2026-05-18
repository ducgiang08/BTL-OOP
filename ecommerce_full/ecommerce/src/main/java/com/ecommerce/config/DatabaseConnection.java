package com.ecommerce.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Quản lý kết nối SQL Server dùng chung cho toàn dự án.
 * Sử dụng Singleton pattern để tránh tạo nhiều connection.
 */
public class DatabaseConnection {

    // ── Cấu hình kết nối SQL Server ──────────────────────────────────────────
    private static final String SERVER   = "localhost";
    private static final String PORT     = "1433";
    private static final String DATABASE = "ECommerceDB";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "YourPassword123!"; // ← đổi theo máy

    private static final String URL =
        "jdbc:sqlserver://" + SERVER + ":" + PORT
        + ";databaseName=" + DATABASE
        + ";encrypt=false"
        + ";trustServerCertificate=true";

    private static Connection instance;

    private DatabaseConnection() {}

    /**
     * Trả về Connection duy nhất; tự kết nối lại nếu đã đóng.
     */
    public static Connection getConnection() throws SQLException {
        try {
            if (instance == null || instance.isClosed()) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                instance = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("[DB] Kết nối SQL Server thành công.");
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Không tìm thấy SQL Server JDBC Driver: " + e.getMessage());
        }
        return instance;
    }

    /**
     * Đóng connection — gọi khi tắt ứng dụng.
     */
    public static void close() {
        try {
            if (instance != null && !instance.isClosed()) {
                instance.close();
                System.out.println("[DB] Đã đóng kết nối.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Lỗi khi đóng: " + e.getMessage());
        }
    }
}
