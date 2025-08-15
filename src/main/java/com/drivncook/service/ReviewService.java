package com.drivncook.service;

import com.drivncook.model.Review;
import java.util.*;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class ReviewService {
    private static final String FILE = "reviews.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private List<Review> reviews = new ArrayList<>();

    public ReviewService() { load(); }

    public List<Review> findAll() { return reviews; }
    public Optional<Review> findById(String id) {
        return reviews.stream().filter(r -> r.getId().equals(id)).findFirst();
    }
    public void save(Review review) {
        findById(review.getId()).ifPresentOrElse(
            r -> { reviews.remove(r); reviews.add(review); },
            () -> reviews.add(review)
        );
        persist();
    }
    public void delete(String id) {
        reviews.removeIf(r -> r.getId().equals(id));
        persist();
    }
    private void load() {
        try {
            File f = new File(FILE);
            if (f.exists()) reviews = mapper.readValue(f, new TypeReference<List<Review>>(){});
        } catch (Exception e) { reviews = new ArrayList<>(); }
    }
    private void persist() {
        try { mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE), reviews); }
        catch (Exception ignored) {}
    }
}
