package com.drivncook.ui;

import com.drivncook.model.Event;
import com.drivncook.service.EventService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;

public class EventView extends VBox {
    private final EventService eventService = new EventService();
    private final ObservableList<Event> eventList = FXCollections.observableArrayList();
    private final TableView<Event> table = new TableView<>();
    private final TextField searchField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField dateField = new TextField();
    private final TextField locationField = new TextField();
    private final TextArea descriptionArea = new TextArea();
    private final Button addButton = new Button("Ajouter");
    private final Button updateButton = new Button("Modifier");
    private final Button deleteButton = new Button("Supprimer");
    private final Label errorLabel = new Label("");

    public EventView() {
        setSpacing(16);
        setPadding(new Insets(24, 32, 24, 32));
        Label title = new Label("Gestion des événements");
        title.getStyleClass().add("label");
        getChildren().add(title);

        searchField.setPromptText("Rechercher par nom, lieu ou date...");
        searchField.getStyleClass().add("text-field");
        searchField.textProperty().addListener((obs, old, val) -> filterEvents(val));
        getChildren().add(searchField);

        table.setItems(eventList);
        TableColumn<Event, String> nameCol = new TableColumn<>("Nom");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        TableColumn<Event, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate()));
        TableColumn<Event, String> locationCol = new TableColumn<>("Lieu");
        locationCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLocation()));
        TableColumn<Event, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        table.getColumns().addAll(nameCol, dateCol, locationCol, descCol);
    table.setPrefHeight(200);
    table.setStyle("-fx-background-color: #23272f; -fx-text-fill: #fff; -fx-background-radius: 12px; -fx-table-cell-border-color: #23272f; -fx-table-header-background: #23272f; -fx-table-header-border-color: #23272f;");
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    getChildren().add(table);

        nameField.setPromptText("Nom de l'événement");
        dateField.setPromptText("Date (ex: 2025-09-01)");
        locationField.setPromptText("Lieu");
        descriptionArea.setPromptText("Description");
        HBox formBox = new HBox(12, nameField, dateField, locationField, descriptionArea);
        getChildren().add(formBox);

        HBox buttonBox = new HBox(12, addButton, updateButton, deleteButton);
        addButton.getStyleClass().add("button primary");
        updateButton.getStyleClass().add("button info");
        deleteButton.getStyleClass().add("button danger");
        getChildren().add(buttonBox);

        errorLabel.getStyleClass().add("label error");
        getChildren().add(errorLabel);

        addButton.setOnAction(e -> addEvent());
        updateButton.setOnAction(e -> updateEvent());
        deleteButton.setOnAction(e -> deleteEvent());
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> fillForm(selected));

        refreshEvents();
    }

    private void refreshEvents() {
        eventList.setAll(eventService.findAll());
    }

    private void filterEvents(String query) {
        if (query == null || query.isEmpty()) {
            refreshEvents();
            return;
        }
        String q = query.toLowerCase();
        List<Event> filtered = new ArrayList<>();
        for (Event ev : eventService.findAll()) {
            if (ev.getName().toLowerCase().contains(q) ||
                ev.getLocation().toLowerCase().contains(q) ||
                ev.getDate().toLowerCase().contains(q)) {
                filtered.add(ev);
            }
        }
        eventList.setAll(filtered);
    }

    private void fillForm(Event ev) {
        if (ev == null) {
            nameField.clear();
            dateField.clear();
            locationField.clear();
            descriptionArea.clear();
            return;
        }
        nameField.setText(ev.getName());
        dateField.setText(ev.getDate());
        locationField.setText(ev.getLocation());
        descriptionArea.setText(ev.getDescription());
    }

    private void addEvent() {
        errorLabel.setText("");
        String name = nameField.getText().trim();
        String date = dateField.getText().trim();
        String location = locationField.getText().trim();
        String desc = descriptionArea.getText().trim();
        if (name.isEmpty() || date.isEmpty() || location.isEmpty()) {
            errorLabel.setText("Nom, date et lieu obligatoires.");
            return;
        }
        Event ev = new Event(null, name, desc, date, location);
        eventService.save(ev);
        refreshEvents();
        clearForm();
    }

    private void updateEvent() {
        errorLabel.setText("");
        Event selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Sélectionnez un événement à modifier.");
            return;
        }
        String name = nameField.getText().trim();
        String date = dateField.getText().trim();
        String location = locationField.getText().trim();
        String desc = descriptionArea.getText().trim();
        if (name.isEmpty() || date.isEmpty() || location.isEmpty()) {
            errorLabel.setText("Nom, date et lieu obligatoires.");
            return;
        }
        Event ev = new Event(selected.getId(), name, desc, date, location);
        eventService.save(ev);
        refreshEvents();
        clearForm();
    }

    private void deleteEvent() {
        errorLabel.setText("");
        Event selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Sélectionnez un événement à supprimer.");
            return;
        }
        eventService.delete(selected.getId());
        refreshEvents();
        clearForm();
    }

    private void clearForm() {
        nameField.clear();
        dateField.clear();
        locationField.clear();
        descriptionArea.clear();
        table.getSelectionModel().clearSelection();
    }
}
