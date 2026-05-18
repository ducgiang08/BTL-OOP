package com.ecommerce.dao;

import com.ecommerce.config.DatabaseConnection;
import com.ecommerce.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<Category> findAll() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Categories ORDER BY name";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Category> findAllActive() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Categories WHERE is_active=1 ORDER BY name";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Category findById(int id) throws SQLException {
        String sql = "SELECT * FROM Categories WHERE category_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public Category save(Category c) throws SQLException {
        String sql = "INSERT INTO Categories (name, description) VALUES (?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) c.setCategoryId(keys.getInt(1));
        }
        return c;
    }

    public boolean update(Category c) throws SQLException {
        String sql = "UPDATE Categories SET name=?, description=? WHERE category_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.setInt(3, c.getCategoryId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean toggleActive(int id, boolean active) throws SQLException {
        String sql = "UPDATE Categories SET is_active=? WHERE category_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setBoolean(1, active);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean existsByName(String name) throws SQLException {
        String sql = "SELECT COUNT(1) FROM Categories WHERE name=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    private Category mapRow(ResultSet rs) throws SQLException {
        Category c = new Category();
        c.setCategoryId(rs.getInt("category_id"));
        c.setName(rs.getString("name"));
        c.setDescription(rs.getString("description"));
        c.setActive(rs.getBoolean("is_active"));
        return c;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getConnection();
    }
}
