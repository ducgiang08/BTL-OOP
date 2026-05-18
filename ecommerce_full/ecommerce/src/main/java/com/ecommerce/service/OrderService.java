package com.ecommerce.service;

import com.ecommerce.config.DatabaseConnection;
import com.ecommerce.dao.*;
import com.ecommerce.model.*;
import com.ecommerce.util.SessionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final OrderDAO   orderDAO   = new OrderDAO();
    private final CartDAO    cartDAO    = new CartDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    /**
     * UC8: Checkout — tạo đơn hàng từ giỏ hàng.
     * Dùng transaction để đảm bảo tính toàn vẹn dữ liệu.
     */
    public Order checkout(int addressId, String note, String paymentMethod) throws SQLException {
        requireBuyer();
        int userId = SessionManager.getCurrentUser().getUserId();
        List<CartItem> cart = cartDAO.findByUserId(userId);
        if (cart.isEmpty()) throw new IllegalStateException("Giỏ hàng trống.");

        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            // Tính tổng tiền và tạo order items
            BigDecimal total = BigDecimal.ZERO;
            List<OrderItem> items = new ArrayList<>();

            for (CartItem ci : cart) {
                Product p = productDAO.findById(ci.getProductId());
                if (p == null || !p.isActive())
                    throw new IllegalStateException("Sản phẩm '" + ci.getProductName() + "' không còn bán.");
                if (ci.getQuantity() > p.getStock())
                    throw new IllegalStateException("Sản phẩm '" + p.getName()
                            + "' chỉ còn " + p.getStock() + " trong kho.");

                OrderItem item = new OrderItem(0, p.getProductId(), ci.getQuantity(), p.getPrice());
                items.add(item);
                total = total.add(p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));

                // Trừ tồn kho
                productDAO.updateStock(p.getProductId(), p.getStock() - ci.getQuantity(), conn);
            }

            // Tạo Order
            Order order = new Order(userId, addressId, total, note);
            orderDAO.save(order, conn);

            // Gán order_id cho từng item rồi lưu
            for (OrderItem item : items) item.setOrderId(order.getOrderId());
            orderDAO.saveItems(items, conn);

            // Tạo Payment
            Payment payment = new Payment(order.getOrderId(), total, paymentMethod);
            paymentDAO.save(payment, conn);

            // Xóa giỏ hàng
            cartDAO.deleteAllByUser(userId);

            conn.commit();
            order.setItems(items);
            return order;

        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public List<Order> getMyOrders() throws SQLException {
        requireBuyer();
        return orderDAO.findByBuyerId(SessionManager.getCurrentUser().getUserId());
    }

    public Order getOrderDetail(int orderId) throws SQLException {
        return orderDAO.findById(orderId);
    }

    public void cancelOrder(int orderId) throws SQLException {
        requireBuyer();
        Order o = orderDAO.findById(orderId);
        if (o == null) throw new IllegalArgumentException("Không tìm thấy đơn hàng.");
        if (!o.getStatus().equals("PENDING"))
            throw new IllegalStateException("Chỉ hủy được đơn hàng ở trạng thái PENDING.");
        orderDAO.updateStatus(orderId, "CANCELLED");
    }

    // Seller: xem đơn của gian hàng mình
    public List<Order> getShopOrders(int shopId) throws SQLException {
        return orderDAO.findByShopId(shopId);
    }

    public void updateStatus(int orderId, String status) throws SQLException {
        if (!SessionManager.isSeller() && !SessionManager.isAdmin())
            throw new SecurityException("Không có quyền cập nhật trạng thái.");
        orderDAO.updateStatus(orderId, status);
    }

    private void requireBuyer() {
        if (!SessionManager.isLoggedIn())
            throw new IllegalStateException("Bạn chưa đăng nhập.");
    }
}
