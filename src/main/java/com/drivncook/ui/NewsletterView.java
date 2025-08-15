package com.drivncook.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.drivncook.util.NewsletterUtil;
import java.util.*;

public class NewsletterView extends VBox {
    private final TextField emailField = new TextField();
    private final TextField subjectField = new TextField();
    private final TextArea contentArea = new TextArea();
    private final Button sendButton = new Button("Envoyer");
    private final Label statusLabel = new Label("");
    private final ListView<String> emailListView = new ListView<>();
    private final Button addEmailButton = new Button("Ajouter");
    private final Button removeEmailButton = new Button("Retirer");

    public NewsletterView() {
        setSpacing(16);
        setPadding(new Insets(24, 32, 24, 32));
        Label title = new Label("Gestion de la newsletter");
        title.getStyleClass().add("label");
        getChildren().add(title);

        HBox emailBox = new HBox(10, emailField, addEmailButton, removeEmailButton);
        emailField.setPromptText("Email à ajouter");
        addEmailButton.getStyleClass().add("button info");
        removeEmailButton.getStyleClass().add("button danger");
        emailListView.setPrefHeight(120);
        VBox emailListBox = new VBox(8, new Label("Destinataires :"), emailListView, emailBox);
        getChildren().add(emailListBox);

        subjectField.setPromptText("Sujet de la newsletter");
        contentArea.setPromptText("Contenu de la newsletter");
        getChildren().addAll(subjectField, contentArea, sendButton, statusLabel);
        sendButton.getStyleClass().add("button primary");
        statusLabel.getStyleClass().add("label error");

        addEmailButton.setOnAction(e -> {
            String email = emailField.getText().trim();
            if (!email.isEmpty() && !emailListView.getItems().contains(email)) {
                emailListView.getItems().add(email);
                emailField.clear();
            }
        });
        removeEmailButton.setOnAction(e -> {
            String selected = emailListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                emailListView.getItems().remove(selected);
            }
        });
        sendButton.setOnAction(e -> {
            List<String> emails = new ArrayList<>(emailListView.getItems());
            String subject = subjectField.getText().trim();
            String content = contentArea.getText().trim();
            if (emails.isEmpty() || subject.isEmpty() || content.isEmpty()) {
                statusLabel.setText("Veuillez remplir tous les champs et ajouter au moins un destinataire.");
                return;
            }
            NewsletterUtil.sendNewsletter(emails, subject, content);
            statusLabel.setText("Newsletter envoyée !");
        });
    }
}
