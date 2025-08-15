package com.drivncook.model;

public class User {
    private String prenom;
    private String adresseLivraison;
    private String dateNaissance;
    private String role;
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getAdresseLivraison() { return adresseLivraison; }
    public void setAdresseLivraison(String adresseLivraison) { this.adresseLivraison = adresseLivraison; }

    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    private String id;
    private String nom; // correspond à 'nom' dans MongoDB
    private String email;
    private String password;
    private String loyaltyCardId;

    public User() {}

    public User(String id, String nom, String email, String password, String loyaltyCardId) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.loyaltyCardId = loyaltyCardId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getLoyaltyCardId() { return loyaltyCardId; }
    public void setLoyaltyCardId(String loyaltyCardId) { this.loyaltyCardId = loyaltyCardId; }
}
