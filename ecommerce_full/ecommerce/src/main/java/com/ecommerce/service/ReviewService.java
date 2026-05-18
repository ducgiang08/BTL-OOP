package com.ecommerce.service;

import com.ecommerce.dao.ReviewDAO;
import com.ecommerce.model.Review;
import com.ecommerce.util.SessionManager;

import java.sql.SQLException;
import java.util.List;

public class ReviewService {

    private final ReviewDAO dao = new ReviewDAO();

    public List<Review> getByProduct(int productId) throws SQLException {
        return dao.findByProductId(productId);
    }

    public void addReview(int productId, int rating, String comment) throws SQLException {
        requireLogin();
        if (rating < 1 || rating > 5)
            throw new IllegalArgumentException("Điểm đánh giá phải từ 1 đến 5.");
        int userId = SessionManager.getCurrentUser().getUserId();
        if (dao.findByUserAndProduct(userId, productId) != null)
            throw new IllegalStateException("Bạn đã đánh giá sản phẩm này rồi.");
        dao.save(new Review(productId, userId, rating, comment));
    }

    public void updateReview(int reviewId, int rating, String comment) throws SQLException {
        requireLogin();
        if (rating < 1 || rating > 5)
            throw new IllegalArgumentException("Điểm đánh giá phải từ 1 đến 5.");
        Review r = new Review();
        r.setReviewId(reviewId);
        r.setRating(rating);
        r.setComment(comment);
        dao.update(r);
    }

    public void deleteReview(int reviewId) throws SQLException {
        requireLogin();
        dao.delete(reviewId);
    }

    private void requireLogin() {
        if (!SessionManager.isLoggedIn())
            throw new IllegalStateException("Bạn chưa đăng nhập.");
    }
}
