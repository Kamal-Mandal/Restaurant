package com.lude.resturantmanagementsystem;

import javafx.application.Application;
import javafx.stage.Stage;
import com.lude.resturantmanagementsystem.Middleware.ApplicationController;

public class RestaurantManagementSystem extends Application {

    @Override
    public void start(Stage primaryStage) {
        ApplicationController controller = new ApplicationController(primaryStage);
        controller.showWelcomeScreen();

        primaryStage.setTitle("Restaurant Management System");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}