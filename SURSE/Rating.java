package org.example;

import java.util.Vector;

public class Rating implements Subject{
    private String username;
    private int rating;
    private String comment;
    private Vector<User> observers = new Vector<>();
    public Rating() {

    }

    public Rating(String username, int rating, String comment) {
        this.username = username;
        this.rating = rating;
        this.comment = comment;

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Vector<User> getObservers() {
        return observers;
    }

    public void setObservers(Vector<User> observers) {
        this.observers = observers;
    }

    @Override
    public void addObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.addElement((User) observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(User user, String title) {
        for (Observer observer : this.getObservers()) {
            if (observer != null) {
                observer.update(user, title);
            }
        }
    }

    @Override
    public String toString() {
        return "Rating{" +
                "username='" + username + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}
