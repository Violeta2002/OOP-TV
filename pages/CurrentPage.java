package pages;

import tvmechanism.Movies;
import tvmechanism.Notification;
import tvmechanism.Users;

import java.util.ArrayList;
import java.util.HashMap;

// I used the Singleton design pattern for the CurrentPage class because I wanted to have
// only one instance of it and I wanted to have access to the current page, current user
// and other information from all the classes
public final class CurrentPage extends Page {
    private static CurrentPage instance = null;
    private String name;
    private volatile static int alreadyLogged;
    private ArrayList<Users> currentUsers;
    private ArrayList<Movies> currentMovies;

    private Users currentUser;

    private ArrayList<Movies> seeDetailsMovies = new ArrayList<>();

    private Movies selectedMovie;

    private ArrayList<Movies> filteredMovies = new ArrayList<>();

    private HashMap<Users, ArrayList<Notification>> notifcations = new HashMap<>();

    private HashMap<Users, ArrayList<String>> subscriptions = new HashMap<>();

    private int filtered;

    private CurrentPage() {
        alreadyLogged = 0;
        filtered = 0;
    }

    /**
     *
     * @return - the current page instance
     */
    public static CurrentPage getInstance() {
        if (instance == null) {
            instance = new CurrentPage();
        }
        return instance;
    }

    /**
     *
     * @param pageName - page name to be visited
     * @return - 1 if the page can be visited, 0 otherwise
     */
    public int canVisit(final String pageName) {
        switch (this.name) {
            case "Homepage neautentificat" -> {
                HomepageNeautentificatPage homepage = new HomepageNeautentificatPage();
                return homepage.canVisit(pageName);
            }
            case "login" -> {
                LoginPage loginPage = new LoginPage();
                return loginPage.canVisit(pageName);
            }
            case "register" -> {
                RegisterPage registerPage = new RegisterPage();
                return registerPage.canVisit(pageName);
            }
            case "movies" -> {
                MoviesPage moviesPage = new MoviesPage();
                return moviesPage.canVisit(pageName);
            }
            case "upgrades" -> {
                UpgradesPage upgradesPage = new UpgradesPage();
                return upgradesPage.canVisit(pageName);
            }
            case "see details" -> {
                SeeDetailsPage seeDetailsPage = new SeeDetailsPage();
                return seeDetailsPage.canVisit(pageName);
            }
            default -> { }
        }
        return 1;
    }


    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getAlreadyLogged() {
        return alreadyLogged;
    }

    public void setAlreadyLogged(final int alreadyLogged) {
        CurrentPage.alreadyLogged = alreadyLogged;
    }

    public ArrayList<Users> getCurrentUsers() {
        return currentUsers;
    }

    public void setCurrentUsers(final ArrayList<Users> currentUsers) {
        this.currentUsers = currentUsers;
    }

    public ArrayList<Movies> getCurrentMovies() {
        return currentMovies;
    }

    public void setCurrentMovies(final ArrayList<Movies> currentMovies) {
        this.currentMovies = currentMovies;
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(final Users currentUser) {
        this.currentUser = currentUser;
    }

    public ArrayList<Movies> getSeeDetailsMovies() {
        return seeDetailsMovies;
    }

    public void setSeeDetailsMovies(final ArrayList<Movies> seeDetailsMovies) {
        this.seeDetailsMovies = seeDetailsMovies;
    }

    public Movies getSelectedMovie() {
        return selectedMovie;
    }

    public void setSelectedMovie(final Movies selectedMovie) {
        this.selectedMovie = selectedMovie;
    }

    public ArrayList<Movies> getFilteredMovies() {
        return filteredMovies;
    }

    public void setFilteredMovies(final ArrayList<Movies> filteredMovies) {
        this.filteredMovies = filteredMovies;
    }

    public int getFiltered() {
        return filtered;
    }

    public void setFiltered(final int filtered) {
        this.filtered = filtered;
    }

    public HashMap<Users, ArrayList<Notification>> getNotifcations() {
        return notifcations;
    }

    public void setNotifcations(final HashMap<Users, ArrayList<Notification>> notifcations) {
        this.notifcations = notifcations;
    }

    public HashMap<Users, ArrayList<String>> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(final HashMap<Users, ArrayList<String>> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
