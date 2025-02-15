package org.example;
import java.util.*;

public abstract class Staff  <T extends Comparable<T>> extends User<T> implements StaffInterface {
    private List<Request> requestList;
    private SortedSet<T> contributions;


    public Staff() {
        this.contributions = new TreeSet<>();
        this.requestList = new ArrayList<>();
    }

    public SortedSet<T> getContributions() {
        return contributions;
    }

    public void setContributions(SortedSet<T> contributionItem) {
        this.contributions = contributionItem;
    }

    public void addContributions(T item) {
        contributions.add(item);
    }

    public void addContribution(String string) {
        List<Production> productions = IMDB.getInstance().getProductions();
        List<Actor> actors = IMDB.getInstance().getActors();

        for (Production production : productions) {
            if (production.getTitle().equals(string)) {
                this.contributions.add((T) production);
                break;
            }
        }
        for (Actor actor : actors) {
            if (actor.getName().equals(string)) {
                this.contributions.add((T) actor);
                break;
            }
        }
    }

    public void viewContributions() {
        for (T contribution : contributions) {
            if (contribution instanceof Actor) {
                System.out.println(((Actor) contribution).getName());
            }
            if (contribution instanceof Movie) {
                System.out.println(((Movie) contribution).getTitle());
            }
            if (contribution instanceof Series) {
                System.out.println(((Series) contribution).getTitle());
            }
        }
    }

    public void viewProductionContributions() {
        for (T contribution : contributions) {
            if (contribution instanceof Production) {
                System.out.println(((Production) contribution).getTitle());
            }
        }
    }

    public void viewActorContributions() {
        for (T contribution : contributions) {
            if (contribution instanceof Actor) {
                System.out.println(((Actor) contribution).getName());
            }
        }
    }

    public void removeContribution(String string) {
        for (T item : contributions) {
            if (item instanceof Actor) {
                if (((Actor) item).getName().equals(string)) {
                    contributions.remove(item);
                    break;
                }
            }
            if (item instanceof Movie) {
                if (((Movie) item).getTitle().equals(string)) {
                    contributions.remove(item);
                    break;
                }
            }
            if (item instanceof Series) {
                if (((Series) item).getTitle().equals(string)) {
                    contributions.remove(item);
                    break;
                }
            }
        }
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }



    public boolean productionContExists (String string) {
        for (T item : contributions) {
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

    public boolean findContribution (String string) {
        for (T item : contributions) {
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
            if (item instanceof Actor) {
                if (((Actor) item).getName().equals(string)) {
                    return true;
                }
            }

        }
        return false;
    }

    public boolean actorContExists (String string) {
        for (T item : contributions) {
            if (item instanceof Actor) {
                if (((Actor) item).getName().equals(string)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void addProductionSystem(Production p) {
        List<Production> productions = IMDB.getInstance().getProductions();
        productions.add(p);
        this.addContributions((T) p);

        if (this instanceof Contributor<T>) {
            AddItemExperienceStrategy addItemStrategy = new AddItemExperienceStrategy();
            int experienceGained = addItemStrategy.calculateExperience();

            // Acordarea experienței pentru utilizator
            this.updateExperience(experienceGained);
            IMDB.getInstance().sortRatingsByUserExperience();
        }
    }

    @Override
    public void addActorSystem(Actor a) {
        List<Actor> actors = IMDB.getInstance().getActors();
        actors.add(a);
        this.addContributions((T) a);

        if (this instanceof Contributor<T>) {
            AddItemExperienceStrategy addItemStrategy = new AddItemExperienceStrategy();
            int experienceGained = addItemStrategy.calculateExperience();

            // Acordarea experienței pentru utilizator
            this.updateExperience(experienceGained);
            IMDB.getInstance().sortRatingsByUserExperience();
        }

    }

    @Override
    public void removeProductionSystem(String name) {
        List<Production> productions = IMDB.getInstance().getProductions();
        Production p = IMDB.getInstance().findProduction(name);

        List<Actor> actors = IMDB.getInstance().getActors();
        for (Actor actor : actors) {
            for (Map<String, String> performance : actor.getPerformances()) {
                if (performance.get("title").equals(name)) {
                    List<Map<String, String>> newPerformances = actor.getPerformances();
                    newPerformances.remove(performance);
                    actor.setPerformances(newPerformances);
                    break;
                }
            }
        }
        productions.remove(p);
        this.removeContribution(name);
    }

    @Override
    public void removeActorSystem(String name) {
        List<Actor> actors = IMDB.getInstance().getActors();
        Actor actor = IMDB.getInstance().findActor(name);

        List<Production> productions = IMDB.getInstance().getProductions();
        for (Production production : productions) {
            for (String nameActor : production.getActors()) {
                if (nameActor.equals(name)) {
                    List<String> newActors = production.getActors();
                    newActors.remove(nameActor);
                    production.setActors(newActors);
                    break;
                }
            }
        }
        actors.remove(actor);

    }

    @Override
    public void updateProduction(Production p) throws InvalidCommandException {
        System.out.println("Ce vrei sa modifici? ");
        if (p instanceof Movie) {
            System.out.println("Title/Directors/Actors/Genres/Plot/Duration/Release Year");
        }
        if (p instanceof Series) {
            System.out.println("Title/Directors/Actors/Genres/Plot/Release Year/Seasons");
        }
        Scanner scanner = new Scanner(System.in);
        String option = scanner.nextLine();
        if (option.equals("Title")) {
            System.out.println("Enter new title: ");
            String newTitle = scanner.nextLine();
            p.setTitle(newTitle);
        }
        else if (option.equals("Genres")) {
            System.out.println("Genres: ");
            System.out.println(p.getGenres());
            System.out.println("Alege o cifra: ");
            System.out.println("1. Stergere genre");
            System.out.println("2. Adaugare genre");
            int op = scanner.nextInt();
            scanner.nextLine();
            if (op == 1) {
                System.out.println("Ce genre vrei sa stergi?");
                String genreToRemove = scanner.nextLine();
                Genre genre = Genre.valueOf(genreToRemove);
                if (p.getGenres().contains(genre)) {
                    p.getGenres().remove(genre);
                } else {
                    throw new InvalidCommandException("Nu exista acest gen");
                }
            }
            else if (op == 2) {
                System.out.println("Ce genre vrei sa adaugi?");
                String genreToAdd = scanner.nextLine();
                try {
                    Genre genre = Genre.valueOf(genreToAdd);
                    if (!p.getGenres().contains(genre)) {
                        p.getGenres().add(genre);
                        System.out.println("Genul a fost adaugat cu succes.");
                        System.out.println("Genres: ");
                        System.out.println(p.getGenres());
                    } else {
                        throw new InvalidCommandException("Genul exista deja in lista de Genres.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Genul introdus este invalid.");
                }
            } else {
                throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
            }
        }
        else if (option.equals("Directors")) {
            System.out.println("Acestia sunt regizorii: ");
            System.out.println(p.getDirectors());
            System.out.println("Alege o cifra: ");
            System.out.println("1. Stergere regizor");
            System.out.println("2. Adaugare regizor");
            System.out.println("3. Modifica nume regizor");
            int op1 = scanner.nextInt();
            scanner.nextLine();
            if (op1 == 1) {
                System.out.println("Introdu numele regizorului pe care vrei sa il stergi");
                String deleteDirector = scanner.nextLine();
                if (p.getDirectors().contains(deleteDirector)) {
                    p.getDirectors().remove(deleteDirector);
                } else {
                    throw new InvalidCommandException("Directorul nu este in productie. Comanda invalida!");
                }
            }
            else if (op1 == 2) {
                System.out.println("Introdu numele regizorului pe care vrei sa il adaugi");
                String addDirector = scanner.nextLine();
                p.getDirectors().add(addDirector);
            }
            else if (op1 == 3) {
                System.out.println("Introdu numele regizorului pe care vrei sa il modifici");
                String director = scanner.nextLine();
                if (p.getDirectors().contains(director)) {
                    System.out.println("Introdu noul nume");
                    String newDirector = scanner.nextLine();
                    p.getDirectors().remove(director);
                    p.getDirectors().add(newDirector);
                } else {
                    throw new InvalidCommandException("Directorul nu este in productie. Comanda invalida!");
                }
            } else {
                throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
            }
        }
        else if (option.equals("Actors")) {
            System.out.println("Acestia sunt actorii: ");
            System.out.println(p.getActors());
            System.out.println("Alege o cifra: ");
            System.out.println("1. Stergere actori");
            System.out.println("2. Adaugare actori");
            int op1 = scanner.nextInt();
            scanner.nextLine();
            if (op1 == 1) {
                System.out.println("Ce actor vrei sa stergi?");
                String actorName = scanner.nextLine();
                if (p.getActors().contains(actorName)) {
                    p.getActors().remove(actorName);
                    Actor actor = IMDB.getInstance().findActor(actorName);

                    for (Map<String, String> performance : actor.getPerformances()) {
                        if (performance.get("title").equals(p.getTitle())) {
                            actor.getPerformances().remove(performance);
                            break;
                        }
                    }
                    System.out.println("Noii actori: ");
                    System.out.println(p.getActors());
                } else {
                    throw new InvalidCommandException("Actorul nu este in productie. Comanda invalida!");
                }
            } else if (op1 == 2) {
                System.out.println("Ce actor vrei sa adaugi?");
                String actorName = scanner.nextLine();
                if (IMDB.getInstance().actorExists(actorName)) {
                    p.getActors().add(actorName);

                    Actor actor = IMDB.getInstance().findActor(actorName);
                    Map<String, String> performance = new HashMap<>();
                    performance.put("title", p.getTitle());
                    performance.put("type", p.getType());

                    actor.getPerformances().add(performance);
                    System.out.println("Noii actori: ");
                    System.out.println(p.getActors());
                } else {
                    throw new InvalidCommandException("Actorul nu exista in sistem. Comanda invalida!");
                }
            } else {
                throw new InvalidCommandException("Comanda invalida!");
            }
        }
        else if (option.equals("Plot")) {
            System.out.println("Enter new plot: ");
            String newPlot = scanner.nextLine();
            p.setPlot(newPlot);
        }
        else if (option.equals("Duration")) {
            System.out.println("Enter new duration: ");
            String newDuration = scanner.nextLine();
            if (p instanceof Movie) {
                ((Movie) p).setDuration(newDuration);
            }
        }
        else if (option.equals("Release Year")) {
            System.out.println("Enter new release year: ");
            Scanner scanner1 = new Scanner(System.in);
            int newRelaseYear = scanner1.nextInt();
            if (p instanceof Movie) {
                ((Movie) p).setReleaseYear(newRelaseYear);
            }
        }
        else if (option.equals("Seasons")) {
            if (p instanceof Series) {
                System.out.println("Acestea sunt sezoanele: ");
                ((Series) p).viewSeasons();

                System.out.println("Alege o cifra: 1) adaugare sezoane " +
                        "2) adaugare episoade " +
                        "3) stergere episoade " +
                        "4) modificare nume sau durata episod");

                Scanner scanner1 = new Scanner(System.in);
                int option1 = scanner1.nextInt();
                int nr = ((Series) p).getNumSeasons();
                Map<String, List<Episode>> seasons = ((Series) p).getSeasons();
                if (option1 == 1) {
                    System.out.println("Cate sezoane adaugi?");
                    int numSeasons = scanner1.nextInt();

                    for (int i = 1 + nr; i <= numSeasons + nr; i++) {
                        String nrSeason = "Season " + i;
                        List<Episode> episode = new ArrayList<>();

                        System.out.println("Episodes for " + nrSeason + " (type 'finish' to end): ");
                        IMDB.getInstance().addEpisode(episode);
                        seasons.put(nrSeason, episode);
                    }
                    ((Series) p).setSeasons(seasons);
                    ((Series) p).viewSeasons();
                }
                else if (option1 == 2) {
                    System.out.println("In ce sezon vrei sa adaugi episoade?");
                    int nrsez = scanner1.nextInt();

                    if (nrsez > nr || nrsez < 1) {
                        throw new InvalidCommandException("Comanda invalida!");
                    } else {
                        String season = "Season " + nrsez;
                        List<Episode> newEpisodes = seasons.get(season);

                        IMDB.getInstance().addEpisode(newEpisodes);
                        seasons.put(season, newEpisodes);
                        ((Series) p).viewSeasons();
                    }
                }
                else if (option1 == 3) {
                    System.out.println("Ce episod vrei sa stergi?");
                    String episodDeSters = scanner.nextLine();
                    ((Series) p).deleteEpisode(episodDeSters);

                    ((Series) p).viewSeasons();
                }
                else if (option1 == 4) {
                    System.out.println("Ce episod vrei sa modifici?");
                    String episodModificat = scanner.nextLine();

                    ((Series) p).modifyEpisode(episodModificat);
                    ((Series) p).viewSeasons();
                }
                else {
                    throw new InvalidCommandException("Comanda invalida!");
                }
            } else {
                throw new InvalidCommandException("Comanda invalida!");
            }
        } else {
            throw new InvalidCommandException("Comanda invalida! Introdu o comanda valida.");
        }
    }

    @Override
    public void updateActor(Actor a) throws InvalidCommandException {
        System.out.println("Ce doresti sa modifici? Alege o cifra");
        System.out.println("1. Nume");
        System.out.println("2. Biografie");
        System.out.println("3. Performances");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.nextLine();

        if (option == 1) {
            System.out.println("Introdu noul nume");
            String newName = scanner.nextLine();
            a.setName(newName);

        } else if (option == 2) {
            System.out.println("Introdu noua biografie");
            String newBiography = scanner.nextLine();
            a.setBiography(newBiography);

        } else if (option == 3) {
            System.out.println("1. Adauga");
            System.out.println("2. Sterge");
            int op = scanner.nextInt();
            scanner.nextLine();

            if (op == 1) {
                try {
                    List<Map<String, String>> existingPerformances = a.getPerformances();
                    System.out.println("Adauga pana tastezi 'finish': ");
                    IMDB.getInstance().addPerformances(existingPerformances);
                    a.viewActorDetails();
                } catch (InvalidCommandException e) {
                    System.out.println("Comanda invalida! Introdu o comanda valida.");
                }
            }
            else  if (op == 2) {
                boolean change = false;
                System.out.println("Introdu titlul pe care vrei sa il stergi");
                a.viewPerformancesDetails();
                String performanceToDelete = scanner.nextLine();
                if (IMDB.getInstance().productionExists(performanceToDelete)) {
                    for (Map<String, String> performance : a.getPerformances()) {
                        if (performance.containsValue(performanceToDelete)) {
                            a.getPerformances().remove(performance);
                            System.out.println("Performanta " + performanceToDelete + " a fost stearsa pentru actorul " + a.getName());
                            change = true;
                            Production p = IMDB.getInstance().findProduction(performanceToDelete);
                            p.getActors().remove(a.getName());
                            break;
                        }
                    }
                    if (!change) {
                        throw new InvalidCommandException("Productia nu se afla printre performantele actorului!");
                    }
                } else {
                    throw new InvalidCommandException("Productia nu exista!");
                }
            } else {
                throw new InvalidCommandException("Comanda invalida!");
            }
        } else {
            throw new InvalidCommandException("Comanda invalida!");
        }
    }

    public List<String> getContributionsAsArray() {

        List<String> contributionsList = new ArrayList<>();
        for (T item : contributions) {
            if (item instanceof Production) {
                contributionsList.add(((Production) item).getTitle());
            } else {
                contributionsList.add(((Actor) item).getName());
            }
        }
        if (this instanceof Admin) {
            SortedSet<T> contributionsForAdmins = ((Admin) this).getContributionsForAdmins();
            if (contributionsForAdmins != null) {
                for (T item : contributionsForAdmins) {
                    if (item instanceof Production) {
                        contributionsList.add(((Production) item).getTitle());
                    } else {
                        contributionsList.add(((Actor) item).getName());
                    }
                }
            }
        }

        return contributionsList;
    }

    public List<String> getProductionContributionsAsArray() {
        List<String> contributionsList = new ArrayList<>();
        for (T item : contributions) {
            if (item instanceof Production) {
                contributionsList.add(((Production) item).getTitle());
            }
        }
        if (this instanceof Admin) {
            SortedSet<T> contributionsForAdmins = ((Admin) this).getContributionsForAdmins();
            if (contributionsForAdmins != null) {
                for (T item : contributionsForAdmins) {
                    if (item instanceof Production) {
                        contributionsList.add(((Production) item).getTitle());
                    }
                }
            }
        }

        return contributionsList;
    }

    public List<String> getActorsContributionsAsArray() {
        List<String> contributionsList = new ArrayList<>();
        for (T item : contributions) {
            if (item instanceof Actor) {
                contributionsList.add(((Actor) item).getName());
            }
        }
        if (this instanceof Admin) {
            SortedSet<T> contributionsForAdmins = ((Admin) this).getContributionsForAdmins();
            if (contributionsForAdmins != null) {
                for (T item : contributionsForAdmins) {
                    if (item instanceof Actor) {
                        contributionsList.add(((Actor) item).getName());
                    }
                }
            }
        }

        return contributionsList;
    }


}
