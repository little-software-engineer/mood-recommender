package com.moodrecommender.model;


public class Recommendation {
    private String movie;
    private String song;
    private String kdrama;


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
