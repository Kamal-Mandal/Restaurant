package com.lude.resturantmanagementsystem.Middleware;

import com.lude.resturantmanagementsystem.BackEnd.Database;
import com.lude.resturantmanagementsystem.BackEnd.MenuItem;
import com.lude.resturantmanagementsystem.BackEnd.Order;

import java.util.List;

public class OrderService {
    private Database database;

    public OrderService(Database database) {
        this.database = database;
    }

    public List<Order> getActiveOrders() {
        return database.getActiveOrders();
    }

    public List<Order> getAllOrders() {
        return database.getAllOrders();
    }

    public List<Order> getOrderHistory() {
        return database.getOrderHistory();
    }

    public Order createOrder(int tableNumber, List<MenuItem> items) {
        if (tableNumber <= 0) {
            throw new IllegalArgumentException("Table number must be positive");
        }

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        Order newOrder = new Order(tableNumber);
        for (MenuItem item : items) {
            newOrder.addItem(item);
        }

        database.addOrder(newOrder);
        return newOrder;
    }

    public String getOrderDetails(Order order) {
        if (order == null) {
            return "";
        }

        StringBuilder details = new StringBuilder();
        details.append("Order ID: ").append(order.getOrderId()).append("\n");
        details.append("Order Time: ").append(order.getFormattedOrderTime()).append("\n");
        details.append("Status: ").append(order.getStatus()).append("\n");
        details.append("Table Number: ").append(order.getTableNumber()).append("\n");
        if (!order.getCustomerName().isEmpty()) {
            details.append("Customer: ").append(order.getCustomerName()).append("\n");
        }
        details.append("\nItems:\n");

        for (MenuItem item : order.getItems()) {
            details.append("- ").append(item.getName())
                    .append(" ($").append(String.format("%.2f", item.getPrice())).append(")\n");
        }

        details.append("\nTotal: $").append(String.format("%.2f", order.getTotalPrice()));

        return details.toString();
    }

    public void markOrderAsCompleted(Order order) {
        if (order != null) {
            order.setStatus("Completed");
            database.moveOrderToHistory(order);
        }
    }

    public void cancelOrder(Order order) {
        if (order != null) {
            order.setStatus("Cancelled");
            database.moveOrderToHistory(order);
        }
    }
}