package com.ecommerce.ui;

import com.ecommerce.model.CartItem;
import com.ecommerce.service.CartService;
import com.ecommerce.util.InputUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CartMenu {

    private final CartService service = new CartService();

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║   🛒  GIỎ HÀNG                   ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Xem giỏ hàng                 ║");
            System.out.println("║  2. Thêm sản phẩm vào giỏ        ║");
            System.out.println("║  3. Cập nhật số lượng            ║");
            System.out.println("║  4. Xóa sản phẩm khỏi giỏ       ║");
            System.out.println("║  5. Xóa toàn bộ giỏ hàng        ║");
            System.out.println("║  0. Quay lại                     ║");
            System.out.println("╚══════════════════════════════════╝");
            int choice = InputUtil.readInt("Lựa chọn: ");
            try {
                switch (choice) {
                    case 1 -> viewCart();
                    case 2 -> addItem();
                    case 3 -> updateQty();
                    case 4 -> removeItem();
                    case 5 -> clearCart();
                    case 0 -> running = false;
                    default -> System.out.println("⚠ Lựa chọn không hợp lệ.");
                }
            } catch (Exception e) { System.out.println("⚠ " + e.getMessage()); }
        }
    }

    private void viewCart() throws SQLException {
        List<CartItem> cart = service.getMyCart();
        System.out.println("\n─── GIỎ HÀNG CỦA TÔI (" + cart.size() + " sản phẩm) ───");
        if (cart.isEmpty()) { System.out.println("  Giỏ hàng trống."); InputUtil.pressEnter(); return; }
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cart) {
            System.out.printf("  [%d] %s%n", ci.getCartItemId(), ci);
            total = total.add(ci.getSubtotal());
        }
        System.out.printf("  ─────────────────────────────%n");
        System.out.printf("  TỔNG CỘNG: %,.0f VND%n", total);
        InputUtil.pressEnter();
    }

    private void addItem() throws SQLException {
        int productId = InputUtil.readInt("  ID sản phẩm: ");
        int qty       = InputUtil.readInt("  Số lượng   : ");
        service.addToCart(productId, qty);
        System.out.println("✅ Đã thêm vào giỏ hàng.");
    }

    private void updateQty() throws SQLException {
        int cartItemId = InputUtil.readInt("  ID mục giỏ hàng: ");
        int qty        = InputUtil.readInt("  Số lượng mới   : ");
        service.updateQuantity(cartItemId, qty);
        System.out.println("✅ Đã cập nhật.");
    }

    private void removeItem() throws SQLException {
        int cartItemId = InputUtil.readInt("  ID mục cần xóa: ");
        service.removeItem(cartItemId);
        System.out.println("✅ Đã xóa.");
    }

    private void clearCart() throws SQLException {
        String confirm = InputUtil.readLine("  Xóa toàn bộ giỏ hàng? (y/n): ");
        if ("y".equalsIgnoreCase(confirm)) {
            service.clearCart();
            System.out.println("✅ Đã xóa giỏ hàng.");
        }
    }
}
