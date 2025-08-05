package com.user;

import com.inven.Inventory;
import com.ord.Order;
import com.prod.Product;

public class Customer {
    private Inventory inventory;

    public Customer(Inventory inventory) {
        this.inventory = inventory;
    }

    public Order placeOrder(int productId, int quantity) throws Exception {
    Product p = inventory.getProductById(productId);
    if (p == null) throw new Exception("Product not found.");
    if (p.getStock() < quantity) throw new Exception("Insufficient stock.");

    p.reduceStock(quantity);
    return new Order(p, quantity);

    }

    public Product[] viewProducts() {
        return inventory.getAllProducts().toArray(new Product[0]);
    }
}
