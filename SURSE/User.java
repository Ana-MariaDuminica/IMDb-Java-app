package org.example;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class User<T extends Comparable<T>> implements Observer{

    public static class Information {
        private String name;
        private String country;
        private int age;
        private String gender;
        private Credentials credentials;
        private LocalDate birthDate;

        public Information() {

        }

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }

        public int getAge() {
            return age;
        }

        public String getGender() {
            return gender;
        }

        public LocalDate getBirthDate() {
            return birthDate;
        }

        public Credentials getCredentials() {
            return credentials;
        }



        private Information(InformationBuilder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.birthDate = builder.birthDate;
        }

        public static class InformationBuilder {
            private String name;
            private String country;
            private int age;
            private String gender;
            private LocalDate birthDate;
            private Credentials credentials;


            // Metode pentru setarea atributelor opționale sau configurabile
            public InformationBuilder name(String name) {
                this.name = name;
                return this;
            }

            public InformationBuilder country(String country) {
                this.country = country;
                return this;
            }

            public InformationBuilder age(int age) {
                this.age = age;
                return this;
            }


            public InformationBuilder gender(String gender) {
                this.gender = gender;
                return this;
            }

            public InformationBuilder birthDate(LocalDate birthDate) {
                this.birthDate = birthDate;
                return this;
            }
            public InformationBuilder credentials(String email, String password) {
                this.credentials = new Credentials(email, password);
                return this;
            }

            public Information build() {
                return new Information(this);
            }

        }

    }

    private Information information;
    private AccountType userType;
    private String username;
    private Integer experience;
    private List<String> notifications;
    private SortedSet<T> favorites;


    public User() {
        this.favorites = new TreeSet<>();
        this.notifications = new ArrayList<>();
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public AccountType getUserType() {
        return userType;
    }

    public void setUserType(AccountType userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    public SortedSet<T> getFavorites() {
        return favorites;
    }

    public void setFavorites(SortedSet<T> favorites) {
        this.favorites = favorites;
    }

    public void addFavorites(String string) {
        List<Production> productions = IMDB.getInstance().getProductions();
        List<Actor> actors = IMDB.getInstance().getActors();

        for (Production production : productions) {
            if (production.getTitle().equals(string)) {
                this.favorites.add((T) production);
                break;
            }
        }
        for (Actor actor : actors) {
            if (actor.getName().equals(string)) {
                this.favorites.add((T) actor);
                break;
            }
        }
    }

    public boolean searchInFavorites(String string) {
        for (T item : favorites) {
            if (item instanceof Actor) {
                if (((Actor) item).getName().equals(string)) {
                    return true;
                }
            }
            if (item instanceof Movie) {
                if (((Movie) item).getTitle().equals(string)) {
                    return true;
                }
            }
            if (item instanceof Series) {
                if (((Series) item).getTitle().equals(string)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void removeFavorite(String string) {
        for (T item : favorites) {
            if (item instanceof Actor) {
                if (((Actor) item).getName().equals(string)) {
                    favorites.remove(item);
                    break;
                }
            }
            if (item instanceof Movie) {
                if (((Movie) item).getTitle().equals(string)) {
                    favorites.remove(item);
                    break;
                }
            }
            if (item instanceof Series) {
                if (((Series) item).getTitle().equals(string)) {
                    favorites.remove(item);
                    break;
                }
            }
        }
    }

    public void updateExperience(int experience) {
        this.experience += experience;
    }


    public String logout() throws InvalidCommandException {
        System.out.println("Choose an option: Enter credentials again/Exit");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.nextLine();
        if (option.equals("Enter credentials again")) {
            IMDB.getInstance().initiateApplicationFlow();
        }
        else if (option.equals("Exit")) {
            System.exit(0);
        } else {
            throw new InvalidCommandException("Comanda invalida!");
        }
        return null;
    }



    public void viewFavorites() {
        SortedSet<T> list = getFavorites();
        for (T element : list) {
            if (element instanceof Actor) {
                System.out.println(((Actor) element).getName());
            }
            if (element instanceof Production) {
                System.out.println(((Production) element).getTitle());
            }
        }
    }



    public void viewNotifications() {
        List<String> notifications = getNotifications();
        if (notifications.isEmpty()) {
            System.out.println("No notifications");
        } else {
            System.out.println("Notifications:");
            for (String notification : notifications) {
                System.out.println(notification);
            }
        }
    }

    public void addNotifications(String notification) {
        notifications.add(notification);
    }


    @Override
    public void update(User user, String title) {
        int nota = 0;
        Production p = IMDB.getInstance().findProduction(title);
        if (p != null) {
            for (Rating rating : p.getRatings()) {
                if (rating.getUsername().equals(user.getUsername())) {
                    nota = rating.getRating();
                    break;
                }
            }
            String notificationMessage = user.getUsername() + " a adaugat un review la productia " + title + " -> " + nota;
            this.addNotifications(notificationMessage);
        } else {

            List<Request> requests = IMDB.getInstance().getRequests();
            for (Request request : requests) {
                if (user != null) {
                    if (request.getDescription().equals(title) && request.getUsername().equals(user.getUsername())) {
                        String notificationMessage = user.getUsername() + " a adaugat o cerere de tip " + request.getType() + " -> " + request.getDescription();
                        this.addNotifications(notificationMessage);
                        break;
                    }
                    if (request.getDescription().equals(title) && request.getTo().equals(user.getUsername())) {
                        if (!request.isChange()) {
                            String notificationMessage = user.getUsername() + " a respins cererea ta " + request.getType() + " -> " + request.getDescription();
                            this.addNotifications(notificationMessage);
                            break;
                        } else {
                            String notificationMessage = user.getUsername() + " a rezolvat cererea ta " + request.getType() + " -> " + request.getDescription();
                            this.addNotifications(notificationMessage);
                            break;
                        }
                    }
                } else {
                    if (request.getDescription().equals(title)) {
                        if (!request.isChange()) {
                            String notificationMessage = "Echipa de admini a respins cererea ta " + request.getType() + " -> " + request.getDescription();
                            this.addNotifications(notificationMessage);
                            break;
                        } else {
                            String notificationMessage = "Echipa de admini a rezolvat cererea ta " + request.getType() + " -> " + request.getDescription();
                            this.addNotifications(notificationMessage);
                            break;
                        }
                    }
                }
            }
        }
    }



}
