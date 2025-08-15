package com.drivncook.ui;

import com.drivncook.model.Order;
import com.drivncook.service.OrderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.Arrays;

public class OrderView extends VBox {
    private final OrderService orderService = new OrderService();
    private final ObservableList<Order> orders = FXCollections.observableArrayList();
    private final TableView<Order> table = new TableView<>();
    private final TextField searchField = new TextField();
    private final TextField userIdField = new TextField();
    private final TextField dishIdsField = new TextField();
    private final TextField totalField = new TextField();
    private final ComboBox<String> statusBox = new ComboBox<>();
    private final Button addBtn = new Button("Ajouter");
    private final Button updateBtn = new Button("Modifier");
    private final Button cancelBtn = new Button("Annuler");
    private final Label errorLabel = new Label();
    private Order editingOrder = null;

    public OrderView() {
        setSpacing(18);
        setPadding(new Insets(24));
        setStyle("-fx-background-color: #23272f; -fx-background-radius: 18px;");

        Label title = new Label("Gestion des commandes");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #fff;");
        getChildren().add(title);

        // Recherche
        searchField.setPromptText("Rechercher (client, statut)");
        searchField.textProperty().addListener((obs, old, val) -> refreshTable());
        HBox searchBox = new HBox(searchField);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0, 0, 10, 0));
        getChildren().add(searchBox);

        // Table
    TableColumn<Order, String> userCol = new TableColumn<>("Client");
    userCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUserId()));
    TableColumn<Order, String> dishesCol = new TableColumn<>("Plats");
    dishesCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.join(", ", data.getValue().getDishIds())));
    TableColumn<Order, String> totalCol = new TableColumn<>("Total (€)");
    totalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", data.getValue().getTotal())));
    TableColumn<Order, String> statusCol = new TableColumn<>("Statut");
    statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        TableColumn<Order, Void> actionCol = new TableColumn<>("");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Éditer");
            private final Button delBtn = new Button("Supprimer");
            {
                editBtn.setStyle("-fx-background-color: #00bfff; -fx-text-fill: #fff; -fx-background-radius: 6px;");
                delBtn.setStyle("-fx-background-color: #ff4d4f; -fx-text-fill: #fff; -fx-background-radius: 6px;");
                editBtn.setOnAction(e -> editOrder(getTableView().getItems().get(getIndex())));
                delBtn.setOnAction(e -> deleteOrder(getTableView().getItems().get(getIndex())));
                HBox box = new HBox(8, editBtn, delBtn);
                box.setAlignment(Pos.CENTER);
                setGraphic(box);
            }
            @Override public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : getGraphic());
            }
        });
        table.getColumns().addAll(userCol, dishesCol, totalCol, statusCol, actionCol);
        table.setItems(orders);
        table.setPrefHeight(320);
        table.setStyle("-fx-background-color: #23272f; -fx-text-fill: #fff; -fx-background-radius: 12px;");
        getChildren().add(table);

        // Formulaire
        userIdField.setPromptText("ID client");
        dishIdsField.setPromptText("ID plats (séparés par ,)");
        totalField.setPromptText("Total (€)");
        statusBox.setPromptText("Statut");
        statusBox.getItems().addAll("en attente", "payée", "livrée", "annulée");
        HBox formBox = new HBox(12, userIdField, dishIdsField, totalField, statusBox, addBtn, updateBtn, cancelBtn);
        formBox.setAlignment(Pos.CENTER_LEFT);
        getChildren().add(formBox);
        updateBtn.setVisible(false);
        cancelBtn.setVisible(false);

        addBtn.setStyle("-fx-background-color: #00bfff; -fx-text-fill: #fff; -fx-background-radius: 6px;");
        updateBtn.setStyle("-fx-background-color: #ffc107; -fx-text-fill: #23272f; -fx-background-radius: 6px;");
        cancelBtn.setStyle("-fx-background-color: #23272f; -fx-text-fill: #fff; -fx-background-radius: 6px; border: 1px solid #fff;");

        addBtn.setOnAction(e -> addOrder());
        updateBtn.setOnAction(e -> updateOrder());
        cancelBtn.setOnAction(e -> cancelEdit());

        errorLabel.setStyle("-fx-text-fill: #ff4d4f; -fx-font-size: 14px;");
        getChildren().add(errorLabel);

        loadOrders();
    }

    private void loadOrders() {
        orders.setAll(orderService.findAll());
        refreshTable();
    }

    private void refreshTable() {
        String search = searchField.getText().toLowerCase();
        if (search.isEmpty()) {
            table.setItems(orders);
        } else {
            table.setItems(orders.filtered(o ->
                (o.getUserId() != null && o.getUserId().toLowerCase().contains(search)) ||
                (o.getStatus() != null && o.getStatus().toLowerCase().contains(search))
            ));
        }
    }

    private void addOrder() {
        errorLabel.setText("");
        String userId = userIdField.getText().trim();
        String dishIds = dishIdsField.getText().trim();
        String totalStr = totalField.getText().trim();
        String status = statusBox.getValue();
        if (userId.isEmpty() || dishIds.isEmpty() || totalStr.isEmpty() || status == null) {
            errorLabel.setText("Tous les champs sont obligatoires.");
            return;
        }
        double total;
        try { total = Double.parseDouble(totalStr); } catch (Exception e) { errorLabel.setText("Total invalide"); return; }
        Order order = new Order(null, userId, Arrays.asList(dishIds.split(",")), total, status);
        orderService.save(order);
        loadOrders();
        userIdField.clear(); dishIdsField.clear(); totalField.clear(); statusBox.setValue(null);
    }

    private void editOrder(Order order) {
        editingOrder = order;
        userIdField.setText(order.getUserId());
        dishIdsField.setText(String.join(",", order.getDishIds()));
        totalField.setText(String.valueOf(order.getTotal()));
        statusBox.setValue(order.getStatus());
        addBtn.setVisible(false);
        updateBtn.setVisible(true);
        cancelBtn.setVisible(true);
    }

    private void updateOrder() {
        if (editingOrder == null) return;
        editingOrder.setUserId(userIdField.getText().trim());
        editingOrder.setDishIds(Arrays.asList(dishIdsField.getText().trim().split(",")));
        try { editingOrder.setTotal(Double.parseDouble(totalField.getText().trim())); } catch (Exception e) { errorLabel.setText("Total invalide"); return; }
        editingOrder.setStatus(statusBox.getValue());
        orderService.save(editingOrder);
        loadOrders();
        cancelEdit();
    }

    private void cancelEdit() {
        editingOrder = null;
        userIdField.clear(); dishIdsField.clear(); totalField.clear(); statusBox.setValue(null);
        addBtn.setVisible(true);
        updateBtn.setVisible(false);
        cancelBtn.setVisible(false);
    }

    private void deleteOrder(Order order) {
        orderService.delete(order.getId());
        loadOrders();
    }
}
