package com.ecommerce.dao;

import com.ecommerce.config.DatabaseConnection;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public Order save(Order o, Connection conn) throws SQLException {
        String sql = """
            INSERT INTO Orders (buyer_id, address_id, total, status, note)
            VALUES (?,?,?,?,?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, o.getBuyerId());
            ps.setInt(2, o.getAddressId());
            ps.setBigDecimal(3, o.getTotal());
            ps.setString(4, o.getStatus());
            ps.setString(5, o.getNote());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) o.setOrderId(keys.getInt(1));
        }
        return o;
    }

    public void saveItems(List<OrderItem> items, Connection conn) throws SQLException {
        String sql = "INSERT INTO OrderItems (order_id, product_id, quantity, unit_price) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (OrderItem item : items) {
                ps.setInt(1, item.getOrderId());
                ps.setInt(2, item.getProductId());
                ps.setInt(3, item.getQuantity());
                ps.setBigDecimal(4, item.getUnitPrice());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public List<Order> findByBuyerId(int buyerId) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE buyer_id=? ORDER BY created_at DESC";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, buyerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Order> findByShopId(int shopId) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = """
            SELECT DISTINCT o.* FROM Orders o
            JOIN OrderItems oi ON o.order_id = oi.order_id
            JOIN Products p ON oi.product_id = p.product_id
            WHERE p.shop_id=?
            ORDER BY o.created_at DESC
            """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, shopId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Order findById(int orderId) throws SQLException {
        String sql = "SELECT * FROM Orders WHERE order_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Order o = mapRow(rs);
                o.setItems(findItemsByOrderId(orderId));
                return o;
            }
        }
        return null;
    }

    public List<OrderItem> findItemsByOrderId(int orderId) throws SQLException {
        List<OrderItem> list = new ArrayList<>();
        String sql = """
            SELECT oi.*, p.name AS product_name FROM OrderItems oi
            JOIN Products p ON oi.product_id = p.product_id
            WHERE oi.order_id=?
            """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setOrderItemId(rs.getInt("order_item_id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnitPrice(rs.getBigDecimal("unit_price"));
                item.setProductName(rs.getString("product_name"));
                list.add(item);
            }
        }
        return list;
    }

    public boolean updateStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE Orders SET status=? WHERE order_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    private Order mapRow(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
        o.setBuyerId(rs.getInt("buyer_id"));
        o.setAddressId(rs.getInt("address_id"));
        o.setTotal(rs.getBigDecimal("total"));
        o.setStatus(rs.getString("status"));
        o.setNote(rs.getString("note"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) o.setCreatedAt(ts.toLocalDateTime());
        return o;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getConnection();
    }
}
