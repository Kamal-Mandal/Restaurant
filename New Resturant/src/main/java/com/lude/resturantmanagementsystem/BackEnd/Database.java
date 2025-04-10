package com.lude.resturantmanagementsystem.BackEnd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Database {

    private List<MenuItem> menuItems;
    private List<Order> activeOrders;
    private List<Order> orderHistory; // New list for completed/cancelled orders

    public Database() {
        menuItems = new ArrayList<>();
        activeOrders = new ArrayList<>();
        orderHistory = new ArrayList<>();
    }

    public void initializeDatabase() {
        // Add some default menu items
        menuItems.add(new MenuItem("Burger", 8.99));
        menuItems.add(new MenuItem("Pizza", 12.99));
        menuItems.add(new MenuItem("Pasta", 10.99));
        menuItems.add(new MenuItem("Salad", 6.99));
        menuItems.add(new MenuItem("Soda", 1.99));
    }

    public List<MenuItem> getMenuItems() {
        return new ArrayList<>(menuItems);
    }

    public void addMenuItem(MenuItem item) {
        menuItems.add(item);
    }

    public void removeMenuItem(String itemName) {
        // Fixed to properly remove items by name
        Iterator<MenuItem> iterator = menuItems.iterator();
        while (iterator.hasNext()) {
            MenuItem item = iterator.next();
            if (item.getName().equals(itemName)) {
                iterator.remove();
                break;
            }
        }
    }

    public void addOrder(Order order) {
        activeOrders.add(order);
    }

    public List<Order> getActiveOrders() {
        return new ArrayList<>(activeOrders);
    }

    public List<Order> getAllOrders() {
        List<Order> allOrders = new ArrayList<>();
        allOrders.addAll(activeOrders);
        allOrders.addAll(orderHistory);
        return allOrders;
    }

    public List<Order> getOrderHistory() {
        return new ArrayList<>(orderHistory);
    }

    public void moveOrderToHistory(Order order) {
        if (activeOrders.remove(order)) {
            orderHistory.add(order);
        }
    }
}