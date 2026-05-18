package com.ecommerce;

import com.ecommerce.config.DatabaseConnection;
import com.ecommerce.ui.MainMenu;

/**
 * Điểm khởi động ứng dụng Thương mại Điện tử.
 * Chạy: java -cp .;lib/* com.ecommerce.Main
 */
public class Main {

    public static void main(String[] args) {
        try {
            // Kiểm tra kết nối DB ngay khi khởi động
            DatabaseConnection.getConnection();

            // Chạy ứng dụng
            new MainMenu().run();

        } catch (Exception e) {
            System.err.println("❌ Không thể khởi động ứng dụng: " + e.getMessage());
            System.err.println("   Kiểm tra cấu hình DB trong DatabaseConnection.java");
        } finally {
            DatabaseConnection.close();
        }
    }
}
