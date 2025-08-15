package com.drivncook.ui;

import com.drivncook.model.LoyaltyCard;
import com.drivncook.service.LoyaltyCardService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoyaltyView extends VBox {
    private final LoyaltyCardService loyaltyService = new LoyaltyCardService();
    private final ObservableList<LoyaltyCard> cards = FXCollections.observableArrayList();
    private final TableView<LoyaltyCard> table = new TableView<>();
    private final TextField searchField = new TextField();
    private final TextField userIdField = new TextField();
    private final TextField pointsField = new TextField();
    private final TextField advantagesField = new TextField();
    private final Button addBtn = new Button("Ajouter");
    private final Button updateBtn = new Button("Modifier");
    private final Button cancelBtn = new Button("Annuler");
    private final Label errorLabel = new Label();
    private LoyaltyCard editingCard = null;

    public LoyaltyView() {
        setSpacing(18);
        setPadding(new Insets(24));
        setStyle("-fx-background-color: #23272f; -fx-background-radius: 18px;");

        Label title = new Label("Gestion des cartes de fidélité");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #fff;");
        getChildren().add(title);

        // Recherche
        searchField.setPromptText("Rechercher (client, avantages)");
        searchField.textProperty().addListener((obs, old, val) -> refreshTable());
        HBox searchBox = new HBox(searchField);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0, 0, 10, 0));
        getChildren().add(searchBox);

        // Table
    TableColumn<LoyaltyCard, String> userCol = new TableColumn<>("ID Client");
    userCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUserId()));
    TableColumn<LoyaltyCard, String> pointsCol = new TableColumn<>("Points");
    pointsCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getPoints())));
    TableColumn<LoyaltyCard, String> advCol = new TableColumn<>("Avantages");
    advCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAdvantages()));
        TableColumn<LoyaltyCard, Void> actionCol = new TableColumn<>("");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Éditer");
            private final Button delBtn = new Button("Supprimer");
            {
                editBtn.setStyle("-fx-background-color: #00bfff; -fx-text-fill: #fff; -fx-background-radius: 6px;");
                delBtn.setStyle("-fx-background-color: #ff4d4f; -fx-text-fill: #fff; -fx-background-radius: 6px;");
                editBtn.setOnAction(e -> editCard(getTableView().getItems().get(getIndex())));
                delBtn.setOnAction(e -> deleteCard(getTableView().getItems().get(getIndex())));
                HBox box = new HBox(8, editBtn, delBtn);
                box.setAlignment(Pos.CENTER);
                setGraphic(box);
            }
            @Override public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : getGraphic());
            }
        });
        table.getColumns().addAll(userCol, pointsCol, advCol, actionCol);
        table.setItems(cards);
        table.setPrefHeight(320);
        table.setStyle("-fx-background-color: #23272f; -fx-text-fill: #fff; -fx-background-radius: 12px;");
        getChildren().add(table);

        // Formulaire
        userIdField.setPromptText("ID client");
        pointsField.setPromptText("Points");
        advantagesField.setPromptText("Avantages");
        HBox formBox = new HBox(12, userIdField, pointsField, advantagesField, addBtn, updateBtn, cancelBtn);
        formBox.setAlignment(Pos.CENTER_LEFT);
        getChildren().add(formBox);
        updateBtn.setVisible(false);
        cancelBtn.setVisible(false);

        addBtn.setStyle("-fx-background-color: #00bfff; -fx-text-fill: #fff; -fx-background-radius: 6px;");
        updateBtn.setStyle("-fx-background-color: #ffc107; -fx-text-fill: #23272f; -fx-background-radius: 6px;");
        cancelBtn.setStyle("-fx-background-color: #23272f; -fx-text-fill: #fff; -fx-background-radius: 6px; border: 1px solid #fff;");

        addBtn.setOnAction(e -> addCard());
        updateBtn.setOnAction(e -> updateCard());
        cancelBtn.setOnAction(e -> cancelEdit());

        errorLabel.setStyle("-fx-text-fill: #ff4d4f; -fx-font-size: 14px;");
        getChildren().add(errorLabel);

        loadCards();
    }

    private void loadCards() {
        cards.setAll(loyaltyService.findAll());
        refreshTable();
    }

    private void refreshTable() {
        String search = searchField.getText().toLowerCase();
        if (search.isEmpty()) {
            table.setItems(cards);
        } else {
            table.setItems(cards.filtered(c ->
                (c.getUserId() != null && c.getUserId().toLowerCase().contains(search)) ||
                (c.getAdvantages() != null && c.getAdvantages().toLowerCase().contains(search))
            ));
        }
    }

    private void addCard() {
        errorLabel.setText("");
        String userId = userIdField.getText().trim();
        String pointsStr = pointsField.getText().trim();
        String advantages = advantagesField.getText().trim();
        if (userId.isEmpty() || pointsStr.isEmpty()) {
            errorLabel.setText("ID client et points obligatoires.");
            return;
        }
        int points;
        try { points = Integer.parseInt(pointsStr); } catch (Exception e) { errorLabel.setText("Points invalides"); return; }
        LoyaltyCard card = new LoyaltyCard(null, userId, points, advantages);
        loyaltyService.save(card);
        loadCards();
        userIdField.clear(); pointsField.clear(); advantagesField.clear();
    }

    private void editCard(LoyaltyCard card) {
        editingCard = card;
        userIdField.setText(card.getUserId());
        pointsField.setText(String.valueOf(card.getPoints()));
        advantagesField.setText(card.getAdvantages());
        addBtn.setVisible(false);
        updateBtn.setVisible(true);
        cancelBtn.setVisible(true);
    }

    private void updateCard() {
        if (editingCard == null) return;
        editingCard.setUserId(userIdField.getText().trim());
        try { editingCard.setPoints(Integer.parseInt(pointsField.getText().trim())); } catch (Exception e) { errorLabel.setText("Points invalides"); return; }
        editingCard.setAdvantages(advantagesField.getText().trim());
        loyaltyService.save(editingCard);
        loadCards();
        cancelEdit();
    }

    private void cancelEdit() {
        editingCard = null;
        userIdField.clear(); pointsField.clear(); advantagesField.clear();
        addBtn.setVisible(true);
        updateBtn.setVisible(false);
        cancelBtn.setVisible(false);
    }

    private void deleteCard(LoyaltyCard card) {
        loyaltyService.delete(card.getId());
        loadCards();
    }
}
