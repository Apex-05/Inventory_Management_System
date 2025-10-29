
# Inventory Management System

A JavaFX-based desktop application to manage products, stock, and customer orders. This system provides two roles: **Manager** and **Customer**, allowing for product additions, updates, inventory tracking, and order placement.

---

## Tech Stack

- Java 17
- JavaFX SDK (GUI)
- VS Code (Recommended IDE)

---

## Features

### Manager Portal:
- Login with username & password
- Add new products (ID, Name, Category, Stock, Price)
- Update existing product details
- View all available products
- View low-stock items
- View sales statistics
- View customer purchase log

### 🛒 Customer Portal:
- View product list
- Place order by entering product ID and quantity
- See total cost and delivery mode (Land or Sea)

---

## How to Run

### 1. Setup JavaFx SDK
Extract and place it somewhere accessible. Note the `/lib` folder path.

### 2. Compile
```bash
javac --module-path "/path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -d bin src/com/ui/InventoryManagement.java
```
### 3. Run
```bash
java --module-path "/path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp bin com.ui.InventoryManagement
```
Replace "/path/to/javafx-sdk/lib" with the actual path on your system.
