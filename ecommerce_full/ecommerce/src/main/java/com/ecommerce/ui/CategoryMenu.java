package com.ecommerce.ui;

import com.ecommerce.model.Category;
import com.ecommerce.service.CategoryService;
import com.ecommerce.util.InputUtil;
import com.ecommerce.util.SessionManager;

import java.sql.SQLException;
import java.util.List;

public class CategoryMenu {

    private final CategoryService service = new CategoryService();

    public void show() {
        if (SessionManager.isAdmin()) showAdminMenu();
        else showPublicList();
    }

    private void showAdminMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║   📂  QUẢN LÝ DANH MỤC           ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Xem tất cả danh mục          ║");
            System.out.println("║  2. Thêm danh mục mới            ║");
            System.out.println("║  3. Sửa danh mục                 ║");
            System.out.println("║  4. Ẩn / Hiện danh mục           ║");
            System.out.println("║  0. Quay lại                     ║");
            System.out.println("╚══════════════════════════════════╝");
            int choice = InputUtil.readInt("Lựa chọn: ");
            try {
                switch (choice) {
                    case 1 -> listAll();
                    case 2 -> add();
                    case 3 -> edit();
                    case 4 -> toggle();
                    case 0 -> running = false;
                    default -> System.out.println("⚠ Lựa chọn không hợp lệ.");
                }
            } catch (Exception e) { System.out.println("⚠ " + e.getMessage()); }
        }
    }

    private void showPublicList() {
        try {
            List<Category> list = service.getAllActive();
            System.out.println("\n─── DANH MỤC SẢN PHẨM ─────────────");
            list.forEach(c -> System.out.println("  " + c));
            InputUtil.pressEnter();
        } catch (SQLException e) { System.out.println("❌ " + e.getMessage()); }
    }

    private void listAll() throws SQLException {
        List<Category> list = service.getAll();
        System.out.println("\n─── TẤT CẢ DANH MỤC (" + list.size() + ") ───────────");
        list.forEach(c -> System.out.println("  " + c));
        InputUtil.pressEnter();
    }

    private void add() throws SQLException {
        String name = InputUtil.readLine("  Tên danh mục: ");
        String desc = InputUtil.readLine("  Mô tả       : ");
        Category c = service.add(name, desc);
        System.out.println("✅ Đã thêm danh mục ID: " + c.getCategoryId());
    }

    private void edit() throws SQLException {
        int id      = InputUtil.readInt("  ID danh mục: ");
        String name = InputUtil.readLine("  Tên mới    : ");
        String desc = InputUtil.readLine("  Mô tả mới  : ");
        service.update(id, name, desc);
        System.out.println("✅ Đã cập nhật.");
    }

    private void toggle() throws SQLException {
        int id = InputUtil.readInt("  ID danh mục: ");
        System.out.println("  1. Ẩn   2. Hiện");
        int c = InputUtil.readInt("  Lựa chọn: ");
        if (c == 1) { service.hide(id); System.out.println("✅ Đã ẩn."); }
        else        { service.show(id); System.out.println("✅ Đã hiện."); }
    }
}
