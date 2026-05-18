package com.ecommerce.service;

import com.ecommerce.dao.CategoryDAO;
import com.ecommerce.model.Category;
import com.ecommerce.util.SessionManager;

import java.sql.SQLException;
import java.util.List;

public class CategoryService {

    private final CategoryDAO dao = new CategoryDAO();

    public List<Category> getAllActive() throws SQLException {
        return dao.findAllActive();
    }

    public List<Category> getAll() throws SQLException {
        requireAdmin();
        return dao.findAll();
    }

    public Category getById(int id) throws SQLException {
        Category c = dao.findById(id);
        if (c == null) throw new IllegalArgumentException("Không tìm thấy danh mục ID: " + id);
        return c;
    }

    public Category add(String name, String description) throws SQLException {
        requireAdmin();
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Tên danh mục không được để trống.");
        if (dao.existsByName(name.trim()))
            throw new IllegalStateException("Danh mục '" + name + "' đã tồn tại.");
        return dao.save(new Category(name.trim(), description));
    }

    public void update(int id, String name, String description) throws SQLException {
        requireAdmin();
        Category c = getById(id);
        c.setName(name.trim());
        c.setDescription(description);
        dao.update(c);
    }

    public void hide(int id) throws SQLException {
        requireAdmin();
        dao.toggleActive(id, false);
    }

    public void show(int id) throws SQLException {
        requireAdmin();
        dao.toggleActive(id, true);
    }

    private void requireAdmin() {
        if (!SessionManager.isAdmin())
            throw new SecurityException("Chức năng chỉ dành cho Admin.");
    }
}
