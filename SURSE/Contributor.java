package org.example;

import java.util.Iterator;
import java.util.List;

public class Contributor<T extends Comparable<T>> extends Staff<T> implements RequestsManager{

    public Contributor() {

    }

    @Override
    public void createRequest(Request r) {
        List<Request> requestList = IMDB.getInstance().getRequests();
        requestList.add(r);
        System.out.println("Acestea sunt cererile tale: ");
        List<Request> requestList2 = IMDB.getInstance().getRequests();
        for (Request request : requestList2) {
            if (request.getUsername().equals(this.getUsername())) {
                request.viewRequest();
            }
        }
        List<User> users = IMDB.getInstance().getUsers();
        if (r.getTo().equals("ADMIN")) {
            for (User user : users) {
                if (user instanceof Admin) {
                    r.addObserver(user);
                }
            }
            r.notifyObservers(this, r.getDescription());
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
        for (Object observer :  r.getObservers()) {

            if (((User) observer).getNotifications() != null) {
                List<String> notifications = ((User) observer).getNotifications();
                Iterator<String> iterator = notifications.iterator();
                while (iterator.hasNext()) {
                    String notif = iterator.next();
                    if (notif.contains(r.getUsername()) && notif.contains(r.getDescription())) {
                        iterator.remove();
                    }
                }
            }
        }
        List<Request> requestList = IMDB.getInstance().getRequests();
        requestList.remove(r);

        System.out.println("Acestea sunt cererile tale: ");
        List<Request> requestList2 = IMDB.getInstance().getRequests();
        for (Request request : requestList2) {
            if (request.getUsername().equals(this.getUsername())) {
                request.viewRequest();
            }
        }
    }


}
