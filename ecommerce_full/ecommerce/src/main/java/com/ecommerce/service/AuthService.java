package com.ecommerce.service;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import com.ecommerce.util.PasswordUtil;
import com.ecommerce.util.SessionManager;

import java.sql.SQLException;

/**
 * Business logic cho:
 *   UC1 – Đăng nhập / Đăng xuất
 *   UC2 – Đăng ký
 *
 * Phụ trách: Vũ Đức Giang - B22DCVT168
 */
public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    // ── UC1: Đăng nhập ───────────────────────────────────────────────────────

    /**
     * Đăng nhập: xác thực email + password, cập nhật session.
     *
     * @return User nếu thành công, null nếu sai thông tin.
     * @throws SQLException lỗi DB
     */
    public User login(String credential, String password) throws SQLException {
        if (credential == null || credential.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Thông tin đăng nhập không được để trống.");
        }
        String hash = PasswordUtil.hash(password);
        User user = userDAO.findByCredentialAndPassword(credential.trim(), hash);
        if (user == null) return null;

        SessionManager.login(user);
        return user;
    }

    // ── UC1: Đăng xuất ───────────────────────────────────────────────────────

    /**
     * Đăng xuất: xóa session hiện tại.
     */
    public void logout() {
        SessionManager.logout();
    }

    // ── UC2: Đăng ký ─────────────────────────────────────────────────────────

    /**
     * Đăng ký tài khoản mới.
     *
     * @param fullName  Họ và tên
     * @param email     Email (phải duy nhất)
     * @param phone     Số điện thoại
     * @param password  Mật khẩu plaintext
     * @param role      BUYER hoặc SELLER
     * @return User vừa tạo
     */
    public User register(String fullName, String email, String phone,
                         String password, String role) throws SQLException {

        // Validate
        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("Họ tên không được để trống.");
        if (email == null || !email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$"))
            throw new IllegalArgumentException("Email không hợp lệ.");
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự.");
        if (!"BUYER".equals(role) && !"SELLER".equals(role))
            throw new IllegalArgumentException("Loại tài khoản không hợp lệ.");

        // Kiểm tra email trùng
        if (userDAO.existsByEmail(email.trim()))
            throw new IllegalStateException("Email này đã được đăng ký.");

        // Tạo user
        User newUser = new User(fullName.trim(), email.trim(), phone,
                                PasswordUtil.hash(password), role);
        return userDAO.save(newUser);
    }
}
