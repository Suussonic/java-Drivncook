package com.drivncook.service;

import com.drivncook.model.User;
import java.util.*;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class UserService {
    private static final String FILE = "users.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private List<User> users = new ArrayList<>();

    public UserService() { load(); }

    public List<User> findAll() { return users; }
    public Optional<User> findById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }
    public void save(User user) {
        findById(user.getId()).ifPresentOrElse(
            u -> { users.remove(u); users.add(user); },
            () -> users.add(user)
        );
        persist();
    }
    public void delete(String id) {
        users.removeIf(u -> u.getId().equals(id));
        persist();
    }
    private void load() {
        try {
            File f = new File(FILE);
            if (f.exists()) users = mapper.readValue(f, new TypeReference<List<User>>(){});
        } catch (Exception e) { users = new ArrayList<>(); }
    }
    private void persist() {
        try { mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE), users); }
        catch (Exception ignored) {}
    }
}
