package com.ecommerce.ui;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.model.Review;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.ReviewService;
import com.ecommerce.util.InputUtil;
import com.ecommerce.util.SessionManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProductMenu {

    private final ProductService  productService  = new ProductService();
    private final CategoryService categoryService = new CategoryService();
    private final ReviewService   reviewService   = new ReviewService();

    public void show() {
        if (SessionManager.isSeller()) showSellerMenu();
        else showBuyerMenu();
    }

    // ── Buyer / Guest ─────────────────────────────────────────────────────────

    private void showBuyerMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║   🛍  SẢN PHẨM                   ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Xem tất cả sản phẩm          ║");
            System.out.println("║  2. Tìm kiếm sản phẩm            ║");
            System.out.println("║  3. Xem chi tiết sản phẩm        ║");
            System.out.println("║  0. Quay lại                     ║");
            System.out.println("╚══════════════════════════════════╝");
            int choice = InputUtil.readInt("Lựa chọn: ");
            try {
                switch (choice) {
                    case 1 -> listAll();
                    case 2 -> search();
                    case 3 -> viewDetail();
                    case 0 -> running = false;
                    default -> System.out.println("⚠ Lựa chọn không hợp lệ.");
                }
            } catch (Exception e) { System.out.println("⚠ " + e.getMessage()); }
        }
    }

    private void listAll() throws SQLException {
        List<Product> list = productService.getAll();
        printProductList(list);
    }

    private void search() throws SQLException {
        System.out.println("\n─── TÌM KIẾM SẢN PHẨM ─────────────");
        String keyword = InputUtil.readLine("  Từ khóa (Enter để bỏ qua): ");
        System.out.println("  Lọc theo danh mục? (0 = bỏ qua)");
        int catId = InputUtil.readInt("  ID danh mục: ");
        String minStr = InputUtil.readLine("  Giá tối thiểu (Enter = 0): ");
        String maxStr = InputUtil.readLine("  Giá tối đa   (Enter = bỏ qua): ");
        double min = minStr.isBlank() ? 0 : Double.parseDouble(minStr);
        double max = maxStr.isBlank() ? 0 : Double.parseDouble(maxStr);
        List<Product> list = productService.search(keyword, catId, min, max);
        printProductList(list);
    }

    private void viewDetail() throws SQLException {
        int id = InputUtil.readInt("  ID sản phẩm: ");
        Product p = productService.getById(id);
        System.out.println("\n─── CHI TIẾT SẢN PHẨM ─────────────");
        System.out.println("  Tên      : " + p.getName());
        System.out.println("  Danh mục : " + p.getCategoryName());
        System.out.println("  Gian hàng: " + p.getShopName());
        System.out.println("  Giá      : " + String.format("%,.0f VND", p.getPrice()));
        System.out.println("  Tồn kho  : " + p.getStock());
        System.out.println("  Mô tả    : " + p.getDescription());

        List<Review> reviews = reviewService.getByProduct(id);
        System.out.println("\n  ─── Đánh giá (" + reviews.size() + ") ───");
        if (reviews.isEmpty()) System.out.println("  Chưa có đánh giá.");
        else reviews.forEach(r -> System.out.println("  " + r));
        InputUtil.pressEnter();
    }

    private void printProductList(List<Product> list) {
        System.out.println("\n─── KẾT QUẢ (" + list.size() + " sản phẩm) ────────────");
        if (list.isEmpty()) { System.out.println("  Không có sản phẩm nào."); }
        else list.forEach(p -> System.out.println("  " + p));
        InputUtil.pressEnter();
    }

    // ── Seller ────────────────────────────────────────────────────────────────

    private void showSellerMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║   📦  QUẢN LÝ SẢN PHẨM           ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Sản phẩm của tôi             ║");
            System.out.println("║  2. Thêm sản phẩm                ║");
            System.out.println("║  3. Sửa sản phẩm                 ║");
            System.out.println("║  4. Ẩn / Hiện sản phẩm           ║");
            System.out.println("║  0. Quay lại                     ║");
            System.out.println("╚══════════════════════════════════╝");
            int choice = InputUtil.readInt("Lựa chọn: ");
            try {
                switch (choice) {
                    case 1 -> listMyProducts();
                    case 2 -> addProduct();
                    case 3 -> editProduct();
                    case 4 -> toggleProduct();
                    case 0 -> running = false;
                    default -> System.out.println("⚠ Lựa chọn không hợp lệ.");
                }
            } catch (Exception e) { System.out.println("⚠ " + e.getMessage()); }
        }
    }

    private void listMyProducts() throws SQLException {
        List<Product> list = productService.getMyShopProducts();
        printProductList(list);
    }

    private void addProduct() throws SQLException {
        System.out.println("\n─── THÊM SẢN PHẨM ─────────────────");
        showCategories();
        int catId    = InputUtil.readInt("  ID danh mục   : ");
        String name  = InputUtil.readLine("  Tên sản phẩm  : ");
        String desc  = InputUtil.readLine("  Mô tả         : ");
        double price = InputUtil.readDouble("  Giá (VND)    : ");
        int stock    = InputUtil.readInt("  Số lượng kho  : ");
        Product p = productService.add(catId, name, desc, BigDecimal.valueOf(price), stock);
        System.out.println("✅ Đã thêm sản phẩm ID: " + p.getProductId());
    }

    private void editProduct() throws SQLException {
        int id = InputUtil.readInt("  ID sản phẩm  : ");
        showCategories();
        int catId    = InputUtil.readInt("  ID danh mục  : ");
        String name  = InputUtil.readLine("  Tên mới      : ");
        String desc  = InputUtil.readLine("  Mô tả mới    : ");
        double price = InputUtil.readDouble("  Giá mới    : ");
        int stock    = InputUtil.readInt("  Tồn kho mới  : ");
        productService.update(id, catId, name, desc, BigDecimal.valueOf(price), stock);
        System.out.println("✅ Đã cập nhật sản phẩm.");
    }

    private void toggleProduct() throws SQLException {
        int id = InputUtil.readInt("  ID sản phẩm: ");
        System.out.println("  1. Ẩn   2. Hiện");
        int c = InputUtil.readInt("  Lựa chọn: ");
        productService.toggleVisible(id, c == 2);
        System.out.println("✅ Đã cập nhật.");
    }

    private void showCategories() throws SQLException {
        List<Category> cats = categoryService.getAllActive();
        System.out.println("  Danh mục hiện có:");
        cats.forEach(c -> System.out.println("    [" + c.getCategoryId() + "] " + c.getName()));
    }
}
