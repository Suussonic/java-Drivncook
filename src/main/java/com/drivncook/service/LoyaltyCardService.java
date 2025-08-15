package com.drivncook.service;

import com.drivncook.model.LoyaltyCard;
import com.drivncook.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.*;

public class LoyaltyCardService {
    private final ObjectMapper mapper = new ObjectMapper();

    public List<LoyaltyCard> findAll() {
        try {
            String json = HttpUtil.get("/fidelity");
            return mapper.readValue(json, new TypeReference<List<LoyaltyCard>>(){});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<LoyaltyCard> findById(String id) {
        try {
            String json = HttpUtil.get("/fidelity/" + id);
            LoyaltyCard card = mapper.readValue(json, LoyaltyCard.class);
            return Optional.ofNullable(card);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void save(LoyaltyCard card) {
        try {
            String json = mapper.writeValueAsString(card);
            if (card.getId() == null || card.getId().isEmpty()) {
                HttpUtil.post("/fidelity", json);
            } else {
                HttpUtil.post("/fidelity/" + card.getId(), json); // à adapter selon l'API (PUT ou POST)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String id) {
        try {
            HttpUtil.delete("/fidelity/" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
