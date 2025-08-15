package com.drivncook.service;

import com.drivncook.model.NewsletterSub;
import com.drivncook.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.*;

public class NewsletterService {
    private final ObjectMapper mapper = new ObjectMapper();

    public List<NewsletterSub> findAll() {
        try {
            String json = HttpUtil.get("/newslettersubs");
            return mapper.readValue(json, new TypeReference<List<NewsletterSub>>(){});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<NewsletterSub> findById(String id) {
        try {
            String json = HttpUtil.get("/newslettersubs/" + id);
            NewsletterSub sub = mapper.readValue(json, NewsletterSub.class);
            return Optional.ofNullable(sub);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void save(NewsletterSub sub) {
        try {
            String json = mapper.writeValueAsString(sub);
            if (sub.getId() == null || sub.getId().isEmpty()) {
                HttpUtil.post("/newslettersubs", json);
            } else {
                HttpUtil.post("/newslettersubs/" + sub.getId(), json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String id) {
        try {
            HttpUtil.delete("/newslettersubs/" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
