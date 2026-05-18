package com.ecommerce.dao;

import com.ecommerce.config.DatabaseConnection;
import com.ecommerce.model.Address;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO {

    public List<Address> findByUserId(int userId) throws SQLException {
        List<Address> list = new ArrayList<>();
        String sql = "SELECT * FROM Addresses WHERE user_id=? ORDER BY is_default DESC, address_id";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Address findById(int id) throws SQLException {
        String sql = "SELECT * FROM Addresses WHERE address_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public Address findDefault(int userId) throws SQLException {
        String sql = "SELECT * FROM Addresses WHERE user_id=? AND is_default=1";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public Address save(Address a) throws SQLException {
        String sql = """
            INSERT INTO Addresses (user_id, recipient, phone, street, district, city, is_default)
            VALUES (?,?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getUserId());
            ps.setString(2, a.getRecipient());
            ps.setString(3, a.getPhone());
            ps.setString(4, a.getStreet());
            ps.setString(5, a.getDistrict());
            ps.setString(6, a.getCity());
            ps.setBoolean(7, a.isDefault());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) a.setAddressId(keys.getInt(1));
        }
        return a;
    }

    public boolean update(Address a) throws SQLException {
        String sql = """
            UPDATE Addresses SET recipient=?, phone=?, street=?, district=?, city=?
            WHERE address_id=?
            """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, a.getRecipient());
            ps.setString(2, a.getPhone());
            ps.setString(3, a.getStreet());
            ps.setString(4, a.getDistrict());
            ps.setString(5, a.getCity());
            ps.setInt(6, a.getAddressId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Addresses WHERE address_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public void clearDefault(int userId) throws SQLException {
        String sql = "UPDATE Addresses SET is_default=0 WHERE user_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    public boolean setDefault(int addressId, int userId) throws SQLException {
        clearDefault(userId);
        String sql = "UPDATE Addresses SET is_default=1 WHERE address_id=? AND user_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, addressId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    private Address mapRow(ResultSet rs) throws SQLException {
        Address a = new Address();
        a.setAddressId(rs.getInt("address_id"));
        a.setUserId(rs.getInt("user_id"));
        a.setRecipient(rs.getString("recipient"));
        a.setPhone(rs.getString("phone"));
        a.setStreet(rs.getString("street"));
        a.setDistrict(rs.getString("district"));
        a.setCity(rs.getString("city"));
        a.setDefault(rs.getBoolean("is_default"));
        return a;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getConnection();
    }
}
