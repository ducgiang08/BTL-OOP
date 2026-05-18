package com.ecommerce.model;

import java.math.BigDecimal;

public class OrderItem {
    private int        orderItemId;
    private int        orderId;
    private int        productId;
    private int        quantity;
    private BigDecimal unitPrice;
    private String     productName; // join phụ

    public OrderItem() {}

    public OrderItem(int orderId, int productId, int quantity, BigDecimal unitPrice) {
        this.orderId   = orderId;
        this.productId = productId;
        this.quantity  = quantity;
        this.unitPrice = unitPrice;
    }

    public int        getOrderItemId()           { return orderItemId; }
    public void       setOrderItemId(int v)      { this.orderItemId = v; }
    public int        getOrderId()               { return orderId; }
    public void       setOrderId(int v)          { this.orderId = v; }
    public int        getProductId()             { return productId; }
    public void       setProductId(int v)        { this.productId = v; }
    public int        getQuantity()              { return quantity; }
    public void       setQuantity(int v)         { this.quantity = v; }
    public BigDecimal getUnitPrice()             { return unitPrice; }
    public void       setUnitPrice(BigDecimal v) { this.unitPrice = v; }
    public String     getProductName()           { return productName; }
    public void       setProductName(String v)   { this.productName = v; }

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String toString() {
        return String.format("  %-25s | SL: %d | %,.0f VND",
                productName, quantity, getSubtotal());
    }
}
