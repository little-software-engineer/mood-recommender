package com.moodrecommender.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Load JSON file
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(new File("src/main/resources/recommendations.json"));

            Iterator<String> categories = rootNode.fieldNames();
            while (categories.hasNext()) {
                String category = categories.next();
                JsonNode categoryNode = rootNode.get(category);

                insertRecommendations(category, "movie", categoryNode.get("movie"));
                insertRecommendations(category, "song", categoryNode.get("song"));
                insertRecommendations(category, "kdrama", categoryNode.get("kdrama"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertRecommendations(String category, String type, JsonNode recommendations) {
        if (recommendations != null) {
            recommendations.forEach(recommendation -> {
                jdbcTemplate.update("INSERT INTO recommendation (category, type, name) VALUES (?, ?, ?)",
                        category, type, recommendation.asText());
            });
        }
    }
}

