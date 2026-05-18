package com.ecommerce.ui;

import com.ecommerce.model.Shop;
import com.ecommerce.service.ShopService;
import com.ecommerce.util.InputUtil;
import com.ecommerce.util.SessionManager;

import java.sql.SQLException;
import java.util.List;

public class ShopMenu {

    private final ShopService service = new ShopService();

    public void show() {
        if (SessionManager.isAdmin()) showAdminMenu();
        else showSellerMenu();
    }

    private void showSellerMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║   🏪  GIAN HÀNG CỦA TÔI          ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Xem thông tin gian hàng      ║");
            System.out.println("║  2. Đăng ký mở gian hàng         ║");
            System.out.println("║  3. Cập nhật gian hàng           ║");
            System.out.println("║  4. Đóng / Mở gian hàng          ║");
            System.out.println("║  0. Quay lại                     ║");
            System.out.println("╚══════════════════════════════════╝");
            int choice = InputUtil.readInt("Lựa chọn: ");
            try {
                switch (choice) {
                    case 1 -> viewMyShop();
                    case 2 -> registerShop();
                    case 3 -> updateShop();
                    case 4 -> toggleShop();
                    case 0 -> running = false;
                    default -> System.out.println("⚠ Lựa chọn không hợp lệ.");
                }
            } catch (Exception e) { System.out.println("⚠ " + e.getMessage()); }
        }
    }

    private void viewMyShop() throws SQLException {
        Shop s = service.getMyShop();
        if (s == null) { System.out.println("  Bạn chưa có gian hàng."); return; }
        System.out.println("\n─── THÔNG TIN GIAN HÀNG ───────────");
        System.out.println("  ID       : " + s.getShopId());
        System.out.println("  Tên      : " + s.getName());
        System.out.println("  Mô tả    : " + s.getDescription());
        System.out.println("  Địa chỉ  : " + s.getAddress());
        System.out.println("  Trạng thái: " + (s.isActive() ? "Đang mở" : "Đã đóng"));
        InputUtil.pressEnter();
    }

    private void registerShop() throws SQLException {
        System.out.println("\n─── ĐĂNG KÝ GIAN HÀNG ─────────────");
        String name = InputUtil.readLine("  Tên gian hàng: ");
        String desc = InputUtil.readLine("  Mô tả        : ");
        String addr = InputUtil.readLine("  Địa chỉ      : ");
        Shop s = service.register(name, desc, addr);
        System.out.println("✅ Đã đăng ký gian hàng ID: " + s.getShopId());
    }

    private void updateShop() throws SQLException {
        String name = InputUtil.readLine("  Tên mới  : ");
        String desc = InputUtil.readLine("  Mô tả mới: ");
        String addr = InputUtil.readLine("  Địa chỉ  : ");
        service.update(name, desc, addr);
        System.out.println("✅ Đã cập nhật gian hàng.");
    }

    private void toggleShop() throws SQLException {
        System.out.println("  1. Đóng gian hàng   2. Mở gian hàng");
        int c = InputUtil.readInt("  Lựa chọn: ");
        if (c == 1) { service.closeShop(); System.out.println("✅ Đã đóng gian hàng."); }
        else        { service.openShop();  System.out.println("✅ Đã mở gian hàng."); }
    }

    private void showAdminMenu() {
        try {
            List<Shop> shops = service.getAll();
            System.out.println("\n─── TẤT CẢ GIAN HÀNG (" + shops.size() + ") ────────");
            shops.forEach(s -> System.out.println("  " + s));
            System.out.println("\n  Khóa/mở gian hàng? Nhập ID (0 = bỏ qua):");
            int id = InputUtil.readInt("  ID: ");
            if (id > 0) {
                System.out.println("  1. Đóng   2. Mở");
                int c = InputUtil.readInt("  Lựa chọn: ");
                service.adminToggle(id, c == 2);
                System.out.println("✅ Đã cập nhật.");
            }
        } catch (Exception e) { System.out.println("⚠ " + e.getMessage()); }
    }
}
