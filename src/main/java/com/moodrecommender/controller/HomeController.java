package com.moodrecommender.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodrecommender.model.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class HomeController {

    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    public HomeController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/recommend")
    public String recommend(@RequestParam String mood, Model model) {
        Recommendation recommendation = getRecommendationFromFile(mood);
        model.addAttribute("recommendation", recommendation);
        log.info("Recommendations for mood {}: {}", mood, recommendation);
        return "result";
    }

    private Recommendation getRecommendationFromFile(String mood) {
        try {

            ClassPathResource resource = new ClassPathResource("recommendations.json");
            Map<String, Map<String, List<String>>> allRecommendations = objectMapper.readValue(resource.getInputStream(), Map.class);


            Map<String, List<String>> recommendationsForMood = allRecommendations.getOrDefault(mood, Collections.emptyMap());
            return createRecommendation(recommendationsForMood);
        } catch (IOException e) {

            log.error("Error reading recommendations from file", e);
            return new Recommendation();
        }
    }

    private Recommendation createRecommendation(Map<String, List<String>> recommendationsForMood) {
        Recommendation recommendation = new Recommendation();
        Random random = new Random();

        recommendation.setMovie(getRandomRecommendation(recommendationsForMood.get("movie"), random));
        recommendation.setSong(getRandomRecommendation(recommendationsForMood.get("song"), random));
        recommendation.setKdrama(getRandomRecommendation(recommendationsForMood.get("kdrama"), random));

        return recommendation;
    }

    private String getRandomRecommendation(List<String> options, Random random) {
        return options != null && !options.isEmpty() ? options.get(random.nextInt(options.size())) : "No Recommendation";
    }
}
