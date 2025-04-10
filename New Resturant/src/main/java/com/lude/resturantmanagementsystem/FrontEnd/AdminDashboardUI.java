package com.lude.resturantmanagementsystem.FrontEnd;

import com.lude.resturantmanagementsystem.BackEnd.MenuItem;
import com.lude.resturantmanagementsystem.BackEnd.Order;
import com.lude.resturantmanagementsystem.Middleware.ApplicationController;
import com.lude.resturantmanagementsystem.Middleware.MenuService;
import com.lude.resturantmanagementsystem.Middleware.OrderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDashboardUI {
    private Stage stage;
    private ApplicationController controller;
    private MenuService menuService;
    private OrderService orderService;
    private FlowPane menuItemsContainer;
    private ListView<Order> ordersListView;
    private ListView<Order> orderHistoryListView;
    private Map<MenuItem, CheckBox> menuItemSelections;

    // Color constants for the chocolate theme
    private final String DARK_CHOCOLATE = "#3D2314";
    private final String MEDIUM_CHOCOLATE = "#5C3A21";
    private final String LIGHT_CHOCOLATE = "#8B5A2B";
    private final String CREAM = "#FFF8E7";
    private final String GOLD_ACCENT = "#D4AF37";

    public AdminDashboardUI(Stage stage, ApplicationController controller, MenuService menuService, OrderService orderService) {
        this.stage = stage;
        this.controller = controller;
        this.menuService = menuService;
        this.orderService = orderService;
        this.menuItemSelections = new HashMap<>();
    }

    public void show() {
        BorderPane root = new BorderPane();

        // Apply background color to the entire root
        BackgroundFill backgroundFill = new BackgroundFill(Color.web(DARK_CHOCOLATE), CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(backgroundFill));

        // Create top bar with logo and logout button
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // Create tab pane with custom styling
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Style the tab pane
        tabPane.setStyle("-fx-background-color: " + DARK_CHOCOLATE + ";" +
                "-fx-tab-min-width: 100px;");

        // Menu Management tab
        Tab menuTab = new Tab("Menu Management");
        menuTab.setContent(createMenuManagementContent());

        // Active Orders tab
        Tab ordersTab = new Tab("Active Orders");
        ordersTab.setContent(createOrdersContent());

        // Order History tab
        Tab historyTab = new Tab("Order History");
        historyTab.setContent(createOrderHistoryContent());

        tabPane.getTabs().addAll(menuTab, ordersTab, historyTab);
        root.setCenter(tabPane);

        // Apply custom styles to tab pane
        tabPane.setStyle(
                "-fx-tab-min-height: 40px; " +
                        "-fx-tab-max-height: 40px;"
        );

        // Create scene with applied styles
        Scene scene = new Scene(root, 1000, 800);
        applyStyles(scene);

        stage.setTitle("Restaurant Management System - Admin Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private void applyStyles(Scene scene) {
        // Apply styles directly to components instead of loading from external file
        String css =
                ".tab-pane .tab-header-area .tab-header-background { -fx-background-color: " + MEDIUM_CHOCOLATE + "; }" +
                        ".tab-pane .tab { -fx-background-color: " + LIGHT_CHOCOLATE + "; }" +
                        ".tab-pane .tab:selected { -fx-background-color: " + GOLD_ACCENT + "; }" +
                        ".tab-pane .tab .tab-label { -fx-text-fill: " + CREAM + "; }" +
                        ".tab-pane .tab:selected .tab-label { -fx-text-fill: " + DARK_CHOCOLATE + "; }" +
                        ".button { -fx-background-color: " + GOLD_ACCENT + "; -fx-text-fill: " + DARK_CHOCOLATE + "; -fx-font-weight: bold; }" +
                        ".button:hover { -fx-background-color: derive(" + GOLD_ACCENT + ", 10%); }" +
                        ".label { -fx-text-fill: " + CREAM + "; }" +
                        ".text-field, .text-area { -fx-background-color: " + CREAM + "; -fx-text-fill: " + DARK_CHOCOLATE + "; }" +
                        ".list-view { -fx-background-color: " + MEDIUM_CHOCOLATE + "; -fx-control-inner-background: " + MEDIUM_CHOCOLATE + "; }" +
                        ".list-cell { -fx-background-color: " + MEDIUM_CHOCOLATE + "; -fx-text-fill: " + CREAM + "; -fx-padding: 5px; }" +
                        ".list-cell:filled:selected { -fx-background-color: " + GOLD_ACCENT + "; -fx-text-fill: " + DARK_CHOCOLATE + "; }";

        scene.getRoot().setStyle(css);
    }

    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: " + MEDIUM_CHOCOLATE + ";");
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Restaurant name/logo
        Label logoLabel = new Label("LUDE RESTAURANT");
        logoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        logoLabel.setStyle("-fx-text-fill: " + GOLD_ACCENT + ";");

        // Spacer to push logout button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Logout button
        Button logoutButton = new Button("LOGOUT");
        logoutButton.setStyle(
                "-fx-background-color: " + GOLD_ACCENT + ";" +
                        "-fx-text-fill: " + DARK_CHOCOLATE + ";" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 15;" +
                        "-fx-border-radius: 5;"
        );
        logoutButton.setOnAction(e -> logout());

        topBar.getChildren().addAll(logoLabel, spacer, logoutButton);

        return topBar;
    }

    private void logout() {
        controller.showWelcomeScreen();
    }

    private VBox createMenuManagementContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: " + DARK_CHOCOLATE + ";");

        // Section title
        Label titleLabel = new Label("MENU ITEMS");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: " + GOLD_ACCENT + ";");

        // Add new menu item controls in a styled panel
        VBox addItemPanel = new VBox(10);
        addItemPanel.setPadding(new Insets(15));
        addItemPanel.setStyle(
                "-fx-background-color: " + MEDIUM_CHOCOLATE + ";" +
                        "-fx-background-radius: 10px;"
        );

        Label addItemLabel = new Label("Add New Menu Item");
        addItemLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        addItemLabel.setStyle("-fx-text-fill: " + CREAM + ";");

        HBox inputFields = new HBox(10);
        TextField nameField = new TextField();
        nameField.setPromptText("Item Name");
        nameField.setPrefWidth(250);
        nameField.setStyle(
                "-fx-background-color: " + CREAM + ";" +
                        "-fx-prompt-text-fill: grey;"
        );

        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        priceField.setPrefWidth(100);
        priceField.setStyle(
                "-fx-background-color: " + CREAM + ";" +
                        "-fx-prompt-text-fill: grey;"
        );

        Button addButton = new Button("Add Item");
        addButton.setStyle(
                "-fx-background-color: " + GOLD_ACCENT + ";" +
                        "-fx-text-fill: " + DARK_CHOCOLATE + ";" +
                        "-fx-font-weight: bold;"
        );
        addButton.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());

                if (!name.isEmpty() && price > 0) {
                    menuService.addMenuItem(name, price);
                    updateMenuItemsDisplay();
                    nameField.clear();
                    priceField.clear();
                } else {
                    showAlert("Please enter a valid name and price greater than zero.");
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid price format. Please enter a valid number.");
            } catch (IllegalArgumentException ex) {
                showAlert(ex.getMessage());
            }
        });

        inputFields.getChildren().addAll(nameField, priceField, addButton);
        addItemPanel.getChildren().addAll(addItemLabel, inputFields);

        // Menu items display in a flow pane (grid-like blocks)
        Label menuItemsLabel = new Label("Current Menu Items");
        menuItemsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        menuItemsLabel.setStyle("-fx-text-fill: " + CREAM + ";");

        // Create a scroll pane to contain the flow pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background: " + DARK_CHOCOLATE + ";" +
                        "-fx-background-color: " + DARK_CHOCOLATE + ";" +
                        "-fx-border-color: " + LIGHT_CHOCOLATE + ";"
        );

        // Create a flow pane for menu items displayed as blocks
        menuItemsContainer = new FlowPane();
        menuItemsContainer.setPadding(new Insets(10));
        menuItemsContainer.setHgap(15);
        menuItemsContainer.setVgap(15);
        menuItemsContainer.setPrefWrapLength(800); // Preferred width
        menuItemsContainer.setStyle("-fx-background-color: " + DARK_CHOCOLATE + ";");

        scrollPane.setContent(menuItemsContainer);

        // Add remove button below the scroll pane with fixed functionality
        Button removeButton = new Button("Remove Selected Item");
        removeButton.setStyle(
                "-fx-background-color: #B22222;" + // Dark red for delete action
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;"
        );
        removeButton.setOnAction(e -> {
            // Find the selected menu item and remove it
            MenuItem selectedItem = null;
            for (Map.Entry<MenuItem, CheckBox> entry : menuItemSelections.entrySet()) {
                if (entry.getValue().isSelected()) {
                    selectedItem = entry.getKey();
                    break;
                }
            }

            if (selectedItem != null) {
                menuService.removeMenuItem(selectedItem.getName());
                updateMenuItemsDisplay();
                showAlert("Menu item \"" + selectedItem.getName() + "\" removed successfully.");
            } else {
                showAlert("Please select an item to remove.");
            }
        });

        // Update menu items display
        updateMenuItemsDisplay();

        content.getChildren().addAll(titleLabel, addItemPanel, menuItemsLabel, scrollPane, removeButton);

        return content;
    }

    private void updateMenuItemsDisplay() {
        menuItemsContainer.getChildren().clear();
        menuItemSelections.clear();
        List<MenuItem> menuItems = menuService.getAllMenuItems();

        for (MenuItem item : menuItems) {
            VBox itemBlock = createMenuItemBlock(item);
            menuItemsContainer.getChildren().add(itemBlock);
        }
    }

    private VBox createMenuItemBlock(MenuItem item) {
        VBox block = new VBox(8);
        block.setPadding(new Insets(15));
        block.setPrefWidth(200);
        block.setPrefHeight(120);
        block.setAlignment(Pos.CENTER);
        block.setStyle(
                "-fx-background-color: " + LIGHT_CHOCOLATE + ";" +
                        "-fx-background-radius: 10px;" +
                        "-fx-border-color: " + GOLD_ACCENT + ";" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-cursor: hand;"
        );

        // Add hover effect
        block.setOnMouseEntered(e -> block.setStyle(
                "-fx-background-color: " + MEDIUM_CHOCOLATE + ";" +
                        "-fx-background-radius: 10px;" +
                        "-fx-border-color: " + GOLD_ACCENT + ";" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-cursor: hand;"
        ));

        block.setOnMouseExited(e -> block.setStyle(
                "-fx-background-color: " + LIGHT_CHOCOLATE + ";" +
                        "-fx-background-radius: 10px;" +
                        "-fx-border-color: " + GOLD_ACCENT + ";" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-cursor: hand;"
        ));

        // Item name
        Label nameLabel = new Label(item.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setStyle("-fx-text-fill: " + CREAM + ";");
        nameLabel.setWrapText(true);
        nameLabel.setAlignment(Pos.CENTER);

        // Item price
        Label priceLabel = new Label("$" + String.format("%.2f", item.getPrice()));
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        priceLabel.setStyle("-fx-text-fill: " + GOLD_ACCENT + ";");

        // Add checkbox for selection with improved functionality
        CheckBox selectBox = new CheckBox();
        selectBox.setStyle("-fx-text-fill: " + CREAM + ";");

        // Store reference to checkbox for later retrieval
        menuItemSelections.put(item, selectBox);

        // Ensure only one item can be selected at a time
        selectBox.setOnAction(e -> {
            if (selectBox.isSelected()) {
                // Deselect all other checkboxes
                for (Map.Entry<MenuItem, CheckBox> entry : menuItemSelections.entrySet()) {
                    if (entry.getKey() != item) {
                        entry.getValue().setSelected(false);
                    }
                }
            }
        });

        block.getChildren().addAll(nameLabel, priceLabel, selectBox);
        return block;
    }

    private VBox createOrdersContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: " + DARK_CHOCOLATE + ";");

        Label titleLabel = new Label("ACTIVE ORDERS");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: " + GOLD_ACCENT + ";");

        // Orders list with custom styling
        ordersListView = new ListView<>();
        ordersListView.setPrefHeight(250);
        ordersListView.setStyle(
                "-fx-background-color: " + MEDIUM_CHOCOLATE + ";" +
                        "-fx-control-inner-background: " + MEDIUM_CHOCOLATE + ";" +
                        "-fx-border-color: " + GOLD_ACCENT + ";" +
                        "-fx-border-width: 1px;"
        );

        // Set cell factory to display Order objects properly with custom styling
        ordersListView.setCellFactory(param -> new ListCell<Order>() {
            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);
                if (empty || order == null) {
                    setText(null);
                    setStyle("-fx-background-color: " + MEDIUM_CHOCOLATE + ";");
                } else {
                    setText("Order #" + order.getOrderId() + " - Table " + order.getTableNumber() +
                            " - " + order.getFormattedOrderTime());
                    setStyle(
                            "-fx-background-color: " + MEDIUM_CHOCOLATE + ";" +
                                    "-fx-text-fill: " + CREAM + ";" +
                                    "-fx-font-size: 14px;" +
                                    "-fx-padding: 8px;"
                    );
                }
            }
        });
        updateActiveOrdersList();

        // Order details area with better styling
        Label detailsLabel = new Label("Order Details");
        detailsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        detailsLabel.setStyle("-fx-text-fill: " + CREAM + ";");

        TextArea orderDetailsArea = new TextArea();
        orderDetailsArea.setEditable(false);
        orderDetailsArea.setPrefHeight(150);
        orderDetailsArea.setStyle(
                "-fx-control-inner-background: " + CREAM + ";" +
                        "-fx-text-fill: " + DARK_CHOCOLATE + ";" +
                        "-fx-font-size: 14px;"
        );

        ordersListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                orderDetailsArea.setText(orderService.getOrderDetails(newVal));
            } else {
                orderDetailsArea.clear();
            }
        });

        // Buttons for order management with better styling
        HBox buttonBox = new HBox(15);
        buttonBox.setPadding(new Insets(10, 0, 10, 0));
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button refreshButton = new Button("Refresh Orders");
        refreshButton.setStyle(
                "-fx-background-color: " + GOLD_ACCENT + ";" +
                        "-fx-text-fill: " + DARK_CHOCOLATE + ";" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 15;"
        );
        refreshButton.setOnAction(e -> updateActiveOrdersList());

        Button completeButton = new Button("Mark as Completed");
        completeButton.setStyle(
                "-fx-background-color: #228B22;" + // Dark green for positive action
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 15;"
        );
        completeButton.setOnAction(e -> {
            Order selectedOrder = ordersListView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                orderService.markOrderAsCompleted(selectedOrder);
                updateActiveOrdersList();
                updateOrderHistoryList("All");
                showAlert("Order marked as completed.");
            } else {
                showAlert("Please select an order to mark as completed.");
            }
        });

        Button cancelButton = new Button("Cancel Order");
        cancelButton.setStyle(
                "-fx-background-color: #B22222;" + // Dark red for negative action
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 15;"
        );
        cancelButton.setOnAction(e -> {
            Order selectedOrder = ordersListView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                orderService.cancelOrder(selectedOrder);
                updateActiveOrdersList();
                updateOrderHistoryList("All");
                showAlert("Order has been cancelled.");
            } else {
                showAlert("Please select an order to cancel.");
            }
        });

        buttonBox.getChildren().addAll(refreshButton, completeButton, cancelButton);

        content.getChildren().addAll(titleLabel, ordersListView, detailsLabel, orderDetailsArea, buttonBox);

        return content;
    }

    private VBox createOrderHistoryContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: " + DARK_CHOCOLATE + ";");

        Label titleLabel = new Label("ORDER HISTORY");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: " + GOLD_ACCENT + ";");

        // Create filter controls with better styling
        HBox filterBox = new HBox(15);
        filterBox.setPadding(new Insets(10, 0, 10, 0));
        filterBox.setAlignment(Pos.CENTER_LEFT);

        Label statusLabel = new Label("Filter by Status:");
        statusLabel.setStyle("-fx-text-fill: " + CREAM + ";");

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All", "Completed", "Cancelled");
        statusFilter.setValue("All");
        statusFilter.setStyle(
                "-fx-background-color: " + CREAM + ";" +
                        "-fx-text-fill: " + DARK_CHOCOLATE + ";"
        );

        Button applyFilterButton = new Button("Apply Filter");
        applyFilterButton.setStyle(
                "-fx-background-color: " + GOLD_ACCENT + ";" +
                        "-fx-text-fill: " + DARK_CHOCOLATE + ";" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 5 10;"
        );
        applyFilterButton.setOnAction(e -> updateOrderHistoryList(statusFilter.getValue()));

        filterBox.getChildren().addAll(statusLabel, statusFilter, applyFilterButton);

        // Order history list with custom styling
        orderHistoryListView = new ListView<>();
        orderHistoryListView.setPrefHeight(250);
        orderHistoryListView.setStyle(
                "-fx-background-color: " + MEDIUM_CHOCOLATE + ";" +
                        "-fx-control-inner-background: " + MEDIUM_CHOCOLATE + ";" +
                        "-fx-border-color: " + GOLD_ACCENT + ";" +
                        "-fx-border-width: 1px;"
        );

        orderHistoryListView.setCellFactory(param -> new ListCell<Order>() {
            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);
                if (empty || order == null) {
                    setText(null);
                    setStyle("-fx-background-color: " + MEDIUM_CHOCOLATE + ";");
                } else {
                    setText("Order #" + order.getOrderId() + " - Table " + order.getTableNumber() +
                            " - " + order.getFormattedOrderTime() + " - " + order.getStatus());

                    // Different style based on order status
                    String statusColor;
                    if (order.getStatus().equals("Completed")) {
                        statusColor = "#228B22"; // Dark green
                    } else if (order.getStatus().equals("Cancelled")) {
                        statusColor = "#B22222"; // Dark red
                    } else {
                        statusColor = GOLD_ACCENT; // Default gold
                    }

                    setStyle(
                            "-fx-background-color: " + MEDIUM_CHOCOLATE + ";" +
                                    "-fx-text-fill: " + CREAM + ";" +
                                    "-fx-font-size: 14px;" +
                                    "-fx-padding: 8px;" +
                                    "-fx-border-left: 4px solid " + statusColor + ";"
                    );
                }
            }
        });
        updateOrderHistoryList("All");

        // Order details area with better styling
        Label historyDetailsLabel = new Label("Order Details");
        historyDetailsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        historyDetailsLabel.setStyle("-fx-text-fill: " + CREAM + ";");

        TextArea historyDetailsArea = new TextArea();
        historyDetailsArea.setEditable(false);
        historyDetailsArea.setPrefHeight(150);
        historyDetailsArea.setStyle(
                "-fx-control-inner-background: " + CREAM + ";" +
                        "-fx-text-fill: " + DARK_CHOCOLATE + ";" +
                        "-fx-font-size: 14px;"
        );

        orderHistoryListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                historyDetailsArea.setText(orderService.getOrderDetails(newVal));
            } else {
                historyDetailsArea.clear();
            }
        });

        // Export button with improved styling
        Button exportButton = new Button("Export Order History");
        exportButton.setStyle(
                "-fx-background-color: " + GOLD_ACCENT + ";" +
                        "-fx-text-fill: " + DARK_CHOCOLATE + ";" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 15;"
        );
        exportButton.setOnAction(e -> exportOrderHistory());

        content.getChildren().addAll(titleLabel, filterBox, orderHistoryListView,
                historyDetailsLabel, historyDetailsArea, exportButton);

        return content;
    }

    private void updateActiveOrdersList() {
        ObservableList<Order> orders = FXCollections.observableArrayList(orderService.getActiveOrders());
        ordersListView.setItems(orders);
    }

    private void updateOrderHistoryList(String filter) {
        List<Order> historyOrders = orderService.getOrderHistory();
        ObservableList<Order> filteredOrders = FXCollections.observableArrayList();

        for (Order order : historyOrders) {
            if (filter.equals("All") || order.getStatus().equals(filter)) {
                filteredOrders.add(order);
            }
        }

        orderHistoryListView.setItems(filteredOrders);
    }

    private void exportOrderHistory() {
        // Implementation for exporting order history (could save to CSV/PDF)
        showAlert("Order history export feature will be implemented in a future version.");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Style the alert dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: " + MEDIUM_CHOCOLATE + ";" +
                        "-fx-text-fill: " + CREAM + ";"
        );

        // Get and style the buttons in the alert
        ButtonType okButton = ButtonType.OK;
        Button button = (Button) dialogPane.lookupButton(okButton);
        if (button != null) {
            button.setStyle(
                    "-fx-background-color: " + GOLD_ACCENT + ";" +
                            "-fx-text-fill: " + DARK_CHOCOLATE + ";" +
                            "-fx-font-weight: bold;"
            );
        }

        alert.showAndWait();
    }
}