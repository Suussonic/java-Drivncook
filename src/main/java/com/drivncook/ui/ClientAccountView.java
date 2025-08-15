package com.drivncook.ui;

import com.drivncook.model.User;
import com.drivncook.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ClientAccountView extends VBox {
    private final UserService userService = new UserService();
    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final TableView<User> table = new TableView<>();
    private final TextField searchField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField emailField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button addBtn = new Button("Ajouter");
    private final Button updateBtn = new Button("Modifier");
    private final Button cancelBtn = new Button("Annuler");
    private final Label errorLabel = new Label();
    private User editingUser = null;

    public ClientAccountView() {
        setSpacing(18);
        setPadding(new Insets(24));
        setStyle("-fx-background-color: #23272f; -fx-background-radius: 18px;");

        Label title = new Label("Gestion des comptes clients");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #fff;");
        getChildren().add(title);

        // Recherche
        searchField.setPromptText("Rechercher (nom, email)");
        searchField.textProperty().addListener((obs, old, val) -> refreshTable());
        HBox searchBox = new HBox(searchField);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0, 0, 10, 0));
        getChildren().add(searchBox);

        // Table
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        TableColumn<User, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
        TableColumn<User, String> prenomCol = new TableColumn<>("Prénom");
        prenomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPrenom()));
        TableColumn<User, String> adresseCol = new TableColumn<>("Adresse");
        adresseCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAdresseLivraison()));
        TableColumn<User, String> dateCol = new TableColumn<>("Date de naissance");
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDateNaissance()));
        TableColumn<User, String> roleCol = new TableColumn<>("Rôle");
        roleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRole()));
        TableColumn<User, Void> actionCol = new TableColumn<>("");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Éditer");
            private final Button delBtn = new Button("Supprimer");
            {
                editBtn.setStyle("-fx-background-color: #00bfff; -fx-text-fill: #fff; -fx-background-radius: 6px;");
                delBtn.setStyle("-fx-background-color: #ff4d4f; -fx-text-fill: #fff; -fx-background-radius: 6px;");
                editBtn.setOnAction(e -> editUser(getTableView().getItems().get(getIndex())));
                delBtn.setOnAction(e -> deleteUser(getTableView().getItems().get(getIndex())));
                HBox box = new HBox(8, editBtn, delBtn);
                box.setAlignment(Pos.CENTER);
                setGraphic(box);
            }
            @Override public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : getGraphic());
            }
        });
        table.getColumns().setAll(emailCol, nomCol, prenomCol, adresseCol, dateCol, roleCol, actionCol);
        table.setItems(users);
    table.setPrefHeight(320);
    table.setStyle("-fx-background-color: #23272f; -fx-text-fill: #fff; -fx-background-radius: 12px; -fx-table-cell-border-color: #23272f; -fx-table-header-background: #23272f; -fx-table-header-border-color: #23272f;");
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    getChildren().add(table);

    // ...formulaire supprimé...

        loadUsers();
    }

    private void loadUsers() {
        users.setAll(userService.findAll());
        refreshTable();
    }

    private void refreshTable() {
        String search = searchField.getText().toLowerCase();
        if (search.isEmpty()) {
            table.setItems(users);
        } else {
            table.setItems(users.filtered(u ->
                (u.getNom() != null && u.getNom().toLowerCase().contains(search)) ||
                (u.getEmail() != null && u.getEmail().toLowerCase().contains(search))
            ));
        }
    }

    private void addUser() {
        errorLabel.setText("");
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Tous les champs sont obligatoires.");
            return;
        }
    User user = new User(null, name, email, password, null);
        userService.save(user);
        loadUsers();
        nameField.clear(); emailField.clear(); passwordField.clear();
    }

    private void editUser(User user) {
        editingUser = user;
    nameField.setText(user.getNom());
        emailField.setText(user.getEmail());
        passwordField.clear();
        addBtn.setVisible(false);
        updateBtn.setVisible(true);
        cancelBtn.setVisible(true);
    }

    private void updateUser() {
        if (editingUser == null) return;
    editingUser.setNom(nameField.getText().trim());
        editingUser.setEmail(emailField.getText().trim());
        if (!passwordField.getText().isEmpty()) {
            editingUser.setPassword(passwordField.getText());
        }
        userService.save(editingUser);
        loadUsers();
        cancelEdit();
    }

    private void cancelEdit() {
        editingUser = null;
        nameField.clear(); emailField.clear(); passwordField.clear();
        addBtn.setVisible(true);
        updateBtn.setVisible(false);
        cancelBtn.setVisible(false);
    }

    private void deleteUser(User user) {
        userService.delete(user.getId());
        loadUsers();
    }
}
