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
            new Tab("Événements", new EventView())
        );
    Scene scene = new Scene(tabs, 900, 600);
    scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
    primaryStage.setTitle("Driv'n Cook - Gestion Clients");
    // Ajout du logo
    primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/logo.png")));
    primaryStage.setScene(scene);
    primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}
