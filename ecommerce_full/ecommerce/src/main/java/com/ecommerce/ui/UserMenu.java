package com.ecommerce.ui;

import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import com.ecommerce.util.InputUtil;
import com.ecommerce.util.SessionManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Giao diện console cho:
 *   UC3 – Quản lý Người dùng
 *     • Admin : danh sách, chi tiết, khóa/mở khóa
 *     • User  : xem hồ sơ, cập nhật, đổi mật khẩu
 *
 * Phụ trách: Vũ Đức Giang - B22DCVT168
 */
public class UserMenu {

    private final UserService userService = new UserService();

    /**
     * Điểm vào duy nhất — tự chọn menu Admin hay User dựa trên vai trò.
     */
    public void show() {
        if (SessionManager.isAdmin()) {
            showAdminMenu();
        } else {
            showProfileMenu();
        }
    }

    // ── Admin menu ────────────────────────────────────────────────────────────

    private void showAdminMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║   👥  QUẢN LÝ NGƯỜI DÙNG [ADMIN] ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Danh sách tất cả người dùng  ║");
            System.out.println("║  2. Xem chi tiết người dùng      ║");
            System.out.println("║  3. Khóa tài khoản               ║");
            System.out.println("║  4. Mở khóa tài khoản            ║");
            System.out.println("║  0. Quay lại                     ║");
            System.out.println("╚══════════════════════════════════╝");

            int choice = InputUtil.readInt("Lựa chọn: ");
            try {
                switch (choice) {
                    case 1 -> listAllUsers();
                    case 2 -> viewUserDetail();
                    case 3 -> lockUser();
                    case 4 -> unlockUser();
                    case 0 -> running = false;
                    default -> System.out.println("⚠ Lựa chọn không hợp lệ.");
                }
            } catch (SecurityException e) {
                System.out.println("🔒 " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("⚠ " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("❌ Lỗi hệ thống: " + e.getMessage());
            }
        }
    }

    private void listAllUsers() throws SQLException {
        List<User> users = userService.getAllUsers();
        System.out.println("\n─── DANH SÁCH NGƯỜI DÙNG (" + users.size() + ") ───────────");
        System.out.printf("%-6s %-25s %-30s %-8s %-10s%n",
                          "ID", "Họ tên", "Email", "Vai trò", "Trạng thái");
        System.out.println("─".repeat(82));
        for (User u : users) {
            System.out.printf("%-6d %-25s %-30s %-8s %-10s%n",
                    u.getUserId(),
                    truncate(u.getFullName(), 24),
                    truncate(u.getEmail(), 29),
                    u.getRole(),
                    u.isActive() ? "Hoạt động" : "Bị khóa");
        }
        InputUtil.pressEnter();
    }

    private void viewUserDetail() throws SQLException {
        int id = InputUtil.readInt("  Nhập User ID: ");
        User u = userService.getUserById(id);
        System.out.println("\n─── CHI TIẾT NGƯỜI DÙNG ────────────────");
        System.out.println("  ID        : " + u.getUserId());
        System.out.println("  Họ tên    : " + u.getFullName());
        System.out.println("  Email     : " + u.getEmail());
        System.out.println("  SĐT       : " + u.getPhone());
        System.out.println("  Vai trò   : " + u.getRole());
        System.out.println("  Trạng thái: " + (u.isActive() ? "Hoạt động" : "Bị khóa"));
        System.out.println("  Ngày tạo  : " + u.getCreatedAt());
        InputUtil.pressEnter();
    }

    private void lockUser() throws SQLException {
        int id = InputUtil.readInt("  Nhập User ID cần khóa: ");
        userService.lockUser(id);
        System.out.println("✅ Đã khóa tài khoản ID: " + id);
    }

    private void unlockUser() throws SQLException {
        int id = InputUtil.readInt("  Nhập User ID cần mở khóa: ");
        userService.unlockUser(id);
        System.out.println("✅ Đã mở khóa tài khoản ID: " + id);
    }

    // ── Người dùng thường ─────────────────────────────────────────────────────

    private void showProfileMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║   👤  THÔNG TIN CÁ NHÂN          ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Xem thông tin cá nhân        ║");
            System.out.println("║  2. Cập nhật họ tên & SĐT        ║");
            System.out.println("║  3. Đổi mật khẩu                 ║");
            System.out.println("║  0. Quay lại                     ║");
            System.out.println("╚══════════════════════════════════╝");

            int choice = InputUtil.readInt("Lựa chọn: ");
            try {
                switch (choice) {
                    case 1 -> viewMyProfile();
                    case 2 -> updateMyProfile();
                    case 3 -> changePassword();
                    case 0 -> running = false;
                    default -> System.out.println("⚠ Lựa chọn không hợp lệ.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("⚠ " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("❌ Lỗi hệ thống: " + e.getMessage());
            }
        }
    }

    private void viewMyProfile() throws SQLException {
        User u = userService.getMyProfile();
        System.out.println("\n─── HỒ SƠ CỦA TÔI ─────────────────");
        System.out.println("  Họ tên    : " + u.getFullName());
        System.out.println("  Email     : " + u.getEmail());
        System.out.println("  SĐT       : " + u.getPhone());
        System.out.println("  Vai trò   : " + u.getRole());
        System.out.println("  Ngày tạo  : " + u.getCreatedAt());
        InputUtil.pressEnter();
    }

    private void updateMyProfile() throws SQLException {
        System.out.println("\n─── CẬP NHẬT THÔNG TIN ─────────────");
        String name  = InputUtil.readLine("  Họ tên mới  : ");
        String phone = InputUtil.readLine("  Số ĐT mới   : ");
        userService.updateMyProfile(name, phone);
        System.out.println("✅ Cập nhật thành công.");
    }

    private void changePassword() throws SQLException {
        System.out.println("\n─── ĐỔI MẬT KHẨU ───────────────────");
        String oldPw  = InputUtil.readPassword("  Mật khẩu cũ: ");
        String newPw  = InputUtil.readPassword("  Mật khẩu mới: ");
        String confirm = InputUtil.readPassword("  Xác nhận mới: ");
        if (!newPw.equals(confirm)) {
            System.out.println("⚠ Mật khẩu xác nhận không khớp.");
            return;
        }
        userService.changePassword(oldPw, newPw);
        System.out.println("✅ Đổi mật khẩu thành công.");
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max - 1) + "…" : s;
    }
}
