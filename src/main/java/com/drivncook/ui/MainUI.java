package com.drivncook.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        TabPane tabs = new TabPane();
        tabs.getTabs().addAll(
            new Tab("Clients", new ClientAccountView()),
            new Tab("Menus", new MenuView()),
            new Tab("Plats", new DishView()),
            new Tab("Commandes", new OrderView()),
            new Tab("Fidélité", new LoyaltyView()),
            new Tab("Événements", new EventView()),
            new Tab("Avis", new ReviewView())
        );
        primaryStage.setTitle("Driv'n Cook - Gestion Clients");
        primaryStage.setScene(new Scene(tabs, 900, 600));
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args); }
}
