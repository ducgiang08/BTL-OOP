package com.ecommerce.service;

import com.ecommerce.dao.ProductDAO;
import com.ecommerce.dao.ShopDAO;
import com.ecommerce.model.Product;
import com.ecommerce.model.Shop;
import com.ecommerce.util.SessionManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProductService {

    private final ProductDAO productDAO = new ProductDAO();
    private final ShopDAO    shopDAO    = new ShopDAO();

    public List<Product> getAll() throws SQLException {
        return productDAO.findAllActive();
    }

    public List<Product> search(String keyword, int categoryId, double minPrice, double maxPrice)
            throws SQLException {
        return productDAO.search(keyword, categoryId, minPrice, maxPrice);
    }

    public Product getById(int id) throws SQLException {
        Product p = productDAO.findById(id);
        if (p == null) throw new IllegalArgumentException("Không tìm thấy sản phẩm ID: " + id);
        return p;
    }

    public List<Product> getMyShopProducts() throws SQLException {
        requireSeller();
        Shop shop = shopDAO.findByOwnerId(SessionManager.getCurrentUser().getUserId());
        if (shop == null) throw new IllegalStateException("Bạn chưa có gian hàng.");
        return productDAO.findByShopId(shop.getShopId());
    }

    public Product add(int categoryId, String name, String description,
                       BigDecimal price, int stock) throws SQLException {
        requireSeller();
        validate(name, price, stock);
        Shop shop = shopDAO.findByOwnerId(SessionManager.getCurrentUser().getUserId());
        if (shop == null) throw new IllegalStateException("Bạn chưa có gian hàng.");
        return productDAO.save(new Product(shop.getShopId(), categoryId, name, description, price, stock));
    }

    public void update(int productId, int categoryId, String name,
                       String description, BigDecimal price, int stock) throws SQLException {
        requireSeller();
        validate(name, price, stock);
        Product p = getById(productId);
        checkOwnership(p);
        p.setCategoryId(categoryId);
        p.setName(name);
        p.setDescription(description);
        p.setPrice(price);
        p.setStock(stock);
        productDAO.update(p);
    }

    public void toggleVisible(int productId, boolean active) throws SQLException {
        requireSeller();
        Product p = getById(productId);
        checkOwnership(p);
        productDAO.toggleActive(productId, active);
    }

    private void validate(String name, BigDecimal price, int stock) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Giá phải >= 0.");
        if (stock < 0)
            throw new IllegalArgumentException("Số lượng tồn kho phải >= 0.");
    }

    private void checkOwnership(Product p) throws SQLException {
        Shop shop = shopDAO.findByOwnerId(SessionManager.getCurrentUser().getUserId());
        if (shop == null || shop.getShopId() != p.getShopId())
            throw new SecurityException("Bạn không có quyền chỉnh sửa sản phẩm này.");
    }

    private void requireSeller() {
        if (!SessionManager.isSeller() && !SessionManager.isAdmin())
            throw new SecurityException("Chức năng chỉ dành cho Người bán.");
    }
}
