package com.drivncook.model;

public class Dish {
    private String id;
    private String menuId;
    private String name;
    private String description;
    private double price;
    private String language;

    public Dish() {}

    public Dish(String id, String menuId, String name, String description, double price, String language) {
        this.id = id;
        this.menuId = menuId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.language = language;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMenuId() { return menuId; }
    public void setMenuId(String menuId) { this.menuId = menuId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}
