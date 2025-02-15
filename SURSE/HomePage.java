package org.example;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import javax.swing.JOptionPane;

class ImagePanel extends JPanel {
    private Image backgroundImage;

    public ImagePanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}

public class HomePage <T extends Comparable<T>> extends JFrame {
    private JButton searchButton;
    private JButton menuButton;
    private JButton backButton;
    private JPanel menuPanel;
    private JPanel mainPanel;
    private JButton viewProductionsButton;
    private JButton viewActorsButton;
    private JButton viewNotificationsButton;
    private JButton searchForActorMovieSeries;
    private JButton addDeleteActorMovieSeriesFavorites;
    private JButton addDeleteRequest;
    private JButton addDeleteRating;
    private JButton logout;
    private JButton addDeleteUser;
    private JButton addDeleteActorMovieSeriesSystem;
    private JButton updateProductionDetails;
    private JButton updateActorDetails;
    private JButton solveRequest;
    private CardLayout cardLayout;
    private JButton actorsButton;
    private JButton recommendationsButton;
    List<Actor> actors = IMDB.getInstance().getActors();
    List<Production> productions = IMDB.getInstance().getProductions();
    List<User> users = IMDB.getInstance().getUsers();
    List<Request> requests = IMDB.getInstance().getRequests();
    private User<T> authenticatedUser;
    private NavigationManager navigationManager;


    public HomePage(User<T> authenticatedUser, NavigationManager navigationManager) {
        setTitle("Home Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.authenticatedUser = authenticatedUser;
        this.navigationManager = navigationManager;
        menuButton = new JButton("Meniu");
        backButton = new JButton("Back");
        searchButton = new JButton("Search");
        actorsButton = new JButton("Actors");
        recommendationsButton = new JButton("Recommendations");

        mainPanel = createMainPanel();
        menuPanel = createMenuPanel();
        mainPanel.setVisible(true);
        menuPanel.setVisible(false);

        initComponents();
    }


    private void initComponents() {
        setLayout(new BorderLayout());

        // Configurare CardLayout
        cardLayout = new CardLayout();
        JPanel contentPane = new JPanel(cardLayout);
        contentPane.add(mainPanel, "MAIN");
        contentPane.add(menuPanel, "MENU");

        mainPanel.setLayout(new BorderLayout());

        ImageIcon backgroundImg = new ImageIcon("C:\\Users\\anama\\OneDrive\\Desktop\\imdb-internet-movie-database5351.jpg");
        Image background = backgroundImg.getImage();

        ImagePanel imagePanel = new ImagePanel(background);
        imagePanel.setLayout(new BorderLayout());

        JButton searchButton = new JButton("Search");
        JButton actorsButton = new JButton("Actors");
        JButton recommendationsButton = new JButton("Recommendations");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(searchButton);
        buttonPanel.add(actorsButton);
        buttonPanel.add(recommendationsButton);

        imagePanel.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.add(imagePanel);

        menuButton.addActionListener(e -> {
            cardLayout.show(contentPane, "MENU");
            backButton.setVisible(true);
            menuButton.setVisible(false);
            searchButton.setVisible(false);
        });

        searchButton.addActionListener(e -> {
            showSearchForActorMovieSeries();
        });

        actorsButton.addActionListener(e -> {
            showActors();
        });

        recommendationsButton.addActionListener(e -> {
            showRecommendations();
        });

        backButton.addActionListener(e -> {
            cardLayout.show(contentPane, "MAIN");
            backButton.setVisible(false);
            menuButton.setVisible(true);
            searchButton.setVisible(true);
        });

        viewProductionsButton.addActionListener(e -> {
            showProductionDetails();
        });

        viewActorsButton.addActionListener(e -> {
            showActorsDetails();
        });

        viewNotificationsButton.addActionListener(e -> {
            showNotifications();
        });

        searchForActorMovieSeries.addActionListener(e -> {
            showSearchForActorMovieSeries();
        });

        addDeleteActorMovieSeriesFavorites.addActionListener(e -> {
            showAddDeleteActorMovieSeriesFavorites();
        });

        addDeleteRequest.addActionListener(e -> {
            showAddDeleteRequest();
        });

        logout.addActionListener(e -> {
            showLogout();
        });

        solveRequest.addActionListener(e -> {
            showSolveRequest();
        });

        addDeleteUser.addActionListener(e -> {
            showAddDeleteUser();
        });

        addDeleteActorMovieSeriesSystem.addActionListener(e -> {
            showAddDeleteActorMovieSeriesSystem();
        });

        updateProductionDetails.addActionListener(e -> {
            showUpdateProduction();
        });

        updateActorDetails.addActionListener(e -> {
            showUpdateActor();
        });

        addDeleteRating.addActionListener(e -> {
            showAddDeleteRating();
        });

        add(contentPane, BorderLayout.CENTER);
        add(menuButton, BorderLayout.NORTH);
        add(backButton, BorderLayout.WEST);
        backButton.setVisible(false);
    }

    public void displayHomePage() {
        setVisible(true);
    }

    private JPanel createMainPanel() {
        return new JPanel();
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new GridLayout(0, 1));

        viewProductionsButton = new JButton("View Productions Details");
        viewActorsButton = new JButton("View Actors Details");
        viewNotificationsButton = new JButton("View Notifications");
        searchForActorMovieSeries = new JButton("Search for actor/movie/series");
        addDeleteActorMovieSeriesFavorites = new JButton("Add/Delete actor/movie/series to/from favorites");
        addDeleteRequest = new JButton("Add/Delete a request");
        addDeleteRating = new JButton("Add/Delete rating for a production");
        logout = new JButton("Logout");
        addDeleteUser = new JButton("Add/Delete user");
        addDeleteActorMovieSeriesSystem = new JButton("Add/Delete actor/movie/series from system");
        updateProductionDetails = new JButton("Update Production Details");
        updateActorDetails = new JButton("Update Actor Details");
        solveRequest = new JButton("Solve a request");

        stylizeMenuButton(viewProductionsButton);
        stylizeMenuButton(viewActorsButton);
        stylizeMenuButton(viewNotificationsButton);
        stylizeMenuButton(searchForActorMovieSeries);
        stylizeMenuButton(addDeleteActorMovieSeriesFavorites);
        stylizeMenuButton(addDeleteRequest);
        stylizeMenuButton(addDeleteRating);
        stylizeMenuButton(logout);
        stylizeMenuButton(addDeleteUser);
        stylizeMenuButton(addDeleteActorMovieSeriesSystem);
        stylizeMenuButton(updateProductionDetails);
        stylizeMenuButton(updateActorDetails);
        stylizeMenuButton(solveRequest);

        if (authenticatedUser instanceof Regular) {
            menuPanel.add(viewProductionsButton);
            menuPanel.add(viewActorsButton);
            menuPanel.add(viewNotificationsButton);
            menuPanel.add(searchForActorMovieSeries);
            menuPanel.add(addDeleteActorMovieSeriesFavorites);
            menuPanel.add(addDeleteRequest);
            menuPanel.add(addDeleteRating);
            menuPanel.add(logout);
        }
        if (authenticatedUser instanceof Admin) {
            menuPanel.add(viewProductionsButton);
            menuPanel.add(viewActorsButton);
            menuPanel.add(viewNotificationsButton);
            menuPanel.add(searchForActorMovieSeries);
            menuPanel.add(addDeleteActorMovieSeriesFavorites);
            menuPanel.add(addDeleteUser);
            menuPanel.add(addDeleteActorMovieSeriesSystem);
            menuPanel.add(updateProductionDetails);
            menuPanel.add(updateActorDetails);
            menuPanel.add(solveRequest);
            menuPanel.add(logout);
        }
        if (authenticatedUser instanceof Contributor) {
            menuPanel.add(viewProductionsButton);
            menuPanel.add(viewActorsButton);
            menuPanel.add(viewNotificationsButton);
            menuPanel.add(searchForActorMovieSeries);
            menuPanel.add(addDeleteActorMovieSeriesFavorites);
            menuPanel.add(addDeleteRequest);
            menuPanel.add(addDeleteActorMovieSeriesSystem);
            menuPanel.add(updateProductionDetails);
            menuPanel.add(updateActorDetails);
            menuPanel.add(solveRequest);
            menuPanel.add(logout);
        }
        return menuPanel;
    }

    private void stylizeMenuButton(JButton button) {
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(button.getFont().deriveFont(Font.BOLD));
    }

    private void showRecommendations() {
        List<Actor> randomActors = getRandomItems(actors, 5);
        List<Production> randomProductions = getRandomItems(productions, 5);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> recommendationList = new JList<>(listModel);

        for (Actor actor : randomActors) {
            listModel.addElement(actor.getName());
        }
        for (Production production : randomProductions) {
            listModel.addElement(production.getTitle());
        }

        JScrollPane scrollPane = new JScrollPane(recommendationList);
        scrollPane.setPreferredSize(new Dimension(300, 400));

        JFrame frame = new JFrame("Random Recommendations");
        frame.add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }



    private <T> List<T> getRandomItems(List<T> list, int count) {
        List<T> randomItems = new ArrayList<>();
        Random random = new Random();
        while (randomItems.size() < count && !list.isEmpty()) {
            int index = random.nextInt(list.size());
            randomItems.add(list.remove(index));
        }
        return randomItems;
    }

    private void showActors() {
        Collections.sort(actors);

        JFrame frame = new JFrame("List of Actors");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultListModel<String> actorListModel = new DefaultListModel<>();

        for (Actor actor : actors) {
            actorListModel.addElement(actor.getName());
        }

        JList<String> actorList = new JList<>(actorListModel);
        actorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(actorList);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setSize(300, 400);
        frame.setVisible(true);
    }

    private void showProductionDetails() {
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setEditable(false);

        StringBuilder productionDetails = new StringBuilder();
        for (Production production : productions) {
            productionDetails.append("Title: ").append(production.getTitle()).append("\n");
            productionDetails.append("Type: ").append(production.getType()).append("\n");
            productionDetails.append("Directors: ").append(production.getDirectors()).append("\n");
            productionDetails.append("Actors: ").append(production.getActors()).append("\n");
            productionDetails.append("Genres: ").append(production.getGenres()).append("\n");
            productionDetails.append("Ratings: ").append("\n");
            for (Rating rating : production.getRatings()) {
                productionDetails.append(rating.toString()).append("\n");
            }
            productionDetails.append("Average Rating: ").append(production.getAverageRating()).append("\n");
            productionDetails.append("Plot: ").append(production.getPlot()).append("\n");
            if (production instanceof Movie) {
                productionDetails.append("Duration: ").append(((Movie) production).getDuration()).append("\n");
                if (((Movie) production).getReleaseYear() != 0) {
                    productionDetails.append("Release Year: ").append(((Movie) production).getReleaseYear()).append("\n");
                }
            } else if (production instanceof Series) {
                productionDetails.append("Number seasons: ").append(((Series) production).getNumSeasons()).append("\n");
                if (((Series) production).getReleaseYear() != 0) {
                    productionDetails.append("Release Year: ").append(((Series) production).getReleaseYear()).append("\n");
                }
                if (((Series) production).getSeasons() != null) {
                    productionDetails.append("Seasons:\n");
                    for (Map.Entry<String, List<Episode>> entry : ((Series) production).getSeasons().entrySet()) {
                        String seasonName = entry.getKey();
                        List<Episode> episodes = entry.getValue();

                        productionDetails.append("Season: ").append(seasonName).append("\n");
                        for (Episode episode : episodes) {
                            productionDetails.append("Episode: ").append(episode.getEpisodeName())
                                    .append(", Duration: ").append(episode.getDuration()).append("\n");
                        }
                    }
                }
            }
            productionDetails.append("\n");
        }

        textArea.setText(productionDetails.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(
                this, scrollPane, "Production Details", JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showActorsDetails() {
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setEditable(false);

        StringBuilder actorDetails = new StringBuilder();
        for (Actor actor : actors) {
            if (actor.getName() != null) {
                actorDetails.append("Name: ").append(actor.getName()).append("\n");
            }
            if (actor.getBiography() != null) {
                actorDetails.append("Biography: ").append(actor.getBiography()).append("\n");
            }
            if (actor.getPerformances() != null) {
                actorDetails.append("Performances: ").append("\n");
                for (Map<String, String> performance : actor.getPerformances()) {
                    for (Map.Entry<String, String> entry : performance.entrySet()) {
                        actorDetails.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                    }
                    actorDetails.append("\n");
                }
            }
            actorDetails.append("\n");
        }

        textArea.setText(actorDetails.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(
                this, scrollPane, "Actor Details", JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showNotifications() {
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setEditable(false);

        List<String> notifications = authenticatedUser.getNotifications();

        StringBuilder notificationsDetails = new StringBuilder();
        if (notifications != null) {
            for (String notification : notifications) {
                notificationsDetails.append(notification).append("\n");
            }
        }

        textArea.setText(notificationsDetails.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(
                this, scrollPane, "Notifications", JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showSearchForActorMovieSeries() {
        String searchTerm = JOptionPane.showInputDialog(this, "Introduceti numele actorului sau al productiei:");

        boolean found = false;

        for (Actor actor : actors) {
            if (actor.getName().equalsIgnoreCase(searchTerm)) {
                found = true;
                displayActorDetails(actor);
                break;
            }
        }

        for (Production production : productions) {
            if (production.getTitle().equalsIgnoreCase(searchTerm)) {
                found = true;
                displayProductionDetails(production);
                break;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "Nu exista rezultate pentru: " + searchTerm);
        }
    }

    private void displayActorDetails(Actor actor) {
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setEditable(false);

        StringBuilder actorDetails = new StringBuilder();

        if (actor.getName() != null) {
            actorDetails.append("Name: ").append(actor.getName()).append("\n");
        }
        if (actor.getBiography() != null) {
            actorDetails.append("Biography: ").append(actor.getBiography()).append("\n");
        }
        if (actor.getPerformances() != null) {
            actorDetails.append("Performances: ").append("\n");
            for (Map<String, String> performance : actor.getPerformances()) {
                for (Map.Entry<String, String> entry : performance.entrySet()) {
                    actorDetails.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
                actorDetails.append("\n");
            }
        }
        actorDetails.append("\n");

        textArea.setText(actorDetails.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        int option = JOptionPane.showOptionDialog(
                this, scrollPane, "Actor Details", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new String[]{"Add to Favorites", "No"}, "Add to Favorites"
        );

        if (option == JOptionPane.YES_OPTION) {
            authenticatedUser.addFavorites(actor.getName());
        } else if (option == JOptionPane.NO_OPTION) {

        }
    }


    private void displayProductionDetails(Production production) {

        JTextArea textArea = new JTextArea(20, 40);
        textArea.setEditable(false);

        StringBuilder productionDetails = new StringBuilder();

        productionDetails.append("Title: ").append(production.getTitle()).append("\n");
        productionDetails.append("Type: ").append(production.getType()).append("\n");
        productionDetails.append("Directors: ").append(production.getDirectors()).append("\n");
        productionDetails.append("Actors: ").append(production.getActors()).append("\n");
        productionDetails.append("Genres: ").append(production.getGenres()).append("\n");
        productionDetails.append("Ratings: ").append("\n");
        for (Rating rating : production.getRatings()) {
            productionDetails.append(rating.toString()).append("\n");
        }
        productionDetails.append("Average Rating: ").append(production.getAverageRating()).append("\n");
        productionDetails.append("Plot: ").append(production.getPlot()).append("\n");
        if (production instanceof Movie) {
            productionDetails.append("Duration: ").append(((Movie) production).getDuration()).append("\n");
            if (((Movie) production).getReleaseYear() != 0) {
                productionDetails.append("Release Year: ").append(((Movie) production).getReleaseYear()).append("\n");
            }
        } else if (production instanceof Series) {
            productionDetails.append("Number seasons: ").append(((Series) production).getNumSeasons()).append("\n");
            if (((Series) production).getReleaseYear() != 0) {
                productionDetails.append("Release Year: ").append(((Series) production).getReleaseYear()).append("\n");
            }
            if (((Series) production).getSeasons() != null) {
                productionDetails.append("Seasons:\n");
                for (Map.Entry<String, List<Episode>> entry : ((Series) production).getSeasons().entrySet()) {
                    String seasonName = entry.getKey();
                    List<Episode> episodes = entry.getValue();

                    productionDetails.append("Season: ").append(seasonName).append("\n");
                    for (Episode episode : episodes) {
                        productionDetails.append("Episode: ").append(episode.getEpisodeName())
                                .append(", Duration: ").append(episode.getDuration()).append("\n");
                    }
                }
            }
        }

        productionDetails.append("\n");

        textArea.setText(productionDetails.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        int option = JOptionPane.CANCEL_OPTION;

        if (authenticatedUser instanceof Regular) {
            option = JOptionPane.showOptionDialog(
                    this, scrollPane, "Production Details", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    new String[]{"Add to Favorites", "Add Rating", "Cancel"}, "Add to Favorites"
            );
        } else {
            option = JOptionPane.showOptionDialog(
                    this, scrollPane, "Production Details", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    new String[]{"Add to Favorites", "Cancel"}, "Add to Favorites"
            );
        }

        if (option == JOptionPane.YES_OPTION) {
            authenticatedUser.addFavorites(production.getTitle());
        } else if (option == JOptionPane.NO_OPTION && authenticatedUser instanceof Regular) {
            String ratingInput = JOptionPane.showInputDialog(this, "Introduceti nota (de la 1 la 10):");
            if (ratingInput != null && !ratingInput.isEmpty()) {
                try {
                    int ratingValue = Integer.parseInt(ratingInput);
                    if (ratingValue >= 1 && ratingValue <= 10) {
                        String comment = JOptionPane.showInputDialog(this, "Adaugati un comentariu:");

                        Rating rating = new Rating(authenticatedUser.getUsername(), ratingValue, comment);
                        ((Regular) authenticatedUser).addRatingRegular(production.getTitle(), rating);

                    } else {
                        JOptionPane.showMessageDialog(this, "Introduceti o nota valida intre 1 si 10!", "Nota invalida", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Introduceti o nota valida intre 1 si 10!", "Nota invalida", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showAddDeleteActorMovieSeriesFavorites() {
        JTextArea favoritesTextArea = new JTextArea(20, 40);
        favoritesTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(favoritesTextArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        StringBuilder favoritesText = new StringBuilder();
        SortedSet<T> list = authenticatedUser.getFavorites();
        for (T favorite : list) {
            if (favorite instanceof Actor actor) {
                favoritesText.append(actor.getDetails()).append("\n");
            } else if (favorite instanceof Movie movie) {
                favoritesText.append(movie.getDetails()).append("\n");
            } else if (favorite instanceof Series series) {
                favoritesText.append(series.getDetails()).append("\n");
            } else {
                favoritesText.append("Invalid entry: ").append(favorite.toString()).append("\n");
            }
        }

        favoritesTextArea.setText(favoritesText.toString());

        String[] options = {"Add", "Delete"};
        int choice = JOptionPane.showOptionDialog(this, scrollPane, "Add/Delete Favorites",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            showAddFavorites(favoritesTextArea);
        } else if (choice == 1) {
            showDeleteFavorites(favoritesTextArea);
        }
    }

    private void showAddFavorites(JTextArea favoritesTextArea) {
        JTextField inputField = new JTextField();
        Object[] message = {"Enter actor name/title movie/title series:", inputField};
        int option = JOptionPane.showConfirmDialog(this, message, "Add to Favorites", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String userInput = inputField.getText();

            if (IMDB.getInstance().productionExists(userInput) || IMDB.getInstance().actorExists(userInput)) {
                authenticatedUser.addFavorites(userInput);
                favoritesTextArea.setText(authenticatedUser.toString());
                JOptionPane.showMessageDialog(this, "Added to favorites: " + userInput, "Added", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid entry: " + userInput, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showDeleteFavorites(JTextArea favoritesTextArea) {
        JTextField inputField = new JTextField();
        Object[] message = {"Enter actor name/title movie/title series to delete:", inputField};
        int option = JOptionPane.showConfirmDialog(this, message, "Delete from Favorites", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String userInput = inputField.getText();

            if (authenticatedUser.searchInFavorites(userInput)) {
                authenticatedUser.removeFavorite(userInput);
                favoritesTextArea.setText(authenticatedUser.toString());
                JOptionPane.showMessageDialog(this, "Deleted from favorites: " + userInput, "Deleted", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Not found in favorites: " + userInput, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAddDeleteRequest() {
        Object[] options = {"Add", "Delete"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose Add or Delete:",
                "Add/Delete Request",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            showAddRequestDialog();
        } else if (choice == 1) {
            showDeleteRequestDialog();
        }
    }

    private void showAddRequestDialog() {
        LocalDateTime createdDate = LocalDateTime.now();
        String username = authenticatedUser.getUsername();

        Object[] types = {"OTHERS", "DELETE_ACCOUNT", "MOVIE_ISSUE", "ACTOR_ISSUE"};
        String selectedType = (String) JOptionPane.showInputDialog(
                this,
                "Select Request Type:",
                "Add Request",
                JOptionPane.PLAIN_MESSAGE,
                null,
                types,
                types[0]
        );

        RequestTypes type = RequestTypes.valueOf(selectedType);

        if (type == RequestTypes.MOVIE_ISSUE) {
            String movieTitle = showProductionSelectionDialog();
            String to = JOptionPane.showInputDialog(
                    this,
                    "Enter To:",
                    "Add Request",
                    JOptionPane.PLAIN_MESSAGE
            );
            String description = JOptionPane.showInputDialog(
                    this,
                    "Enter Description:",
                    "Add Request",
                    JOptionPane.PLAIN_MESSAGE
            );

            String actorName = null;
            Request r = new Request(type, username, createdDate, to, description, actorName, movieTitle);
            if (authenticatedUser instanceof Regular<T>) {
                ((Regular<T>) authenticatedUser).createRequest(r);
            }

        } else if (type == RequestTypes.ACTOR_ISSUE) {
            String actorName = showActorSelectionDialog();
            String to = JOptionPane.showInputDialog(
                    this,
                    "Enter To:",
                    "Add Request",
                    JOptionPane.PLAIN_MESSAGE
            );
            // dialog pentru introducerea descrierii cererii
            String description = JOptionPane.showInputDialog(
                    this,
                    "Enter Description:",
                    "Add Request",
                    JOptionPane.PLAIN_MESSAGE
            );
            String movieTitle = null;
            Request r = new Request(type, username, createdDate, to, description, actorName, movieTitle);
            if (authenticatedUser instanceof Regular<T>) {
                ((Regular<T>) authenticatedUser).createRequest(r);
            }

        } else if (type == RequestTypes.OTHERS || type == RequestTypes.DELETE_ACCOUNT) {
            String to = "ADMIN";
            // dialog pentru introducerea descrierii cererii
            String description = JOptionPane.showInputDialog(
                    this,
                    "Enter Description:",
                    "Add Request",
                    JOptionPane.PLAIN_MESSAGE
            );

            String actorName = null;
            String movieTitle = null;
            Request r = new Request(type, username, createdDate, to, description, actorName, movieTitle);

            if (authenticatedUser instanceof Regular<T>) {
                ((Regular<T>) authenticatedUser).createRequest(r);
            }
        }
    }

    private List<String> getUserRequests() {
        List<String> reqDescription = new ArrayList<>();
        for (Request request : requests) {
            if (request.getUsername().equals(authenticatedUser.getUsername())) {
                reqDescription.add(request.getDescription());
            }
        }
        return reqDescription;
    }

    private void showDeleteRequestDialog() {
        List<String> userRequests = getUserRequests();
        if (!userRequests.isEmpty()) {
            String[] requestDescriptions = userRequests.toArray(new String[0]);

            String selectedRequest = (String) JOptionPane.showInputDialog(
                    null,
                    "Alege cererea pe care doresti sa o stergi:",
                    "Stergere cerere",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    requestDescriptions,
                    requestDescriptions[0]
            );

            boolean deleted = false;

            if (selectedRequest != null) {
                for (Request request : requests) {
                    if (request.getDescription().equals(selectedRequest)) {
                        if (authenticatedUser instanceof Regular) {
                            ((Regular) authenticatedUser).removeRequest(request);
                            deleted = true;
                        } else {
                            ((Contributor) authenticatedUser).removeRequest(request);
                            deleted = true;
                        }
                        break;
                    }
                }
                if (deleted) {
                    JOptionPane.showMessageDialog(null, "Cererea a fost atearsa cu succes.", "Stergere cerere", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Eroare la stergerea cererii.", "Eroare", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nu exista cereri de sters.", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String showProductionSelectionDialog() {
        List<String> productionTitles = new ArrayList<>();

        for (Production production : productions) {
            productionTitles.add(production.getTitle());
        }

        String[] productionArray = productionTitles.toArray(new String[0]);

        String selectedProductionTitle = (String) JOptionPane.showInputDialog(
                this,
                "Select Production:",
                "Select Production",
                JOptionPane.QUESTION_MESSAGE,
                null,
                productionArray,
                productionArray[0]
        );
        return selectedProductionTitle;
    }


    private String showActorSelectionDialog() {
        List<String> actorNames = new ArrayList<>();

        for (Actor actor : actors) {
            actorNames.add(actor.getName());
        }

        String[] actorArray = actorNames.toArray(new String[0]);

        String selectedActor = (String) JOptionPane.showInputDialog(
                this,
                "Select Actor:",
                "Select Actor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                actorArray,
                actorArray[0]
        );

        return selectedActor;
    }

    private void showLogout() {
        Object[] options = {"ENTER CREDENTIALS AGAIN", "EXIT"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Select an option:",
                "Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.YES_OPTION) {
            navigationManager.logout();

        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    private void showSolveRequest() {
        List<Request> allRequests = Admin.RequestsHolder.getAllRequests();
        List<Request> userRequests = ((Staff) authenticatedUser).getRequestList();

        JPanel panel = new JPanel(new BorderLayout());
        JTextArea requestDetails = new JTextArea(20, 40);
        requestDetails.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(requestDetails);

        if (userRequests != null) {
            requestDetails.append("Cererile tale:\n");
            for (Request request : userRequests) {
                requestDetails.append(request.getType() + "\n");
                requestDetails.append(request.getCreatedDate() + "\n");
                requestDetails.append(request.getUsername() + "\n");
                if (request.getActorName() != null) {
                    requestDetails.append(request.getActorName() + "\n");
                }
                if (request.getMovieTitle() != null) {
                    requestDetails.append(request.getMovieTitle() + "\n");
                }
                requestDetails.append(request.getDescription() + "\n");
            }
            requestDetails.append("\n");
        }

        if (allRequests != null && authenticatedUser instanceof Admin) {
            requestDetails.append("\nCererile pentru toti adminii:\n");
            for (Request request : allRequests) {
                requestDetails.append(request.getType() + "\n");
                requestDetails.append(request.getCreatedDate() + "\n");
                requestDetails.append(request.getUsername() + "\n");
                if (request.getActorName() != null) {
                    requestDetails.append(request.getActorName() + "\n");
                }
                if (request.getMovieTitle() != null) {
                    requestDetails.append(request.getMovieTitle() + "\n");
                }
                requestDetails.append(request.getDescription() + "\n");
            }
            requestDetails.append("\n");
        }

        panel.add(scrollPane, BorderLayout.CENTER);

        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null,
                new Object[]{"Rezolva", "Respinge", "Marcheaza ca rezolvata"}, "Rezolva");

        JDialog dialog = optionPane.createDialog("Gestioneaza cererile");
        dialog.setVisible(true);

        Object selectedValue = optionPane.getValue();

        if (selectedValue != null) {
            if (selectedValue.equals("Rezolva")) {
            } else if (selectedValue.equals("Respinge")) {
                showSolve("Respinsa");
            } else if (selectedValue.equals("Marcheaza ca rezolvata")) {
                showSolve("Rezolvata");
            }
        } else {

        }
    }


    private void showSolve(String req) {
        List<Request> userRequests = ((Staff) authenticatedUser).getRequestList();
        List<Request> allRequests = Admin.RequestsHolder.getAllRequests();

        JComboBox<String> comboBox = new JComboBox<>();

        if (userRequests != null) {
            for (Request request : userRequests) {
                comboBox.addItem(request.getDescription());
            }
        }

        if (allRequests != null && authenticatedUser instanceof Admin) {
            for (Request request : allRequests) {
                comboBox.addItem(request.getDescription());
            }
        }

        if (comboBox.getItemCount() > 0) {
            int result = JOptionPane.showConfirmDialog(null, comboBox, "Selecteaza cererea",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String selectedDescription = (String) comboBox.getSelectedItem();

                if (userRequests != null) {
                    for (Request request : userRequests) {
                        if (request.getDescription().equals(selectedDescription)) {
                            if (req.equals("Rezolvata")) {
                                markRequestAsResolved(request, "Rezolvata");
                            } else if (req.equals("Respinsa")) {
                                markRequestAsResolved(request, "Respinsa");
                            }
                            return;
                        }
                    }
                }

                if (allRequests != null && authenticatedUser instanceof Admin) {
                    for (Request request : allRequests) {
                        if (request.getDescription().equals(selectedDescription)) {
                            if (req.equals("Rezolvata")) {
                                markRequestAsResolvedForAdmin(request, "Rezolvata");
                            } else if (req.equals("Respinsa")) {
                                markRequestAsResolvedForAdmin(request, "Respinsa");
                            }
                            return;
                        }
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nu exista cereri de rezolvat.");
        }
    }

    private void markRequestAsResolved(Request request, String op) {
        User user1 = IMDB.getInstance().findUser(request.getUsername());
        request.removeObserver(authenticatedUser);
        request.addObserver(user1);
        if (op.equals("Rezolvata")) {
            request.setChange(true);
        } else if (op.equals("Respinsa")) {
            request.setChange(false);
        }
        request.notifyObservers(authenticatedUser, request.getDescription());
        // notificare pentru userul care a dat cererea
        ((Staff) authenticatedUser).getRequestList().remove(request);
        requests.remove(request);

        ResolveRequestExperienceStrategy resolveStrategy = new ResolveRequestExperienceStrategy();
        int experienceGained = resolveStrategy.calculateExperience();

        // Acordarea experientei pentru utilizator
        user1.updateExperience(experienceGained);
        IMDB.getInstance().sortRatingsByUserExperience();
    }

    private void markRequestAsResolvedForAdmin(Request request, String op) {
        User user1 = IMDB.getInstance().findUser(request.getUsername());
        for (User adminUser : users) {
            if (authenticatedUser instanceof Admin) {
                request.removeObserver(adminUser);
            }
        }
        request.addObserver(user1);
        if (op.equals("Rezolvata")) {
            request.setChange(true);
        } else if (op.equals("Respinsa")) {
            request.setChange(false);
        }
        User userNull = null;
        request.notifyObservers(userNull, request.getDescription());
        // notificare pentru userul care a dat cererea
        Admin.RequestsHolder.removeRequest(request);
        requests.remove(request);

        ResolveRequestExperienceStrategy resolveStrategy = new ResolveRequestExperienceStrategy();
        int experienceGained = resolveStrategy.calculateExperience();

        user1.updateExperience(experienceGained);
        IMDB.getInstance().sortRatingsByUserExperience();
    }

    private void showAddDeleteUser() {
        String[] options = {"Adauga utilizator", "Sterge utilizator"};

        int choice = JOptionPane.showOptionDialog(
                null,
                "Ce doresti sa faci?",
                "Adauga/sterge utilizator",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            String name = JOptionPane.showInputDialog(null, "Numele complet:");
            String email = JOptionPane.showInputDialog(null, "Email:");
            String country = JOptionPane.showInputDialog(null, "Tara:");
            int age = Integer.parseInt(JOptionPane.showInputDialog(null, "Varsta:"));
            String gender = JOptionPane.showInputDialog(null, "Gen:");
            String birthDate = JOptionPane.showInputDialog(null, "Data nasterii (yyyy-MM-dd):");
            String type = JOptionPane.showInputDialog(null, "Tip utilizator (Regular/Admin/Contributor):");

            try {
                ((Admin) authenticatedUser).addUser(name, email, country, age, gender, birthDate, type);
            } catch (InformationIncompleteException e) {
                System.out.println("Informatii incomplete!");
            }
            for (User newUser : users) {
                if (newUser.getInformation().getName().equals(name)) {
                    String generatedUsername = newUser.getUsername();
                    String generatedPassword = newUser.getInformation().getCredentials().getPassword();

                    JOptionPane.showMessageDialog(null,
                            "Username: " + generatedUsername + "\n" +
                                    "Password: " + generatedPassword,
                            "Detalii utilizator",
                            JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }

        } else if (choice == 1) {
            List<User> users = IMDB.getInstance().getUsers();

            String[] userNames = new String[users.size()];
            for (int i = 0; i < users.size(); i++) {
                userNames[i] = users.get(i).getUsername();
            }

            String selectedUserName = (String) JOptionPane.showInputDialog(
                    null,
                    "Alege utilizatorul pe care vrei sa il stergi:",
                    "Sterge utilizator",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    userNames,
                    userNames[0]);

            ((Admin) authenticatedUser).removeUser(selectedUserName);
        }
    }

    private void showAddDeleteActorMovieSeriesSystem() {
        String[] options = {"Adauga", "Sterge"};

        int choice = JOptionPane.showOptionDialog(
                null,
                "Ce doresti sa faci?",
                "Adauga/Sterge",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            // Daca se alege "Adauga"
            String[] addOptions = {"Adauga Actor", "Adauga Film/Serie"};

            int addChoice = JOptionPane.showOptionDialog(
                    null,
                    "Ce doresti sa adaugi?",
                    "Adauga",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    addOptions,
                    addOptions[0]
            );

            if (addChoice == 0) {
                addActor();
            } else if (addChoice == 1) {
                addMovieOrSeries();
            }
        } else if (choice == 1) {
            showDeleteContribution();
        }
    }

    private List<String> getProductionInfo() {
        List<String> productionInfo = new ArrayList<>();

        for (Production production : productions) {
            productionInfo.add(production.getTitle());
        }

        return productionInfo;
    }

    private void addActor() {
        String actorName = JOptionPane.showInputDialog("Introdu numele actorului:");
        String biography = JOptionPane.showInputDialog("Introdu biografia actorului:");

        List<String> productionInfo = getProductionInfo();
        Object[] performanceOptions = productionInfo.toArray();

        List<Map<String, String>> performances = new ArrayList<>();

        while (true) {
            String selectedTitle = (String) JOptionPane.showInputDialog(
                    null,
                    "Selecteaza un titlu de productie si apasa Cancel cand ai terminat:",
                    "Adauga performanta",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    performanceOptions,
                    null);

            if (selectedTitle == null) {
                break;
            } else {
                Map<String, String> selectedPerformances = new HashMap<>();
                selectedPerformances.put("title", selectedTitle);
                Production p = IMDB.getInstance().findProduction(selectedTitle);
                selectedPerformances.put("type", p.getType());
                performances.add(selectedPerformances);
            }
        }

        Actor actor = new Actor(actorName, performances, biography);
        ((Staff) authenticatedUser).addActorSystem(actor);


    }

    private void addMovieOrSeries() {
        String[] types = {"Movie", "Series"};
        String selectedType = (String) JOptionPane.showInputDialog(
                null,
                "Selecteaza tipul:",
                "Adauga Movie/Series",
                JOptionPane.PLAIN_MESSAGE,
                null,
                types,
                types[0]);

        if (selectedType != null) {
            String title = JOptionPane.showInputDialog(
                    null,
                    "Introdu titlul:",
                    "Adauga Titlu",
                    JOptionPane.PLAIN_MESSAGE
            );

            String plot = JOptionPane.showInputDialog("Introdu plot-ul:");

            List<String> actorNames = new ArrayList<>();
            while (true) {
                String actorName = JOptionPane.showInputDialog("Introdu numele actorului (Cancel pentru a incheia):");
                if (actorName == null || actorName.isEmpty()) {
                    break;
                }
                actorNames.add(actorName);
                IMDB.getInstance().addActorFromProduction((Staff) authenticatedUser, actorName, title, selectedType);
            }

            List<String> directorNames = new ArrayList<>();
            while (true) {
                String directorName = JOptionPane.showInputDialog("Introdu numele regizorului (Cancel pentru a incheia):");
                if (directorName == null || directorName.isEmpty()) {
                    break;
                }
                directorNames.add(directorName);
            }

            Genre[] genres = Genre.values();
            List<String> genreNames = new ArrayList<>();

            for (Genre genre : genres) {
                genreNames.add(genre.toString());
            }

            List<Genre> newGenres = new ArrayList<>();
            while (true) {
                String selectedGenre = (String) JOptionPane.showInputDialog(
                        null,
                        "Selecteaza genul (Cancel pentru a termina):",
                        "Adauga Gen",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        genreNames.toArray(new String[0]),
                        genreNames.get(0)
                );

                if (selectedGenre == null) {
                    break;
                }
                Genre genre = Genre.valueOf(selectedGenre);
                newGenres.add(genre);
            }

            int releaseYear = Integer.parseInt(JOptionPane.showInputDialog("Introdu anul de lansare:"));
            List<Rating> ratings = new ArrayList<>();

            if (selectedType.equals("Movie")) {
                int durationInt = Integer.parseInt(JOptionPane.showInputDialog("Introdu durata in minute:"));
                String duration = durationInt + " minutes";
                Movie movie = new Movie(title, directorNames, actorNames, newGenres, ratings, plot, 0, selectedType, duration, releaseYear);
                ((Staff) authenticatedUser).addProductionSystem(movie);

            } else if (selectedType.equals("Series")) {
                int nrSeasons = Integer.parseInt(JOptionPane.showInputDialog("Introdu numarul de sezoane:"));

                Map<String, List<Episode>> seasons = new HashMap<>();
                for (int i = 1; i <= nrSeasons; i++) {
                    int episodes = Integer.parseInt(JOptionPane.showInputDialog("Introdu numarul de episoade pentru sezonul " + i + ":"));

                    String nrSeason = "Season " + i;
                    List<Episode> episode = new ArrayList<>();

                    for (int j = 1; j <= episodes; j++) {
                        String episodeName = JOptionPane.showInputDialog("Introdu numele episodului " + j + " din sezonul " + i + ":");
                        int episodeDurationInt = Integer.parseInt(JOptionPane.showInputDialog("Introdu durata episodului " + j + " din sezonul " + i + " in minute:"));
                        String duration = episodeDurationInt + " minutes";

                        Episode episode1 = new Episode(episodeName, duration);
                        episode.add(episode1);
                    }
                    seasons.put(nrSeason, episode);
                }

                Series series = new Series(title, directorNames, actorNames, newGenres, ratings, plot, 0, releaseYear, nrSeasons, seasons, selectedType);
                ((Staff) authenticatedUser).addProductionSystem(series);
            }
        }
    }

    private void showDeleteContribution() {

        List<String> contributionsString = ((Staff) authenticatedUser).getContributionsAsArray();

        String[] options = contributionsString.toArray(new String[0]);

        String option = (String) JOptionPane.showInputDialog(
                null,
                "Selecteaza contributia de sters:",
                "Sterge contributie",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (option != null) {
            if (((Staff) authenticatedUser).actorContExists(option)) {
                ((Staff) authenticatedUser).removeActorSystem(option);

                for (User user1 : users) {
                    if(user1.searchInFavorites(option)) {
                        user1.removeFavorite(option);
                    }
                }
                ((Staff) authenticatedUser).removeContribution(option);

            } else if (((Staff) authenticatedUser).productionContExists(option)) {
                ((Staff) authenticatedUser).removeProductionSystem(option);

                for (User user1 : users) {
                    if(user1.searchInFavorites(option)) {
                        user1.removeFavorite(option);
                    }
                }

                ((Staff) authenticatedUser).removeContribution(option);

            } else if ((authenticatedUser instanceof Admin) && ((Admin) authenticatedUser).getContributionsForAdmins().contains(option)) {
                for (User user : users) {
                    if (user instanceof Admin) {
                        ((Admin) user).removeContributionForAdmin(option);
                    }
                }

            }
        }
    }

    private void showUpdateProduction() {

            List<String> contributionsString = ((Staff) authenticatedUser).getProductionContributionsAsArray();
            String[] options = contributionsString.toArray(new String[0]);

            String title = (String) JOptionPane.showInputDialog(
                    null,
                    "Selecteaza productia pe care vrei sa o modifici:",
                    "Modifica productie",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            // detaliile productiei
            Production production = IMDB.getInstance().findProduction(title);

            if (production != null) {
                if (production instanceof Movie) {
                    String[] option = {"Title", "Directors", "Actors", "Genres", "Plot", "Duration", "Release Year"};
                    String selectedOption = (String) JOptionPane.showInputDialog(
                            null,
                            "Selecteaza ce vrei sa actualizezi:",
                            "Actualizeaza productie",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            option,
                            option[0]);

                    if (selectedOption != null) {
                        switch (selectedOption) {
                            case "Title":
                                String newTitle = JOptionPane.showInputDialog(
                                        null,
                                        "Introdu noul titlu:",
                                        "Actualizeaza Titlu",
                                        JOptionPane.PLAIN_MESSAGE
                                );
                                production.setTitle(newTitle);
                                break;
                            case "Directors":
                                manageDirectors(production);
                                break;
                            case "Actors":
                                manageActors(production);
                                break;
                            case "Genres":
                                manageGenres(production);
                                break;
                            case "Plot":
                                String newPlot = JOptionPane.showInputDialog(
                                        null,
                                        "Introdu noul plot:",
                                        "Actualizeaza plot",
                                        JOptionPane.PLAIN_MESSAGE
                                );
                                production.setPlot(newPlot);
                                break;
                            case "Duration":
                                String durationInput = JOptionPane.showInputDialog(
                                        null,
                                        "Introdu noua durata (in minute):",
                                        "Actualizeaza Durata",
                                        JOptionPane.PLAIN_MESSAGE
                                );

                                if (durationInput != null && !durationInput.isEmpty()) {
                                    try {
                                        int newDuration = Integer.parseInt(durationInput);
                                        String duration = newDuration + " minutes";
                                        ((Movie) production).setDuration(duration);
                                    } catch (NumberFormatException e) {
                                        JOptionPane.showMessageDialog(null, "Introdu o valoare numerica pentru durata!");
                                    }
                                }

                                break;
                            case "Release Year":
                                String releaseYearInput = JOptionPane.showInputDialog(
                                        null,
                                        "Introdu noul an de lansare:",
                                        "Actualizeaza Anul de Lansare",
                                        JOptionPane.PLAIN_MESSAGE
                                );

                                if (releaseYearInput != null && !releaseYearInput.isEmpty()) {
                                    try {
                                        int newReleaseYear = Integer.parseInt(releaseYearInput);
                                        ((Movie) production).setReleaseYear(newReleaseYear);
                                    } catch (NumberFormatException e) {
                                        JOptionPane.showMessageDialog(null, "Introdu un an valid!");
                                    }
                                }

                                break;
                            default:
                                break;
                        }
                    } else {

                    }
                } else if (production instanceof Series) {
                    String[] option = {"Title", "Directors", "Actors", "Genres", "Plot", "Seasons", "Release Year"};
                    String selectedOption = (String) JOptionPane.showInputDialog(
                            null,
                            "Selecteaza ce vrei sa actualizezi:",
                            "Actualizeaza productie",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            option,
                            option[0]);

                    switch (selectedOption) {
                        case "Title":
                            String newTitle = JOptionPane.showInputDialog(
                                    null,
                                    "Introdu noul titlu:",
                                    "Actualizeaza Titlu",
                                    JOptionPane.PLAIN_MESSAGE
                            );
                            production.setTitle(newTitle);
                            break;
                        case "Directors":
                            manageDirectors(production);
                            break;
                        case "Actors":
                            manageActors(production);
                            break;
                        case "Genres":
                            manageGenres(production);
                            break;
                        case "Plot":
                            String newPlot = JOptionPane.showInputDialog(
                                    null,
                                    "Introdu noul plot:",
                                    "Actualizeaza plot",
                                    JOptionPane.PLAIN_MESSAGE
                            );
                            production.setPlot(newPlot);
                            break;
                        case "Seasons":
                            String[] optionsSeasons = {"Adaugare sezoane", "Adaugare episoade", "Stergere episoade", "Modificare nume sau durata episod"};
                            String selectedSeasonOption = (String) JOptionPane.showInputDialog(
                                    null,
                                    "Alege optiunea:",
                                    "Gestioneaza Sezoane si Episoade",
                                    JOptionPane.PLAIN_MESSAGE,
                                    null,
                                    optionsSeasons,
                                    optionsSeasons[0]
                            );

                            if (selectedSeasonOption != null) {
                                Map<String, List<Episode>> seasons = ((Series) production).getSeasons();
                                switch (selectedSeasonOption) {
                                    case "Adaugare sezoane":
                                        addSeasonsAndEpisodes(production);
                                        break;
                                    case "Adaugare episoade":
                                        addEpisodesToSeason(seasons, production);
                                        break;
                                    case "Stergere episoade":
                                        deleteEpisodeFromSeason(seasons, production);
                                        break;
                                    case "Modificare nume sau durata episod":
                                        editEpisodeInSeason(seasons, production);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            break;
                        case "Release Year":
                            String releaseYearInput = JOptionPane.showInputDialog(
                                    null,
                                    "Introdu noul an de lansare:",
                                    "Actualizeaza Anul de Lansare",
                                    JOptionPane.PLAIN_MESSAGE
                            );

                            if (releaseYearInput != null && !releaseYearInput.isEmpty()) {
                                try {
                                    int newReleaseYear = Integer.parseInt(releaseYearInput);
                                    ((Movie) production).setReleaseYear(newReleaseYear);
                                } catch (NumberFormatException e) {
                                    JOptionPane.showMessageDialog(null, "Introdu un an valid!");
                                }
                            }

                            break;
                        default:
                            break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Productia nu a fost gasita!");
            }
    }
    private void showUpdateActor() {
        List<String> contributionsString = ((Staff) authenticatedUser).getActorsContributionsAsArray();
        String[] options = contributionsString.toArray(new String[0]);

        String selectedActor = (String) JOptionPane.showInputDialog(
                null,
                "Selecteaza actorul pe care vrei sa il modifici:",
                "Modificare actor",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selectedActor != null) {
            Actor actorToModify = IMDB.getInstance().findActor(selectedActor);

            String[] editOptions = {"Modificare nume", "Modificare biografie", "Modificare performances"};
            String selectedEditOption = (String) JOptionPane.showInputDialog(
                    null,
                    "Ce doresti sa modifici pentru actorul " + selectedActor + "?",
                    "Alege optiunea",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    editOptions,
                    editOptions[0]
            );

            if (selectedEditOption != null) {
                switch (selectedEditOption) {
                    case "Modificare nume":
                        String newActorName = JOptionPane.showInputDialog("Introdu noul nume pentru actorul " + selectedActor + ":");
                        actorToModify.setName(newActorName);
                        displayActorDetails(actorToModify);
                        break;
                    case "Modificare biografie":
                        String newActorBio = JOptionPane.showInputDialog("Introdu noua biografie pentru actorul " + selectedActor + ":");
                        actorToModify.setBiography(newActorBio);
                        displayActorDetails(actorToModify);
                        break;
                    case "Modificare performances":
                        String[] addOrDelete = {"Add", "Delete"};
                        String selectedAction = (String) JOptionPane.showInputDialog(
                                null,
                                "Alege actiunea:",
                                "Adauga/Sterge performante",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                addOrDelete,
                                addOrDelete[0]
                        );

                        if (selectedAction != null) {
                            if (selectedAction.equals("Add")) {

                                // Afiseaza performantele curente ale actorului
                                List<Map<String, String>> performances = actorToModify.getPerformances();
                                StringBuilder currentPerformances = new StringBuilder("Performante curente ale actorului:\n");
                                for (Map<String, String> performance : performances) {
                                    currentPerformances.append("- ").append(performance.get("title")).append(" (").append(performance.get("type")).append(")\n");
                                }
                                JOptionPane.showMessageDialog(null, currentPerformances.toString(), "Performante curente", JOptionPane.INFORMATION_MESSAGE);

                                String productionTitleToAdd = JOptionPane.showInputDialog("Introdu titlul productiei:");
                                Production productionToAdd = IMDB.getInstance().findProduction(productionTitleToAdd);

                                if (productionToAdd != null) {
                                    boolean productionExists = false;
                                    for (Map<String, String> performance : actorToModify.getPerformances()) {
                                        if (performance.get("title").equals(productionToAdd.getTitle())) {
                                            productionExists = true;
                                            break;
                                        }
                                    }

                                    if (!productionExists) {
                                        Map<String, String> newPerformance = new HashMap<>();
                                        newPerformance.put("title", productionToAdd.getTitle());
                                        newPerformance.put("type", productionToAdd.getType());

                                        actorToModify.getPerformances().add(newPerformance);
                                        productionToAdd.getActors().add(actorToModify.getName());

                                        JOptionPane.showMessageDialog(null, "Performanta a fost adaugata cu succes pentru actorul " + selectedActor, "Adaugare performanta", JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Productia cu titlul " + productionToAdd.getTitle() + " exista deja pentru actorul " + selectedActor, "Eroare", JOptionPane.ERROR_MESSAGE);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Productia cu titlul " + productionTitleToAdd + " nu exista!", "Eroare", JOptionPane.ERROR_MESSAGE);
                                }
                            } else if (selectedAction.equals("Delete")) {
                                List<Map<String, String>> performances = actorToModify.getPerformances();
                                String[] performanceTitles = new String[performances.size()];
                                for (int i = 0; i < performances.size(); i++) {
                                    performanceTitles[i] = performances.get(i).get("title");
                                }

                                String selectedPerformanceToDelete = (String) JOptionPane.showInputDialog(
                                        null,
                                        "Alege performanța pe care vrei sa o stergi:",
                                        "Stergere performanta",
                                        JOptionPane.PLAIN_MESSAGE,
                                        null,
                                        performanceTitles,
                                        performanceTitles[0]
                                );

                                if (selectedPerformanceToDelete != null) {
                                    for (Map<String, String> performance : performances) {
                                        if (performance.get("title").equals(selectedPerformanceToDelete)) {
                                            actorToModify.getPerformances().remove(performance);
                                            Production p = IMDB.getInstance().findProduction(selectedPerformanceToDelete);
                                            if (p != null) {
                                                p.getActors().remove(actorToModify.getName());
                                            }
                                            JOptionPane.showMessageDialog(null, "Performanta " + selectedPerformanceToDelete + " a fost stearsa pentru actorul " + selectedActor, "Stergere performanta", JOptionPane.INFORMATION_MESSAGE);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void manageActors(Production production) {
        String[] addOrDeleteActors = {"Adauga", "Sterge"};
        String selectedActorAction = (String) JOptionPane.showInputDialog(
                null,
                "Alege actiunea:",
                "Adauga/Sterge Actor",
                JOptionPane.PLAIN_MESSAGE,
                null,
                addOrDeleteActors,
                addOrDeleteActors[0]
        );

        if (selectedActorAction != null) {
            if (selectedActorAction.equals("Adauga")) {
                List<String> actorsList = production.getActors();

                StringBuilder currentActors = new StringBuilder("Actori actuali:\n");
                for (String actor : actorsList) {
                    currentActors.append(actor).append("\n");
                }
                JOptionPane.showMessageDialog(null, currentActors.toString(), "Actori actuali", JOptionPane.INFORMATION_MESSAGE);

                String newActor = JOptionPane.showInputDialog(
                        null,
                        "Introdu numele noului actor:",
                        "Adauga Actor",
                        JOptionPane.PLAIN_MESSAGE
                );

                if (newActor != null && !newActor.isEmpty()) {
                    if (actorsList.contains(newActor)) {
                        JOptionPane.showMessageDialog(null, "Eroare: Actorul este deja in lista.", "Eroare", JOptionPane.ERROR_MESSAGE);
                    } else {
                        actorsList.add(newActor);
                        production.setActors(actorsList);
                        IMDB.getInstance().addActorFromProduction((Staff) authenticatedUser, newActor, production.getTitle(), production.getType());
                    }

                }
            } else if (selectedActorAction.equals("Sterge")) {
                List<String> actorsList = production.getActors();

                StringBuilder currentActors = new StringBuilder("Actori actuali:\n");
                for (String actor : actorsList) {
                    currentActors.append(actor).append("\n");
                }
                JOptionPane.showMessageDialog(null, currentActors.toString(), "Actori actuali", JOptionPane.INFORMATION_MESSAGE);

                String[] actors = actorsList.toArray(new String[0]);
                String selectedActor = (String) JOptionPane.showInputDialog(
                        null,
                        "Alege actorul pentru stergere:",
                        "Sterge Actor",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        actors,
                        actors[0]
                );

                if (selectedActor != null) {
                    actorsList.remove(selectedActor);
                    production.setActors(actorsList);

                    Actor actor = IMDB.getInstance().findActor(selectedActor);

                    for (Map<String, String> performance : actor.getPerformances()) {
                        if (performance.get("title").equals(production.getTitle())) {
                            actor.getPerformances().remove(performance);
                            break;
                        }
                    }
                }
            }
        }
    }
    private void manageGenres(Production production) {
        String[] addOrDeleteGenres = {"Adauga", "Sterge"};
        String selectedGenreAction = (String) JOptionPane.showInputDialog(
                null,
                "Alege actiunea:",
                "Adauga/Sterge Gen",
                JOptionPane.PLAIN_MESSAGE,
                null,
                addOrDeleteGenres,
                addOrDeleteGenres[0]
        );

        if (selectedGenreAction != null) {
            if (selectedGenreAction.equals("Adauga")) {
                List<Genre> genresList = production.getGenres();

                StringBuilder currentGenres = new StringBuilder("Genuri actuale:\n");
                for (Genre genre : genresList) {
                    currentGenres.append(genre).append("\n");
                }
                JOptionPane.showMessageDialog(null, currentGenres.toString(), "Genuri actuale", JOptionPane.INFORMATION_MESSAGE);


                Genre newGenre = (Genre) JOptionPane.showInputDialog(
                        null,
                        "Alege noul gen:",
                        "Adauga Gen",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        Genre.values(),
                        Genre.values()[0]
                );

                if (newGenre != null) {
                    if (genresList.contains(newGenre)) {
                        JOptionPane.showMessageDialog(null, "Eroare: Genul exista deja.", "Eroare", JOptionPane.ERROR_MESSAGE);
                    } else {
                        genresList.add(newGenre);
                        production.setGenres(genresList);
                    }
                }
            } else if (selectedGenreAction.equals("Sterge")) {
                List<Genre> genresList = production.getGenres();

                StringBuilder currentGenres = new StringBuilder("Genuri actuale:\n");
                for (Genre genre : genresList) {
                    currentGenres.append(genre).append("\n");
                }
                JOptionPane.showMessageDialog(null, currentGenres.toString(), "Genuri actuale", JOptionPane.INFORMATION_MESSAGE);

                Genre[] genres = genresList.toArray(new Genre[0]);
                Genre selectedGenre = (Genre) JOptionPane.showInputDialog(
                        null,
                        "Alege genul pentru stergere:",
                        "Sterge Gen",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        genres,
                        genres[0]
                );

                if (selectedGenre != null) {
                    genresList.remove(selectedGenre);
                    production.setGenres(genresList);
                }
            }
        }
    }
    private void manageDirectors(Production production) {
        String[] addOrDelete = {"Adauga", "Sterge"};
        String selectedAction = (String) JOptionPane.showInputDialog(
                null,
                "Alege actiunea:",
                "Adauga/Sterge Director",
                JOptionPane.PLAIN_MESSAGE,
                null,
                addOrDelete,
                addOrDelete[0]
        );

        if (selectedAction != null) {
            if (selectedAction.equals("Adauga")) {
                List<String> directorsList = production.getDirectors();

                StringBuilder currentDirectors = new StringBuilder("Directori actuali:\n");
                for (String director : directorsList) {
                    currentDirectors.append(director).append("\n");
                }
                JOptionPane.showMessageDialog(null, currentDirectors.toString(), "Directori actuali", JOptionPane.INFORMATION_MESSAGE);

                String newDirector = JOptionPane.showInputDialog(
                        null,
                        "Introdu numele noului director:",
                        "Adauga Director",
                        JOptionPane.PLAIN_MESSAGE
                );

                if (newDirector != null && !newDirector.isEmpty()) {
                    if (directorsList.contains(newDirector)) {
                        JOptionPane.showMessageDialog(null, "Eroare: Directorul este deja in lista.", "Eroare", JOptionPane.ERROR_MESSAGE);
                    } else {
                        directorsList.add(newDirector);
                        production.setDirectors(directorsList);
                    }
                }
            } else if (selectedAction.equals("Sterge")) {
                List<String> directorsList = production.getDirectors();

                String[] directors = directorsList.toArray(new String[0]);
                String selectedDirector = (String) JOptionPane.showInputDialog(
                        null,
                        "Alege directorul pentru stergere:",
                        "Sterge Director",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        directors,
                        directors[0]
                );

                if (selectedDirector != null) {
                    directorsList.remove(selectedDirector);
                    production.setDirectors(directorsList);
                }
            }
        }
    }

    private void addSeasonsAndEpisodes(Production production) {
        int nrSeasons = Integer.parseInt(JOptionPane.showInputDialog("Introdu numarul de sezoane:"));
        Map<String, List<Episode>> seasons = ((Series) production).getSeasons();
        int nr = ((Series) production).getNumSeasons();

        for (int i = 1 + nrSeasons; i <= nrSeasons + nr; i++) {
            int episodes = Integer.parseInt(JOptionPane.showInputDialog("Introdu numarul de episoade pentru sezonul " + i + ":"));

            String nrSeason = "Season " + i;
            List<Episode> episode = new ArrayList<>();

            for (int j = 1; j <= episodes; j++) {
                String episodeName = JOptionPane.showInputDialog("Introdu numele episodului " + j + " din sezonul " + i + ":");
                int episodeDurationInt = Integer.parseInt(JOptionPane.showInputDialog("Introdu durata episodului " + j + " din sezonul " + i + " in minute:"));
                String duration = episodeDurationInt + " minutes";

                Episode episode1 = new Episode(episodeName, duration);
                episode.add(episode1);
            }
            seasons.put(nrSeason, episode);
        }
        ((Series) production).setSeasons(seasons);
        displayProductionDetails(production);

    }
    private void addEpisodesToSeason(Map<String, List<Episode>> seasons, Production production) {
        String seasonNumber = JOptionPane.showInputDialog("Introdu numarul sezonului pentru adaugarea episoadelor:");

        List<Episode> seasonEpisodes = seasons.get(seasonNumber);
        if (seasonEpisodes != null) {
            int episodesToAdd = Integer.parseInt(JOptionPane.showInputDialog("Introdu numarul de episoade pentru sezonul " + seasonNumber + ":"));

            for (int j = 1; j <= episodesToAdd; j++) {
                String episodeName = JOptionPane.showInputDialog("Introdu numele episodului " + j + " din sezonul " + seasonNumber + ":");
                int episodeDurationInt = Integer.parseInt(JOptionPane.showInputDialog("Introdu durata episodului " + j + " din sezonul " + seasonNumber + " în minute:"));
                String duration = episodeDurationInt + " minutes";

                Episode episode = new Episode(episodeName, duration);
                seasonEpisodes.add(episode);
            }

            seasons.put(seasonNumber, seasonEpisodes);

            displayProductionDetails(production);
        } else {
            JOptionPane.showMessageDialog(null, "Sezonul " + seasonNumber + " nu exista!", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void deleteEpisodeFromSeason(Map<String, List<Episode>> seasons, Production production) {

        StringBuilder allSeasons = new StringBuilder("Sezoane disponibile:\n");
        for (String season : seasons.keySet()) {
            allSeasons.append(season).append("\n");
        }
        String seasonNumber = JOptionPane.showInputDialog("Introdu numele sezonului din care vrei sa stergi un episod (Season + nr):\n" + allSeasons.toString());

        List<Episode> seasonEpisodes = seasons.get(seasonNumber);
        if (seasonEpisodes != null) {

            StringBuilder episodesInSeason = new StringBuilder("Episoade disponibile in sezonul " + seasonNumber + ":\n");
            for (Episode episode : seasonEpisodes) {
                episodesInSeason.append(episode.getEpisodeName()).append("\n");
            }
            String episodeToDelete = JOptionPane.showInputDialog("Introdu numele episodului pe care vrei sa il stergi din sezonul " + seasonNumber + ":\n" + episodesInSeason.toString());

            // Sterge episodul din lista de episoade a sezonului
            seasonEpisodes.removeIf(episode -> episode.getEpisodeName().equals(episodeToDelete));

            seasons.put(seasonNumber, seasonEpisodes);

            displayProductionDetails(production);
        } else {
            JOptionPane.showMessageDialog(null, "Sezonul " + seasonNumber + " nu exista!", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void editEpisodeInSeason(Map<String, List<Episode>> seasons, Production production) {
        // Afiseaza toate sezoanele disponibile
        StringBuilder allSeasons = new StringBuilder("Sezoane disponibile:\n");
        for (String season : seasons.keySet()) {
            allSeasons.append(season).append("\n");
        }
        String seasonNumber = JOptionPane.showInputDialog("Introdu numele sezonului in care vrei sa modifici un episod: (Season + nr)\n" + allSeasons.toString());

        List<Episode> seasonEpisodes = seasons.get(seasonNumber);
        if (seasonEpisodes != null) {
            // Afiseaza toate episoadele din sezonul respectiv
            StringBuilder episodesInSeason = new StringBuilder("Episoade disponibile in sezonul " + seasonNumber + ":\n");
            for (Episode episode : seasonEpisodes) {
                episodesInSeason.append(episode.getEpisodeName()).append("\n");
            }
            String episodeToEdit = JOptionPane.showInputDialog("Introdu numele episodului pe care vrei sa il modifici din sezonul " + seasonNumber + ":\n" + episodesInSeason.toString());

            for (Episode episode : seasonEpisodes) {
                if (episode.getEpisodeName().equals(episodeToEdit)) {
                    String[] options = {"Modificare nume", "Modificare durata"};
                    String selectedOption = (String) JOptionPane.showInputDialog(
                            null,
                            "Ce doresti sa modifici pentru episodul " + episodeToEdit + "?",
                            "Alege optiunea",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            options[0]
                    );

                    if (selectedOption != null) {
                        if (selectedOption.equals("Modificare nume")) {
                            String newEpisodeName = JOptionPane.showInputDialog("Introdu noul nume pentru episodul " + episodeToEdit + ":");
                            episode.setEpisodeName(newEpisodeName);
                        } else if (selectedOption.equals("Modificare durata")) {
                            int newEpisodeDuration = Integer.parseInt(JOptionPane.showInputDialog("Introdu noua durata pentru episodul " + episodeToEdit + " in minute:"));
                            String duration = newEpisodeDuration + " minutes";
                            episode.setDuration(duration);
                        }
                    }
                    break;
                }
            }

            seasons.put(seasonNumber, seasonEpisodes);

            displayProductionDetails(production);
        } else {
            JOptionPane.showMessageDialog(null, "Sezonul " + seasonNumber + " nu exista!", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddDeleteRating() {
        String[] addOrDelete = {"Add", "Delete"};
        String selectedAction = (String) JOptionPane.showInputDialog(
                null,
                "Alege actiunea:",
                "Adauga/Sterge rating",
                JOptionPane.PLAIN_MESSAGE,
                null,
                addOrDelete,
                addOrDelete[0]
        );

        if (selectedAction != null) {
            if (selectedAction.equals("Add")) {
                String productionTitle = JOptionPane.showInputDialog("Introdu titlul productiei:");

                Production production = IMDB.getInstance().findProduction(productionTitle);
                if (production != null) {

                    if (!((Regular<T>) authenticatedUser).findRating(productionTitle)) {
                        int option = JOptionPane.showConfirmDialog(null, "Aveti deja un rating pentru aceasta productie. Doriti sa il actualizati?", "Rating existent", JOptionPane.YES_NO_OPTION);

                        if (option == JOptionPane.YES_OPTION) {
                            ((Regular<T>) authenticatedUser).deleteRatingRegular(productionTitle);

                            int newRating = Integer.parseInt(JOptionPane.showInputDialog("Introdu rating-ul pentru productia " + productionTitle + ":"));
                            String comment = JOptionPane.showInputDialog("Introdu comentariul pentru productia " + productionTitle + ":");
                            Rating Rating = new Rating(authenticatedUser.getUsername(), newRating, comment);
                            ((Regular<T>)authenticatedUser).addRatingRegular(productionTitle, Rating);
                            JOptionPane.showMessageDialog(null, "Rating-ul a fost actualizat cu succes pentru producția " + productionTitle, "Actualizare rating", JOptionPane.INFORMATION_MESSAGE);

                        } else if (option == JOptionPane.NO_OPTION) {

                        }
                    } else {
                        int newRating = Integer.parseInt(JOptionPane.showInputDialog("Introdu rating-ul pentru productia " + productionTitle + ":"));
                        String comment = JOptionPane.showInputDialog("Introdu comentariul pentru productia " + productionTitle + ":");

                        Rating Rating = new Rating(authenticatedUser.getUsername(), newRating, comment);
                        ((Regular<T>)authenticatedUser).addRatingRegular(productionTitle, Rating);
                        JOptionPane.showMessageDialog(null, "Rating-ul a fost adaugat cu succes pentru productia " + productionTitle, "Adaugare rating", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Productia cu titlul " + productionTitle + " nu exista!", "Eroare", JOptionPane.ERROR_MESSAGE);
                }
            } else if (selectedAction.equals("Delete")) {
                List<String> productionTitles = ((Regular<T>) authenticatedUser).getUserRatedProductions();

                if (!productionTitles.isEmpty()) {
                    String selectedProductionTitleToDelete = (String) JOptionPane.showInputDialog(
                            null,
                            "Alege productia pentru care vrei sa stergi rating-ul:",
                            "Stergere rating",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            productionTitles.toArray(new String[0]),
                            productionTitles.get(0)
                    );

                    if (selectedProductionTitleToDelete != null) {
                        ((Regular<T>) authenticatedUser).deleteRatingRegular(selectedProductionTitleToDelete);
                        JOptionPane.showMessageDialog(null, "Rating-ul pentru productia " + selectedProductionTitleToDelete + " a fost sters cu succes.", "Stergere rating", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Nu exista productii cu rating de sters.", "Eroare", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
