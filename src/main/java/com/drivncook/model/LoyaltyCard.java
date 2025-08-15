package com.drivncook.model;

public class LoyaltyCard {
    private String id;
    private String userId;
    private int points;
    private String advantages;

    public LoyaltyCard() {}

    public LoyaltyCard(String id, String userId, int points, String advantages) {
        this.id = id;
        this.userId = userId;
        this.points = points;
        this.advantages = advantages;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public String getAdvantages() { return advantages; }
    public void setAdvantages(String advantages) { this.advantages = advantages; }
}
