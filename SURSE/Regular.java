package org.example;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;

public class Regular <T extends Comparable<T>> extends User<T> implements RequestsManager{

    public Regular() {

    }

    @Override
    public void createRequest(Request r) {
        List<Request> requestList = IMDB.getInstance().getRequests();
        requestList.add(r);

        List<User> users = IMDB.getInstance().getUsers();
        if (r.getTo().equals("ADMIN")) {
            for (User user : users) {
                if (user instanceof Admin) {
                    r.addObserver(user);
                }
            }
            r.notifyObservers(this, r.getDescription());
            Admin.RequestsHolder.addRequest(r);
        } else {
            String Observer = r.getTo();
            User userObserver = IMDB.getInstance().findUser(Observer);
            ((Staff) userObserver).getRequestList().add(r);
            r.addObserver(userObserver);
            r.notifyObservers(this, r.getDescription());
        }
    }

    @Override
    public void removeRequest(Request r) {
        Vector<User> observers = r.getObservers();
        if (observers != null) {
            for (User observer : observers) {

                if (observer.getNotifications() != null) {
                    List<String> notifications = observer.getNotifications();
                    Iterator<String> iterator = notifications.iterator();
                    while (iterator.hasNext()) {
                        String notif = iterator.next();
                        if (notif.contains(r.getUsername()) && notif.contains(r.getDescription())) {
                            iterator.remove();
                        }
                    }
                }
            }
        }

        if (!r.getTo().equals("ADMIN")) {
            User user = IMDB.getInstance().findUser(r.getTo());
            ((Staff) user).getRequestList().remove(r);
        } else {
            Admin.RequestsHolder.removeRequest(r);
        }

        List<Request> requestList = IMDB.getInstance().getRequests();
        requestList.remove(r);

    }




    public boolean findRating(String string) {
        Production p = IMDB.getInstance().findProduction(string);
            for (Rating rating : p.getRatings()) {
                if(rating.getUsername().equals(this.getUsername())) {
                    return false;
                }
            }
            return true;
    }



    public void addRatingRegular(String productionName, Rating newRating) {
        Production p = IMDB.getInstance().findProduction(productionName);

        for (Rating rating : p.getRatings()) {
            User user = IMDB.getInstance().findUser(rating.getUsername());
            if (user instanceof Regular) {
                newRating.addObserver(user);
            }
        }

        List<User> users = IMDB.getInstance().getUsers();
        for (User user : users) {
            if (user instanceof Admin || user instanceof Contributor) {
                if(((Staff) user).findContribution(productionName)) {
                    newRating.addObserver(user);
                    break;
                }
            }
        }

        p.getRatings().add(newRating);
        p.calculateRating();
        newRating.notifyObservers(this, productionName);

        ExperienceStrategy ratingStrategy = new RatingExperienceStrategy();
        int experiencePoints = ratingStrategy.calculateExperience();

        this.updateExperience(experiencePoints);
        IMDB.getInstance().sortRatingsByUserExperience();
    }



    public void deleteRatingRegular(String productionName) {
        List<Production> productions = IMDB.getInstance().getProductions();
        for (Production production : productions) {
            List<Rating> ratings = production.getRatings();
            for (Rating rating : ratings) {
                if (rating.getUsername().equals(this.getUsername()) && production.getTitle().equals(productionName)) {
                    production.getRatings().remove(rating);
                    break;
                }
            }
            for (Rating rating : ratings) {
                User user = IMDB.getInstance().findUser(rating.getUsername());
                if (user.getNotifications() != null) {
                    List<String> notifications = user.getNotifications();
                    Iterator<String> iterator = notifications.iterator();
                    while (iterator.hasNext()) {
                        String notif = iterator.next();
                        if (notif.contains(this.getUsername()) && notif.contains(productionName)) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }

    public List<String> getUserRatedProductions() {
        List<Production> productions = IMDB.getInstance().getProductions();
        List<String> p = new ArrayList<>();
        for (Production production : productions) {
            if(!this.findRating(production.getTitle())) {
                p.add(production.getTitle());
            }
        }
        return p;
    }


}
