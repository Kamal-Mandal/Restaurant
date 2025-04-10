package com.lude.resturantmanagementsystem.FrontEnd;

import com.lude.resturantmanagementsystem.Middleware.ApplicationController;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class WelcomeScreenUI {
    private Stage stage;
    private ApplicationController controller;

    // Define color theme for restaurant - warm, appetizing colors
    private final Color PRIMARY_DARK = Color.web("#5D2A16");     // Rich brown
    private final Color PRIMARY_MEDIUM = Color.web("#A35E38");   // Cinnamon
    private final Color PRIMARY_LIGHT = Color.web("#F3C08D");    // Pale gold
    private final Color ACCENT_COLOR = Color.web("#D64C31");     // Tomato red
    private final Color TEXT_LIGHT = Color.web("#FFF8EA");       // Creamy white

    public WelcomeScreenUI(Stage stage, ApplicationController controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void show() {
        // Create a StackPane as the root container
        StackPane root = new StackPane();

        // Create a layered background
        try {
            // Primary background image
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
        glassPanel.setWidth(550);
        glassPanel.setHeight(550);
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

        // Content VBox
        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        content.setMaxWidth(500);
        content.setMaxHeight(550);

        // Logo/Icon (you can replace this with your actual restaurant logo)
        try {
            ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/images/restaurant-logo.png")));
            logoView.setFitHeight(80);
            logoView.setPreserveRatio(true);
            content.getChildren().add(logoView);
        } catch (Exception e) {
            // Fallback if logo not found - create text-based logo
            Text logoText = new Text("RMS");
            logoText.setFont(Font.font("Script MT Bold", FontWeight.BOLD, 60));
            logoText.setFill(ACCENT_COLOR);

            DropShadow logoShadow = new DropShadow();
            logoShadow.setRadius(5.0);
            logoShadow.setOffsetX(3.0);
            logoShadow.setOffsetY(3.0);
            logoShadow.setColor(Color.rgb(0, 0, 0, 0.7));
            logoText.setEffect(logoShadow);

            content.getChildren().add(logoText);
        }

        // Title with enhanced styling
        Text title = new Text("Restaurant Management System");
        title.setFont(Font.font("Palatino Linotype", FontWeight.BOLD, 34));
        title.setFill(TEXT_LIGHT);
        title.setTextAlignment(TextAlignment.CENTER);

        // Add fancy text effect
        DropShadow titleShadow = new DropShadow();
        titleShadow.setRadius(10.0);
        titleShadow.setOffsetX(4.0);
        titleShadow.setOffsetY(4.0);
        titleShadow.setColor(Color.rgb(0, 0, 0, 0.7));

        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setRadius(2.0);
        innerShadow.setColor(ACCENT_COLOR);
        innerShadow.setInput(titleShadow);
        title.setEffect(innerShadow);

        // Subtitle
        Text subtitle = new Text("Delicious Food, Delightful Experience");
        subtitle.setFont(Font.font("Calibri", FontWeight.NORMAL, 18));
        subtitle.setFill(TEXT_LIGHT);
        subtitle.setTextAlignment(TextAlignment.CENTER);

        // Welcome text
        Text welcomeText = new Text("Welcome! Please select your role");
        welcomeText.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        welcomeText.setFill(PRIMARY_LIGHT);

        // Define button styling - more food-themed colors
        String customerButtonStyle =
                "-fx-background-color: linear-gradient(to bottom, #D64C31, #A93226);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 30px;" +
                        "-fx-padding: 15px 30px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 2, 2);";

        String customerButtonHoverStyle =
                "-fx-background-color: linear-gradient(to bottom, #F75C41, #C13A2E);" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 30px;" +
                        "-fx-padding: 15px 30px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 15, 0, 3, 3);";

        String adminButtonStyle =
                "-fx-background-color: linear-gradient(to bottom, #A35E38, #5D2A16);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 30px;" +
                        "-fx-padding: 15px 30px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 2, 2);";

        String adminButtonHoverStyle =
                "-fx-background-color: linear-gradient(to bottom, #C27048, #7D3A20);" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 30px;" +
                        "-fx-padding: 15px 30px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 15, 0, 3, 3);";

        // Customer button with improved styling
        Button customerButton = new Button("Customer");
        customerButton.setPrefWidth(300);
        customerButton.setPrefHeight(60);
        customerButton.setStyle(customerButtonStyle);
        styleButton(customerButton, customerButtonStyle, customerButtonHoverStyle);

        // Add icon to customer button if available
        try {
            ImageView customerIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/customer-icon.png")));
            customerIcon.setFitHeight(24);
            customerIcon.setFitWidth(24);
            customerButton.setGraphic(customerIcon);
            customerButton.setGraphicTextGap(10);
        } catch (Exception e) {
            // Continue without icon if not found
        }

        customerButton.setOnAction(e -> {
            animateButtonClick(customerButton);
            controller.showCustomerDashboard();
        });

        // Admin button with improved styling
        Button adminButton = new Button("Admin");
        adminButton.setPrefWidth(300);
        adminButton.setPrefHeight(60);
        adminButton.setStyle(adminButtonStyle);
        styleButton(adminButton, adminButtonStyle, adminButtonHoverStyle);

        // Add icon to admin button if available
        try {
            ImageView adminIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/admin-icon.png")));
            adminIcon.setFitHeight(24);
            adminIcon.setFitWidth(24);
            adminButton.setGraphic(adminIcon);
            adminButton.setGraphicTextGap(10);
        } catch (Exception e) {
            // Continue without icon if not found
        }

        adminButton.setOnAction(e -> {
            animateButtonClick(adminButton);
            controller.showAdminLogin();
        });

        // Add spacers for better layout
        Region spacer1 = new Region();
        spacer1.setPrefHeight(10);
        Region spacer2 = new Region();
        spacer2.setPrefHeight(10);
        Region bottomSpacer = new Region();
        bottomSpacer.setPrefHeight(20);

        // Add footer text
        Text footerText = new Text("© 2025 Your Restaurant Name");
        footerText.setFont(Font.font("Calibri", 14));
        footerText.setFill(Color.web("#AAA"));

        // Add all elements to content VBox
        content.getChildren().addAll(
                title,
                subtitle,
                spacer1,
                welcomeText,
                spacer2,
                customerButton,
                adminButton,
                bottomSpacer,
                footerText
        );

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
        stage.setTitle("Restaurant Management System");
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.setMinWidth(600);
        stage.setMinHeight(550);

        // Add entrance animations
        Platform.runLater(() -> animateEntrance(glassPanel, content));
    }

    // Enhanced method for button styling
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

    // Enhanced button click animation
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

    // Enhanced entrance animation
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
}