# 🛒 Hệ thống Thương mại Điện tử – Java + SQL Server

## 👥 Phân công nhóm

| STT | Thành viên       | MSSV       | Use Case                               |
|-----|------------------|------------|----------------------------------------|
| 1   | Vũ Đức Giang     | B22DCVT168 | UC1 Đăng nhập/Đăng xuất, UC2 Đăng ký, UC3 Quản lý Người dùng |
| 2   | Lý Đình Đức      | B22DCVT152 | UC4 Quản lý Danh mục, UC5 Quản lý Sản phẩm, UC6 Tìm kiếm    |
| 3   | Trần Sách Trường | B22DCVT576 | UC7 Quản lý Giỏ hàng, UC8 Quản lý Đơn hàng, UC9 Thanh toán  |
| 4   | Đoàn Minh Tuấn   | B22DCVT480 | UC10 Gian hàng, UC11 Đánh giá, UC12 Địa chỉ nhận hàng       |

---

## 📁 Cấu trúc project

```
ecommerce/
├── database/
│   └── schema.sql                  ← Chạy file này trên SQL Server trước
├── lib/
│   └── mssql-jdbc-12.x.x.jar      ← Driver SQL Server (tải về thêm)
└── src/main/java/com/ecommerce/
    ├── Main.java                   ← Điểm chạy chính
    ├── config/
    │   └── DatabaseConnection.java ← Cấu hình kết nối DB (sửa password)
    ├── util/
    │   ├── InputUtil.java          ← Scanner dùng chung
    │   ├── PasswordUtil.java       ← Băm mật khẩu SHA-256
    │   └── SessionManager.java     ← Quản lý phiên đăng nhập
    ├── model/
    │   ├── User.java               ✅ Giang
    │   ├── Category.java           ← Đức tạo
    │   ├── Product.java            ← Đức tạo
    │   ├── CartItem.java           ← Trường tạo
    │   ├── Order.java              ← Trường tạo
    │   ├── OrderItem.java          ← Trường tạo
    │   ├── Payment.java            ← Trường tạo
    │   ├── Shop.java               ← Tuấn tạo
    │   ├── Review.java             ← Tuấn tạo
    │   └── Address.java            ← Tuấn tạo
    ├── dao/
    │   ├── UserDAO.java            ✅ Giang
    │   ├── CategoryDAO.java        ← Đức tạo
    │   ├── ProductDAO.java         ← Đức tạo
    │   ├── CartDAO.java            ← Trường tạo
    │   ├── OrderDAO.java           ← Trường tạo
    │   ├── PaymentDAO.java         ← Trường tạo
    │   ├── ShopDAO.java            ← Tuấn tạo
    │   ├── ReviewDAO.java          ← Tuấn tạo
    │   └── AddressDAO.java         ← Tuấn tạo
    ├── service/
    │   ├── AuthService.java        ✅ Giang (UC1, UC2)
    │   ├── UserService.java        ✅ Giang (UC3)
    │   ├── CategoryService.java    ← Đức tạo
    │   ├── ProductService.java     ← Đức tạo
    │   ├── CartService.java        ← Trường tạo
    │   ├── OrderService.java       ← Trường tạo
    │   ├── PaymentService.java     ← Trường tạo
    │   ├── ShopService.java        ← Tuấn tạo
    │   ├── ReviewService.java      ← Tuấn tạo
    │   └── AddressService.java     ← Tuấn tạo
    └── ui/
        ├── MainMenu.java           ✅ Giang (khung chung)
        ├── AuthMenu.java           ✅ Giang (UC1, UC2)
        ├── UserMenu.java           ✅ Giang (UC3)
        ├── CategoryMenu.java       ← Đức tạo
        ├── ProductMenu.java        ← Đức tạo
        ├── CartMenu.java           ← Trường tạo
        ├── OrderMenu.java          ← Trường tạo
        ├── PaymentMenu.java        ← Trường tạo
        ├── ShopMenu.java           ← Tuấn tạo
        ├── ReviewMenu.java         ← Tuấn tạo
        └── AddressMenu.java        ← Tuấn tạo
```

---

## 🚀 Hướng dẫn chạy

### 1. Chuẩn bị Database
```sql
-- Chạy file database/schema.sql trên SQL Server Management Studio
```

### 2. Cấu hình kết nối
Mở `src/.../config/DatabaseConnection.java`, sửa:
```java
private static final String PASSWORD = "YourPassword123!"; // ← mật khẩu SQL Server của bạn
```

### 3. Tải SQL Server JDBC Driver
- Tải `mssql-jdbc-12.x.x.jar` từ https://learn.microsoft.com/en-us/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server
- Đặt vào thư mục `lib/`

### 4. Compile & Chạy
```bash
# Compile
javac -cp "lib/*" -d out -sourcepath src/main/java src/main/java/com/ecommerce/Main.java

# Chạy
java -cp "out;lib/*" com.ecommerce.Main
```

### 5. Tài khoản Admin mặc định
- Email: `admin@ecommerce.com`
- Mật khẩu: `admin`

---

## 📋 Hướng dẫn merge cho các thành viên

### Pattern bắt buộc (để code đồng nhất khi merge):

**Model:**
```java
package com.ecommerce.model;
public class Category {
    private int categoryId;
    private String name;
    // getters, setters, toString()
}
```

**DAO:**
```java
package com.ecommerce.dao;
import com.ecommerce.config.DatabaseConnection;
public class CategoryDAO {
    private Connection conn() throws SQLException {
        return DatabaseConnection.getConnection(); // PHẢI dùng hàm này
    }
    // findAll(), findById(), save(), update(), delete()
}
```

**Service:**
```java
package com.ecommerce.service;
import com.ecommerce.util.SessionManager; // Dùng để kiểm tra quyền
public class CategoryService {
    private final CategoryDAO dao = new CategoryDAO();
    // Validate input, kiểm tra quyền, gọi DAO
}
```

**UI Menu:**
```java
package com.ecommerce.ui;
import com.ecommerce.util.InputUtil; // PHẢI dùng InputUtil thay Scanner thẳng
public class CategoryMenu {
    private final CategoryService service = new CategoryService();
    public void show() { /* vòng lặp menu */ }
}
```

**Sau khi tạo xong UI, uncomment trong MainMenu.java:**
```java
// Tìm dòng:
case 2  -> System.out.println("[Chờ Lý Đình Đức implement CategoryMenu]");
// Sửa thành:
case 2  -> categoryMenu.show();
// Và thêm field:
private final CategoryMenu categoryMenu = new CategoryMenu();
```

---

## ⚠️ Lưu ý khi merge
1. **Không** tạo `Scanner` mới — dùng `InputUtil`
2. **Không** tạo `Connection` mới — dùng `DatabaseConnection.getConnection()`
3. Kiểm tra quyền qua `SessionManager.isAdmin()`, `isLoggedIn()`, v.v.
4. Mọi exception từ DB phải `throws SQLException` lên UI xử lý
