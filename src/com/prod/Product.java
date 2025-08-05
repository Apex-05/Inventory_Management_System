
package com.prod;

public class Product {
    private int id;
    private String name;
    private String category;
    private int stock;
    private double price;

    public Product(int id, String name, String category, int stock, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.stock = stock;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getStock() { return stock; }
    public double getPrice() { return price; }
    public void setStock(int stock) { this.stock = stock; }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    



    public void reduceStock(int quantity) throws Exception {
        if (quantity > stock) throw new Exception("Insufficient stock.");
        stock -= quantity;
    }

    public boolean isLowStock(int threshold) {
        return stock < threshold;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Category: %s | Stock: %d | Price: %.2f",
                id, name, category, stock, price);
    }
}