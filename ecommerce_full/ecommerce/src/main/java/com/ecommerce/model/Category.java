package com.ecommerce.model;

public class Category {
    private int    categoryId;
    private String name;
    private String description;
    private boolean isActive;

    public Category() {}

    public Category(String name, String description) {
        this.name        = name;
        this.description = description;
        this.isActive    = true;
    }

    public int     getCategoryId()            { return categoryId; }
    public void    setCategoryId(int v)       { this.categoryId = v; }
    public String  getName()                  { return name; }
    public void    setName(String v)          { this.name = v; }
    public String  getDescription()           { return description; }
    public void    setDescription(String v)   { this.description = v; }
    public boolean isActive()                 { return isActive; }
    public void    setActive(boolean v)       { this.isActive = v; }

    @Override
    public String toString() {
        return String.format("[%d] %-20s | %s | %s",
                categoryId, name, description,
                isActive ? "Hoạt động" : "Ẩn");
    }
}
