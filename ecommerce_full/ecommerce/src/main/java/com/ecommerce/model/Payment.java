package com.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private int           paymentId;
    private int           orderId;
    private BigDecimal    amount;
    private String        method;  // CASH|BANK_TRANSFER|E_WALLET
    private String        status;  // PENDING|COMPLETED|FAILED|REFUNDED
    private LocalDateTime paidAt;

    public Payment() {}

    public Payment(int orderId, BigDecimal amount, String method) {
        this.orderId = orderId;
        this.amount  = amount;
        this.method  = method;
        this.status  = "PENDING";
    }

    public int           getPaymentId()             { return paymentId; }
    public void          setPaymentId(int v)        { this.paymentId = v; }
    public int           getOrderId()               { return orderId; }
    public void          setOrderId(int v)          { this.orderId = v; }
    public BigDecimal    getAmount()                { return amount; }
    public void          setAmount(BigDecimal v)    { this.amount = v; }
    public String        getMethod()                { return method; }
    public void          setMethod(String v)        { this.method = v; }
    public String        getStatus()                { return status; }
    public void          setStatus(String v)        { this.status = v; }
    public LocalDateTime getPaidAt()                { return paidAt; }
    public void          setPaidAt(LocalDateTime v) { this.paidAt = v; }

    @Override
    public String toString() {
        return String.format("[%d] Đơn #%d | %,.0f VND | %s | %s",
                paymentId, orderId, amount, method, status);
    }
}
