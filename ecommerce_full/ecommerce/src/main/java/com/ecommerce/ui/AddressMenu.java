package com.ecommerce.ui;

import com.ecommerce.model.Address;
import com.ecommerce.service.AddressService;
import com.ecommerce.util.InputUtil;

import java.sql.SQLException;
import java.util.List;

public class AddressMenu {

    private final AddressService service = new AddressService();

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║   📍  ĐỊA CHỈ NHẬN HÀNG          ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Danh sách địa chỉ            ║");
            System.out.println("║  2. Thêm địa chỉ mới             ║");
            System.out.println("║  3. Sửa địa chỉ                  ║");
            System.out.println("║  4. Xóa địa chỉ                  ║");
            System.out.println("║  5. Đặt làm địa chỉ mặc định     ║");
            System.out.println("║  0. Quay lại                     ║");
            System.out.println("╚══════════════════════════════════╝");
            int choice = InputUtil.readInt("Lựa chọn: ");
            try {
                switch (choice) {
                    case 1 -> listAddresses();
                    case 2 -> addAddress();
                    case 3 -> editAddress();
                    case 4 -> deleteAddress();
                    case 5 -> setDefault();
                    case 0 -> running = false;
                    default -> System.out.println("⚠ Lựa chọn không hợp lệ.");
                }
            } catch (Exception e) { System.out.println("⚠ " + e.getMessage()); }
        }
    }

    private void listAddresses() throws SQLException {
        List<Address> list = service.getMyAddresses();
        System.out.println("\n─── ĐỊA CHỈ CỦA TÔI (" + list.size() + ") ────────────");
        if (list.isEmpty()) System.out.println("  Chưa có địa chỉ nào.");
        else list.forEach(a -> System.out.println("  " + a));
        InputUtil.pressEnter();
    }

    private void addAddress() throws SQLException {
        System.out.println("\n─── THÊM ĐỊA CHỈ MỚI ──────────────");
        String recipient = InputUtil.readLine("  Tên người nhận: ");
        String phone     = InputUtil.readLine("  Số điện thoại : ");
        String street    = InputUtil.readLine("  Số nhà, đường : ");
        String district  = InputUtil.readLine("  Quận/Huyện    : ");
        String city      = InputUtil.readLine("  Tỉnh/Thành phố: ");
        Address a = service.add(recipient, phone, street, district, city);
        System.out.println("✅ Đã thêm địa chỉ ID: " + a.getAddressId());
    }

    private void editAddress() throws SQLException {
        int id           = InputUtil.readInt("  ID địa chỉ    : ");
        String recipient = InputUtil.readLine("  Tên người nhận: ");
        String phone     = InputUtil.readLine("  Số điện thoại : ");
        String street    = InputUtil.readLine("  Số nhà, đường : ");
        String district  = InputUtil.readLine("  Quận/Huyện    : ");
        String city      = InputUtil.readLine("  Tỉnh/Thành phố: ");
        service.update(id, recipient, phone, street, district, city);
        System.out.println("✅ Đã cập nhật địa chỉ.");
    }

    private void deleteAddress() throws SQLException {
        int id = InputUtil.readInt("  ID địa chỉ cần xóa: ");
        service.delete(id);
        System.out.println("✅ Đã xóa địa chỉ.");
    }

    private void setDefault() throws SQLException {
        int id = InputUtil.readInt("  ID địa chỉ muốn đặt mặc định: ");
        service.setDefault(id);
        System.out.println("✅ Đã đặt làm địa chỉ mặc định.");
    }
}
