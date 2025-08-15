package com.drivncook.service;

import com.drivncook.model.Dish;
import java.util.*;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class DishService {
    private static final String FILE = "dishes.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private List<Dish> dishes = new ArrayList<>();

    public DishService() { load(); }

    public List<Dish> findAll() { return dishes; }
    public Optional<Dish> findById(String id) {
        return dishes.stream().filter(d -> d.getId().equals(id)).findFirst();
    }
    public void save(Dish dish) {
        findById(dish.getId()).ifPresentOrElse(
            d -> { dishes.remove(d); dishes.add(dish); },
            () -> dishes.add(dish)
        );
        persist();
    }
    public void delete(String id) {
        dishes.removeIf(d -> d.getId().equals(id));
        persist();
    }
    private void load() {
        try {
            File f = new File(FILE);
            if (f.exists()) dishes = mapper.readValue(f, new TypeReference<List<Dish>>(){});
        } catch (Exception e) { dishes = new ArrayList<>(); }
    }
    private void persist() {
        try { mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE), dishes); }
        catch (Exception ignored) {}
    }
}
