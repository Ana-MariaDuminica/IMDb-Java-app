package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class IMDB {
    private static IMDB instance;
    private List<Actor> actors;
    private List<Request> requests;
    private UserFactory userFactory;
    private List<Production> productions;
    private List <User> users;

    private IMDB() {
        this.actors = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.userFactory = new UserFactory();
        this.productions = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }
        return instance;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public List<Production> getProductions() {
        return productions;
    }


    public List<User> getUsers() {
        return users;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void run() {
        loadData();
        initiateApplicationFlow();
    }

    public void loadData() {
        loadProductions("C:\\Users\\anama\\Downloads\\POO-Tema-2023-checker (1)\\POO-Tema-2023-checker\\POO-Tema-2023-checker\\src\\test\\resources\\testResources\\production.json");
        loadActors("C:\\Users\\anama\\Downloads\\POO-Tema-2023-checker (1)\\POO-Tema-2023-checker\\POO-Tema-2023-checker\\src\\test\\resources\\testResources\\actors.json");
        loadRequests("C:\\Users\\anama\\Downloads\\POO-Tema-2023-checker (1)\\POO-Tema-2023-checker\\POO-Tema-2023-checker\\src\\test\\resources\\testResources\\requests.json");
        loadUsers("C:\\Users\\anama\\Downloads\\POO-Tema-2023-checker (1)\\POO-Tema-2023-checker\\POO-Tema-2023-checker\\src\\test\\resources\\testResources\\accounts.json");
        ratingProductions();
        sortRatingsByUserExperience();
        parseRequests();

    }

    public Staff findContributionStaff(String string) {
        for (User user : users) {
            if (user instanceof Admin || user instanceof Contributor) {
                if (((Staff) user).findContribution(string)) {
                    return (Staff) user;
                }
            }
        }
        return null;
    }

    public void sortRatingsByUserExperience() {
        for (Production production : productions) {
            List<Rating> ratings = production.getRatings();

            Comparator<Rating> byUserExperience = (rating1, rating2) -> {
                User user1 = findUser(rating1.getUsername());
                User user2 = findUser(rating2.getUsername());

                int experience1 = (user1 != null && user1.getExperience() != null) ? user1.getExperience() : 0;
                int experience2 = (user2 != null && user2.getExperience() != null) ? user2.getExperience() : 0;

                return Integer.compare(experience2, experience1);
            };

            ratings.sort(byUserExperience);
        }
    }


    public void ratingProductions() {
        for (Production production : productions) {

            for (Rating rating : production.getRatings()) {
                String nameUser = rating.getUsername();
                User user = findUser(nameUser);

                for (Rating rating1 : production.getRatings()) {
                    if (!rating1.getUsername().equals(nameUser)) {
                        User observerUser = findUser(rating1.getUsername());
                        if (observerUser instanceof Regular) {
                            rating.addObserver(observerUser);
                        }
                    }
                }
                Staff staff = findContributionStaff(production.getTitle());
                rating.addObserver(staff);

                if (rating.getObservers() != null) {
                    rating.notifyObservers(user, production.getTitle());
                }


            }
        }
    }
    public void parseRequests() {
        for (Request request : requests) {
            if (request.getType() == RequestTypes.OTHERS || request.getType() == RequestTypes.DELETE_ACCOUNT) {
                for (User user : users) {
                    if (user instanceof Admin) {
                        request.addObserver(user);
                    }
                }
                User user1 = findUser(request.getUsername());
                request.notifyObservers(user1, request.getDescription());
                Admin.RequestsHolder.addRequest(request);
            }
            if (request.getType() == RequestTypes.ACTOR_ISSUE || request.getType() == RequestTypes.MOVIE_ISSUE) {
                User userObserver = findUser(request.getTo());
                ((Staff) userObserver).getRequestList().add(request);
                User user = findUser(request.getUsername());
                request.addObserver(userObserver);
                request.notifyObservers(user, request.getDescription());
            }
        }
    }

    public boolean productionExists(String title) {
        for (Production production : productions) {
            if (production.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    public boolean actorExists(String name) {
        for (Actor actor : actors) {
            if (actor.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    public boolean userExists(String name) {
        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Actor findActor(String name) {
        for (Actor actor : actors) {
            if (actor.getName().equals(name)) {
                return actor;
            }
        }
        return null;
    }

    public Production findProduction(String name) {
        for (Production production : productions) {
            if (production.getTitle().equals(name)) {
                return production;
            }
        }
        return null;
    }
    public User findUser(String name) {
        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }


    private void loadUsers(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(new File(fileName));

            for (JsonNode userNode : rootNode) {
                AccountType userType = AccountType.valueOf(userNode.get("userType").asText());
                User user = userFactory.createUser(userType);

                String username = userNode.get("username").asText();
                int experience = userNode.get("experience").asInt();
                if (userNode.has("notifications")) {
                    JsonNode notifications = userNode.get("notifications");

                    if (notifications != null && notifications.isArray()) {
                        List<String> notif = new ArrayList<>();

                        for (JsonNode notification : notifications) {
                            String notificationText = notification.asText();
                            notif.add(notificationText);
                        }

                       user.setNotifications(notif);
                    }
                }

                JsonNode informationNode = userNode.get("information");
                String name = informationNode.get("name").asText();
                String country = informationNode.get("country").asText();
                int age = informationNode.get("age").asInt();
                String gender = informationNode.get("gender").asText();
                String birthDateStr = informationNode.get("birthDate").asText();

                JsonNode credentialsNode = informationNode.get("credentials");
                String email = credentialsNode.get("email").asText();
                String password = credentialsNode.get("password").asText();
                User.Information.InformationBuilder builder = new User.Information.InformationBuilder();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate birthDate = LocalDate.parse(birthDateStr, formatter);

                if (email != null && password != null && name != null && user != null) {
                    User.Information userInfo = builder
                            .credentials(email, password)
                            .name(name)
                            .country(country)
                            .age(age)
                            .gender(gender)
                            .birthDate(birthDate)
                            .build();
                    user.setInformation(userInfo);
                }
                if (user != null) {
                    user.setUsername(username);
                    user.setExperience(experience);
                    user.setUserType(userType);

                }
                if (user instanceof Regular) {
                    JsonNode favoriteActorsNode = userNode.get("favoriteActors");
                    JsonNode favoriteProductionsNode = userNode.get("favoriteProductions");

                    if (favoriteActorsNode != null && favoriteProductionsNode != null) {
                        List<String> favoriteActors = new ArrayList<>();
                        List<String> favoriteProductions = new ArrayList<>();

                        for (JsonNode actor : favoriteActorsNode) {
                            favoriteActors.add(actor.asText());
                        }
                        for (JsonNode production : favoriteProductionsNode) {
                            favoriteProductions.add(production.asText());
                        }

                        for (String item : favoriteActors) {
                            user.addFavorites(item);
                        }
                        for (String item : favoriteProductions) {
                            user.addFavorites(item);
                        }
                    }

                    users.add(user);
                }

                if (user instanceof Admin) {

                    JsonNode favoriteActorsNode = userNode.get("favoriteActors");
                    JsonNode favoriteProductionsNode = userNode.get("favoriteProductions");

                    if (favoriteActorsNode != null && favoriteProductionsNode != null) {
                        List<String> favoriteActors = new ArrayList<>();
                        List<String> favoriteProductions = new ArrayList<>();

                        for (JsonNode actor : favoriteActorsNode) {
                            favoriteActors.add(actor.asText());
                        }
                        for (JsonNode production : favoriteProductionsNode) {
                            favoriteProductions.add(production.asText());
                        }

                        for (String item : favoriteActors) {
                            user.addFavorites(item);
                        }
                        for (String item : favoriteProductions) {
                            user.addFavorites(item);
                        }
                    }
                    JsonNode actorsContributionNode = userNode.get("actorsContribution");
                    JsonNode productionsContributionNode = userNode.get("productionsContribution");

                    if (actorsContributionNode != null && productionsContributionNode != null) {
                        List<String> actorsContribution = new ArrayList<>();
                        List<String> productionsContribution = new ArrayList<>();

                        for (JsonNode actor : actorsContributionNode) {
                            actorsContribution.add(actor.asText());
                        }
                        for (JsonNode production : productionsContributionNode) {
                            productionsContribution.add(production.asText());
                        }

                        for (String item : actorsContribution) {
                            ((Admin) user).addContribution(item);
                        }
                        for (String item : productionsContribution) {
                            ((Admin) user).addContribution(item);
                        }
                    }

                    users.add(user);
                }

                if (user instanceof Contributor) {

                    JsonNode favoriteActorsNode = userNode.get("favoriteActors");
                    JsonNode favoriteProductionsNode = userNode.get("favoriteProductions");

                    if (favoriteActorsNode != null && favoriteProductionsNode != null) {
                        List<String> favoriteActors = new ArrayList<>();
                        List<String> favoriteProductions = new ArrayList<>();

                        for (JsonNode actor : favoriteActorsNode) {
                            favoriteActors.add(actor.asText());
                        }
                        for (JsonNode production : favoriteProductionsNode) {
                            favoriteProductions.add(production.asText());
                        }

                        for (String item : favoriteActors) {
                            user.addFavorites(item);
                        }
                        for (String item : favoriteProductions) {
                            user.addFavorites(item);
                        }
                    }
                    JsonNode actorsContributionNode = userNode.get("actorsContribution");
                    JsonNode productionsContributionNode = userNode.get("productionsContribution");

                    if (actorsContributionNode != null && productionsContributionNode != null) {
                        List<String> actorsContribution = new ArrayList<>();
                        List<String> productionsContribution = new ArrayList<>();

                        for (JsonNode actor : actorsContributionNode) {
                            actorsContribution.add(actor.asText());
                        }
                        for (JsonNode production : productionsContributionNode) {
                            productionsContribution.add(production.asText());
                        }

                        for (String item : actorsContribution) {
                            ((Contributor) user).addContribution(item);
                        }
                        for (String item : productionsContribution) {
                            ((Contributor) user).addContribution(item);
                        }
                    }
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRequests(String fileName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(fileName);
            Request[] requests1Array = objectMapper.readValue(file, Request[].class);

            for (Request request : requests1Array) {

                requests.add(request);
                request.setFormattedBirthDate();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("eroare grava");
        }
    }

    private void loadActors(String fileName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(fileName);
            Actor[] actorArray = objectMapper.readValue(file, Actor[].class);

            for (Actor actor : actorArray) {
                String actorName = actor.getName();

                if (!actorExists(actorName)) {
                    actors.add(actor);

                } else if (actorExists(actorName)) {
                    String newBiography = actor.getBiography();

                    Actor existingActor = findActor(actorName);
                    existingActor.setBiography(newBiography);

                    List<Map<String, String>> performances = actor.getPerformances();
                    List<Map<String, String>> performancesActor = new ArrayList<>();
                    List<Map<String, String>> existingPerformances = existingActor.getPerformances();

                    for (Map<String, String> performance : performances) {
                        String performanceType = performance.get("type");
                        String performanceTitle = performance.get("title");

                        if (performanceType != null && performanceTitle != null) {
                            boolean exists = false;

                            for (Map<String, String> existingPerformance : existingPerformances) {
                                String existingTitle = existingPerformance.get("title");
                                String existingType = existingPerformance.get("type");

                                if (existingTitle != null && existingType != null && existingTitle.equals(performanceTitle)) {
                                    exists = true;
                                    break;
                                }
                            }

                            if (!exists) {
                                Map<String, String> newPerformance = new HashMap<>();
                                newPerformance.put("title", performanceTitle);
                                newPerformance.put("type", performanceType);
                                performancesActor.add(newPerformance);
                            }
                        }
                    }
                    if (performancesActor != null) {
                        for (Map<String, String> performance : performancesActor) {
                            existingActor.getPerformances().add(performance);
                        }
                    }

                }
            }
            Collections.sort(productions);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("eroare grava");
        }
    }


    private void loadProductions(String fileName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(fileName);
            JsonNode[] nodes = objectMapper.readValue(file, JsonNode[].class);

            for (JsonNode node : nodes) {
                if (node.has("type")) {
                    String productionType = node.get("type").asText();
                    if (productionType.equals("Movie") || productionType.equals("Series")) {
                        Production production;
                        if (productionType.equals("Movie")) {
                            production = objectMapper.treeToValue(node, Movie.class);
                        } else {
                            production = objectMapper.treeToValue(node, Series.class);
                        }

                        if (production != null) {
                            productions.add(production);

                            List<String> actors1 = production.getActors();
                            for (String actorName : actors1) {
                                if (!actorExists(actorName)) {
                                    Actor actor = new Actor();
                                    actor.setName(actorName);

                                    List<Map<String, String>> performancesActor = new ArrayList<>();
                                    Map<String, String> newPerformance = new HashMap<>();
                                    newPerformance.put("title", production.getTitle());
                                    newPerformance.put("type", production.getType());
                                    performancesActor.add(newPerformance);

                                    actor.setPerformances(performancesActor);
                                    actors.add(actor);
                                } else {
                                    Actor existingActor = findActor(actorName);
                                    List<Map<String, String>> performances = existingActor.getPerformances();

                                    Map<String, String> newPerformance = new HashMap<>();
                                    newPerformance.put("title", production.getTitle());
                                    newPerformance.put("type", production.getType());

                                    performances.add(newPerformance);
                                    existingActor.setPerformances(performances);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public User authenticateUser(String email, String password) {
        List<User> loadedUsers = getUsers();
        for (User user : loadedUsers) {
            User.Information userInformation = user.getInformation();
            if (userInformation != null && userInformation.getCredentials() != null) {
                if (userInformation.getCredentials().getEmail().equals(email) && userInformation.getCredentials().getPassword().equals(password)) {
                    return user;
                }
            }
        }
        return null;
    }

    public void initiateApplicationFlow() {
        System.out.println("Welcome back! Enter your credentials!");
        System.out.print("email: ");
        Scanner scanner = new Scanner(System.in);

        String email = scanner.nextLine();
        System.out.print("password: ");
        String password = scanner.nextLine();

        User authenticatedUser = authenticateUser(email, password);

        if (authenticatedUser != null) {
            System.out.println("Welcome back user " + authenticatedUser.getUsername() + "!");
            System.out.println("Username: " + authenticatedUser.getUsername());
            System.out.println("User experience: " + authenticatedUser.getExperience());
            if (authenticatedUser instanceof Regular) {
                menuForRegular((Regular) authenticatedUser);
            } else if (authenticatedUser instanceof Admin) {
                menuForAdmin((Admin) authenticatedUser);
            } else if (authenticatedUser instanceof Contributor) {
                menuForContributor((Contributor) authenticatedUser);
            }
        } else {
            System.out.println("Wrong credentials!");
            initiateApplicationFlow();
        }
    }

    public void menuForRegular(Regular regular) {
        try {
            System.out.println("Choose action: ");
            System.out.println("1) View productions details");
            System.out.println("2) View actors details");
            System.out.println("3) View notifications");
            System.out.println("4) Search for actor/movie/series");
            System.out.println("5) Add/Delete actor/movie/series to/from favorites");
            System.out.println("6) Add/Delete a request");
            System.out.println("7) Add/Delete rating for a production");
            System.out.println("8) Logout");

            Scanner scanner = new Scanner(System.in);
            String selectedAction = scanner.nextLine();
            actions(selectedAction, regular);
        } catch (InvalidCommandException e) {
            System.out.println("Comanda invalida! Introdu o comanda valida.");
        }
    }


    public void actions(String selectedAction, User user) throws InvalidCommandException {
        Scanner scanner = new Scanner(System.in);
        if (selectedAction.equals("View productions details")) {
            viewProductionsDetails();
        }
        else if (selectedAction.equals("View actors details")) {
            viewActorsDetails();
        }
        else if (selectedAction.equals("Search for actor/movie/series")) {
            searchForActorMovieSeries();
        }
        else if (selectedAction.equals("View notifications")) {
            user.viewNotifications();
        }
        else if (selectedAction.equals("Add/Delete actor/movie/series to/from favorites")) {
            System.out.println("Choose one: Add/Delete");
            String option = scanner.nextLine();
            if (option.equals("Add")) {
                addActorMovieSeriesFavorites(user);
            }
            else if (option.equals("Delete")) {
                deleteActorMovieSeriesFavorites(user);
            } else {
                throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
            }
        }
        else if (selectedAction.equals("Add/Delete rating for a production")) {
            addDeleteRatingForProduction(user);
        }
        else if (selectedAction.equals("Add/Delete user")) {
            if (user instanceof Admin) {
                System.out.println("Alege o cifra: ");
                System.out.println("1. Add");
                System.out.println("2. Delete");
                int option = scanner.nextInt();
                scanner.nextLine();

                if (option == 1) {
                    System.out.println("Please enter name: ");
                    String name = scanner.nextLine();
                    System.out.println("Email: ");
                    String email = scanner.nextLine();
                    System.out.println("Country: ");
                    String country = scanner.nextLine();
                    System.out.println("Age: ");

                    String age = scanner.nextLine();
                    try {
                        int userAge = Integer.parseInt(age);
                        System.out.println("Gender: ");
                        String gender = scanner.nextLine();
                        System.out.println("Birth Date: ");
                        String birthDate = scanner.nextLine();
                        System.out.println("User type: ");
                        String type = scanner.nextLine();
                        if (type.equals("Regular") || type.equals("Admin") || type.equals("Contributor")) {
                            try {
                                ((Admin) user).addUser(name, email, country, userAge, gender, birthDate, type);
                                for (User newUser : users) {
                                    if (newUser.getInformation().getName().equals(name)) {
                                        System.out.println("This is your username: " + newUser.getUsername());
                                        System.out.println("This is your password: " + newUser.getInformation().getCredentials().getPassword());
                                        break;
                                    }
                                }
                            } catch (InformationIncompleteException e) {
                                System.out.println("Informatii incomplete!");
                            }
                        } else {
                            throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
                        }
                    } catch (NumberFormatException e) {
                        throw new InvalidCommandException("Varsta trebuie sa fie un numar!");
                    }
                }
                else if (option == 2) {
                    System.out.println("Ce utilizator vrei sa stergi?");
                    for (User user1 : users) {
                        System.out.println(user1.getUsername());
                    }
                    String name = scanner.nextLine();
                    if (userExists(name)) {
                        ((Admin) user).removeUser(name);
                    } else {
                        throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
                    }
                } else {
                    throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
                }
            }
        }
        else if (selectedAction.equals("Add/Delete actor/movie/series from system")) {
            if (user instanceof Admin || user instanceof Contributor) {
                System.out.println("Alege o cifra:");
                System.out.println("1. Add");
                System.out.println("2. Delete");
                int option = scanner.nextInt();
                scanner.nextLine();

                if (option == 1) {
                    addActorMovieSeriesSystem((Staff) user);
                }
                else if (option == 2) {
                    deleteActorMovieSeriesSystem((Staff) user);
                } else {
                    throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
                }
            }
        }
        else if (selectedAction.equals("Update Production Details")) {
            if (user instanceof Admin || user instanceof Contributor) {
                updateProductionDetails((Staff) user);
            }
        }
        else if (selectedAction.equals("Update Actor Details")) {
            if (user instanceof Admin || user instanceof Contributor) {
                updateActorDetails((Staff) user);
            }
        }
        else if (selectedAction.equals("Add/Delete a request")) {
            addDeleteRequest(user);
        }
        else if (selectedAction.equals("Solve a request")) {
            solveRequest(user);
        }
        else if (selectedAction.equals("Logout")) {
            user.logout();
        } else {
            throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
        }

        if (user instanceof Regular) {
            menuForRegular((Regular) user);
        }
        if (user instanceof Admin) {
            menuForAdmin((Admin) user);
        }
        if (user instanceof Contributor) {
            menuForContributor((Contributor) user);
        }

    }

    public void menuForAdmin(Admin admin) {
        try {
            System.out.println("Choose action: ");
            System.out.println("1) View productions details");
            System.out.println("2) View actors details");
            System.out.println("3) View notifications");
            System.out.println("4) Search for actor/movie/series");
            System.out.println("5) Add/Delete actor/movie/series to/from favorites");
            System.out.println("6) Add/Delete user");
            System.out.println("7) Add/Delete actor/movie/series from system");
            System.out.println("8) Update Production Details");
            System.out.println("9) Update Actor Details");
            System.out.println("10) Solve a request");
            System.out.println("11) Logout");

            Scanner scanner = new Scanner(System.in);
            String selectedAction = scanner.nextLine();
            actions(selectedAction, admin);
        } catch (InvalidCommandException e) {
            System.out.println("Comanda invalida! Introdu o comanda valida.");
        }
    }

    public void menuForContributor(Contributor contributor) {
        try {
            System.out.println("Choose action: ");
            System.out.println("1) View productions details");
            System.out.println("2) View actors details");
            System.out.println("3) View notifications");
            System.out.println("4) Search for actor/movie/series");
            System.out.println("5) Add/Delete actor/movie/series to/from favorites");
            System.out.println("6) Add/Delete a request");
            System.out.println("7) Add/Delete actor/movie/series from system");
            System.out.println("8) Update Production Details");
            System.out.println("9) Update Actor Details");
            System.out.println("10) Solve a request");
            System.out.println("11) Logout");

            Scanner scanner = new Scanner(System.in);
            String selectedAction = scanner.nextLine();
            actions(selectedAction, contributor);
        } catch (InvalidCommandException e) {
            System.out.println("Comanda invalida! Introdu o comanda valida.");
        }
    }

    public void viewProductionsDetails() throws InvalidCommandException {
        System.out.println("Doresti productiile sortate dupa gen sau numarul de ratings?");
        System.out.println("Alege o cifra");
        System.out.println("1. Dupa gen");
        System.out.println("2. Dupa numarul de ratings");
        System.out.println("3. Nu sorta");
        Scanner scanner = new Scanner(System.in);
        int op = scanner.nextInt();
        scanner.nextLine();
        if (op == 3) {
            List<Production> loadedProductions = getProductions();
            System.out.println("Movies: ");
            for (Production production : loadedProductions) {
                if (production instanceof Movie) {
                    production.displayInfo();
                }
            }
            System.out.println("Series: ");
            for (Production production : loadedProductions) {
                if (production instanceof Series) {
                    production.displayInfo();
                }
            }
        } else if (op == 2) {
            System.out.println("Introdu numarul de ratings:");
            int minRatings = scanner.nextInt();
            List<Production> productions1 = filterProductionsByRatings(productions, minRatings);

            System.out.println("Movies: ");
            for (Production production : productions1) {
                if (production instanceof Movie) {
                    production.displayInfo();
                }
            }
            System.out.println("Series: ");
            for (Production production : productions1) {
                if (production instanceof Series) {
                    production.displayInfo();
                }
            }
        } else if (op == 1) {
            System.out.println("Ce gen alegi?");
            for (Genre genre : Genre.values()) {
                System.out.println(genre.toString());
            }

            String chosenGenre = scanner.nextLine();

            boolean found = false;
            for (Genre genre : Genre.values()) {
                if (genre.toString().equalsIgnoreCase(chosenGenre)) {
                    found = true;
                    System.out.println("Productiile cu genul " + genre.toString() + " sunt:");
                    for (Production production : productions) {
                        for (Genre productionGenre : production.getGenres()) {
                            if (productionGenre == genre) {
                                System.out.println(production.getTitle());
                                break;
                            }
                        }
                    }
                }
            }

            if (!found) {
                throw new InvalidCommandException("Genul introdus nu exista in lista de genuri disponibile.");
            }

        } else {
            throw new InvalidCommandException("Comanda invalida!");
        }
    }

    private List<Production> filterProductionsByRatings(List<Production> productions, int minRatings) {
        List<Production> filteredProductions = new ArrayList<>();
        for (Production production : productions) {
            if (production.getNumberOfRatings() == minRatings) {
                filteredProductions.add(production);
            }
        }
        return filteredProductions;
    }

    public void viewActorsDetails() {
        List<Actor> loadedActors = getActors();
        System.out.println("Actors details: ");
        for (Actor actor : loadedActors) {
            actor.viewActorDetails();
            System.out.println("\n");
        }
    }

    public void searchForActorMovieSeries() throws InvalidCommandException {
        System.out.println("Please enter name or title: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        if(!actorExists(name) && !productionExists(name)) {
            throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
        } else {
            for (Actor actor : actors) {
                if(actor.getName().equals(name)) {
                    if (actor.getName() != null) { System.out.println("Name: " + actor.getName()); }
                    if (actor.getPerformances() != null) { System.out.println("Performances: " + actor.getPerformances()); }
                    if (actor.getBiography() != null) { System.out.println("Biography: " + actor.getBiography()); }
                    System.out.println("\n");
                    break;
                }
            }
            for (Production production : productions) {
                if(production.getTitle().equals(name)) {
                    production.displayInfo();
                }
            }
        }
    }

    public void addActorMovieSeriesFavorites (User user) throws InvalidCommandException {
        System.out.println("Your favorites: ");
        user.viewFavorites();
        System.out.println("Enter actor name/title movie/title series");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.nextLine();

        if(productionExists(option) || actorExists(option)) {
            user.addFavorites(option);
            user.viewFavorites();
        } else {
            throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
        }

    }

    public void deleteActorMovieSeriesFavorites (User user) throws InvalidCommandException {
        System.out.println("Your favorites: ");
        user.viewFavorites();
        System.out.println("Enter actor/movie/series you want to delete");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.nextLine();

        if (user.searchInFavorites(option)) {
            user.removeFavorite(option);
            System.out.println("Your new favorites: ");
            user.viewFavorites();
        } else {
            throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
        }
    }
    public void addDeleteRatingForProduction(User user) throws InvalidCommandException{
        Scanner scanner = new Scanner(System.in);
        if (user instanceof Regular) {
            System.out.println("Alege o cifra:");
            System.out.println("1. Add");
            System.out.println("2. Delete");
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                System.out.println("Enter production title: ");
                String title = scanner.nextLine();
                if (productionExists(title)) {
                    if (!((Regular) user).findRating(title)) {
                        System.out.println("Deja ai un rating la aceasta productie!");
                        System.out.println("Doresti sa il stergi si sa introduci altul?");
                        System.out.println("Alege o cifra:");
                        System.out.println("1. Da");
                        System.out.println("2. Nu");
                        int op = scanner.nextInt();
                        scanner.nextLine();
                        if (op == 1) {
                            ((Regular) user).deleteRatingRegular(title);
                            System.out.println("Rating sters");
                        } else if (op == 2) {
                            menuForRegular((Regular) user);
                        } else {
                            throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
                        }
                    }
                    System.out.println("Enter rating (1-10): ");
                    int rating = scanner.nextInt();
                    scanner.nextLine();
                    if (rating >= 1 && rating <= 10) {
                        System.out.println("Enter comment: ");
                        String comment = scanner.nextLine();
                        Rating rating1 = new Rating(user.getUsername(), rating, comment);
                        ((Regular) user).addRatingRegular(title, rating1);
                    } else {
                        throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
                    }
                } else {
                    throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
                }
            }
            else if (option == 2) {
                System.out.println("Enter production name: ");
                String title = scanner.nextLine();
                if (productionExists(title)) {
                    ((Regular) user).deleteRatingRegular(title);
                } else {
                    throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
                }
            } else {
                throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
            }
        }
    }
    public void addDeleteRequest(User user) throws InvalidCommandException {
        Scanner scanner = new Scanner(System.in);
        if (user instanceof Regular || user instanceof Contributor) {
            System.out.println("Alege o cifra:");
            int op = scanner.nextInt();
            scanner.nextLine();

            if (op == 1) {
                System.out.println("Introdu tipul cererii");
                String typeInput = scanner.nextLine();
                try {
                    RequestTypes type = RequestTypes.valueOf(typeInput);
                    System.out.println("Introdu descrierea cererii");
                    String description = scanner.nextLine();
                    LocalDateTime createdDate = LocalDateTime.now();

                    if (type == RequestTypes.OTHERS || type == RequestTypes.DELETE_ACCOUNT) {
                        String to = "ADMIN";
                        String actorName = null;
                        String movieTitle = null;
                        Request r = new Request(type, user.getUsername(), createdDate, to, description, actorName, movieTitle);
                        if (user instanceof Regular) {
                            ((Regular) user).createRequest(r);
                        } else {
                            ((Contributor) user).createRequest(r);
                        }
                    }
                    if (type == RequestTypes.MOVIE_ISSUE || type == RequestTypes.ACTOR_ISSUE) {
                        if (type == RequestTypes.ACTOR_ISSUE) {
                            System.out.println("Introdu numele actorului");
                            String actorName = scanner.nextLine();

                            if (actorExists(actorName)) {
                                String movieTitle = null;
                                Staff staff = findContributionStaff(actorName);
                                if (staff != null) {
                                    String to = staff.getUsername();
                                    Request r = new Request(type, user.getUsername(), createdDate, to, description, actorName, movieTitle);
                                    if (user instanceof Regular) {
                                        System.out.println("Acestea sunt cererile tale: ");
                                        ((Regular) user).createRequest(r);
                                    } else {
                                        System.out.println("Acestea sunt cererile tale: ");
                                        ((Contributor) user).createRequest(r);
                                    }
                                } else {
                                    String to = "ADMIN";
                                    Request r = new Request(type, user.getUsername(), createdDate, to, description, actorName, movieTitle);
                                    if (user instanceof Regular) {
                                        ((Regular) user).createRequest(r);
                                    } else {
                                        ((Contributor) user).createRequest(r);
                                    }
                                }

                            } else {
                                throw new InvalidCommandException("Actorul nu exista in sistem!");
                            }
                        }
                        if (type == RequestTypes.MOVIE_ISSUE) {
                            System.out.println("Introdu titlul productiei");
                            String actorName = null;
                            String movieTitle = scanner.nextLine();

                            if (productionExists(movieTitle)) {
                                Staff staff = findContributionStaff(movieTitle);
                                if (staff != null) {
                                    String to = staff.getUsername();
                                    Request r = new Request(type, user.getUsername(), createdDate, to, description, actorName, movieTitle);
                                    if (user instanceof Regular) {
                                        ((Regular) user).createRequest(r);
                                    } else {
                                        ((Contributor) user).createRequest(r);
                                    }
                                } else {
                                    String to = "ADMIN";
                                    Request r = new Request(type, user.getUsername(), createdDate, to, description, actorName, movieTitle);
                                    if (user instanceof Regular) {
                                        ((Regular) user).createRequest(r);
                                    } else {
                                        ((Contributor) user).createRequest(r);
                                    }
                                }
                            } else {
                                throw new InvalidCommandException("Productia nu exista in sistem!");
                            }
                        }
                    }

                    for (Request request : requests) {
                        if (request.getUsername().equals(user.getUsername())) {
                            request.viewRequest();
                        }
                    }


                } catch (IllegalArgumentException e) {
                    System.out.println("Tipul cererii introdus nu este valid!");
                }
            }
            else if (op == 2) {
                System.out.println("Acestea sunt cererile tale: ");
                int nrRequests = 0;
                for (Request request : requests) {
                    if (request.getUsername().equals(user.getUsername())) {
                        nrRequests++;
                        request.viewRequest();
                    }
                }

                System.out.println("Ce cerere vrei sa stergi? Introdu nr cererii");
                System.out.println(nrRequests);
                int nr = scanner.nextInt();
                scanner.nextLine();

                if (nr <= nrRequests && nr >= 1) {
                    int stop = 0;

                    for (Request request : requests) {
                        if (request.getUsername().equals(user.getUsername())) {
                            stop++;
                            if (stop == nr) {
                                if (user instanceof Regular) {
                                    ((Regular) user).removeRequest(request);
                                } else {
                                    ((Contributor) user).removeRequest(request);
                                }
                                break;
                            }
                        }
                    }
                    System.out.println("Acestea sunt cererile tale: ");
                    for (Request request : requests) {
                        if (request.getUsername().equals(user.getUsername())) {
                            request.viewRequest();
                        }
                    }
                } else {
                    throw new InvalidCommandException("Comanda invalida!");
                }

            } else {
                throw new InvalidCommandException("Comanda invalida!");
            }
        }
    }
    public void solveRequest(User user) throws InvalidCommandException {
        Scanner scanner = new Scanner(System.in);
        List<Request> allRequests = Admin.RequestsHolder.getAllRequests();
        List<Request> requestList = ((Staff) user).getRequestList();
        if (user instanceof Contributor) {
            if (((Staff) user).getRequestList() != null) {
                System.out.println("Acestea sunt cererile pe care poti sa le rezolvi");
                for (Request request : requestList) {
                    request.viewRequest();
                }
            }
        }
        if (user instanceof Admin) {
            if (((Staff) user).getRequestList() != null) {
                System.out.println("Acestea sunt cererile pe care poti sa le rezolvi");
                for (Request request : requestList) {
                    request.viewRequest();
                }
            }
            if (allRequests != null) {
                for (Request request : allRequests) {
                    request.viewRequest();
                }
            }
        }


        System.out.println("Alege o cifra");
        System.out.println("1. Respingere cerere");
        System.out.println("2. Rezolvare cerere");
        System.out.println("3. Marcheaza cerere ca fiind rezolvata");
        System.out.println("4. Iesi");

        int op = scanner.nextInt();
        scanner.nextLine();
        if (op == 1) {
            System.out.println("Ce cerere vrei sa respingi?");
            System.out.println("Introdu urmatoarele date");
            System.out.println("Description: ");
            String description = scanner.nextLine();
            System.out.println("To: ");
            String to = scanner.nextLine();
            boolean change = false;

            if (to.equals("ADMIN")) {
                if (allRequests != null) {
                    for (Request request : allRequests) {
                        if (request.getDescription().equals(description) && request.getTo().equals(to)) {
                            User user1 = findUser(request.getUsername());
                            for (User adminUser : users) {
                                if (user instanceof Admin) {
                                    request.removeObserver(adminUser);
                                }
                            }
                            request.addObserver(user1);
                            request.setChange(false);
                            User userNull = null;
                            request.notifyObservers(userNull, request.getDescription());
                            // notificare pentru userul care a dat cererea
                            Admin.RequestsHolder.removeRequest(request);
                            requests.remove(request);
                            change = true;
                            break;
                        }
                    }
                }
            } else {
                if (requestList != null) {
                    for (Request request : requestList) {
                        if (request.getDescription().equals(description) && request.getTo().equals(to)) {
                            User user1 = findUser(request.getUsername());
                            request.removeObserver(user);
                            request.addObserver(user1);
                            request.setChange(false);
                            request.notifyObservers(user, request.getDescription());
                            // notificare pentru userul care a dat cererea
                            Admin.RequestsHolder.removeRequest(request);
                            requests.remove(request);
                            change = true;
                            break;
                        }
                    }
                }
            }
            if (!change) {
                throw new InvalidCommandException("Cererea nu a fost gasita!");
            }
        }
        else if (op == 2) {
            System.out.println("Rezolva cererea: ");
            if (user instanceof Admin) {
                menuForAdmin((Admin) user);
            } else {
                menuForContributor((Contributor) user);
            }
        }
        else if (op == 3) {
            System.out.println("Ce cerere marchezi ca fiind rezolvata?");
            System.out.println("Introdu urmatoarele date");
            System.out.println("Description: ");
            String description = scanner.nextLine();
            System.out.println("To: ");
            String to = scanner.nextLine();
            boolean change = false;

            if (to.equals("ADMIN")) {
                if (allRequests != null) {
                    for (Request request : allRequests) {
                        if (request.getDescription().equals(description) && request.getTo().equals(to)) {
                            User user1 = findUser(request.getUsername());
                            for (User adminUser : users) {
                                if (user instanceof Admin) {
                                    request.removeObserver(adminUser);
                                }
                            }
                            request.addObserver(user1);
                            request.setChange(true);
                            User userNull = null;
                            request.notifyObservers(userNull, request.getDescription());
                            // notificare pentru userul care a dat cererea
                            Admin.RequestsHolder.removeRequest(request);
                            requests.remove(request);
                            change = true;

                            // cererea este rezolvata
                            ResolveRequestExperienceStrategy resolveStrategy = new ResolveRequestExperienceStrategy();
                            int experienceGained = resolveStrategy.calculateExperience();

                            // acordarea experientei pentru utilizator
                            user1.updateExperience(experienceGained);
                            sortRatingsByUserExperience();
                            break;
                        }
                    }
                }
            } else {
                if (requestList != null) {
                    for (Request request : requestList) {
                        if (request.getDescription().equals(description) && request.getTo().equals(to)) {
                            User user1 = findUser(request.getUsername());
                            request.removeObserver(user);
                            request.addObserver(user1);
                            request.setChange(true);
                            request.notifyObservers(user, request.getDescription());
                            // notificare pentru userul care a dat cererea
                            ((Staff) user).getRequestList().remove(request);
                            requests.remove(request);
                            change = true;

                            ResolveRequestExperienceStrategy resolveStrategy = new ResolveRequestExperienceStrategy();
                            int experienceGained = resolveStrategy.calculateExperience();

                            // acordarea experientei pentru utilizator
                            user1.updateExperience(experienceGained);
                            sortRatingsByUserExperience();

                            break;
                        }
                    }
                }
            }
            if (!change) {
                throw new InvalidCommandException("Cererea nu a fost gasita!");
            }

        } else if (op == 4) {

        }
        else {
            throw new InvalidCommandException("Comanda invalida!");
        }

    }

    public void addPerformances(List<Map<String, String>> performances) throws InvalidCommandException {
        boolean addingPerformances = true;
        Scanner scanner = new Scanner(System.in);

        while (addingPerformances) {
            Map<String, String> performance = new HashMap<>();
            System.out.println("Title: ");
            String title = scanner.nextLine();
            if (title.equals("finish")) {
                addingPerformances = false;
            } else if (productionExists(title)) {
                Production p = findProduction(title);
                String type = p.getType();
                performance.put("title", title);
                performance.put("type", type);
                performances.add(performance);
            } else {
                throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
            }
        }
    }

    public void addActorMovieSeriesSystem(Staff staff) throws InvalidCommandException {
        System.out.println("Alege o cifra:");
        System.out.println("1. Actor");
        System.out.println("2. Production");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.nextLine();

        if (option == 1) {
            System.out.println("Name: ");
            String name = scanner.nextLine();

            System.out.println("Performances (type 'finish' to end): ");
            List<Map<String, String>> performances = new ArrayList<>();

            addPerformances(performances);

            System.out.println("Biography: ");
            String biography = scanner.nextLine();

            Actor actor = new Actor(name, performances, biography);
            staff.addActorSystem(actor);

        }
        else if (option == 2) {
            System.out.println("Title: ");
            String title = scanner.nextLine();

            System.out.println("Type: ");
            String type = scanner.nextLine();

            List<String> directors = new ArrayList<>();
            boolean addingDirectors = true;
            System.out.println("Directors (type 'finish' to end): ");
            while (addingDirectors) {
                String director = scanner.nextLine();
                if (director.equals("finish")) {
                    addingDirectors = false;
                } else {
                    directors.add(director);
                }
            }

            List<String> actors1 = new ArrayList<>();
            boolean addingActors = true;
            System.out.println("Actors (type 'finish' to end): ");
            while (addingActors) {
                String actor = scanner.nextLine();
                if (actor.equals("finish")) {
                    addingActors = false;
                } else {
                    addActorFromProduction(staff, actor, title, type);
                    actors1.add(actor);
                }
            }

            List<Genre> genres = new ArrayList<>();
            boolean addingGenres = true;
            System.out.println("Choose genres from this list (type 'finish' to end): ");

            Genre[] genres1 = Genre.values();
            for (Genre genre : genres1) {
                System.out.println(genre);
            }
            while (addingGenres) {
                String input = scanner.nextLine();
                if (input.equals("finish")) {
                    addingGenres = false;
                } else {
                    try {
                        Genre selectedGenre = Genre.valueOf(input);
                        genres.add(selectedGenre);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid genre. Please choose from the list or type 'finish'");
                    }
                }
            }

            System.out.println("Plot: ");
            String plot = scanner.nextLine();

            System.out.println("Release Year: ");
            Scanner scanner1 = new Scanner(System.in);
            int releaseYear = scanner1.nextInt();

            List<Rating> ratings = new ArrayList<>();

            if (type.equals("Movie")) {
                System.out.println("Duration: ");
                String duration = scanner.nextLine();

                Movie movie = new Movie(title, directors, actors1, genres, ratings, plot, 0, type, duration, releaseYear);
                staff.addProductionSystem(movie);

            }

            if (type.equals("Series")) {
                System.out.println("Number of seasons: ");
                int numSeasons = scanner1.nextInt();

                Map<String, List<Episode>> seasons = new HashMap<>();
                for (int i = 1; i <= numSeasons; i++) {
                    String nrSeason = "Season " + i;
                    List<Episode> episode = new ArrayList<>();

                    System.out.println("Episodes for " + nrSeason + " (type 'finish' to end): ");
                    addEpisode(episode);
                    seasons.put(nrSeason, episode);
                }
                Series series = new Series(title, directors, actors1, genres, ratings, plot, 0, releaseYear, numSeasons, seasons, type);
                staff.addProductionSystem(series);
            }
        } else {
            throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
        }
    }

    public void addActorFromProduction(Staff staff, String actor, String title, String type) {
        if (actorExists(actor)) {
            Map<String, String> performance = new HashMap<>();
            performance.put("title", title);
            if (type.equals("Series")) {
                performance.put("type", "Sitcom");
            } else {
                performance.put("type", "Movie");
            }
            Actor existingActor = findActor(actor);
            List<Map<String, String>> performancesActor = existingActor.getPerformances();
            performancesActor.add(performance);
            existingActor.setPerformances(performancesActor);
        } else {
            Actor newActor = new Actor();
            List<Map<String, String>> performancesActor = new ArrayList<>();
            Map<String, String> performance = new HashMap<>();

            performance.put("title", title);
            if (type.equals("Series")) {
                performance.put("type", "Sitcom");
            } else {
                performance.put("type", "Movie");
            }
            performancesActor.add(performance);
            newActor.setPerformances(performancesActor);
            newActor.setName(actor);
            staff.addActorSystem(newActor);
        }
    }

    public void addEpisode(List<Episode> episode) {
        boolean addingEpisode = true;
        while (addingEpisode) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("episode Name: ");
            String episodeName = scanner.nextLine();

            System.out.println("Duration: ");
            String durationEpisode = scanner.nextLine();
            Episode episode1 = new Episode(episodeName, durationEpisode);
            episode.add(episode1);

            System.out.println("add or finish");
            String option1 = scanner.nextLine();
            if (option1.equals("finish")) {
                addingEpisode = false;
            }
        }
    }

    public void deleteActorMovieSeriesSystem(Staff staff) throws InvalidCommandException {
        System.out.println("Lista de contributii: ");
        staff.viewContributions();
        if (staff instanceof Admin) {
            if (((Admin) staff).getContributionsForAdmins() != null) {
                ((Admin) staff).viewContributionsForAdmins();
            }
        }
        System.out.println("Ce vrei sa stergi?");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.nextLine();

        if (staff.actorContExists(option)) {
            staff.removeActorSystem(option);

            for (User user1 : users) {
                if(user1.searchInFavorites(option)) {
                    user1.removeFavorite(option);
                }
            }
            staff.removeContribution(option);

        } else if (staff.productionContExists(option)) {
            staff.removeProductionSystem(option);

            for (User user1 : users) {
                if(user1.searchInFavorites(option)) {
                    user1.removeFavorite(option);
                }
            }

            staff.removeContribution(option);

        } else if ((staff instanceof Admin) && ((Admin) staff).getContributionsForAdmins().contains(option)) {
            for (User user : users) {
                if (user instanceof Admin) {
                    ((Admin) user).removeContributionForAdmin(option);
                }
            }

        } else {
            throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
        }
    }

    public void updateProductionDetails (Staff staff) throws InvalidCommandException {
        System.out.println("Contributiile tale pe care le poti modifica: ");
        staff.viewProductionContributions();
        System.out.println("Ce vrei sa modifici?");
        Scanner scanner = new Scanner(System.in);
        String title = scanner.nextLine();

        if (staff.productionContExists(title)) {
            Production p = findProduction(title);
            staff.updateProduction(p);

        } else {
            throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
        }
    }

    public void updateActorDetails(Staff staff) throws InvalidCommandException {
        System.out.println("Contributiile tale pe care le poti modifica: ");
        staff.viewActorContributions();
        System.out.println("Ce actor vrei sa modifici?");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        if (staff.actorContExists(name)) {
            Actor actor = findActor(name);
            actor.viewActorDetails();
            staff.updateActor(actor);

        } else {
            throw new InvalidCommandException("Comanda invalida!");
        }
    }


    public static void main(String[] args) {
        IMDB imdb = IMDB.getInstance();

        System.out.println("Select the mode:");
        System.out.println("1. Graphic Interface");
        System.out.println("2. Terminal");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            imdb.loadData();
            NavigationManager navigationManager = new NavigationManager();
            navigationManager.showLoginPage();

        } else if (choice == 2) {
            imdb.run();
        } else {
            System.out.println("Invalid choice!");
        }
    }

}


