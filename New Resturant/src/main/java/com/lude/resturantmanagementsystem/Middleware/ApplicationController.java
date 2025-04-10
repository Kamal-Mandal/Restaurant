package com.lude.resturantmanagementsystem.Middleware;




import com.lude.resturantmanagementsystem.BackEnd.Database;
import com.lude.resturantmanagementsystem.FrontEnd.AdminDashboardUI;
import com.lude.resturantmanagementsystem.FrontEnd.AdminLoginUI;
import com.lude.resturantmanagementsystem.FrontEnd.CustomerDashboardUI;
import com.lude.resturantmanagementsystem.FrontEnd.WelcomeScreenUI;
import javafx.stage.Stage;

public class ApplicationController {
    private Stage primaryStage;
    private Database database;
    private MenuService menuService;
    private OrderService orderService;
    private AuthService authService;

    public ApplicationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.database = new Database();
        this.database.initializeDatabase();

        this.menuService = new MenuService(database);
        this.orderService = new OrderService(database);
        this.authService = new AuthService();
    }

    public void showWelcomeScreen() {
        WelcomeScreenUI welcomeScreen = new WelcomeScreenUI(primaryStage, this);
        welcomeScreen.show();
    }

    public void showAdminLogin() {
        AdminLoginUI loginScreen = new AdminLoginUI(primaryStage, this, authService);
        loginScreen.show();
    }

    public void showAdminDashboard() {
        AdminDashboardUI dashboard = new AdminDashboardUI(primaryStage, this, menuService, orderService);
        dashboard.show();
    }

    public void showCustomerDashboard() {
        CustomerDashboardUI dashboard = new CustomerDashboardUI(primaryStage, this, menuService, orderService);
        dashboard.show();
    }

    public MenuService getMenuService() {
        return menuService;
    }

    public OrderService getOrderService() {
        return orderService;
    }
}