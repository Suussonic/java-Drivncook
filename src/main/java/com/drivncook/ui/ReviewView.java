package com.drivncook.ui;

import com.drivncook.model.Review;
import com.drivncook.service.ReviewService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;

public class ReviewView extends VBox {
    private final ReviewService reviewService = new ReviewService();
    private final ObservableList<Review> reviewList = FXCollections.observableArrayList();
    private final TableView<Review> table = new TableView<>();
    private final TextField searchField = new TextField();
    private final TextField userIdField = new TextField();
    private final TextArea commentArea = new TextArea();
    private final Spinner<Integer> ratingSpinner = new Spinner<>(1, 5, 5);
    private final Button addButton = new Button("Ajouter");
    private final Button updateButton = new Button("Modifier");
    private final Button deleteButton = new Button("Supprimer");
    private final Label errorLabel = new Label("");

    public ReviewView() {
        setSpacing(16);
        setPadding(new Insets(24, 32, 24, 32));
        Label title = new Label("Gestion des avis clients");
        title.getStyleClass().add("label");
        getChildren().add(title);

        searchField.setPromptText("Rechercher par utilisateur ou commentaire...");
        searchField.getStyleClass().add("text-field");
        searchField.textProperty().addListener((obs, old, val) -> filterReviews(val));
        getChildren().add(searchField);

        table.setItems(reviewList);
        TableColumn<Review, String> userCol = new TableColumn<>("Utilisateur");
        userCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUserId()));
        TableColumn<Review, String> commentCol = new TableColumn<>("Commentaire");
        commentCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getComment()));
        TableColumn<Review, Integer> ratingCol = new TableColumn<>("Note");
        ratingCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getRating()).asObject());
        table.getColumns().addAll(userCol, commentCol, ratingCol);
        table.setPrefHeight(200);
        getChildren().add(table);

        userIdField.setPromptText("ID utilisateur");
        commentArea.setPromptText("Commentaire");
        ratingSpinner.setEditable(true);
        HBox formBox = new HBox(12, userIdField, commentArea, ratingSpinner);
        getChildren().add(formBox);

        HBox buttonBox = new HBox(12, addButton, updateButton, deleteButton);
        addButton.getStyleClass().add("button primary");
        updateButton.getStyleClass().add("button info");
        deleteButton.getStyleClass().add("button danger");
        getChildren().add(buttonBox);

        errorLabel.getStyleClass().add("label error");
        getChildren().add(errorLabel);

        addButton.setOnAction(e -> addReview());
        updateButton.setOnAction(e -> updateReview());
        deleteButton.setOnAction(e -> deleteReview());
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> fillForm(selected));

        refreshReviews();
    }

    private void refreshReviews() {
        reviewList.setAll(reviewService.findAll());
    }

    private void filterReviews(String query) {
        if (query == null || query.isEmpty()) {
            refreshReviews();
            return;
        }
        String q = query.toLowerCase();
        List<Review> filtered = new ArrayList<>();
        for (Review rv : reviewService.findAll()) {
            if (rv.getUserId().toLowerCase().contains(q) ||
                rv.getComment().toLowerCase().contains(q)) {
                filtered.add(rv);
            }
        }
        reviewList.setAll(filtered);
    }

    private void fillForm(Review rv) {
        if (rv == null) {
            userIdField.clear();
            commentArea.clear();
            ratingSpinner.getValueFactory().setValue(5);
            return;
        }
        userIdField.setText(rv.getUserId());
        commentArea.setText(rv.getComment());
        ratingSpinner.getValueFactory().setValue(rv.getRating());
    }

    private void addReview() {
        errorLabel.setText("");
        String userId = userIdField.getText().trim();
        String comment = commentArea.getText().trim();
        int rating = ratingSpinner.getValue();
        if (userId.isEmpty() || comment.isEmpty()) {
            errorLabel.setText("Utilisateur et commentaire obligatoires.");
            return;
        }
        Review rv = new Review(null, userId, comment, rating);
        reviewService.save(rv);
        refreshReviews();
        clearForm();
    }

    private void updateReview() {
        errorLabel.setText("");
        Review selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Sélectionnez un avis à modifier.");
            return;
        }
        String userId = userIdField.getText().trim();
        String comment = commentArea.getText().trim();
        int rating = ratingSpinner.getValue();
        if (userId.isEmpty() || comment.isEmpty()) {
            errorLabel.setText("Utilisateur et commentaire obligatoires.");
            return;
        }
        Review rv = new Review(selected.getId(), userId, comment, rating);
        reviewService.save(rv);
        refreshReviews();
        clearForm();
    }

    private void deleteReview() {
        errorLabel.setText("");
        Review selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Sélectionnez un avis à supprimer.");
            return;
        }
        reviewService.delete(selected.getId());
        refreshReviews();
        clearForm();
    }

    private void clearForm() {
        userIdField.clear();
        commentArea.clear();
        ratingSpinner.getValueFactory().setValue(5);
        table.getSelectionModel().clearSelection();
    }
}
