package com.ecommerce.ui;

import com.ecommerce.model.Address;
import com.ecommerce.model.Order;
import com.ecommerce.service.AddressService;
import com.ecommerce.service.OrderService;
import com.ecommerce.util.InputUtil;
import com.ecommerce.util.SessionManager;

import java.sql.SQLException;
import java.util.List;

public class OrderMenu {

    private final OrderService   orderService   = new OrderService();
    private final AddressService addressService = new AddressService();

    public void show() {
        if (SessionManager.isSeller()) showSellerMenu();
        else showBuyerMenu();
    }

    private void showBuyerMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║   📋  ĐƠN HÀNG                   ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Đặt hàng (Checkout)          ║");
            System.out.println("║  2. Lịch sử đơn hàng             ║");
            System.out.println("║  3. Chi tiết đơn hàng            ║");
            System.out.println("║  4. Hủy đơn hàng                 ║");
            System.out.println("║  0. Quay lại                     ║");
            System.out.println("╚══════════════════════════════════╝");
            int choice = InputUtil.readInt("Lựa chọn: ");
            try {
                switch (choice) {
                    case 1 -> checkout();
                    case 2 -> listOrders();
                    case 3 -> viewDetail();
                    case 4 -> cancelOrder();
                    case 0 -> running = false;
                    default -> System.out.println("⚠ Lựa chọn không hợp lệ.");
                }
            } catch (Exception e) { System.out.println("⚠ " + e.getMessage()); }
        }
    }

    private void checkout() throws SQLException {
        // Hiển thị địa chỉ
        List<Address> addresses = addressService.getMyAddresses();
        if (addresses.isEmpty()) {
            System.out.println("⚠ Bạn chưa có địa chỉ nhận hàng. Vui lòng thêm địa chỉ trước.");
            return;
        }
        System.out.println("\n─── CHỌN ĐỊA CHỈ NHẬN HÀNG ────────");
        addresses.forEach(a -> System.out.println("  " + a));
        int addressId = InputUtil.readInt("  ID địa chỉ: ");

        // Phương thức thanh toán
        System.out.println("  Phương thức thanh toán:");
        System.out.println("    1. Tiền mặt (CASH)");
        System.out.println("    2. Chuyển khoản (BANK_TRANSFER)");
        System.out.println("    3. Ví điện tử (E_WALLET)");
        int pm = InputUtil.readInt("  Lựa chọn: ");
        String method = switch (pm) {
            case 2 -> "BANK_TRANSFER";
            case 3 -> "E_WALLET";
            default -> "CASH";
        };

        String note = InputUtil.readLine("  Ghi chú (Enter để bỏ qua): ");
        Order order = orderService.checkout(addressId, note.isBlank() ? null : note, method);
        System.out.printf("✅ Đặt hàng thành công! Đơn #%d – Tổng: %,.0f VND%n",
                order.getOrderId(), order.getTotal());
    }

    private void listOrders() throws SQLException {
        List<Order> orders = orderService.getMyOrders();
        System.out.println("\n─── LỊCH SỬ ĐƠN HÀNG (" + orders.size() + ") ────────");
        if (orders.isEmpty()) System.out.println("  Chưa có đơn hàng.");
        else orders.forEach(o -> System.out.println("  " + o));
        InputUtil.pressEnter();
    }

    private void viewDetail() throws SQLException {
        int id = InputUtil.readInt("  ID đơn hàng: ");
        Order o = orderService.getOrderDetail(id);
        if (o == null) { System.out.println("⚠ Không tìm thấy đơn hàng."); return; }
        System.out.println("\n─── CHI TIẾT ĐƠN #" + o.getOrderId() + " ─────────────");
        System.out.println("  Trạng thái: " + o.getStatus());
        System.out.printf("  Tổng tiền : %,.0f VND%n", o.getTotal());
        System.out.println("  Ghi chú   : " + o.getNote());
        System.out.println("  Ngày tạo  : " + o.getCreatedAt());
        System.out.println("  Sản phẩm:");
        if (o.getItems() != null) o.getItems().forEach(System.out::println);
        InputUtil.pressEnter();
    }

    private void cancelOrder() throws SQLException {
        int id = InputUtil.readInt("  ID đơn hàng cần hủy: ");
        orderService.cancelOrder(id);
        System.out.println("✅ Đã hủy đơn hàng.");
    }

    // ── Seller ────────────────────────────────────────────────────────────────

    private void showSellerMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║   📋  ĐƠN HÀNG [SELLER]          ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Xem đơn hàng khách đặt       ║");
            System.out.println("║  2. Cập nhật trạng thái đơn      ║");
            System.out.println("║  0. Quay lại                     ║");
            System.out.println("╚══════════════════════════════════╝");
            int choice = InputUtil.readInt("Lựa chọn: ");
            try {
                switch (choice) {
                    case 1 -> listShopOrders();
                    case 2 -> updateStatus();
                    case 0 -> running = false;
                    default -> System.out.println("⚠ Lựa chọn không hợp lệ.");
                }
            } catch (Exception e) { System.out.println("⚠ " + e.getMessage()); }
        }
    }

    private void listShopOrders() throws SQLException {
        int shopId = InputUtil.readInt("  ID gian hàng: ");
        List<Order> orders = orderService.getShopOrders(shopId);
        System.out.println("\n─── ĐƠN HÀNG GIAN HÀNG (" + orders.size() + ") ────────");
        orders.forEach(o -> System.out.println("  " + o));
        InputUtil.pressEnter();
    }

    private void updateStatus() throws SQLException {
        int id = InputUtil.readInt("  ID đơn hàng: ");
        System.out.println("  Trạng thái: PENDING / CONFIRMED / SHIPPED / DELIVERED / CANCELLED");
        String status = InputUtil.readLine("  Trạng thái mới: ").toUpperCase();
        orderService.updateStatus(id, status);
        System.out.println("✅ Đã cập nhật trạng thái.");
    }
}
