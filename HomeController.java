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
        // SQL queries for each category, selecting a random recommendation
        String movieSql = "SELECT name FROM recommendation WHERE category = ? AND type = 'movie' ORDER BY RAND() LIMIT 1";
        String songSql = "SELECT name FROM recommendation WHERE category = ? AND type = 'song' ORDER BY RAND() LIMIT 1";
        String kdramaSql = "SELECT name FROM recommendation WHERE category = ? AND type = 'kdrama' ORDER BY RAND() LIMIT 1";

        // Query the database for random results based on the mood
        String movie = jdbcTemplate.queryForObject(movieSql, new Object[]{mood}, String.class);
        String song = jdbcTemplate.queryForObject(songSql, new Object[]{mood}, String.class);
        String kdrama = jdbcTemplate.queryForObject(kdramaSql, new Object[]{mood}, String.class);

        // If no results found, set default values
        if (movie == null) movie = "No movie recommendation";
        if (song == null) song = "No song recommendation";
        if (kdrama == null) kdrama = "No kdrama recommendation";

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

