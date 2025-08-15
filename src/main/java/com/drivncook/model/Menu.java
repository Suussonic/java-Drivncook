package com.drivncook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu {
    @JsonProperty("_id")
    private String id;
    private String name;
    private double price;
    private String desc;

    public Menu() {}

    public Menu(String id, String name, double price, String desc) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.desc = desc;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }
}
