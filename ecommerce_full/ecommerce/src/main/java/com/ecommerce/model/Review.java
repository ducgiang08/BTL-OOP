package com.ecommerce.model;

import java.time.LocalDateTime;

public class Review {
    private int           reviewId;
    private int           productId;
    private int           userId;
    private int           rating; // 1-5
    private String        comment;
    private LocalDateTime createdAt;
    private String        userName;    // join phụ
    private String        productName; // join phụ

    public Review() {}

    public Review(int productId, int userId, int rating, String comment) {
        this.productId = productId;
        this.userId    = userId;
        this.rating    = rating;
        this.comment   = comment;
    }

    public int           getReviewId()              { return reviewId; }
    public void          setReviewId(int v)         { this.reviewId = v; }
    public int           getProductId()             { return productId; }
    public void          setProductId(int v)        { this.productId = v; }
    public int           getUserId()                { return userId; }
    public void          setUserId(int v)           { this.userId = v; }
    public int           getRating()                { return rating; }
    public void          setRating(int v)           { this.rating = v; }
    public String        getComment()               { return comment; }
    public void          setComment(String v)       { this.comment = v; }
    public LocalDateTime getCreatedAt()             { return createdAt; }
    public void          setCreatedAt(LocalDateTime v){ this.createdAt = v; }
    public String        getUserName()              { return userName; }
    public void          setUserName(String v)      { this.userName = v; }
    public String        getProductName()           { return productName; }
    public void          setProductName(String v)   { this.productName = v; }

    private String stars() {
        return "★".repeat(rating) + "☆".repeat(5 - rating);
    }

    @Override
    public String toString() {
        return String.format("%s %s: %s", stars(),
                userName == null ? "Ẩn danh" : userName, comment);
    }
}
