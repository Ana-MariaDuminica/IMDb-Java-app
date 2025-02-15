package org.example;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Actor implements Comparable{
    private String name;
    private List<Map<String, String>> performances;
    private String biography;

    public Actor() {
        this.performances = new ArrayList<>();
    }

    public Actor(String name, List<Map<String, String>> performances, String biography) {
        this.name = name;
        this.performances = performances;
        this.biography = biography;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, String>> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Map<String, String>> performances) {
        this.performances = performances;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }


    public int compareTo(Object o) {
        if (o instanceof Movie) {
            Movie m = (Movie) o;
            return this.name.compareTo(m.getTitle());
        }
        if (o instanceof Series) {
            Series s = (Series) o;
            return this.name.compareTo(s.getTitle());
        }

        Actor p = (Actor) o;
        return this.name.compareTo(p.name);
    }

    public void viewPerformancesDetails() {
        System.out.println("Performances:");
        for (Map<String, String> performance : performances) {
            String title = performance.get("title");
            String type = performance.get("type");

            System.out.println("Title: " + title + ", Type: " + type);

        }
    }

    public void viewActorDetails() {
        System.out.println("Name: " + this.getName());
        if (this.getBiography() != null) { System.out.println("Biography: " + this.getBiography()); }
        if (this.getPerformances() != null) { this.viewPerformancesDetails(); }
    }

    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Name: ").append(this.name).append("\n");
        details.append("Biography: ").append(this.biography).append("\n");
        details.append("Performances: ").append("\n");

        for (Map<String, String> performance : this.performances) {
            for (Map.Entry<String, String> entry : performance.entrySet()) {
                details.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        return details.toString();
    }

}

