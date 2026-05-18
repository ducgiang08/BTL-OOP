package com.ecommerce.ui;

import com.ecommerce.util.InputUtil;
import com.ecommerce.util.SessionManager;

public class MainMenu {

    private final AuthMenu     authMenu     = new AuthMenu();
    private final UserMenu     userMenu     = new UserMenu();
    private final CategoryMenu categoryMenu = new CategoryMenu();
    private final ProductMenu  productMenu  = new ProductMenu();
    private final CartMenu     cartMenu     = new CartMenu();
    private final OrderMenu    orderMenu    = new OrderMenu();
    private final ShopMenu     shopMenu     = new ShopMenu();
    private final ReviewMenu   reviewMenu   = new ReviewMenu();
    private final AddressMenu  addressMenu  = new AddressMenu();

    public void run() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║    🛒  HỆ THỐNG THƯƠNG MẠI ĐIỆN TỬ    ║");
        System.out.println("╚════════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            if (!SessionManager.isLoggedIn()) {
                boolean loggedIn = authMenu.showWelcomeMenu();
                if (!loggedIn) { running = false; continue; }
            }
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
            System.out.println("║  2.  Quản lý Danh mục (UC4)              ║");
            System.out.println("║  3.  Sản phẩm (UC5 & UC6)                ║");
            System.out.println("║  4.  Giỏ hàng (UC7)                      ║");
            System.out.println("║  5.  Đơn hàng (UC8)                      ║");
            System.out.println("║  6.  Gian hàng (UC10)                    ║");
            System.out.println("║  7.  Đánh giá (UC11)                     ║");
            System.out.println("║  8.  Địa chỉ nhận hàng (UC12)            ║");
            System.out.println("║  0.  Đăng xuất                           ║");
            System.out.println("╚══════════════════════════════════════════╝");

            int choice = InputUtil.readInt("Lựa chọn: ");
            switch (choice) {
                case 1 -> userMenu.show();
                case 2 -> categoryMenu.show();
                case 3 -> productMenu.show();
                case 4 -> cartMenu.show();
                case 5 -> orderMenu.show();
                case 6 -> shopMenu.show();
                case 7 -> reviewMenu.show();
                case 8 -> addressMenu.show();
                case 0 -> { authMenu.logout(); inSession = false; }
                default -> System.out.println("⚠ Lựa chọn không hợp lệ.");
            }
        }
    }
}
