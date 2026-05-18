package com.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int           orderId;
    private int           buyerId;
    private int           addressId;
    private BigDecimal    total;
    private String        status; // PENDING|CONFIRMED|SHIPPED|DELIVERED|CANCELLED
    private String        note;
    private LocalDateTime createdAt;

    // Join phụ
    private String         buyerName;
    private String         addressDetail;
    private List<OrderItem> items;

    public Order() {}

    public Order(int buyerId, int addressId, BigDecimal total, String note) {
        this.buyerId   = buyerId;
        this.addressId = addressId;
        this.total     = total;
        this.note      = note;
        this.status    = "PENDING";
    }

    public int           getOrderId()               { return orderId; }
    public void          setOrderId(int v)          { this.orderId = v; }
    public int           getBuyerId()               { return buyerId; }
    public void          setBuyerId(int v)          { this.buyerId = v; }
    public int           getAddressId()             { return addressId; }
    public void          setAddressId(int v)        { this.addressId = v; }
    public BigDecimal    getTotal()                 { return total; }
    public void          setTotal(BigDecimal v)     { this.total = v; }
    public String        getStatus()                { return status; }
    public void          setStatus(String v)        { this.status = v; }
    public String        getNote()                  { return note; }
    public void          setNote(String v)          { this.note = v; }
    public LocalDateTime getCreatedAt()             { return createdAt; }
    public void          setCreatedAt(LocalDateTime v){ this.createdAt = v; }
    public String        getBuyerName()             { return buyerName; }
    public void          setBuyerName(String v)     { this.buyerName = v; }
    public String        getAddressDetail()         { return addressDetail; }
    public void          setAddressDetail(String v) { this.addressDetail = v; }
    public List<OrderItem> getItems()               { return items; }
    public void          setItems(List<OrderItem> v){ this.items = v; }

    @Override
    public String toString() {
        return String.format("[%d] %-12s | %,.0f VND | %s",
                orderId, status, total, createdAt);
    }
}
