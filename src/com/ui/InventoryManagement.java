// === InventoryManagement.java (Updated with fixes) ===

package com.ui;

import com.inven.Inventory;
import com.prod.Product;
import com.user.Customer;
import com.user.Manager;
import com.ord.Order;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;




public class InventoryManagement extends Application {
    private Inventory inventory = new Inventory();
    private Manager manager = new Manager(inventory);
    private Customer customer = new Customer(inventory);
    private Stage window;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private ObservableList<String> purchaseLog = FXCollections.observableArrayList();




    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Inventory Management System");
        showLoginScreen();
    }

    private void showLoginScreen() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextField userField = new TextField();
        userField.setPromptText("Username");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");

        Button loginBtn = new Button("Manager Login");
        loginBtn.setOnAction(e -> {
            if (manager.login(userField.getText(), passField.getText())) {
                showManagerMenu();
            } else {
                showAlert("Invalid credentials!");
            }
        });

        Button custBtn = new Button("Continue as Customer");
        custBtn.setOnAction(e -> showCustomerMenu());

        layout.getChildren().addAll(new Label("Login"), userField, passField, loginBtn, custBtn);
        window.setScene(new Scene(layout, 300, 200));
        window.show();
    }

    private void showCustomerMenu() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextField idField = new TextField();
        TextField qtyField = new TextField();
        idField.setPromptText("Product ID");
        qtyField.setPromptText("Quantity");

        Button viewBtn = new Button("View Products");
        viewBtn.setOnAction(e -> window.setScene(new Scene(getProductTableView(null), 600, 400)));

        Button orderBtn = new Button("Place Order");
        orderBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                int qty = Integer.parseInt(qtyField.getText());
                Order o = customer.placeOrder(id, qty);
                String summary = o.getSummary() + "\nTime: " + dtf.format(LocalDateTime.now());
                purchaseLog.add(summary);
                showToast(summary);
            } catch (Exception ex) {
                showToast("Order failed: " + ex.getMessage());
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> showLoginScreen());

        layout.getChildren().addAll(new Label("Customer Menu"), idField, qtyField, viewBtn, orderBtn, backBtn);
        window.setScene(new Scene(layout, 400, 300));
    }

    private void showManagerMenu() {
        TabPane tabPane = new TabPane();

        tabPane.getTabs().addAll(
            new Tab("Add Product", getAddProductPane(tabPane)),
            new Tab("View Products", getManagerProductTableView()),
            new Tab("Statistics", getTextDisplayTab(tabPane, manager.viewStatistics())),
            new Tab("Low Stock", getTextDisplayTab(tabPane, manager.viewLowStock(5))),
            new Tab("Update Product", getUpdateStockPane(tabPane)),
            new Tab("Customer Purchases", getCustomerPurchaseTab())
        );

        // Add back tab with handler
        Tab backTab = new Tab("Back");
        backTab.setOnSelectionChanged(e -> {
            if (backTab.isSelected()) {
                showLoginScreen();
            }
        });
        tabPane.getTabs().add(backTab);

        window.setScene(new Scene(tabPane, 800, 550));
}


    private VBox getAddProductPane(TabPane parent) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField catField = new TextField();
        TextField stockField = new TextField();
        TextField priceField = new TextField();

        idField.setPromptText("Product ID");
        nameField.setPromptText("Name");
        catField.setPromptText("Category (Goods/Cargo)");
        stockField.setPromptText("Stock");
        priceField.setPromptText("Price");

        Button addBtn = new Button("Add Product");
        addBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String category = catField.getText();
                int stock = Integer.parseInt(stockField.getText());
                double price = Double.parseDouble(priceField.getText());

                Product p = new Product(id, name, category, stock, price);
                manager.addProduct(p);

                showToast("Product added at " + dtf.format(LocalDateTime.now()));

                idField.clear(); nameField.clear(); catField.clear(); stockField.clear(); priceField.clear();

                parent.getTabs().set(2, new Tab("Statistics", getTextDisplayTab(parent, manager.viewStatistics())));
                parent.getTabs().set(1, new Tab("View Products", getManagerProductTableView()));

            } catch (Exception ex) {
                showToast("Invalid input: " + ex.getMessage());
            }
        });

        Button clearBtn = new Button("Clear Fields");
        clearBtn.setOnAction(e -> {
            idField.clear(); nameField.clear(); catField.clear(); stockField.clear(); priceField.clear();
        });

        layout.getChildren().addAll(new Label("Add New Product"), idField, nameField, catField, stockField, priceField,
                new HBox(10, addBtn, clearBtn));
        return layout;
    }

    private VBox getUpdateStockPane(TabPane parent) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextField idField = new TextField("1");
        TextField nameField = new TextField();
        TextField stockField = new TextField();
        TextField priceField = new TextField();

        idField.setPromptText("Product ID");
        nameField.setPromptText("New Name (optional)");
        stockField.setPromptText("New Stock (optional)");
        priceField.setPromptText("New Price (optional)");

        Button updateBtn = new Button("Update Product");
        updateBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText().trim();
                String stockText = stockField.getText().trim();
                String priceText = priceField.getText().trim();

                Integer stock = stockText.isEmpty() ? null : Integer.parseInt(stockText);
                Double price = priceText.isEmpty() ? null : Double.parseDouble(priceText);

                String result = manager.updateProductDetails(id, name.isEmpty() ? null : name, price, stock);
                showToast(result);
                idField.clear(); nameField.clear(); stockField.clear(); priceField.clear();

                parent.getTabs().set(2, new Tab("Statistics", getTextDisplayTab(parent, manager.viewStatistics())));
                parent.getTabs().set(1, new Tab("View Products", getManagerProductTableView()));

            } catch (Exception ex) {
                showToast("Invalid input.");
            }
        });

        layout.getChildren().addAll(new Label("Update Product Details"), idField, nameField, stockField, priceField, updateBtn);
        return layout;
    }

    private VBox getCustomerPurchaseTab() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().add(new Label("Customer Orders:"));

        VBox logBox = new VBox(5);
        refreshPurchaseLog(logBox);

        layout.getChildren().add(new ScrollPane(logBox));
        return layout;
    }

    private void refreshPurchaseLog(VBox logBox) {
        logBox.getChildren().clear();
        for (String order : new ArrayList<>(purchaseLog)) {
            CheckBox cb = new CheckBox(order);
            cb.setOnAction(e -> purchaseLog.remove(order));
            logBox.getChildren().add(cb);
        }
    }

    private VBox getManagerProductTableView() {
        TableView<Product> table = new TableView<>();
        ObservableList<Product> data = FXCollections.observableArrayList(customer.viewProducts());

        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.setItems(data);
        table.getColumns().addAll(idCol, nameCol, catCol, stockCol, priceCol);

        VBox box = new VBox(table);
        box.setPadding(new Insets(10));
        return box;
    }

    private VBox getProductTableView(TabPane parent) {
        TableView<Product> table = new TableView<>();
        ObservableList<Product> data = FXCollections.observableArrayList(customer.viewProducts());

        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.setItems(data);
        table.getColumns().addAll(idCol, nameCol, catCol, stockCol, priceCol);

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> showCustomerMenu());

        VBox box = new VBox(10, table, backBtn);
        box.setPadding(new Insets(10));
        return box;
    }

    private VBox getTextDisplayTab(TabPane parent, String content) {
        TextArea area = new TextArea(content);
        area.setWrapText(true);
        area.setEditable(false);
        VBox box = new VBox(area);
        box.setPadding(new Insets(10));
        return box;
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showToast(String message) {
        Label toast = new Label(message);
        toast.setStyle("-fx-background-color: lightgray; -fx-padding: 10px; -fx-border-color: darkgray;");
        StackPane toastPane = new StackPane(toast);
        Scene toastScene = new Scene(toastPane, 400, 80);
        Stage toastStage = new Stage();
        toastStage.setScene(toastScene);
        toastStage.setAlwaysOnTop(true);
        toastStage.setTitle("Notification");
        toastStage.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> toastStage.close());
        delay.play();
    }
   
}
