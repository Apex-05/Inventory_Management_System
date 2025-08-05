package com.inven;

import com.prod.Product;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Product> products = new ArrayList<>();

    public synchronized void addProduct(Product product) {
        products.add(product);
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public synchronized void updateStock(int productId, int quantity) throws Exception {
        for (Product p : products) {
            if (p.getId() == productId) {
                p.reduceStock(quantity);
                return;
            }
        }
        throw new Exception("Product not found");
    }

    public Product findProductById(int id) {
        for (Product p : products) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public List<Product> getLowStockProducts(int threshold) {
        List<Product> lowStock = new ArrayList<>();
        for (Product p : products) {
            if (p.isLowStock(threshold)) {
                lowStock.add(p);
            }
        }
        return lowStock;
    }

    public int countByCategory(String category) {
        return (int) products.stream().filter(p -> p.getCategory().equalsIgnoreCase(category)).count();
    }
    public Product getProductById(int id) {
    for (Product p : products) {
        if (p.getId() == id) return p;
    }
    return null;}

}