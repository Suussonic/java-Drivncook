package com.drivncook.service;

import com.drivncook.model.Dish;
import com.drivncook.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.*;

public class DishService {
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Dish> findAll() {
        try {
            String json = HttpUtil.get("/dishes");
            return mapper.readValue(json, new TypeReference<List<Dish>>(){});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<Dish> findById(String id) {
        try {
            String json = HttpUtil.get("/dishes/" + id);
            Dish dish = mapper.readValue(json, Dish.class);
            return Optional.ofNullable(dish);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void save(Dish dish) {
        try {
            String json = mapper.writeValueAsString(dish);
            if (dish.getId() == null || dish.getId().isEmpty()) {
                HttpUtil.post("/dishes", json);
            } else {
                HttpUtil.post("/dishes/" + dish.getId(), json); // à adapter selon l'API (PUT ou POST)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String id) {
        try {
            HttpUtil.delete("/dishes/" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
