package com.ecommerce.dao;

import com.ecommerce.config.DatabaseConnection;
import com.ecommerce.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO xử lý tất cả thao tác DB liên quan đến bảng Users.
 *
 * Use case:
 *   UC1  – Đăng nhập / Đăng xuất   → findByEmailAndPassword()
 *   UC2  – Đăng ký                  → findByEmail(), save()
 *   UC3  – Quản lý người dùng       → findAll(), findById(), update(), toggleActive()
 */
public class UserDAO {

    // ── UC2: Đăng ký ─────────────────────────────────────────────────────────

    /**
     * Kiểm tra email đã tồn tại chưa (dùng khi đăng ký).
     */
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(1) FROM Users WHERE email = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    /**
     * Lưu user mới vào DB — trả về user có userId được DB gán.
     */
    public User save(User user) throws SQLException {
        String sql = """
            INSERT INTO Users (full_name, email, phone, password_hash, role, is_active)
            VALUES (?, ?, ?, ?, ?, 1)
            """;
        try (PreparedStatement ps = conn().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPasswordHash());
            ps.setString(5, user.getRole());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                user.setUserId(keys.getInt(1));
            }
        }
        return user;
    }

    // ── UC1: Đăng nhập ───────────────────────────────────────────────────────

    /**
     * Tìm user theo email và password hash — dùng cho đăng nhập.
     * Trả về null nếu không tìm thấy hoặc bị khóa.
     */
    public User findByCredentialAndPassword(String credential, String passwordHash)
            throws SQLException {
        String sql = """
            SELECT * FROM Users
            WHERE (email = ? OR phone = ?) AND password_hash = ? AND is_active = 1
            """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, credential);
            ps.setString(2, credential);
            ps.setString(3, passwordHash);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    // ── UC3: Quản lý người dùng ───────────────────────────────────────────────

    /**
     * Lấy toàn bộ danh sách user (Admin dùng).
     */
    public List<User> findAll() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM Users ORDER BY created_at DESC";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    /**
     * Tìm user theo ID.
     */
    public User findById(int userId) throws SQLException {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    /**
     * Cập nhật thông tin cá nhân (fullName, phone) — người dùng thường dùng.
     */
    public boolean updateProfile(int userId, String fullName, String phone)
            throws SQLException {
        String sql = "UPDATE Users SET full_name=?, phone=? WHERE user_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật mật khẩu (hash).
     */
    public boolean updatePassword(int userId, String newHash) throws SQLException {
        String sql = "UPDATE Users SET password_hash=? WHERE user_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, newHash);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Admin: Khóa / mở khóa tài khoản.
     */
    public boolean toggleActive(int userId, boolean active) throws SQLException {
        String sql = "UPDATE Users SET is_active=? WHERE user_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setBoolean(1, active);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Admin: Xem thông tin một user bất kỳ (alias findById, dùng tên rõ nghĩa).
     */
    public User findByIdForAdmin(int userId) throws SQLException {
        return findById(userId);
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRole(rs.getString("role"));
        u.setActive(rs.getBoolean("is_active"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) u.setCreatedAt(ts.toLocalDateTime());
        return u;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getConnection();
    }
}
