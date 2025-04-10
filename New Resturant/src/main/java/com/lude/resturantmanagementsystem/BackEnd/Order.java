package com.lude.resturantmanagementsystem.BackEnd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Order {
    private static int lastOrderId = 0;

    private int orderId;
    private int tableNumber;
    private Date orderTime;
    private List<MenuItem> items;
    private String status; // "Active", "Completed", "Cancelled"

    public Order(int tableNumber) {
        this.orderId = ++lastOrderId;
        this.tableNumber = tableNumber;
        this.orderTime = new Date();
        this.items = new ArrayList<>();
        this.status = "Active";
    }

    public int getOrderId() {
        return orderId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public String getFormattedOrderTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        return sdf.format(orderTime);
    }

    public List<MenuItem> getItems() {
        return new ArrayList<>(items);
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotal() {
        double total = 0;
        for (MenuItem item : items) {  // Fixed loop syntax
            total += item.getPrice();
        }
        return total;
    }

    public void removeItem(MenuItem item) {
        items.remove(item);
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }

    public int getItemCount() {
        return items.size();
    }

    @Override
    public String toString() {
        return "Order #" + orderId +
                " | Table: " + tableNumber +
                " | Time: " + getFormattedOrderTime() +
                " | Status: " + status +
                " | Items: " + items.size() +
                " | Total: $" + String.format("%.2f", getTotal());
    }

    // Method to mark the order as completed
    public void complete() {
        this.status = "Completed";
    }

    // Method to mark the order as cancelled
    public void cancel() {
        this.status = "Cancelled";
    }

    // Method to check if the order is active
    public boolean isActive() {
        return "Active".equals(status);
    }

    // Reset the order ID counter (useful for testing or system resets)
    public static void resetOrderIdCounter() {
        lastOrderId = 0;
    }

    public Object getTotalPrice() {
        return null;
    }

    public Collection<Object> getCustomerName() {
        return List.of();
    }
}
