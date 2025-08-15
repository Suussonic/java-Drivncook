package com.drivncook.service;

import com.drivncook.model.LoyaltyCard;
import java.util.*;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class LoyaltyCardService {
    private static final String FILE = "loyaltycards.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private List<LoyaltyCard> cards = new ArrayList<>();

    public LoyaltyCardService() { load(); }

    public List<LoyaltyCard> findAll() { return cards; }
    public Optional<LoyaltyCard> findById(String id) {
        return cards.stream().filter(c -> c.getId().equals(id)).findFirst();
    }
    public void save(LoyaltyCard card) {
        findById(card.getId()).ifPresentOrElse(
            c -> { cards.remove(c); cards.add(card); },
            () -> cards.add(card)
        );
        persist();
    }
    public void delete(String id) {
        cards.removeIf(c -> c.getId().equals(id));
        persist();
    }
    private void load() {
        try {
            File f = new File(FILE);
            if (f.exists()) cards = mapper.readValue(f, new TypeReference<List<LoyaltyCard>>(){});
        } catch (Exception e) { cards = new ArrayList<>(); }
    }
    private void persist() {
        try { mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE), cards); }
        catch (Exception ignored) {}
    }
}
