package org.example;

import java.util.List;

public class Movie extends Production {
    private String duration;
    private int releaseYear;

    public Movie() {

    }

    public Movie(String title, List<String> directors, List<String> actors,
                 List<Genre> genres, List<Rating> ratings, String plot,
                 double averageRating, String type, String duration, int releaseYear) {
        super(title, directors, actors, genres, ratings, plot, averageRating, type);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public int getReleaseYear() {
        return releaseYear;
    }
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public void displayInfo() {
        if (getTitle() != null) { System.out.println("Title: " + getTitle()); }
        if (getType() != null) { System.out.println("Type: " + getType()); }
        if (getDirectors() != null) { System.out.println("Directors: " + getDirectors()); }
        if (getActors() != null) { System.out.println("Actors: " + getActors()); }
        if (getGenres() != null) { System.out.println("Genres: " + getGenres()); }
        if (getPlot() != null) { System.out.println("Plot: " + getPlot()); }
        if (getAverageRating() != 0) { System.out.println("Average Rating: " + getAverageRating()); }
        if (getDuration() != null) { System.out.println("Duration: " + getDuration()); }
        if (getReleaseYear() != 0) { System.out.println("Release Year: " + getReleaseYear()); }
        if (getRatings() != null) {
            System.out.println("Ratings: ");
            List<Rating> rating = getRatings();
            for (Rating rating1 : rating) {
                System.out.println(toStringRatings(rating1));
            }
        }
        System.out.println("\n");
    }


    @Override
    public int compareTo(Object o) {
        return super.compareTo(o);
    }

    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Title: ").append(this.getTitle()).append("\n");
        details.append("Type: ").append(this.getType()).append("\n");
        details.append("Directors: ").append(this.getDirectors()).append("\n");
        details.append("Actors: ").append(this.getActors()).append("\n");
        details.append("Genres: ").append(this.getGenres()).append("\n");
        details.append("Plot: ").append(this.getPlot()).append("\n");
        details.append("Average Rating: ").append(this.getAverageRating()).append("\n");
        details.append("Duration: ").append(this.getDuration()).append("\n");
        details.append("Release Year: ").append(this.getReleaseYear()).append("\n");

        details.append("Ratings: ").append("\n");
        for (Rating rating : this.getRatings()) {
            details.append(rating.toString()).append("\n");
        }

        return details.toString();
    }
}
