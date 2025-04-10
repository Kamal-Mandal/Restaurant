package com.lude.resturantmanagementsystem.FrontEnd;

import com.lude.resturantmanagementsystem.BackEnd.MenuItem;
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
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class CustomerDashboardUI {
    private Stage stage;
    private ApplicationController controller;
    private MenuService menuService;
    private OrderService orderService;
    private FlowPane menuItemsContainer;
    private ListView<MenuItem> orderItemsListView;
    private Label totalPriceLabel;
    private int tableNumber = 1;
    private List<MenuItem> currentOrderItems;

    // Color constants
    private final String CHOCOLATE_BACKGROUND = "#3C1F1E";
    private final String LIGHT_ACCENT = "#D2B48C";
    private final String DARK_ACCENT = "#1E0F0E";
    private final String TEXT_COLOR = "#FFF8E7";
    private final String BUTTON_COLOR = "#8B4513";
    private final String BUTTON_TEXT_COLOR = "#FFFFFF";

    public CustomerDashboardUI(Stage stage, ApplicationController controller, MenuService menuService, OrderService orderService) {
        this.stage = stage;
        this.controller = controller;
        this.menuService = menuService;
        this.orderService = orderService;
        this.currentOrderItems = new ArrayList<>();
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(
                Color.web(CHOCOLATE_BACKGROUND), CornerRadii.EMPTY, Insets.EMPTY)));

        // Create top bar with back button and table number
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // Create main content area
        SplitPane splitPane = new SplitPane();
        splitPane.setStyle("-fx-background-color: " + CHOCOLATE_BACKGROUND + "; -fx-divider-color: " + LIGHT_ACCENT + ";");

        // Menu items on the left in a grid layout
        VBox menuBox = createMenuSection();

        // Current order on the right
        VBox orderBox = createOrderSection();

        splitPane.getItems().addAll(menuBox, orderBox);
        splitPane.setDividerPositions(0.6);

        root.setCenter(splitPane);

        Scene scene = new Scene(root, 900, 650);
        stage.setTitle("Restaurant Management System - Customer Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private HBox createTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(15));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setBackground(new Background(new BackgroundFill(
                Color.web(DARK_ACCENT), CornerRadii.EMPTY, Insets.EMPTY)));

        Button backButton = new Button("Back to Main");
        backButton.setStyle("-fx-background-color: " + BUTTON_COLOR + ";" +
                "-fx-text-fill: " + BUTTON_TEXT_COLOR + ";" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;");
        backButton.setOnAction(e -> controller.showWelcomeScreen());

        Label titleLabel = new Label("Restaurant Menu & Ordering");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label tableLabel = new Label("Table Number:");
        tableLabel.setStyle("-fx-text-fill: " + TEXT_COLOR + ";");

        Spinner<Integer> tableSpinner = new Spinner<>(1, 20, tableNumber);
        tableSpinner.setEditable(true);
        tableSpinner.valueProperty().addListener((obs, oldVal, newVal) -> tableNumber = newVal);
        tableSpinner.setStyle("-fx-base: " + LIGHT_ACCENT + ";");

        topBar.getChildren().addAll(backButton, titleLabel, spacer, tableLabel, tableSpinner);

        return topBar;
    }

    private VBox createMenuSection() {
        VBox menuBox = new VBox(15);
        menuBox.setPadding(new Insets(20));
        menuBox.setBackground(new Background(new BackgroundFill(
                Color.web(CHOCOLATE_BACKGROUND), CornerRadii.EMPTY, Insets.EMPTY)));

        Label menuLabel = new Label("Menu Items");
        menuLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        // Search field for menu items
        TextField searchField = new TextField();
        searchField.setPromptText("Search menu items...");
        searchField.setStyle("-fx-background-color: " + LIGHT_ACCENT + ";" +
                "-fx-prompt-text-fill: " + DARK_ACCENT + ";" +
                "-fx-text-fill: " + DARK_ACCENT + ";");
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterMenuItems(newVal);
        });

        // Create a flow pane for menu items displayed as blocks
        menuItemsContainer = new FlowPane();
        menuItemsContainer.setHgap(15);
        menuItemsContainer.setVgap(15);
        menuItemsContainer.setPadding(new Insets(5));
        menuItemsContainer.setStyle("-fx-background-color: " + CHOCOLATE_BACKGROUND + ";");

        ScrollPane scrollPane = new ScrollPane(menuItemsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: " + CHOCOLATE_BACKGROUND + ";" +
                "-fx-background-color: " + CHOCOLATE_BACKGROUND + ";" +
                "-fx-border-color: " + LIGHT_ACCENT + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        updateMenuList();

        menuBox.getChildren().addAll(menuLabel, searchField, scrollPane);

        return menuBox;
    }

    private VBox createOrderSection() {
        VBox orderBox = new VBox(15);
        orderBox.setPadding(new Insets(20));
        orderBox.setBackground(new Background(new BackgroundFill(
                Color.web(CHOCOLATE_BACKGROUND), CornerRadii.EMPTY, Insets.EMPTY)));

        Label orderLabel = new Label("Current Order");
        orderLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        orderItemsListView = new ListView<>();
        orderItemsListView.setStyle("-fx-background-color: " + CHOCOLATE_BACKGROUND + ";" +
                "-fx-control-inner-background: " + DARK_ACCENT + ";" +
                "-fx-border-color: " + LIGHT_ACCENT + ";");

        // Set cell factory to display MenuItem objects properly
        orderItemsListView.setCellFactory(param -> new ListCell<MenuItem>() {
            @Override
            protected void updateItem(MenuItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: " + DARK_ACCENT + ";");
                } else {
                    setText(item.getName() + " - $" + String.format("%.2f", item.getPrice()));
                    setStyle("-fx-text-fill: " + TEXT_COLOR + ";" +
                            "-fx-background-color: " + DARK_ACCENT + ";");
                }
            }
        });
        VBox.setVgrow(orderItemsListView, Priority.ALWAYS);

        Button removeFromOrderButton = new Button("Remove Selected Item");
        removeFromOrderButton.setStyle("-fx-background-color: " + BUTTON_COLOR + ";" +
                "-fx-text-fill: " + BUTTON_TEXT_COLOR + ";" +
                "-fx-cursor: hand;");
        removeFromOrderButton.setOnAction(e -> {
            int selectedIndex = orderItemsListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                currentOrderItems.remove(selectedIndex);
                updateOrderItemsList();
                updateTotalPrice();
            } else {
                showAlert("Please select an item to remove.");
            }
        });

        totalPriceLabel = new Label("Total: $0.00");
        totalPriceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        Button placeOrderButton = new Button("Place Order");
        placeOrderButton.setStyle("-fx-background-color: " + BUTTON_COLOR + ";" +
                "-fx-text-fill: " + BUTTON_TEXT_COLOR + ";" +
                "-fx-font-size: 14px; -fx-font-weight: bold;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 10px 20px;");
        placeOrderButton.setMaxWidth(Double.MAX_VALUE);
        placeOrderButton.setOnAction(e -> placeOrder());

        orderBox.getChildren().addAll(orderLabel, orderItemsListView, removeFromOrderButton, totalPriceLabel, placeOrderButton);

        return orderBox;
    }

    private void updateMenuList() {
        menuItemsContainer.getChildren().clear();
        List<MenuItem> allItems = menuService.getAllMenuItems();

        for (MenuItem item : allItems) {
            VBox menuItemBlock = createMenuItemBlock(item);
            menuItemsContainer.getChildren().add(menuItemBlock);
        }
    }

    private VBox createMenuItemBlock(MenuItem item) {
        VBox menuItemBlock = new VBox(5);
        menuItemBlock.setPadding(new Insets(10));
        menuItemBlock.setPrefWidth(150);
        menuItemBlock.setMinHeight(150);
        menuItemBlock.setAlignment(Pos.CENTER);
        menuItemBlock.setStyle("-fx-background-color: " + LIGHT_ACCENT + ";" +
                "-fx-background-radius: 5;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 1);" +
                "-fx-cursor: hand;");

        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: " + DARK_ACCENT + ";");
        nameLabel.setWrapText(true);
        nameLabel.setAlignment(Pos.CENTER);

        Label priceLabel = new Label("$" + String.format("%.2f", item.getPrice()));
        priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + DARK_ACCENT + ";");

        Button addButton = new Button("Add to Order");
        addButton.setStyle("-fx-background-color: " + BUTTON_COLOR + ";" +
                "-fx-text-fill: " + BUTTON_TEXT_COLOR + ";" +
                "-fx-font-size: 11px;");
        addButton.setOnAction(e -> {
            currentOrderItems.add(item);
            updateOrderItemsList();
            updateTotalPrice();

            // Animation effect for successful add
            menuItemBlock.setStyle("-fx-background-color: #8B4513;" +
                    "-fx-background-radius: 5;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 1);" +
                    "-fx-cursor: hand;");

            // Return to original color after a short delay
            new Thread(() -> {
                try {
                    Thread.sleep(300);
                    javafx.application.Platform.runLater(() -> {
                        menuItemBlock.setStyle("-fx-background-color: " + LIGHT_ACCENT + ";" +
                                "-fx-background-radius: 5;" +
                                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 1);" +
                                "-fx-cursor: hand;");
                    });
                } catch (InterruptedException ignored) {}
            }).start();
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        menuItemBlock.getChildren().addAll(nameLabel, spacer, priceLabel, addButton);

        return menuItemBlock;
    }

    private void filterMenuItems(String searchText) {
        menuItemsContainer.getChildren().clear();
        List<MenuItem> allItems = menuService.getAllMenuItems();

        for (MenuItem item : allItems) {
            if (searchText == null || searchText.isEmpty() ||
                    item.getName().toLowerCase().contains(searchText.toLowerCase())) {
                VBox menuItemBlock = createMenuItemBlock(item);
                menuItemsContainer.getChildren().add(menuItemBlock);
            }
        }
    }

    private void updateOrderItemsList() {
        ObservableList<MenuItem> items = FXCollections.observableArrayList(currentOrderItems);
        orderItemsListView.setItems(items);
    }

    private void updateTotalPrice() {
        double total = 0.0;
        for (MenuItem item : currentOrderItems) {
            total += item.getPrice();
        }
        totalPriceLabel.setText("Total: $" + String.format("%.2f", total));
    }

    private void placeOrder() {
        if (currentOrderItems.isEmpty()) {
            showAlert("Please add items to your order before placing it.");
            return;
        }

        try {
            orderService.createOrder(tableNumber, currentOrderItems);
            showAlert("Order placed successfully!");

            // Clear the current order
            currentOrderItems.clear();
            updateOrderItemsList();
            updateTotalPrice();
        } catch (IllegalArgumentException ex) {
            showAlert(ex.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}