package com.ecommerce.dao;

import com.ecommerce.config.DatabaseConnection;
import com.ecommerce.model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public Payment save(Payment p, Connection conn) throws SQLException {
        String sql = "INSERT INTO Payments (order_id, amount, method, status) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getOrderId());
            ps.setBigDecimal(2, p.getAmount());
            ps.setString(3, p.getMethod());
            ps.setString(4, p.getStatus());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) p.setPaymentId(keys.getInt(1));
        }
        return p;
    }

    public Payment findByOrderId(int orderId) throws SQLException {
        String sql = "SELECT * FROM Payments WHERE order_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<Payment> findByBuyerId(int buyerId) throws SQLException {
        List<Payment> list = new ArrayList<>();
        String sql = """
            SELECT p.* FROM Payments p
            JOIN Orders o ON p.order_id = o.order_id
            WHERE o.buyer_id=? ORDER BY p.payment_id DESC
            """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, buyerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public boolean updateStatus(int paymentId, String status) throws SQLException {
        String sql = "UPDATE Payments SET status=?, paid_at=? WHERE payment_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setTimestamp(2, "COMPLETED".equals(status) ? new Timestamp(System.currentTimeMillis()) : null);
            ps.setInt(3, paymentId);
            return ps.executeUpdate() > 0;
        }
    }

    private Payment mapRow(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("payment_id"));
        p.setOrderId(rs.getInt("order_id"));
        p.setAmount(rs.getBigDecimal("amount"));
        p.setMethod(rs.getString("method"));
        p.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("paid_at");
        if (ts != null) p.setPaidAt(ts.toLocalDateTime());
        return p;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getConnection();
    }
}
