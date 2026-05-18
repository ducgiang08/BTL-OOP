package com.ecommerce.service;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import com.ecommerce.util.PasswordUtil;
import com.ecommerce.util.SessionManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic cho:
 *   UC3 – Quản lý Người dùng
 *     Admin  : xem danh sách, xem chi tiết, khóa / mở khóa
 *     Người dùng: xem & cập nhật thông tin cá nhân, đổi mật khẩu
 *
 * Phụ trách: Vũ Đức Giang - B22DCVT168
 */
public class UserService {

    private final UserDAO userDAO = new UserDAO();

    // ── ADMIN chức năng ───────────────────────────────────────────────────────

    /**
     * Admin: lấy danh sách toàn bộ user.
     */
    public List<User> getAllUsers() throws SQLException {
        requireAdmin();
        return userDAO.findAll();
    }

    /**
     * Admin: xem chi tiết một user.
     */
    public User getUserById(int userId) throws SQLException {
        requireAdmin();
        User u = userDAO.findById(userId);
        if (u == null) throw new IllegalArgumentException("Không tìm thấy user ID: " + userId);
        return u;
    }

    /**
     * Admin: khóa tài khoản.
     */
    public void lockUser(int userId) throws SQLException {
        requireAdmin();
        preventSelfAction(userId, "khóa");
        if (!userDAO.toggleActive(userId, false))
            throw new IllegalArgumentException("Không tìm thấy user ID: " + userId);
    }

    /**
     * Admin: mở khóa tài khoản.
     */
    public void unlockUser(int userId) throws SQLException {
        requireAdmin();
        if (!userDAO.toggleActive(userId, true))
            throw new IllegalArgumentException("Không tìm thấy user ID: " + userId);
    }

    // ── NGƯỜI DÙNG THƯỜNG chức năng ──────────────────────────────────────────

    /**
     * Người dùng: xem thông tin cá nhân (chính mình).
     */
    public User getMyProfile() throws SQLException {
        requireLogin();
        return userDAO.findById(SessionManager.getCurrentUser().getUserId());
    }

    /**
     * Người dùng: cập nhật họ tên & số điện thoại.
     */
    public void updateMyProfile(String fullName, String phone) throws SQLException {
        requireLogin();
        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("Họ tên không được để trống.");
        int myId = SessionManager.getCurrentUser().getUserId();
        userDAO.updateProfile(myId, fullName.trim(), phone);
        // Refresh session object
        SessionManager.login(userDAO.findById(myId));
    }

    /**
     * Người dùng: đổi mật khẩu.
     */
    public void changePassword(String oldPassword, String newPassword) throws SQLException {
        requireLogin();
        if (newPassword == null || newPassword.length() < 6)
            throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 6 ký tự.");

        User me = userDAO.findById(SessionManager.getCurrentUser().getUserId());
        if (!PasswordUtil.verify(oldPassword, me.getPasswordHash()))
            throw new IllegalArgumentException("Mật khẩu cũ không đúng.");

        userDAO.updatePassword(me.getUserId(), PasswordUtil.hash(newPassword));
    }

    // ── Guard helpers ─────────────────────────────────────────────────────────

    private void requireLogin() {
        if (!SessionManager.isLoggedIn())
            throw new IllegalStateException("Bạn chưa đăng nhập.");
    }

    private void requireAdmin() {
        requireLogin();
        if (!SessionManager.isAdmin())
            throw new SecurityException("Chức năng chỉ dành cho Admin.");
    }

    private void preventSelfAction(int targetId, String action) {
        if (SessionManager.getCurrentUser().getUserId() == targetId)
            throw new IllegalArgumentException("Không thể " + action + " chính mình.");
    }
}
