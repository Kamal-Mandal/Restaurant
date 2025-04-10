package com.lude.resturantmanagementsystem.Middleware;




import com.lude.resturantmanagementsystem.BackEnd.Database;
import com.lude.resturantmanagementsystem.BackEnd.MenuItem;

import java.util.List;

public class MenuService {
    private Database database;

    public MenuService(Database database) {
        this.database = database;
    }

    public List<MenuItem> getAllMenuItems() {
        return database.getMenuItems();
    }

    public void addMenuItem(String name, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Menu item name cannot be empty");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        MenuItem newItem = new MenuItem(name, price);
        database.addMenuItem(newItem);
    }

    public void removeMenuItem(String item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot remove null item");
        }
        database.removeMenuItem(item);
    }
}