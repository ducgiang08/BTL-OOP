-- ============================================================
-- HỆ THỐNG THƯƠNG MẠI ĐIỆN TỬ - SQL SERVER SCHEMA
-- ============================================================

CREATE DATABASE ECommerceDB;
GO
USE ECommerceDB;
GO

-- ============================================================
-- BẢNG USERS (UC1: Đăng nhập/Đăng xuất | UC2: Đăng ký | UC3: Quản lý người dùng)
-- Phụ trách: Vũ Đức Giang - B22DCVT168
-- ============================================================
CREATE TABLE Users (
    user_id       INT IDENTITY(1,1) PRIMARY KEY,
    full_name     NVARCHAR(100)  NOT NULL,
    email         VARCHAR(150)   NOT NULL UNIQUE,
    phone         VARCHAR(20),
    password_hash VARCHAR(256)   NOT NULL,
    role          VARCHAR(20)    NOT NULL DEFAULT 'BUYER'
                  CHECK (role IN ('ADMIN','BUYER','SELLER')),
    is_active     BIT            NOT NULL DEFAULT 1,
    created_at    DATETIME       NOT NULL DEFAULT GETDATE()
);
GO

-- ============================================================
-- BẢNG CATEGORIES (UC4: Quản lý Danh mục)
-- Phụ trách: Lý Đình Đức - B22DCVT152
-- ============================================================
CREATE TABLE Categories (
    category_id   INT IDENTITY(1,1) PRIMARY KEY,
    name          NVARCHAR(100) NOT NULL UNIQUE,
    description   NVARCHAR(500),
    is_active     BIT NOT NULL DEFAULT 1
);
GO

-- ============================================================
-- BẢNG SHOPS (UC10: Quản lý Gian hàng)
-- Phụ trách: Đoàn Minh Tuấn - B22DCVT480
-- ============================================================
CREATE TABLE Shops (
    shop_id     INT IDENTITY(1,1) PRIMARY KEY,
    owner_id    INT           NOT NULL REFERENCES Users(user_id),
    name        NVARCHAR(150) NOT NULL,
    description NVARCHAR(1000),
    address     NVARCHAR(300),
    is_active   BIT NOT NULL DEFAULT 1,
    created_at  DATETIME NOT NULL DEFAULT GETDATE()
);
GO

-- ============================================================
-- BẢNG PRODUCTS (UC5: Quản lý Sản phẩm | UC6: Tìm kiếm)
-- Phụ trách: Lý Đình Đức - B22DCVT152
-- ============================================================
CREATE TABLE Products (
    product_id   INT IDENTITY(1,1) PRIMARY KEY,
    shop_id      INT              NOT NULL REFERENCES Shops(shop_id),
    category_id  INT              NOT NULL REFERENCES Categories(category_id),
    name         NVARCHAR(200)    NOT NULL,
    description  NVARCHAR(2000),
    price        DECIMAL(18,2)    NOT NULL CHECK (price >= 0),
    stock        INT              NOT NULL DEFAULT 0 CHECK (stock >= 0),
    is_active    BIT              NOT NULL DEFAULT 1,
    created_at   DATETIME         NOT NULL DEFAULT GETDATE()
);
GO

-- ============================================================
-- BẢNG ADDRESSES (UC12: Quản lý Địa chỉ nhận hàng)
-- Phụ trách: Đoàn Minh Tuấn - B22DCVT480
-- ============================================================
CREATE TABLE Addresses (
    address_id   INT IDENTITY(1,1) PRIMARY KEY,
    user_id      INT           NOT NULL REFERENCES Users(user_id),
    recipient    NVARCHAR(100) NOT NULL,
    phone        VARCHAR(20)   NOT NULL,
    street       NVARCHAR(300) NOT NULL,
    district     NVARCHAR(100),
    city         NVARCHAR(100) NOT NULL,
    is_default   BIT NOT NULL DEFAULT 0
);
GO

-- ============================================================
-- BẢNG CART (UC7: Quản lý Giỏ hàng)
-- Phụ trách: Trần Sách Trường - B22DCVT576
-- ============================================================
CREATE TABLE CartItems (
    cart_item_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id      INT NOT NULL REFERENCES Users(user_id),
    product_id   INT NOT NULL REFERENCES Products(product_id),
    quantity     INT NOT NULL CHECK (quantity > 0),
    added_at     DATETIME NOT NULL DEFAULT GETDATE(),
    UNIQUE (user_id, product_id)
);
GO

-- ============================================================
-- BẢNG ORDERS (UC8: Quản lý Đơn hàng)
-- Phụ trách: Trần Sách Trường - B22DCVT576
-- ============================================================
CREATE TABLE Orders (
    order_id    INT IDENTITY(1,1) PRIMARY KEY,
    buyer_id    INT           NOT NULL REFERENCES Users(user_id),
    address_id  INT           NOT NULL REFERENCES Addresses(address_id),
    total       DECIMAL(18,2) NOT NULL,
    status      VARCHAR(30)   NOT NULL DEFAULT 'PENDING'
                CHECK (status IN ('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED')),
    note        NVARCHAR(500),
    created_at  DATETIME      NOT NULL DEFAULT GETDATE()
);
GO

CREATE TABLE OrderItems (
    order_item_id INT IDENTITY(1,1) PRIMARY KEY,
    order_id      INT           NOT NULL REFERENCES Orders(order_id),
    product_id    INT           NOT NULL REFERENCES Products(product_id),
    quantity      INT           NOT NULL CHECK (quantity > 0),
    unit_price    DECIMAL(18,2) NOT NULL
);
GO

-- ============================================================
-- BẢNG PAYMENTS (UC9: Quản lý Thanh toán)
-- Phụ trách: Trần Sách Trường - B22DCVT576
-- ============================================================
CREATE TABLE Payments (
    payment_id  INT IDENTITY(1,1) PRIMARY KEY,
    order_id    INT           NOT NULL UNIQUE REFERENCES Orders(order_id),
    amount      DECIMAL(18,2) NOT NULL,
    method      VARCHAR(30)   NOT NULL CHECK (method IN ('CASH','BANK_TRANSFER','E_WALLET')),
    status      VARCHAR(20)   NOT NULL DEFAULT 'PENDING'
                CHECK (status IN ('PENDING','COMPLETED','FAILED','REFUNDED')),
    paid_at     DATETIME
);
GO

-- ============================================================
-- BẢNG REVIEWS (UC11: Quản lý Đánh giá)
-- Phụ trách: Đoàn Minh Tuấn - B22DCVT480
-- ============================================================
CREATE TABLE Reviews (
    review_id   INT IDENTITY(1,1) PRIMARY KEY,
    product_id  INT NOT NULL REFERENCES Products(product_id),
    user_id     INT NOT NULL REFERENCES Users(user_id),
    rating      TINYINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment     NVARCHAR(1000),
    created_at  DATETIME NOT NULL DEFAULT GETDATE(),
    UNIQUE (product_id, user_id)
);
GO

-- ============================================================
-- DỮ LIỆU MẪU - ADMIN DEFAULT
-- ============================================================
INSERT INTO Users (full_name, email, phone, password_hash, role)
VALUES (N'Administrator', 'admin@ecommerce.com', '0000000000',
        '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', -- admin
        'ADMIN');
GO
