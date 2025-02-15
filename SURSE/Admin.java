package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.security.SecureRandom;

public class Admin<T extends Comparable<T>> extends Staff{

    public static class RequestsHolder {
        private static List<Request> requestsForAdmin = new ArrayList<>();

        public RequestsHolder() {

        }

        public static void addRequest(Request request) {
            requestsForAdmin.add(request);
        }

        public static void removeRequest(Request request) {
            requestsForAdmin.remove(request);
        }

        public static List<Request> getAllRequests() {
            return requestsForAdmin;
        }
    }


    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*";
    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
    private static final SecureRandom random = new SecureRandom();

    private SortedSet<T> contributionsForAdmins;
    public Admin() {
        this.contributionsForAdmins = new TreeSet<>();
    }

    public SortedSet<T> getContributionsForAdmins() { return contributionsForAdmins; }
    public void viewContributionsForAdmins() {
        for (T item : contributionsForAdmins) {
            if (item instanceof Actor) {
                System.out.println(((Actor) item).getName());
            }
            if (item instanceof Production) {
                System.out.println(((Production) item).getTitle());
            }
        }
    }

    public void addContributionsForAdmins(T item) {
        contributionsForAdmins.add(item);
    }

    public static String generateUsername(String fullName) {
        String[] nameParts = fullName.toLowerCase().split(" ");
        StringBuilder username = new StringBuilder(nameParts[0]);

        for (int i = 1; i < nameParts.length; i++) {
            username.append("_").append(nameParts[i]);
        }

        Random rand = new Random();
        int randomNum = rand.nextInt(1000);
        username.append("_").append(randomNum);

        return username.toString();
    }

    public static String generatePassword(int length) {
        StringBuilder password = new StringBuilder(length);

        password.append(selectRandom(CHAR_LOWER));
        password.append(selectRandom(CHAR_UPPER));
        password.append(selectRandom(NUMBER));
        password.append(selectRandom(OTHER_CHAR));

        for (int i = 4; i < length; i++) {
            password.append(selectRandom(PASSWORD_ALLOW_BASE));
        }

        return new String(password);
    }

    private static char selectRandom(String input) {
        int randomIndex = random.nextInt(input.length());
        return input.charAt(randomIndex);
    }

    public void removeContributionForAdmin(String string) {
        for (T item : contributionsForAdmins) {
            if (item instanceof Actor) {
                if (((Actor) item).getName().equals(string)) {
                    contributionsForAdmins.remove(item);
                    break;
                }
            }
            if (item instanceof Movie) {
                if (((Movie) item).getTitle().equals(string)) {
                    contributionsForAdmins.remove(item);
                    break;
                }
            }
            if (item instanceof Series) {
                if (((Series) item).getTitle().equals(string)) {
                    contributionsForAdmins.remove(item);
                    break;
                }
            }
        }
    }

    public void removeUser(String name) {
        List<User> users = IMDB.getInstance().getUsers();
        List<Production> productions = IMDB.getInstance().getProductions();
        User user = IMDB.getInstance().findUser(name);

        if (user instanceof Contributor) {
            for (User user1 : users) {
                if (user1 instanceof Admin) {
                    SortedSet<T> contributions = ((Contributor) user).getContributions();
                    for (T item : contributions) {
                        ((Admin) user1).addContributionsForAdmins(item);
                    }
                }
            }
        }

        for (Production production : productions) {
            for (Rating rating : production.getRatings()) {
                if (rating.getUsername().equals(name)) {
                    Vector<User> observers = rating.getObservers();
                    for (User observer : observers) {
                        if (observer.getNotifications() != null) {
                            List<String> notifications = observer.getNotifications();
                            Iterator<String> iterator = notifications.iterator();
                            while (iterator.hasNext()) {
                                String notif = iterator.next();
                                if (notif.contains(rating.getUsername()) && notif.contains(production.getTitle())) {
                                    iterator.remove();
                                }
                            }
                        }
                    }

                    production.getRatings().remove(rating);
                    break;
                }
            }
        }

        List<Request> requests = IMDB.getInstance().getRequests();
        List<Request> requestsDelete = new ArrayList<>();
        for (Request request : requests) {
            if (request.getUsername().equals(name)) {
                requestsDelete.add(request);
            }
        }

        for (Request request : requestsDelete) {
            if (user instanceof Regular) {
                ((Regular) user).removeRequest(request);
            } else {
                ((Contributor) user).removeRequest(request);
            }
        }

        users.remove(user);
    }

    public void addUser(String name, String email, String country, int age, String gender, String birthDate, String type) throws InformationIncompleteException {
        List<User> users = IMDB.getInstance().getUsers();

        String username = generateUsername(name);
        String password = generatePassword(15);

        if(type.equals("Regular")) {
            Regular newUser = (Regular) UserFactory.createUser(AccountType.Regular);
            newUser.setUsername(username);
            newUser.setExperience(0);

            Information.InformationBuilder builder = new Information.InformationBuilder();


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(birthDate, formatter);

            if (email != null && password != null && name != null) {
                Information userInfo = builder
                        .credentials(email, password)
                        .name(name)
                        .country(country)
                        .age(age)
                        .gender(gender)
                        .birthDate(date)
                        .build();

                newUser.setInformation(userInfo);

                newUser.setUserType(AccountType.Regular);
                users.add(newUser);
            } else {
                throw new InformationIncompleteException("Informatii incomplete!");
            }
        }

        if (type.equals("Admin")) {
            Admin newUser = (Admin) UserFactory.createUser(AccountType.Admin);
            newUser.setUsername(username);
            newUser.setExperience(null);

            Information.InformationBuilder builder = new Information.InformationBuilder();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(birthDate, formatter);

            if (email != null && password != null && name != null) {
                Information userInfo = builder
                        .credentials(email, password)
                        .name(name)
                        .country(country)
                        .age(age)
                        .gender(gender)
                        .birthDate(date)
                        .build();

                newUser.setInformation(userInfo);

                newUser.setUserType(AccountType.Admin);
                users.add(newUser);
            } else {
                throw new InformationIncompleteException("Informatii incomplete!");
            }
        }

        if (type.equals("Contributor")) {
            Contributor newUser = (Contributor) UserFactory.createUser(AccountType.Contributor);
            newUser.setUsername(username);
            newUser.setExperience(0);

            Information.InformationBuilder builder = new Information.InformationBuilder();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(birthDate, formatter);

            if (email != null && password != null && name != null) {
                Information userInfo = builder
                        .credentials(email, password)
                        .name(name)
                        .country(country)
                        .age(age)
                        .gender(gender)
                        .birthDate(date)
                        .build();

                newUser.setInformation(userInfo);

                newUser.setUserType(AccountType.Contributor);
                users.add(newUser);
            } else {
                throw new InformationIncompleteException("Informatii incomplete!");
            }
        }
    }
}
