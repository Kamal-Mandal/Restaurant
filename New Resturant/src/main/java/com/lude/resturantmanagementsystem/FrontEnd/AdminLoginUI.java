package com.lude.resturantmanagementsystem.FrontEnd;

import com.lude.resturantmanagementsystem.Middleware.ApplicationController;
import com.lude.resturantmanagementsystem.Middleware.AuthService;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AdminLoginUI {
    private Stage stage;
    private ApplicationController controller;
    private AuthService authService;

    // Define color theme to match WelcomeScreenUI
    private final Color PRIMARY_DARK = Color.web("#5D2A16");     // Rich brown
    private final Color PRIMARY_MEDIUM = Color.web("#A35E38");   // Cinnamon
    private final Color PRIMARY_LIGHT = Color.web("#F3C08D");    // Pale gold
    private final Color ACCENT_COLOR = Color.web("#D64C31");     // Tomato red
    private final Color TEXT_LIGHT = Color.web("#FFF8EA");       // Creamy white

    public AdminLoginUI(Stage stage, ApplicationController controller, AuthService authService) {
        this.stage = stage;
        this.controller = controller;
        this.authService = authService;
    }

    public void show() {
        // Create a StackPane as the root container
        StackPane root = new StackPane();

        // Create a layered background
        try {
            // Primary background image (same as welcome screen)
            BackgroundImage backgroundImage = new BackgroundImage(
                    new Image(getClass().getResourceAsStream("/images/restaurant-bg.jpg")),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            root.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            // Fallback to gradient if image not found
            LinearGradient gradient = new LinearGradient(
                    0, 0, 1, 1, true, null,
                    new Stop(0, PRIMARY_DARK),
                    new Stop(0.5, PRIMARY_MEDIUM),
                    new Stop(1, PRIMARY_DARK)
            );

            root.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        }

        // Create a semi-transparent overlay for better text readability
        VBox overlay = new VBox();
        overlay.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.65),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));

        // Create a responsive container
        StackPane contentContainer = new StackPane();
        contentContainer.setMaxWidth(Double.MAX_VALUE);
        contentContainer.setMaxHeight(Double.MAX_VALUE);

        // Create glass-effect panel for content
        Rectangle glassPanel = new Rectangle();
        glassPanel.setWidth(480);
        glassPanel.setHeight(450);
        glassPanel.setArcWidth(30);
        glassPanel.setArcHeight(30);
        glassPanel.setFill(Color.rgb(255, 255, 255, 0.15));
        glassPanel.setStroke(Color.rgb(255, 255, 255, 0.5));
        glassPanel.setStrokeWidth(1);

        DropShadow panelShadow = new DropShadow();
        panelShadow.setColor(Color.rgb(0, 0, 0, 0.5));
        panelShadow.setRadius(20);
        panelShadow.setOffsetX(5);
        panelShadow.setOffsetY(5);
        panelShadow.setBlurType(BlurType.GAUSSIAN);
        glassPanel.setEffect(panelShadow);

        // Create VBox for content
        VBox content = new VBox(25);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        content.setMaxWidth(400);

        // Title with enhanced styling
        Text title = new Text("Admin Login");
        title.setFont(Font.font("Palatino Linotype", FontWeight.BOLD, 32));
        title.setFill(TEXT_LIGHT);
        title.setTextAlignment(TextAlignment.CENTER);

        // Add fancy text effect
        DropShadow titleShadow = new DropShadow();
        titleShadow.setRadius(8.0);
        titleShadow.setOffsetX(3.0);
        titleShadow.setOffsetY(3.0);
        titleShadow.setColor(Color.rgb(0, 0, 0, 0.7));

        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setRadius(2.0);
        innerShadow.setColor(ACCENT_COLOR);
        innerShadow.setInput(titleShadow);
        title.setEffect(innerShadow);

        // Admin icon
        try {
            ImageView adminIconView = new ImageView(new Image(getClass().getResourceAsStream("/images/admin-icon.png")));
            adminIconView.setFitHeight(64);
            adminIconView.setPreserveRatio(true);
            content.getChildren().add(adminIconView);
        } catch (Exception e) {
            // Fallback if icon not found - create text-based icon
            Text iconText = new Text("👤");
            iconText.setFont(Font.font("Arial", FontWeight.BOLD, 50));
            iconText.setFill(ACCENT_COLOR);
            content.getChildren().add(iconText);
        }

        // Create GridPane for form fields
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 10, 10, 10));

        // Style for text fields and labels
        String textFieldStyle =
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-prompt-text-fill: rgba(255, 255, 255, 0.7);" +
                        "-fx-background-radius: 5px;" +
                        "-fx-padding: 10px;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-border-radius: 5px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 1, 1);";

        String labelStyle =
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + toHexString(TEXT_LIGHT) + ";";

        // Username field
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle(labelStyle);
        grid.add(usernameLabel, 0, 0);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setStyle(textFieldStyle);
        usernameField.setPrefWidth(250);
        usernameField.setPrefHeight(40);
        grid.add(usernameField, 1, 0);

        // Password field
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle(labelStyle);
        grid.add(passwordLabel, 0, 1);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setStyle(textFieldStyle);
        passwordField.setPrefWidth(250);
        passwordField.setPrefHeight(40);
        grid.add(passwordField, 1, 1);

        // Error message label
        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.web("#FF6B6B"));
        messageLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 14));
        grid.add(messageLabel, 0, 2, 2, 1);

        // Button styling
        String loginButtonStyle =
                "-fx-background-color: linear-gradient(to bottom, #A35E38, #5D2A16);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20px;" +
                        "-fx-padding: 10px 25px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 2, 2);";

        String loginButtonHoverStyle =
                "-fx-background-color: linear-gradient(to bottom, #C27048, #7D3A20);" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20px;" +
                        "-fx-padding: 10px 25px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 12, 0, 3, 3);";

        String backButtonStyle =
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-background-radius: 20px;" +
                        "-fx-padding: 10px 25px;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.5);" +
                        "-fx-border-radius: 20px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 1, 1);";

        String backButtonHoverStyle =
                "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-background-radius: 20px;" +
                        "-fx-padding: 10px 25px;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.7);" +
                        "-fx-border-radius: 20px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 2, 2);";

        // Buttons
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(120);
        loginButton.setPrefHeight(40);
        loginButton.setStyle(loginButtonStyle);
        styleButton(loginButton, loginButtonStyle, loginButtonHoverStyle);

        Button backButton = new Button("Back");
        backButton.setPrefWidth(120);
        backButton.setPrefHeight(40);
        backButton.setStyle(backButtonStyle);
        styleButton(backButton, backButtonStyle, backButtonHoverStyle);

        // Add login icon if available
        try {
            ImageView loginIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/login-icon.png")));
            loginIcon.setFitHeight(16);
            loginIcon.setFitWidth(16);
            loginButton.setGraphic(loginIcon);
            loginButton.setGraphicTextGap(8);
        } catch (Exception e) {
            // Continue without icon if not found
        }

        // Add back icon if available
        try {
            ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/back-icon.png")));
            backIcon.setFitHeight(16);
            backIcon.setFitWidth(16);
            backButton.setGraphic(backIcon);
            backButton.setGraphicTextGap(8);
        } catch (Exception e) {
            // Continue without icon if not found
        }

        // Button container
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(backButton, loginButton);

        // Button actions
        loginButton.setOnAction(e -> {
            animateButtonClick(loginButton);
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (authService.authenticateAdmin(username, password)) {
                // Create a fade out animation before changing scenes
                FadeTransition fadeOut = new FadeTransition(Duration.millis(300), content);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.play();

                fadeOut.setOnFinished(event -> controller.showAdminDashboard());
            } else {
                // Shake animation for error
                TranslateTransition shake = createShakeAnimation(glassPanel);
                shake.play();
                messageLabel.setText("Invalid username or password");
            }
        });

        backButton.setOnAction(e -> {
            animateButtonClick(backButton);

            // Create a fade out animation before changing scenes
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), content);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.play();

            fadeOut.setOnFinished(event -> controller.showWelcomeScreen());
        });

        // Add elements to content
        content.getChildren().addAll(title, grid, messageLabel, buttonBox);

        // Add the glass panel and content to the container
        contentContainer.getChildren().addAll(glassPanel, content);

        // Make the glass panel resize with the content
        glassPanel.widthProperty().bind(content.widthProperty().add(80));
        glassPanel.heightProperty().bind(content.heightProperty().add(80));

        // Center the content in the overlay
        overlay.getChildren().add(contentContainer);
        overlay.setAlignment(Pos.CENTER);

        // Add the overlay to the root
        root.getChildren().add(overlay);

        // Create scene
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Add entrance animations
        Platform.runLater(() -> animateEntrance(glassPanel, content));

        // Set focus on the username field
        Platform.runLater(() -> usernameField.requestFocus());
    }

    // Helper method to style buttons with hover effects
    private void styleButton(Button button, String defaultStyle, String hoverStyle) {
        button.setStyle(defaultStyle);

        // Hover effects
        button.setOnMouseEntered(e -> {
            button.setStyle(hoverStyle);
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.play();
        });

        button.setOnMouseExited(e -> {
            button.setStyle(defaultStyle);
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
        });
    }

    // Button click animation
    private void animateButtonClick(Button button) {
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), button);
        scaleDown.setToX(0.95);
        scaleDown.setToY(0.95);
        scaleDown.play();

        scaleDown.setOnFinished(e -> {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), button);
            scaleUp.setToX(1.0);
            scaleUp.setToY(1.0);
            scaleUp.play();
        });
    }

    // Entrance animation
    private void animateEntrance(Rectangle panel, VBox content) {
        // Initial state
        panel.setScaleX(0.8);
        panel.setScaleY(0.8);
        panel.setOpacity(0);
        content.setOpacity(0);
        content.setTranslateY(30);

        // Animations
        FadeTransition panelFade = new FadeTransition(Duration.millis(800), panel);
        panelFade.setFromValue(0);
        panelFade.setToValue(1.0);

        ScaleTransition panelScale = new ScaleTransition(Duration.millis(1000), panel);
        panelScale.setToX(1.0);
        panelScale.setToY(1.0);

        FadeTransition contentFade = new FadeTransition(Duration.millis(800), content);
        contentFade.setFromValue(0);
        contentFade.setToValue(1.0);
        contentFade.setDelay(Duration.millis(200));

        TranslateTransition contentSlide = new TranslateTransition(Duration.millis(1000), content);
        contentSlide.setFromY(30);
        contentSlide.setToY(0);
        contentSlide.setDelay(Duration.millis(200));

        // Play all animations
        ParallelTransition parallel = new ParallelTransition(panelFade, panelScale, contentFade, contentSlide);
        parallel.play();
    }

    // Shake animation for invalid login
    private TranslateTransition createShakeAnimation(Rectangle panel) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), panel);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        return shake;
    }

    // Helper method to convert Color to hex string
    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}