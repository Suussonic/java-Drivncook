package com.drivncook.service;

import com.drivncook.model.Order;
import java.util.*;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class OrderService {
    private static final String FILE = "orders.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private List<Order> orders = new ArrayList<>();

    public OrderService() { load(); }

    public List<Order> findAll() { return orders; }
    public Optional<Order> findById(String id) {
        return orders.stream().filter(o -> o.getId().equals(id)).findFirst();
    }
    public void save(Order order) {
        findById(order.getId()).ifPresentOrElse(
            o -> { orders.remove(o); orders.add(order); },
            () -> orders.add(order)
        );
        persist();
    }
    public void delete(String id) {
        orders.removeIf(o -> o.getId().equals(id));
        persist();
    }
    private void load() {
        try {
            File f = new File(FILE);
            if (f.exists()) orders = mapper.readValue(f, new TypeReference<List<Order>>(){});
        } catch (Exception e) { orders = new ArrayList<>(); }
    }
    private void persist() {
        try { mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE), orders); }
        catch (Exception ignored) {}
    }
}
