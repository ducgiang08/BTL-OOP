package com.ecommerce.util;

import com.ecommerce.model.User;

/**
 * Lưu trữ phiên đăng nhập hiện tại (Singleton).
 * Các module khác gọi SessionManager.getCurrentUser() để lấy user đang đăng nhập.
 */
public class SessionManager {

    private static User currentUser;

    private SessionManager() {}

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
        System.out.println("Đã đăng xuất khỏi hệ thống.");
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static boolean isAdmin() {
        return isLoggedIn() && "ADMIN".equals(currentUser.getRole());
    }

    public static boolean isSeller() {
        return isLoggedIn() && "SELLER".equals(currentUser.getRole());
    }

    public static boolean isBuyer() {
        return isLoggedIn() && "BUYER".equals(currentUser.getRole());
    }
}
