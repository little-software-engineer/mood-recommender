package com.moodrecommender.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String index() {
        return "index"; // Return the name of the Thymeleaf template for the mood submission form
    }

    @GetMapping("/recommendations")
    public String getRecommendations(@RequestParam String mood, Model model) {
        String sql = "SELECT type, name FROM recommendation WHERE category = ?";
        List<Recommendation> recommendations = jdbcTemplate.query(
                sql,
                new Object[]{mood},
                (rs, rowNum) -> new Recommendation(rs.getString("type"), rs.getString("name"))
        );

        // Initialize default recommendations in case there are no results in the database
        String movie = "No recommendation";
        String song = "No recommendation";
        String kdrama = "No recommendation";

        // Iterate over the results and assign them based on type
        for (Recommendation rec : recommendations) {
            switch (rec.getType()) {
                case "movie":
                    movie = rec.getName();
                    break;
                case "song":
                    song = rec.getName();
                    break;
                case "kdrama":
                    kdrama = rec.getName();
                    break;
            }
        }

        model.addAttribute("recommendation", new RecommendationResponse(movie, song, kdrama));
        return "result"; // Return the name of the Thymeleaf template for displaying recommendations
    }

    public static class Recommendation {
        private String type;
        private String name;

        public Recommendation(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class RecommendationResponse {
        private String movie;
        private String song;
        private String kdrama;

        public RecommendationResponse(String movie, String song, String kdrama) {
            this.movie = movie;
            this.song = song;
            this.kdrama = kdrama;
        }

        public String getMovie() {
            return movie;
        }

        public void setMovie(String movie) {
            this.movie = movie;
        }

        public String getSong() {
            return song;
        }

        public void setSong(String song) {
            this.song = song;
        }

        public String getKdrama() {
            return kdrama;
        }

        public void setKdrama(String kdrama) {
            this.kdrama = kdrama;
        }
    }
}

