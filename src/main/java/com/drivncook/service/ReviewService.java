package com.drivncook.service;

import com.drivncook.model.Review;
import com.drivncook.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.*;

public class ReviewService {
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Review> findAll() {
        try {
            String json = HttpUtil.get("/reviews");
            return mapper.readValue(json, new TypeReference<List<Review>>(){});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<Review> findById(String id) {
        try {
            String json = HttpUtil.get("/reviews/" + id);
            Review review = mapper.readValue(json, Review.class);
            return Optional.ofNullable(review);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void save(Review review) {
        try {
            String json = mapper.writeValueAsString(review);
            if (review.getId() == null || review.getId().isEmpty()) {
                HttpUtil.post("/reviews", json);
            } else {
                HttpUtil.post("/reviews/" + review.getId(), json); // à adapter selon l'API (PUT ou POST)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String id) {
        try {
            HttpUtil.delete("/reviews/" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
