package com.ecommerce.dao;

import com.ecommerce.config.DatabaseConnection;
import com.ecommerce.model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    private static final String BASE_SELECT = """
            SELECT r.*, u.full_name AS user_name, p.name AS product_name
            FROM Reviews r
            JOIN Users u ON r.user_id = u.user_id
            JOIN Products p ON r.product_id = p.product_id
            """;

    public List<Review> findByProductId(int productId) throws SQLException {
        List<Review> list = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE r.product_id=? ORDER BY r.created_at DESC";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Review findByUserAndProduct(int userId, int productId) throws SQLException {
        String sql = BASE_SELECT + " WHERE r.user_id=? AND r.product_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public Review save(Review r) throws SQLException {
        String sql = "INSERT INTO Reviews (product_id, user_id, rating, comment) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getProductId());
            ps.setInt(2, r.getUserId());
            ps.setInt(3, r.getRating());
            ps.setString(4, r.getComment());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) r.setReviewId(keys.getInt(1));
        }
        return r;
    }

    public boolean update(Review r) throws SQLException {
        String sql = "UPDATE Reviews SET rating=?, comment=? WHERE review_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, r.getRating());
            ps.setString(2, r.getComment());
            ps.setInt(3, r.getReviewId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int reviewId) throws SQLException {
        String sql = "DELETE FROM Reviews WHERE review_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, reviewId);
            return ps.executeUpdate() > 0;
        }
    }

    private Review mapRow(ResultSet rs) throws SQLException {
        Review r = new Review();
        r.setReviewId(rs.getInt("review_id"));
        r.setProductId(rs.getInt("product_id"));
        r.setUserId(rs.getInt("user_id"));
        r.setRating(rs.getInt("rating"));
        r.setComment(rs.getString("comment"));
        r.setUserName(rs.getString("user_name"));
        r.setProductName(rs.getString("product_name"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) r.setCreatedAt(ts.toLocalDateTime());
        return r;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getConnection();
    }
}
