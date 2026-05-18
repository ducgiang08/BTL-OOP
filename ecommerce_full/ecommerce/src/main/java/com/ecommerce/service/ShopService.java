package com.ecommerce.service;

import com.ecommerce.dao.ShopDAO;
import com.ecommerce.model.Shop;
import com.ecommerce.util.SessionManager;

import java.sql.SQLException;
import java.util.List;

public class ShopService {

    private final ShopDAO dao = new ShopDAO();

    public Shop getMyShop() throws SQLException {
        requireSeller();
        return dao.findByOwnerId(SessionManager.getCurrentUser().getUserId());
    }

    public List<Shop> getAll() throws SQLException {
        requireAdmin();
        return dao.findAll();
    }

    public Shop register(String name, String description, String address) throws SQLException {
        requireSeller();
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Tên gian hàng không được để trống.");
        if (dao.findByOwnerId(SessionManager.getCurrentUser().getUserId()) != null)
            throw new IllegalStateException("Bạn đã có gian hàng rồi.");
        int ownerId = SessionManager.getCurrentUser().getUserId();
        return dao.save(new Shop(ownerId, name.trim(), description, address));
    }

    public void update(String name, String description, String address) throws SQLException {
        requireSeller();
        Shop shop = getMyShop();
        if (shop == null) throw new IllegalStateException("Bạn chưa có gian hàng.");
        shop.setName(name.trim());
        shop.setDescription(description);
        shop.setAddress(address);
        dao.update(shop);
    }

    public void closeShop() throws SQLException {
        requireSeller();
        Shop shop = getMyShop();
        if (shop == null) throw new IllegalStateException("Bạn chưa có gian hàng.");
        dao.toggleActive(shop.getShopId(), false);
    }

    public void openShop() throws SQLException {
        requireSeller();
        Shop shop = getMyShop();
        if (shop == null) throw new IllegalStateException("Bạn chưa có gian hàng.");
        dao.toggleActive(shop.getShopId(), true);
    }

    public void adminToggle(int shopId, boolean active) throws SQLException {
        requireAdmin();
        dao.toggleActive(shopId, active);
    }

    private void requireSeller() {
        if (!SessionManager.isSeller() && !SessionManager.isAdmin())
            throw new SecurityException("Chức năng chỉ dành cho Người bán.");
    }

    private void requireAdmin() {
        if (!SessionManager.isAdmin())
            throw new SecurityException("Chức năng chỉ dành cho Admin.");
    }
}
