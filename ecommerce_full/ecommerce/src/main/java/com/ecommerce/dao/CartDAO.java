package com.ecommerce.dao;

import com.ecommerce.config.DatabaseConnection;
import com.ecommerce.model.CartItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    private static final String BASE_SELECT = """
            SELECT ci.*, p.name AS product_name, p.price AS unit_price, p.stock
            FROM CartItems ci
            JOIN Products p ON ci.product_id = p.product_id
            """;

    public List<CartItem> findByUserId(int userId) throws SQLException {
        List<CartItem> list = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE ci.user_id=? ORDER BY ci.added_at DESC";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public CartItem findByUserAndProduct(int userId, int productId) throws SQLException {
        String sql = BASE_SELECT + " WHERE ci.user_id=? AND ci.product_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public CartItem save(CartItem item) throws SQLException {
        String sql = """
            INSERT INTO CartItems (user_id, product_id, quantity) VALUES (?,?,?)
            """;
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getUserId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) item.setCartItemId(keys.getInt(1));
        }
        return item;
    }

    public boolean updateQuantity(int cartItemId, int quantity) throws SQLException {
        String sql = "UPDATE CartItems SET quantity=? WHERE cart_item_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int cartItemId) throws SQLException {
        String sql = "DELETE FROM CartItems WHERE cart_item_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteAllByUser(int userId) throws SQLException {
        String sql = "DELETE FROM CartItems WHERE user_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() >= 0;
        }
    }

    private CartItem mapRow(ResultSet rs) throws SQLException {
        CartItem ci = new CartItem();
        ci.setCartItemId(rs.getInt("cart_item_id"));
        ci.setUserId(rs.getInt("user_id"));
        ci.setProductId(rs.getInt("product_id"));
        ci.setQuantity(rs.getInt("quantity"));
        ci.setProductName(rs.getString("product_name"));
        ci.setUnitPrice(rs.getBigDecimal("unit_price"));
        ci.setStock(rs.getInt("stock"));
        Timestamp ts = rs.getTimestamp("added_at");
        if (ts != null) ci.setAddedAt(ts.toLocalDateTime());
        return ci;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getConnection();
    }
}
