package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class Request implements Subject{
    private RequestTypes type;
    private LocalDateTime Date;
    private String username;
    private String to;
    private String description;
    private String actorName;
    private String movieTitle;
    private String createdDate;

    private Vector<User> observers = new Vector<>();
    private boolean change;

    public Request() {

    }

    public Request(RequestTypes type, String username, LocalDateTime Date, String to, String description, String actorName, String movieTitle) {
        this.type = type;
        this.username = username;
        this.to = to;
        this.description = description;
        this.actorName = actorName;
        this.movieTitle = movieTitle;
        this.Date = Date;
    }

    public Vector<User> getObservers() {
        return observers;
    }

    public void setObservers(Vector<User> observers) {
        this.observers = observers;
    }

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public void setFormattedBirthDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        this.Date = LocalDateTime.parse(createdDate, formatter);
    }

    public LocalDateTime getDate() {
        return Date;
    }

    public void setDate(LocalDateTime date) {
        Date = date;
    }

    public RequestTypes getType() {
        return type;
    }

    public void setType(RequestTypes type) {
        this.type = type;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", createdDate=" + Date +
                ", username='" + username + '\'' +
                ", to='" + to + '\'' +
                ", description='" + description + '\'' +
                ", actorName='" + actorName + '\'' +
                ", movieTitle='" + movieTitle + '\'' +
                '}';
    }

    public void viewRequest() {
        System.out.println("Type: " + type);
        System.out.println("createdDate: " + Date);
        System.out.println("username: " + username);
        if(actorName != null) { System.out.println("actorName: " + actorName); }
        if(movieTitle != null) { System.out.println("movieTitle: " + movieTitle); }
        System.out.println("to: " + to);
        System.out.println("description: " + description);
        System.out.println("\n");
    }

    @Override
    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.addElement((User) observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    @Override
    public void notifyObservers(User user, String description) {
        for (int i = observers.size()-1; i >= 0; i--) {
            ((Observer)observers.elementAt(i)).update(user, description);
        }
    }
}
