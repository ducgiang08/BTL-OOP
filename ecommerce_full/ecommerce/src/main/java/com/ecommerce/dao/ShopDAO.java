package com.ecommerce.dao;

import com.ecommerce.config.DatabaseConnection;
import com.ecommerce.model.Shop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShopDAO {

    public List<Shop> findAll() throws SQLException {
        List<Shop> list = new ArrayList<>();
        String sql = """
            SELECT s.*, u.full_name AS owner_name FROM Shops s
            JOIN Users u ON s.owner_id = u.user_id
            ORDER BY s.created_at DESC
            """;
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Shop findByOwnerId(int ownerId) throws SQLException {
        String sql = """
            SELECT s.*, u.full_name AS owner_name FROM Shops s
            JOIN Users u ON s.owner_id = u.user_id
            WHERE s.owner_id=?
            """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public Shop findById(int shopId) throws SQLException {
        String sql = """
            SELECT s.*, u.full_name AS owner_name FROM Shops s
            JOIN Users u ON s.owner_id = u.user_id
            WHERE s.shop_id=?
            """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, shopId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public Shop save(Shop s) throws SQLException {
        String sql = "INSERT INTO Shops (owner_id, name, description, address) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, s.getOwnerId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getDescription());
            ps.setString(4, s.getAddress());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) s.setShopId(keys.getInt(1));
        }
        return s;
    }

    public boolean update(Shop s) throws SQLException {
        String sql = "UPDATE Shops SET name=?, description=?, address=? WHERE shop_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getDescription());
            ps.setString(3, s.getAddress());
            ps.setInt(4, s.getShopId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean toggleActive(int shopId, boolean active) throws SQLException {
        String sql = "UPDATE Shops SET is_active=? WHERE shop_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setBoolean(1, active);
            ps.setInt(2, shopId);
            return ps.executeUpdate() > 0;
        }
    }

    private Shop mapRow(ResultSet rs) throws SQLException {
        Shop s = new Shop();
        s.setShopId(rs.getInt("shop_id"));
        s.setOwnerId(rs.getInt("owner_id"));
        s.setName(rs.getString("name"));
        s.setDescription(rs.getString("description"));
        s.setAddress(rs.getString("address"));
        s.setActive(rs.getBoolean("is_active"));
        s.setOwnerName(rs.getString("owner_name"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) s.setCreatedAt(ts.toLocalDateTime());
        return s;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getConnection();
    }
}
