package com.ecommerce.ui;

import com.ecommerce.util.InputUtil;
import com.ecommerce.util.SessionManager;

/**
 * Menu chính sau khi đăng nhập.
 * Điều phối sang các module của từng thành viên.
 *
 * ┌─────────────────────────────────────────────────────────┐
 * │  Vũ Đức Giang   – UC1(AuthMenu), UC2(AuthMenu), UC3    │
 * │  Lý Đình Đức    – UC4(CategoryMenu), UC5-6(ProductMenu)│
 * │  Trần Sách Trường– UC7(CartMenu), UC8-9(OrderMenu)     │
 * │  Đoàn Minh Tuấn – UC10(ShopMenu), UC11(ReviewMenu),   │
 * │                    UC12(AddressMenu)                    │
 * └─────────────────────────────────────────────────────────┘
 */
public class MainMenu {

    // ── Phần của Vũ Đức Giang ─────────────────────────────────────────────────
    private final AuthMenu authMenu = new AuthMenu();
    private final UserMenu userMenu = new UserMenu();

    // ── Phần của các thành viên khác (sẽ được merge vào) ─────────────────────
    // private final CategoryMenu categoryMenu = new CategoryMenu();
    // private final ProductMenu  productMenu  = new ProductMenu();
    // private final CartMenu     cartMenu     = new CartMenu();
    // private final OrderMenu    orderMenu    = new OrderMenu();
    // private final ShopMenu     shopMenu     = new ShopMenu();
    // private final ReviewMenu   reviewMenu   = new ReviewMenu();
    // private final AddressMenu  addressMenu  = new AddressMenu();

    public void run() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║    🛒  HỆ THỐNG THƯƠNG MẠI ĐIỆN TỬ    ║");
        System.out.println("╚════════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            // Nếu chưa đăng nhập → hiện welcome menu
            if (!SessionManager.isLoggedIn()) {
                boolean loggedIn = authMenu.showWelcomeMenu();
                if (!loggedIn) {
                    running = false;
                    continue;
                }
            }

            // Sau khi đăng nhập → hiện main menu
            showMainMenu();
        }

        System.out.println("Cảm ơn đã sử dụng hệ thống. Tạm biệt!");
    }

    private void showMainMenu() {
        boolean inSession = true;
        while (inSession && SessionManager.isLoggedIn()) {
            String role = SessionManager.getCurrentUser().getRole();
            System.out.println("\n╔══════════════════════════════════════════╗");
            System.out.printf( "║  Xin chào %-30s║%n",
                SessionManager.getCurrentUser().getFullName() + " [" + role + "]");
            System.out.println("╠══════════════════════════════════════════╣");
            System.out.println("║  1.  Thông tin cá nhân (UC3)             ║");
            System.out.println("║  ── Module của Lý Đình Đức ──────────────║");
            System.out.println("║  2.  Quản lý Danh mục (UC4)              ║");
            System.out.println("║  3.  Quản lý Sản phẩm (UC5)              ║");
            System.out.println("║  4.  Tìm kiếm Sản phẩm (UC6)             ║");
            System.out.println("║  ── Module của Trần Sách Trường ─────────║");
            System.out.println("║  5.  Giỏ hàng (UC7)                      ║");
            System.out.println("║  6.  Đơn hàng (UC8)                      ║");
            System.out.println("║  7.  Thanh toán (UC9)                    ║");
            System.out.println("║  ── Module của Đoàn Minh Tuấn ───────────║");
            System.out.println("║  8.  Gian hàng (UC10)                    ║");
            System.out.println("║  9.  Đánh giá (UC11)                     ║");
            System.out.println("║  10. Địa chỉ nhận hàng (UC12)            ║");
            System.out.println("║  ──────────────────────────────────────  ║");
            System.out.println("║  0.  Đăng xuất                           ║");
            System.out.println("╚══════════════════════════════════════════╝");

            int choice = InputUtil.readInt("Lựa chọn: ");
            switch (choice) {
                case 1  -> userMenu.show();
                // ── các module sau sẽ uncomment khi merge ──
                case 2  -> System.out.println("[Chờ Lý Đình Đức implement CategoryMenu]");
                case 3  -> System.out.println("[Chờ Lý Đình Đức implement ProductMenu]");
                case 4  -> System.out.println("[Chờ Lý Đình Đức implement SearchMenu]");
                case 5  -> System.out.println("[Chờ Trần Sách Trường implement CartMenu]");
                case 6  -> System.out.println("[Chờ Trần Sách Trường implement OrderMenu]");
                case 7  -> System.out.println("[Chờ Trần Sách Trường implement PaymentMenu]");
                case 8  -> System.out.println("[Chờ Đoàn Minh Tuấn implement ShopMenu]");
                case 9  -> System.out.println("[Chờ Đoàn Minh Tuấn implement ReviewMenu]");
                case 10 -> System.out.println("[Chờ Đoàn Minh Tuấn implement AddressMenu]");
                case 0  -> {
                    authMenu.logout();
                    inSession = false;
                }
                default -> System.out.println("⚠ Lựa chọn không hợp lệ.");
            }
        }
    }
}
