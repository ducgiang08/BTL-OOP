package com.ecommerce.model;

import java.time.LocalDateTime;

/**
 * Model ánh xạ bảng Users trong SQL Server.
 */
public class User {

    private int           userId;
    private String        fullName;
    private String        email;
    private String        phone;
    private String        passwordHash;
    private String        role;        // ADMIN | BUYER | SELLER
    private boolean       isActive;
    private LocalDateTime createdAt;

    // ── Constructors ──────────────────────────────────────────────────────────

    public User() {}

    /** Constructor dùng khi đăng ký mới */
    public User(String fullName, String email, String phone,
                String passwordHash, String role) {
        this.fullName     = fullName;
        this.email        = email;
        this.phone        = phone;
        this.passwordHash = passwordHash;
        this.role         = role;
        this.isActive     = true;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int           getUserId()       { return userId; }
    public void          setUserId(int v)  { this.userId = v; }

    public String        getFullName()          { return fullName; }
    public void          setFullName(String v)  { this.fullName = v; }

    public String        getEmail()          { return email; }
    public void          setEmail(String v)  { this.email = v; }

    public String        getPhone()          { return phone; }
    public void          setPhone(String v)  { this.phone = v; }

    public String        getPasswordHash()          { return passwordHash; }
    public void          setPasswordHash(String v)  { this.passwordHash = v; }

    public String        getRole()          { return role; }
    public void          setRole(String v)  { this.role = v; }

    public boolean       isActive()            { return isActive; }
    public void          setActive(boolean v)  { this.isActive = v; }

    public LocalDateTime getCreatedAt()           { return createdAt; }
    public void          setCreatedAt(LocalDateTime v) { this.createdAt = v; }

    @Override
    public String toString() {
        return String.format("[%d] %-25s | %-30s | %-8s | %s",
                userId, fullName, email, role, isActive ? "Hoạt động" : "Bị khóa");
    }
}
