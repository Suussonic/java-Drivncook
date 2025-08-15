package com.drivncook.service;

import com.drivncook.model.User;
import com.drivncook.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.*;

public class UserService {
    private final ObjectMapper mapper = new ObjectMapper();

    public List<User> findAll() {
        try {
            String json = HttpUtil.get("/users");
            return mapper.readValue(json, new TypeReference<List<User>>(){});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<User> findById(String id) {
        try {
            String json = HttpUtil.get("/users/" + id);
            User user = mapper.readValue(json, User.class);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void save(User user) {
        try {
            String json = mapper.writeValueAsString(user);
            if (user.getId() == null || user.getId().isEmpty()) {
                HttpUtil.post("/users", json);
            } else {
                HttpUtil.post("/users/" + user.getId(), json); // à adapter selon l'API (PUT ou POST)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String id) {
        try {
            HttpUtil.delete("/users/" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
