package com.user;

import com.prod.Product;
import com.inven.Inventory;
import java.util.List;

public class Manager {
    private final String username = "admin";
    private final String password = "admin123";
    private Inventory inventory;

    public Manager(Inventory inventory) {
        this.inventory = inventory;
    }

    public boolean login(String user, String pass) {
        return username.equals(user) && password.equals(pass);
    }

    public void addProduct(Product product) {
        inventory.addProduct(product);
    }

    public String viewStatistics() {
    StringBuilder stats = new StringBuilder();
    List<Product> products = inventory.getAllProducts();

    if (products.isEmpty()) {
        return "No products in inventory.";
    }

    stats.append("=== Inventory Statistics ===\n");

    int goodsCount = 0;
    int cargoCount = 0;

    for (Product p : products) {
        stats.append(p.toString()).append("\n");
        if (p.getCategory().equalsIgnoreCase("Goods")) {
            goodsCount++;
        } else if (p.getCategory().equalsIgnoreCase("Cargo")) {
            cargoCount++;
        }
    }

    stats.append("\n--- Summary ---\n");
    stats.append("Total Goods: ").append(goodsCount).append("\n");
    stats.append("Total Cargo: ").append(cargoCount);

    return stats.toString();
    }


    public String viewLowStock(int threshold) {
        List<Product> lowStock = inventory.getLowStockProducts(threshold);
        if (lowStock.isEmpty()) return "All stocks are sufficient.";
        StringBuilder sb = new StringBuilder("Low Stock Items:\n");
        for (Product p : lowStock) {
            sb.append(p.toString()).append("\n");
        }
        return sb.toString();
    }

    public String updateProductStock(int productId, int newStock) {
        Product p = inventory.findProductById(productId);
        if (p == null) return "Product not found.";
        p.setStock(newStock);
        return "Stock updated successfully for product ID: " + productId;
    }

    public String searchProduct(String keyword) {
        List<Product> products = inventory.getAllProducts();
        StringBuilder result = new StringBuilder();
        boolean found = false;

        for (Product p : products) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.append(p.toString()).append("\n");
                found = true;
            }
        }

        return found ? result.toString() : "No matching products found.";
    }

    public String deleteProduct(int productId) {
        Product p = inventory.findProductById(productId);
        if (p == null) return "Product not found.";
        inventory.getAllProducts().remove(p);
        return "Product removed from inventory.";
    }
     public String updateProductDetails(int id, String name, Double price, Integer stock) {
    Product p = inventory.getProductById(id);
    if (p == null) return "Product not found.";
    if (name != null && !name.isBlank()) p.setName(name);
    if (price != null) p.setPrice(price);
    if (stock != null) p.setStock(stock);
    return "Product updated successfully.";
    }

   


}
