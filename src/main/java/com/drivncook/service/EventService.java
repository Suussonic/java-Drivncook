package com.drivncook.service;

import com.drivncook.model.Event;
import java.util.*;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class EventService {
    private static final String FILE = "events.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private List<Event> events = new ArrayList<>();

    public EventService() { load(); }

    public List<Event> findAll() { return events; }
    public Optional<Event> findById(String id) {
        return events.stream().filter(e -> e.getId().equals(id)).findFirst();
    }
    public void save(Event event) {
        findById(event.getId()).ifPresentOrElse(
            e -> { events.remove(e); events.add(event); },
            () -> events.add(event)
        );
        persist();
    }
    public void delete(String id) {
        events.removeIf(e -> e.getId().equals(id));
        persist();
    }
    private void load() {
        try {
            File f = new File(FILE);
            if (f.exists()) events = mapper.readValue(f, new TypeReference<List<Event>>(){});
        } catch (Exception e) { events = new ArrayList<>(); }
    }
    private void persist() {
        try { mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE), events); }
        catch (Exception ignored) {}
    }
}
