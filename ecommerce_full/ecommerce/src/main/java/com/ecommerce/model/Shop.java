package com.ecommerce.model;

import java.time.LocalDateTime;

public class Shop {
    private int           shopId;
    private int           ownerId;
    private String        name;
    private String        description;
    private String        address;
    private boolean       isActive;
    private LocalDateTime createdAt;
    private String        ownerName; // join phụ

    public Shop() {}

    public Shop(int ownerId, String name, String description, String address) {
        this.ownerId     = ownerId;
        this.name        = name;
        this.description = description;
        this.address     = address;
        this.isActive    = true;
    }

    public int           getShopId()               { return shopId; }
    public void          setShopId(int v)           { this.shopId = v; }
    public int           getOwnerId()               { return ownerId; }
    public void          setOwnerId(int v)          { this.ownerId = v; }
    public String        getName()                  { return name; }
    public void          setName(String v)          { this.name = v; }
    public String        getDescription()           { return description; }
    public void          setDescription(String v)   { this.description = v; }
    public String        getAddress()               { return address; }
    public void          setAddress(String v)       { this.address = v; }
    public boolean       isActive()                 { return isActive; }
    public void          setActive(boolean v)       { this.isActive = v; }
    public LocalDateTime getCreatedAt()             { return createdAt; }
    public void          setCreatedAt(LocalDateTime v){ this.createdAt = v; }
    public String        getOwnerName()             { return ownerName; }
    public void          setOwnerName(String v)     { this.ownerName = v; }

    @Override
    public String toString() {
        return String.format("[%d] %-25s | %s | %s",
                shopId, name, address, isActive ? "Mở" : "Đóng");
    }
}
