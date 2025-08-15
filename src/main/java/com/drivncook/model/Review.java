package com.drivncook.model;

public class Review {
    private String id;
    private String userId;
    private String comment;
    private int rating;

    public Review() {}

    public Review(String id, String userId, String comment, int rating) {
        this.id = id;
        this.userId = userId;
        this.comment = comment;
        this.rating = rating;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
}
