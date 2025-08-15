package com.drivncook.service;

import com.drivncook.model.Event;
import com.drivncook.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.*;

public class EventService {
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Event> findAll() {
        try {
            String json = HttpUtil.get("/eventcustoms");
            return mapper.readValue(json, new TypeReference<List<Event>>(){});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<Event> findById(String id) {
        try {
            String json = HttpUtil.get("/eventcustoms/" + id);
            Event event = mapper.readValue(json, Event.class);
            return Optional.ofNullable(event);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void save(Event event) {
        try {
            String json = mapper.writeValueAsString(event);
            if (event.getId() == null || event.getId().isEmpty()) {
                HttpUtil.post("/eventcustoms", json);
            } else {
                HttpUtil.post("/eventcustoms/" + event.getId(), json); // à adapter selon l'API (PUT ou POST)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String id) {
        try {
            HttpUtil.delete("/eventcustoms/" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
