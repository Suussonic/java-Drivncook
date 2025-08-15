package com.drivncook.service;

import com.drivncook.model.Order;
import com.drivncook.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.*;

public class OrderService {
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Order> findAll() {
        try {
            String json = HttpUtil.get("/orders");
            return mapper.readValue(json, new TypeReference<List<Order>>(){});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<Order> findById(String id) {
        try {
            String json = HttpUtil.get("/orders/" + id);
            Order order = mapper.readValue(json, Order.class);
            return Optional.ofNullable(order);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void save(Order order) {
        try {
            String json = mapper.writeValueAsString(order);
            if (order.getId() == null || order.getId().isEmpty()) {
                HttpUtil.post("/orders", json);
            } else {
                HttpUtil.post("/orders/" + order.getId(), json); // à adapter selon l'API (PUT ou POST)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String id) {
        try {
            HttpUtil.delete("/orders/" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
