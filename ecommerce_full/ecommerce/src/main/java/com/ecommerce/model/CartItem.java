package com.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CartItem {
    private int           cartItemId;
    private int           userId;
    private int           productId;
    private int           quantity;
    private LocalDateTime addedAt;

    // Join phụ từ bảng Products
    private String     productName;
    private BigDecimal unitPrice;
    private int        stock;

    public CartItem() {}

    public CartItem(int userId, int productId, int quantity) {
        this.userId    = userId;
        this.productId = productId;
        this.quantity  = quantity;
    }

    public int           getCartItemId()            { return cartItemId; }
    public void          setCartItemId(int v)       { this.cartItemId = v; }
    public int           getUserId()                { return userId; }
    public void          setUserId(int v)           { this.userId = v; }
    public int           getProductId()             { return productId; }
    public void          setProductId(int v)        { this.productId = v; }
    public int           getQuantity()              { return quantity; }
    public void          setQuantity(int v)         { this.quantity = v; }
    public LocalDateTime getAddedAt()               { return addedAt; }
    public void          setAddedAt(LocalDateTime v){ this.addedAt = v; }
    public String        getProductName()           { return productName; }
    public void          setProductName(String v)   { this.productName = v; }
    public BigDecimal    getUnitPrice()             { return unitPrice; }
    public void          setUnitPrice(BigDecimal v) { this.unitPrice = v; }
    public int           getStock()                 { return stock; }
    public void          setStock(int v)            { this.stock = v; }

    public BigDecimal getSubtotal() {
        if (unitPrice == null) return BigDecimal.ZERO;
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String toString() {
        return String.format("%-25s | SL: %d | Đơn giá: %,.0f | Tổng: %,.0f VND",
                productName, quantity,
                unitPrice == null ? 0 : unitPrice,
                getSubtotal());
    }
}
