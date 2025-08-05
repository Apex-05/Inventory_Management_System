package com.ord;

import com.prod.Product;

public class Order {
    private Product product;
    private int quantity;
    private double totalCost;

    public Order(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.totalCost = product.getPrice() * quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getDeliveryMode() {
        return product.getCategory().equalsIgnoreCase("Cargo") ? "Sea" : "Land";
    }

    public String getSummary() {
        return String.format(
            "Ordered: %s | Quantity: %d | Total: Rs %.2f | Delivery: %s",
            product.getName(),
            quantity,
            totalCost,
            getDeliveryMode()
        );
    }
}
