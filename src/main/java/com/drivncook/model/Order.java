package com.drivncook.model;

import java.util.List;

public class Order {
    private String id;
    private String userId;
    private List<String> dishIds;
    private double total;
    private String status;

    public Order() {}

    public Order(String id, String userId, List<String> dishIds, double total, String status) {
        this.id = id;
        this.userId = userId;
        this.dishIds = dishIds;
        this.total = total;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<String> getDishIds() { return dishIds; }
    public void setDishIds(List<String> dishIds) { this.dishIds = dishIds; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
