package org.example;

import java.util.*;

public abstract class Production implements Comparable{
    private String title;
    private List<String> directors;
    private List<String> actors;
    private List<Genre> genres;
    private List<Rating> ratings;
    private String plot;
    private double averageRating;
    private String type;

    public Production() {
        this.ratings = new ArrayList<>();
    }

    public Production(String title,
                      List<String> directors,
                      List<String> actors,
                      List<Genre> genres,
                      List<Rating> ratings,
                      String plot,
                      double averageRating,
                      String type) {
        this.title = title;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;

        this.plot = plot;
        this.averageRating = averageRating;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public abstract void displayInfo();

    public int compareTo(Object o) {
        if (o instanceof Actor) {
            Actor a = (Actor) o;
            return this.title.compareTo(a.getName());
        }

        Production p = (Production) o;
        return this.title.compareTo(p.title);
    }


    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getActors() {
        return actors;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getPlot() {
        return plot;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public String getTitle() {
        return title;
    }


    public String toStringRatings(Rating rating) {
        return rating.getUsername() + " " + rating.getRating() + " " + rating.getComment();
    }

    public void calculateRating() {
        int nrRatings = 0;
        double sum = 0;
        for (Rating rating : ratings) {
            nrRatings++;
            sum += rating.getRating();
        }
        double avg = sum / nrRatings;
        this.setAverageRating(avg);
    }

    public int getNumberOfRatings() {
        int nr = 0;
        for (Rating rating : this.ratings) {
            nr++;
        }
        return nr;
    }
}
