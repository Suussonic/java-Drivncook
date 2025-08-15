package com.drivncook.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import com.drivncook.service.MenuService;
import com.drivncook.model.Menu;

import java.util.ArrayList;
import java.util.List;

public class MenuView extends BorderPane {
    private final MenuService menuService = new MenuService();
    private final ListView<String> menuList = new ListView<>();
    private final VBox detailsBox = new VBox(12);
    private final Label errorLabel = new Label();
    private final TextField searchField = new TextField();
    private final ComboBox<String> sortBox = new ComboBox<>();
    private FilteredList<Menu> filteredMenus;
    private SortedList<Menu> sortedMenus;
    private List<Menu> menus = new ArrayList<>();

    public MenuView() {
        // Barre de recherche et tri
        searchField.setPromptText("Rechercher un menu...");
        sortBox.setItems(FXCollections.observableArrayList("Nom", "Prix"));
        sortBox.setPromptText("Trier par...");
        HBox searchSortBox = new HBox(10, searchField, sortBox);
        searchSortBox.setAlignment(Pos.CENTER_LEFT);
        searchSortBox.setPadding(new Insets(0, 0, 10, 0));

        // Liste des menus
        VBox leftBox = new VBox(10, new Label("Menus disponibles"), searchSortBox, menuList);
        leftBox.setPadding(new Insets(10));
        leftBox.setPrefWidth(260);
        leftBox.setAlignment(Pos.TOP_LEFT);
        menuList.setPrefHeight(400);

        // Détail du menu sélectionné
        detailsBox.setPadding(new Insets(18));
        detailsBox.setStyle("-fx-background-color: #181a20; -fx-background-radius: 16px; -fx-effect: dropshadow(gaussian, #181a20, 8, 0.2, 0, 2);");
        detailsBox.setAlignment(Pos.TOP_LEFT);
        detailsBox.setMinWidth(350);
        detailsBox.setMaxWidth(500);
        detailsBox.setSpacing(16);

        // Boutons d'action
        HBox actionButtons = new HBox(12);
        Button addBtn = new Button("Ajouter");
        addBtn.getStyleClass().addAll("button", "primary");
        Button editBtn = new Button("Modifier");
        editBtn.getStyleClass().addAll("button", "info");
        Button delBtn = new Button("Supprimer");
        delBtn.getStyleClass().addAll("button", "danger");
        actionButtons.getChildren().addAll(addBtn, editBtn, delBtn);
        actionButtons.setAlignment(Pos.CENTER_LEFT);

        // Gestion des actions
        addBtn.setOnAction(e -> openMenuForm(null));
        editBtn.setOnAction(e -> {
            String selected = menuList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Menu menu = menus.stream().filter(m -> selected.equals(m.getName())).findFirst().orElse(null);
                if (menu != null) openMenuForm(menu);
            }
        });
        delBtn.setOnAction(e -> {
            String selected = menuList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Menu menu = menus.stream().filter(m -> selected.equals(m.getName())).findFirst().orElse(null);
                if (menu != null) {
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer ce menu ?", ButtonType.YES, ButtonType.NO);
                    confirm.showAndWait().ifPresent(type -> {
                        if (type == ButtonType.YES) {
                            menuService.delete(menu.getId());
                            loadMenus();
                        }
                    });
                }
            }
        });

        VBox rightBox = new VBox(18, new Label("Détail du menu"), detailsBox, actionButtons, errorLabel);
        rightBox.setPadding(new Insets(10, 30, 10, 30));
        rightBox.setAlignment(Pos.TOP_LEFT);
        errorLabel.getStyleClass().add("error");

        setLeft(leftBox);
        setCenter(rightBox);
        setPadding(new Insets(18));
        loadMenus();
        menuList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> showMenuDetails(newVal));
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterMenus());
        sortBox.valueProperty().addListener((obs, oldVal, newVal) -> sortMenus());
    }

    private void loadMenus() {
        menuList.getItems().clear();
        errorLabel.setText("");
        try {
            menus = menuService.findAll();
            filteredMenus = new FilteredList<>(FXCollections.observableArrayList(menus), p -> true);
            sortedMenus = new SortedList<>(filteredMenus);
            updateMenuList();
            if (menus.isEmpty()) {
                errorLabel.setText("Aucun menu trouvé.");
            } else {
                menuList.getSelectionModel().selectFirst();
                showMenuDetails(sortedMenus.get(0).getName());
            }
        } catch (Exception e) {
            errorLabel.setText("Erreur lors de la récupération des menus : " + e.getMessage());
        }
    }

    private void updateMenuList() {
        menuList.getItems().clear();
        for (Menu menu : sortedMenus) {
            menuList.getItems().add(menu.getName());
        }
    }

    private void showMenuDetails(String menuName) {
        detailsBox.getChildren().clear();
        if (menuName == null) return;
        Menu menu = menus.stream().filter(m -> menuName.equals(m.getName())).findFirst().orElse(null);
        if (menu == null) {
            detailsBox.getChildren().add(new Label("Menu introuvable."));
            return;
        }
        Label title = new Label(menu.getName());
        title.setFont(Font.font("System", FontWeight.BOLD, 22));
        title.setStyle("-fx-text-fill: #00d1b2;");
        Label desc = new Label(menu.getDesc());
        desc.setStyle("-fx-text-fill: #b5b5b5; -fx-font-size: 15px;");
        Label price = new Label("Prix : " + menu.getPrice() + " €");
        price.setStyle("-fx-text-fill: #fff; -fx-font-size: 16px;");
        Label id = new Label("ID : " + menu.getId());
        id.setStyle("-fx-text-fill: #b5b5b5; -fx-font-size: 12px;");
        detailsBox.getChildren().addAll(title, desc, price, id);
    }

    private void filterMenus() {
        String filter = searchField.getText().toLowerCase();
        filteredMenus.setPredicate(menu ->
            menu.getName().toLowerCase().contains(filter) ||
            (menu.getDesc() != null && menu.getDesc().toLowerCase().contains(filter))
        );
        updateMenuList();
        if (!sortedMenus.isEmpty()) {
            menuList.getSelectionModel().selectFirst();
            showMenuDetails(sortedMenus.get(0).getName());
        } else {
            detailsBox.getChildren().clear();
        }
    }

    private void sortMenus() {
        String sortBy = sortBox.getValue();
        if (sortBy == null) return;
        sortedMenus.setComparator((m1, m2) -> {
            switch (sortBy) {
                case "Nom":
                    return m1.getName().compareToIgnoreCase(m2.getName());
                case "Prix":
                    return Double.compare(m1.getPrice(), m2.getPrice());
                default:
                    return 0;
            }
        });
        updateMenuList();
        if (!sortedMenus.isEmpty()) {
            menuList.getSelectionModel().selectFirst();
            showMenuDetails(sortedMenus.get(0).getName());
        } else {
            detailsBox.getChildren().clear();
        }
    }

    // Ajout d'un formulaire popup pour ajout/modification
    private void openMenuForm(Menu menu) {
        Dialog<Menu> dialog = new Dialog<>();
        dialog.setTitle(menu == null ? "Ajouter un menu" : "Modifier le menu");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(menu != null ? menu.getName() : "");
        TextField descField = new TextField(menu != null ? menu.getDesc() : "");
        TextField priceField = new TextField(menu != null ? String.valueOf(menu.getPrice()) : "");

        grid.add(new Label("Nom :"), 0, 0); grid.add(nameField, 1, 0);
        grid.add(new Label("Description :"), 0, 1); grid.add(descField, 1, 1);
        grid.add(new Label("Prix :"), 0, 2); grid.add(priceField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    double price = Double.parseDouble(priceField.getText());
                    Menu m = menu == null ? new Menu() : menu;
                    m.setName(nameField.getText());
                    m.setDesc(descField.getText());
                    m.setPrice(price);
                    return m;
                } catch (Exception ex) {
                    return null;
                }
            }
            return null;
        });
        dialog.showAndWait().ifPresent(result -> {
            if (result != null) {
                menuService.save(result);
                loadMenus();
            }
        });
    }
}
