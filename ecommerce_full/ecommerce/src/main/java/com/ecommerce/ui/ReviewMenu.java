package com.ecommerce.ui;

import com.ecommerce.model.Review;
import com.ecommerce.service.ReviewService;
import com.ecommerce.util.InputUtil;

import java.sql.SQLException;
import java.util.List;

public class ReviewMenu {

    private final ReviewService service = new ReviewService();

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║   ⭐  ĐÁNH GIÁ SẢN PHẨM          ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Xem đánh giá sản phẩm        ║");
            System.out.println("║  2. Thêm đánh giá                ║");
            System.out.println("║  3. Sửa đánh giá của tôi         ║");
            System.out.println("║  4. Xóa đánh giá của tôi         ║");
            System.out.println("║  0. Quay lại                     ║");
            System.out.println("╚══════════════════════════════════╝");
            int choice = InputUtil.readInt("Lựa chọn: ");
            try {
                switch (choice) {
                    case 1 -> viewReviews();
                    case 2 -> addReview();
                    case 3 -> editReview();
                    case 4 -> deleteReview();
                    case 0 -> running = false;
                    default -> System.out.println("⚠ Lựa chọn không hợp lệ.");
                }
            } catch (Exception e) { System.out.println("⚠ " + e.getMessage()); }
        }
    }

    private void viewReviews() throws SQLException {
        int productId = InputUtil.readInt("  ID sản phẩm: ");
        List<Review> list = service.getByProduct(productId);
        System.out.println("\n─── ĐÁNH GIÁ (" + list.size() + ") ──────────────────");
        if (list.isEmpty()) System.out.println("  Chưa có đánh giá.");
        else list.forEach(r -> System.out.println("  " + r));
        InputUtil.pressEnter();
    }

    private void addReview() throws SQLException {
        int productId = InputUtil.readInt("  ID sản phẩm : ");
        int rating    = InputUtil.readInt("  Điểm (1-5)  : ");
        String comment = InputUtil.readLine("  Nhận xét    : ");
        service.addReview(productId, rating, comment);
        System.out.println("✅ Đã gửi đánh giá.");
    }

    private void editReview() throws SQLException {
        int reviewId  = InputUtil.readInt("  ID đánh giá : ");
        int rating    = InputUtil.readInt("  Điểm mới    : ");
        String comment = InputUtil.readLine("  Nhận xét mới: ");
        service.updateReview(reviewId, rating, comment);
        System.out.println("✅ Đã cập nhật đánh giá.");
    }

    private void deleteReview() throws SQLException {
        int reviewId = InputUtil.readInt("  ID đánh giá cần xóa: ");
        service.deleteReview(reviewId);
        System.out.println("✅ Đã xóa đánh giá.");
    }
}
