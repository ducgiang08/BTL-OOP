package com.ecommerce.ui;

import com.ecommerce.model.User;
import com.ecommerce.service.AuthService;
import com.ecommerce.util.InputUtil;

import java.sql.SQLException;

/**
 * Giao diện console cho:
 *   UC1 – Đăng nhập / Đăng xuất
 *   UC2 – Đăng ký
 *
 * Phụ trách: Vũ Đức Giang - B22DCVT168
 */
public class AuthMenu {

    private final AuthService authService = new AuthService();

    // ── Menu chào ─────────────────────────────────────────────────────────────

    /**
     * Hiển thị menu khởi động (trước khi đăng nhập).
     * Trả về true nếu đã đăng nhập thành công.
     */
    public boolean showWelcomeMenu() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║   🛒  THƯƠNG MẠI ĐIỆN TỬ         ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Đăng nhập                    ║");
            System.out.println("║  2. Đăng ký tài khoản            ║");
            System.out.println("║  0. Thoát                        ║");
            System.out.println("╚══════════════════════════════════╝");

            int choice = InputUtil.readInt("Lựa chọn: ");
            switch (choice) {
                case 1 -> { if (showLogin()) return true; }
                case 2 -> showRegister();
                case 0 -> { return false; }
                default -> System.out.println("⚠ Lựa chọn không hợp lệ.");
            }
        }
    }

    // ── UC1: Đăng nhập ───────────────────────────────────────────────────────

    private boolean showLogin() {
        System.out.println("\n── ĐĂNG NHẬP ──────────────────────");
        String email    = InputUtil.readLine("  Email / SĐT: ");
        String password = InputUtil.readPassword("  Mật khẩu: ");

        try {
            User user = authService.login(email, password);
            if (user == null) {
                System.out.println("❌ Email hoặc mật khẩu không đúng, hoặc tài khoản bị khóa.");
                return false;
            }
            System.out.printf("✅ Xin chào, %s! (Vai trò: %s)%n",
                              user.getFullName(), user.getRole());
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("⚠ " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("❌ Lỗi hệ thống: " + e.getMessage());
        }
        return false;
    }

    // ── UC1: Đăng xuất ───────────────────────────────────────────────────────

    /**
     * Gọi từ MainMenu khi người dùng chọn Đăng xuất.
     */
    public void logout() {
        String confirm = InputUtil.readLine("Bạn có chắc muốn đăng xuất? (y/n): ");
        if ("y".equalsIgnoreCase(confirm)) {
            authService.logout();
        }
    }

    // ── UC2: Đăng ký ─────────────────────────────────────────────────────────

    private void showRegister() {
        System.out.println("\n── ĐĂNG KÝ TÀI KHOẢN ──────────────");
        String fullName = InputUtil.readLine("  Họ và tên    : ");
        String email    = InputUtil.readLine("  Email        : ");
        String phone    = InputUtil.readLine("  Số điện thoại: ");
        String password = InputUtil.readPassword("  Mật khẩu     : ");
        String confirm  = InputUtil.readPassword("  Xác nhận MK  : ");

        if (!password.equals(confirm)) {
            System.out.println("⚠ Mật khẩu xác nhận không khớp.");
            return;
        }

        System.out.println("  Loại tài khoản:");
        System.out.println("    1. Người mua (BUYER)");
        System.out.println("    2. Người bán (SELLER)");
        int roleChoice = InputUtil.readInt("  Lựa chọn: ");
        String role = (roleChoice == 2) ? "SELLER" : "BUYER";

        try {
            User created = authService.register(fullName, email, phone, password, role);
            System.out.printf("✅ Đăng ký thành công! ID: %d – %s%n",
                              created.getUserId(), created.getFullName());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("⚠ " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("❌ Lỗi hệ thống: " + e.getMessage());
        }
    }
}
