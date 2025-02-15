package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Series extends Production {
    private int releaseYear;
    private int numSeasons;
    private Map<String, List<Episode>>  seasons;

    public Series() {

    }

    public Series(String title,
                  List<String> directors,
                  List<String> actors,
                  List<Genre> genres,
                  List<Rating> ratings,
                  String plot,
                  double averageRating,
                  int releaseYear,
                  int numSeasons,
                  Map<String, List<Episode>> seasons,
                  String type) {
        super(title, directors, actors, genres, ratings, plot, averageRating, type);
        this.releaseYear = releaseYear;
        this.numSeasons = numSeasons;
        this.seasons = seasons;
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
        if (getReleaseYear() != 0) {System.out.println("Release Year: " + getReleaseYear()); }
        if (getNumSeasons() != 0) { System.out.println("Num Seasons: " + getNumSeasons()); }
        if (getRatings() != null) {
            System.out.println("Ratings: ");
            List<Rating> rating = getRatings();
            for (Rating rating1 : rating) {
                System.out.println(toStringRatings(rating1));
            }
        }
        System.out.println("\n");
    }


    public int getNumSeasons() {
        return numSeasons;
    }

    public void setNumSeasons(int numSeasons) {
        this.numSeasons = numSeasons;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Map<String, List<Episode>> getSeasons() {
        return seasons;
    }

    public void setSeasons(Map<String, List<Episode>> seasons) {
        this.seasons = seasons;
    }

    public void viewSeasons() {
        for (Map.Entry<String, List<Episode>> entry : seasons.entrySet()) {
            String seasonName = entry.getKey();
            List<Episode> episodes = entry.getValue();

            System.out.println("Season: " + seasonName);
            for (Episode episode : episodes) {
                System.out.println("Episode: " + episode.getEpisodeName());
                System.out.println("Duration: " + episode.getDuration());
                System.out.println();
            }
        }
    }

    public void deleteEpisode(String episodeTitle) {
        for (List<Episode> episodes : seasons.values()) {
            for (Episode episode : episodes) {
                if (episode.getEpisodeName().equals(episodeTitle)) {
                    episodes.remove(episode);
                    return;
                }
            }
        }
        System.out.println("Episodul " + episodeTitle + " nu a fost gasit.");
    }

    public void modifyEpisode(String episodeToModify) {
        boolean found = false;

        Scanner scanner = new Scanner(System.in);
        for (List<Episode> episodes : seasons.values()) {
            for (Episode episode : episodes) {
                if (episode.getEpisodeName().equalsIgnoreCase(episodeToModify)) {
                    found = true;
                    System.out.println("Ce doresti sa modifici pentru episodul " + episodeToModify + "?");
                    System.out.println("1. Titlul");
                    System.out.println("2. Durata");

                    int option = scanner.nextInt();
                    scanner.nextLine();

                    switch (option) {
                        case 1:
                            System.out.println("Introdu noul titlu:");
                            String newTitle = scanner.nextLine();
                            episode.setEpisodeName(newTitle);
                            break;
                        case 2:
                            System.out.println("Introdu noua durata:");
                            String newDuration = scanner.nextLine();
                            episode.setDuration(newDuration);
                            break;
                        default:
                            System.out.println("Optiune invalida.");
                            return;
                    }
                }
            }
        }
        if (!found) {
            System.out.println("Episodul " + episodeToModify + " nu a fost gasit.");
        }

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
        details.append("Release Year: ").append(this.getReleaseYear()).append("\n");
        details.append("Number of Seasons: ").append(this.getNumSeasons()).append("\n");

        details.append("Ratings: ").append("\n");
        for (Rating rating : this.getRatings()) {
            details.append(rating.toString()).append("\n");
        }

        // Detalii despre episoadele fiecarui sezon
        for (Map.Entry<String, List<Episode>> entry : this.getSeasons().entrySet()) {
            String seasonName = entry.getKey();
            List<Episode> episodes = entry.getValue();
            details.append("Season: ").append(seasonName).append("\n");

            for (Episode episode : episodes) {
                details.append("Episode: ").append(episode.getEpisodeName())
                        .append(", Duration: ").append(episode.getDuration()).append("\n");
            }
        }

        return details.toString();
    }
}
