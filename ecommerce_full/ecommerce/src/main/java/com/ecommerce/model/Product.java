package com.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
    private int          productId;
    private int          shopId;
    private int          categoryId;
    private String       name;
    private String       description;
    private BigDecimal   price;
    private int          stock;
    private boolean      isActive;
    private LocalDateTime createdAt;

    // Tên phụ (join từ bảng khác, không lưu DB)
    private String shopName;
    private String categoryName;

    public Product() {}

    public Product(int shopId, int categoryId, String name,
                   String description, BigDecimal price, int stock) {
        this.shopId      = shopId;
        this.categoryId  = categoryId;
        this.name        = name;
        this.description = description;
        this.price       = price;
        this.stock       = stock;
        this.isActive    = true;
    }

    public int          getProductId()              { return productId; }
    public void         setProductId(int v)         { this.productId = v; }
    public int          getShopId()                 { return shopId; }
    public void         setShopId(int v)            { this.shopId = v; }
    public int          getCategoryId()             { return categoryId; }
    public void         setCategoryId(int v)        { this.categoryId = v; }
    public String       getName()                   { return name; }
    public void         setName(String v)           { this.name = v; }
    public String       getDescription()            { return description; }
    public void         setDescription(String v)    { this.description = v; }
    public BigDecimal   getPrice()                  { return price; }
    public void         setPrice(BigDecimal v)      { this.price = v; }
    public int          getStock()                  { return stock; }
    public void         setStock(int v)             { this.stock = v; }
    public boolean      isActive()                  { return isActive; }
    public void         setActive(boolean v)        { this.isActive = v; }
    public LocalDateTime getCreatedAt()             { return createdAt; }
    public void         setCreatedAt(LocalDateTime v){ this.createdAt = v; }
    public String       getShopName()               { return shopName; }
    public void         setShopName(String v)       { this.shopName = v; }
    public String       getCategoryName()           { return categoryName; }
    public void         setCategoryName(String v)   { this.categoryName = v; }

    @Override
    public String toString() {
        return String.format("[%d] %-25s | %12s VND | Kho: %d | %s",
                productId, name, String.format("%,.0f", price),
                stock, isActive ? "Đang bán" : "Ẩn");
    }
}
