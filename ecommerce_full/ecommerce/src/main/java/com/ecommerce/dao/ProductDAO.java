package com.ecommerce.dao;

import com.ecommerce.config.DatabaseConnection;
import com.ecommerce.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private static final String BASE_SELECT = """
            SELECT p.*, s.name AS shop_name, c.name AS category_name
            FROM Products p
            JOIN Shops s ON p.shop_id = s.shop_id
            JOIN Categories c ON p.category_id = c.category_id
            """;

    public List<Product> findAll() throws SQLException {
        List<Product> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(BASE_SELECT + " ORDER BY p.created_at DESC")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Product> findAllActive() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE p.is_active=1 ORDER BY p.created_at DESC";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Product> findByShopId(int shopId) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE p.shop_id=? ORDER BY p.created_at DESC";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, shopId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Product> search(String keyword, int categoryId, double minPrice, double maxPrice)
            throws SQLException {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT + " WHERE p.is_active=1");
        if (keyword != null && !keyword.isBlank())
            sql.append(" AND p.name LIKE ?");
        if (categoryId > 0)
            sql.append(" AND p.category_id=?");
        if (minPrice >= 0)
            sql.append(" AND p.price >= ?");
        if (maxPrice > 0)
            sql.append(" AND p.price <= ?");
        sql.append(" ORDER BY p.name");

        try (PreparedStatement ps = conn().prepareStatement(sql.toString())) {
            int idx = 1;
            if (keyword != null && !keyword.isBlank())
                ps.setString(idx++, "%" + keyword + "%");
            if (categoryId > 0)
                ps.setInt(idx++, categoryId);
            if (minPrice >= 0)
                ps.setDouble(idx++, minPrice);
            if (maxPrice > 0)
                ps.setDouble(idx++, maxPrice);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Product findById(int id) throws SQLException {
        String sql = BASE_SELECT + " WHERE p.product_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public Product save(Product p) throws SQLException {
        String sql = """
            INSERT INTO Products (shop_id, category_id, name, description, price, stock)
            VALUES (?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getShopId());
            ps.setInt(2, p.getCategoryId());
            ps.setString(3, p.getName());
            ps.setString(4, p.getDescription());
            ps.setBigDecimal(5, p.getPrice());
            ps.setInt(6, p.getStock());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) p.setProductId(keys.getInt(1));
        }
        return p;
    }

    public boolean update(Product p) throws SQLException {
        String sql = """
            UPDATE Products SET category_id=?, name=?, description=?, price=?, stock=?
            WHERE product_id=?
            """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getDescription());
            ps.setBigDecimal(4, p.getPrice());
            ps.setInt(5, p.getStock());
            ps.setInt(6, p.getProductId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean toggleActive(int productId, boolean active) throws SQLException {
        String sql = "UPDATE Products SET is_active=? WHERE product_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setBoolean(1, active);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateStock(int productId, int newStock, Connection conn) throws SQLException {
        String sql = "UPDATE Products SET stock=? WHERE product_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        }
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setShopId(rs.getInt("shop_id"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStock(rs.getInt("stock"));
        p.setActive(rs.getBoolean("is_active"));
        p.setShopName(rs.getString("shop_name"));
        p.setCategoryName(rs.getString("category_name"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) p.setCreatedAt(ts.toLocalDateTime());
        return p;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getConnection();
    }
}
