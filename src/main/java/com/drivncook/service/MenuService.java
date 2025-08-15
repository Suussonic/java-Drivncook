package com.drivncook.service;

import com.drivncook.model.Menu;
import java.util.*;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class MenuService {
    private static final String FILE = "menus.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private List<Menu> menus = new ArrayList<>();

    public MenuService() { load(); }

    public List<Menu> findAll() { return menus; }
    public Optional<Menu> findById(String id) {
        return menus.stream().filter(m -> m.getId().equals(id)).findFirst();
    }
    public void save(Menu menu) {
        findById(menu.getId()).ifPresentOrElse(
            m -> { menus.remove(m); menus.add(menu); },
            () -> menus.add(menu)
        );
        persist();
    }
    public void delete(String id) {
        menus.removeIf(m -> m.getId().equals(id));
        persist();
    }
    private void load() {
        try {
            File f = new File(FILE);
            if (f.exists()) menus = mapper.readValue(f, new TypeReference<List<Menu>>(){});
        } catch (Exception e) { menus = new ArrayList<>(); }
    }
    private void persist() {
        try { mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE), menus); }
        catch (Exception ignored) {}
    }
}
