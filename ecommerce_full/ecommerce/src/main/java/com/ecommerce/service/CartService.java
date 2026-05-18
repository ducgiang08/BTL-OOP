package com.ecommerce.service;

import com.ecommerce.dao.CartDAO;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.util.SessionManager;

import java.sql.SQLException;
import java.util.List;

public class CartService {

    private final CartDAO    cartDAO    = new CartDAO();
    private final ProductDAO productDAO = new ProductDAO();

    public List<CartItem> getMyCart() throws SQLException {
        requireBuyer();
        return cartDAO.findByUserId(SessionManager.getCurrentUser().getUserId());
    }

    public void addToCart(int productId, int quantity) throws SQLException {
        requireBuyer();
        if (quantity <= 0) throw new IllegalArgumentException("Số lượng phải >= 1.");

        Product product = productDAO.findById(productId);
        if (product == null) throw new IllegalArgumentException("Sản phẩm không tồn tại.");
        if (!product.isActive()) throw new IllegalStateException("Sản phẩm đã ngừng bán.");

        int userId = SessionManager.getCurrentUser().getUserId();
        CartItem existing = cartDAO.findByUserAndProduct(userId, productId);

        if (existing != null) {
            int newQty = existing.getQuantity() + quantity;
            if (newQty > product.getStock())
                throw new IllegalStateException("Vượt quá số lượng tồn kho (" + product.getStock() + ").");
            cartDAO.updateQuantity(existing.getCartItemId(), newQty);
        } else {
            if (quantity > product.getStock())
                throw new IllegalStateException("Vượt quá số lượng tồn kho (" + product.getStock() + ").");
            cartDAO.save(new CartItem(userId, productId, quantity));
        }
    }

    public void updateQuantity(int cartItemId, int quantity) throws SQLException {
        requireBuyer();
        if (quantity <= 0) throw new IllegalArgumentException("Số lượng phải >= 1.");
        cartDAO.updateQuantity(cartItemId, quantity);
    }

    public void removeItem(int cartItemId) throws SQLException {
        requireBuyer();
        cartDAO.delete(cartItemId);
    }

    public void clearCart() throws SQLException {
        requireBuyer();
        cartDAO.deleteAllByUser(SessionManager.getCurrentUser().getUserId());
    }

    private void requireBuyer() {
        if (!SessionManager.isLoggedIn())
            throw new IllegalStateException("Bạn chưa đăng nhập.");
        if (SessionManager.isAdmin())
            throw new SecurityException("Admin không có giỏ hàng.");
    }
}
