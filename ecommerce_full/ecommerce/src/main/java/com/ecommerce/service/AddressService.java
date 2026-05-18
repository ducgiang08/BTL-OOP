package com.ecommerce.service;

import com.ecommerce.dao.AddressDAO;
import com.ecommerce.model.Address;
import com.ecommerce.util.SessionManager;

import java.sql.SQLException;
import java.util.List;

public class AddressService {

    private final AddressDAO dao = new AddressDAO();

    public List<Address> getMyAddresses() throws SQLException {
        requireLogin();
        return dao.findByUserId(SessionManager.getCurrentUser().getUserId());
    }

    public Address getById(int id) throws SQLException {
        Address a = dao.findById(id);
        if (a == null) throw new IllegalArgumentException("Không tìm thấy địa chỉ.");
        return a;
    }

    public Address add(String recipient, String phone, String street,
                       String district, String city) throws SQLException {
        requireLogin();
        validate(recipient, phone, street, city);
        int userId = SessionManager.getCurrentUser().getUserId();
        List<Address> existing = dao.findByUserId(userId);
        Address a = new Address(userId, recipient, phone, street, district, city);
        a.setDefault(existing.isEmpty()); // địa chỉ đầu tiên tự động là mặc định
        return dao.save(a);
    }

    public void update(int id, String recipient, String phone,
                       String street, String district, String city) throws SQLException {
        requireLogin();
        validate(recipient, phone, street, city);
        Address a = getById(id);
        a.setRecipient(recipient);
        a.setPhone(phone);
        a.setStreet(street);
        a.setDistrict(district);
        a.setCity(city);
        dao.update(a);
    }

    public void delete(int id) throws SQLException {
        requireLogin();
        dao.delete(id);
    }

    public void setDefault(int id) throws SQLException {
        requireLogin();
        int userId = SessionManager.getCurrentUser().getUserId();
        dao.setDefault(id, userId);
    }

    private void validate(String recipient, String phone, String street, String city) {
        if (recipient == null || recipient.isBlank())
            throw new IllegalArgumentException("Tên người nhận không được để trống.");
        if (phone == null || !phone.matches("^0[0-9]{9}$"))
            throw new IllegalArgumentException("Số điện thoại không hợp lệ.");
        if (street == null || street.isBlank())
            throw new IllegalArgumentException("Địa chỉ không được để trống.");
        if (city == null || city.isBlank())
            throw new IllegalArgumentException("Thành phố không được để trống.");
    }

    private void requireLogin() {
        if (!SessionManager.isLoggedIn())
            throw new IllegalStateException("Bạn chưa đăng nhập.");
    }
}
