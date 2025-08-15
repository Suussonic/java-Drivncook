package com.drivncook.service;

import com.drivncook.model.Menu;
import com.drivncook.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.*;

public class MenuService {
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Menu> findAll() {
        try {
            String json = HttpUtil.get("/menus");
            return mapper.readValue(json, new TypeReference<List<Menu>>(){});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<Menu> findById(String id) {
        try {
            String json = HttpUtil.get("/menus/" + id);
            Menu menu = mapper.readValue(json, Menu.class);
            return Optional.ofNullable(menu);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void save(Menu menu) {
        try {
            String json = mapper.writeValueAsString(menu);
            if (menu.getId() == null || menu.getId().isEmpty()) {
                HttpUtil.post("/menus", json);
            } else {
                HttpUtil.put("/menus/" + menu.getId(), json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String id) {
        try {
            HttpUtil.delete("/menus/" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
