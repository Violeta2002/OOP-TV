package tvmechanism;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import command.CommandHistory;
import command.NextPage;
import command.PrevPage;
import pages.UpgradesPage;
import fileio.ActionsInput;
import observer.Database;
import observer.Subject;
import pages.CurrentPage;
import pages.RegisterPage;
import pages.SeeDetailsPage;
import pages.LoginPage;

import java.util.ArrayList;

import static pages.CurrentPage.getInstance;

public final class Actions {
    private String type;
    private String page;
    private String feature;
    private Credentials credentials;
    private Filters filters;
    private String startsWith;
    private String movie;
    private String count;
    private int rate;

    private String subscribedGenre;

    private String deletedMovie;

    private tvmechanism.Notification notification = new Notification();
    private Movies addedMovie;

    public Actions() {
    }

    public Actions(final ActionsInput actionsInput) {
        this.count = actionsInput.getCount();
        this.movie = actionsInput.getMovie();
        this.feature = actionsInput.getFeature();
        if (actionsInput.getFilters() != null) {
            if (actionsInput.getFilters().getContains() != null
                    && actionsInput.getFilters().getSort() != null) {
                this.filters = new Filters.FilterBuilder()
                        .buildContains(actionsInput.getFilters().getContains())
                        .buildSort(actionsInput.getFilters().getSort()).build();
            }
            if (actionsInput.getFilters().getContains() != null) {
                this.filters = new Filters.FilterBuilder()
                        .buildContains(actionsInput.getFilters().getContains()).build();
            }
            if (actionsInput.getFilters().getSort() != null) {
                this.filters = new Filters.FilterBuilder()
                        .buildSort(actionsInput.getFilters().getSort()).build();
            }
        }
        this.page = actionsInput.getPage();
        this.rate = actionsInput.getRate();
        this.startsWith = actionsInput.getStartsWith();
        this.type = actionsInput.getType();
        if (actionsInput.getCredentials() != null) {
            this.credentials = new Credentials(actionsInput.getCredentials());
        }
        this.subscribedGenre = actionsInput.getSubscribedGenre();
        this.deletedMovie = actionsInput.getDeletedMovie();
        if (actionsInput.getAddedMovie() != null) {
            this.addedMovie = new Movies(actionsInput.getAddedMovie());
        }
    }

    /**
     * Method that adds the credentials of the user to a JsonNode
     *
     * @param node        - the node where the output will be written
     * @param credentials - the credentials of the user
     */
    public static void addCredentialsToNode(final ObjectNode node, final Credentials credentials) {
        node.put("name", credentials.getName());
        node.put("password", credentials.getPassword());
        node.put("accountType", credentials.getAccountType());
        node.put("country", credentials.getCountry());
        node.put("balance", credentials.getBalance());
    }

    /**
     * Method that add the user's data to a JsonNode
     *
     * @param node        - the node where the output will be written
     * @param currentUser - the current user
     */
    public static void addUserData(final ObjectNode node, final Users currentUser) {
        ObjectMapper objectMapper = new ObjectMapper();
        node.put("tokensCount", currentUser.getTokensCount());
        node.put("numFreePremiumMovies", currentUser.getNumFreePremiumMovies());

        ArrayNode purchased = objectMapper.createArrayNode();
        if (currentUser.getPurchasedMovies().size() > 0) {
            addFeaturedListToList(purchased, currentUser.getPurchasedMovies(), currentUser);
        }

        ArrayNode watched = objectMapper.createArrayNode();
        if (currentUser.getPurchasedMovies().size() > 0) {
            addFeaturedListToList(watched, currentUser.getWatchedMovies(), currentUser);
        }

        ArrayNode liked = objectMapper.createArrayNode();
        if (currentUser.getWatchedMovies().size() > 0) {
            addFeaturedListToList(liked, currentUser.getLikedMovies(), currentUser);
        }

        ArrayNode rated = objectMapper.createArrayNode();
        if (currentUser.getRatedMovies().size() > 0) {
            addFeaturedListToList(rated, currentUser.getRatedMovies(), currentUser);
        }

        ArrayNode notif = objectMapper.createArrayNode();
        if (getInstance().getNotifcations().get(currentUser) != null) {
            for (Notification notification : getInstance().getNotifcations().get(currentUser)) {
                ObjectNode notifNode = objectMapper.createObjectNode();
                notifNode.put("movieName", notification.getMovieName());
                notifNode.put("message", notification.getMessage());
                notif.add(notifNode);
            }
        }

        node.put("purchasedMovies", purchased);
        node.put("watchedMovies", watched);
        node.put("likedMovies", liked);
        node.put("ratedMovies", rated);
        node.put("notifications", notif);
    }

    /**
     * Method that adds the movies from a list to a JsonNode
     *
     * @param movieDetails - the details of the movie
     * @param movie        - the movie
     */
    public static void addMovieToNode(final ObjectNode movieDetails, final Movies movie) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode genres = objectMapper.createArrayNode();
        ArrayNode actors = objectMapper.createArrayNode();
        ArrayNode countries = objectMapper.createArrayNode();
        movieDetails.put("name", movie.getName());
        movieDetails.put("year", movie.getYear());
        movieDetails.put("duration", movie.getDuration());
        for (String genre : movie.getGenres()) {
            genres.add(genre);
        }
        movieDetails.put("genres", genres);
        for (String actor : movie.getActors()) {
            actors.add(actor);
        }
        movieDetails.put("actors", actors);
        for (String country : movie.getCountriesBanned()) {
            countries.add(country);
        }
        movieDetails.put("countriesBanned", countries);
        movieDetails.put("numLikes", movie.getNumLikes());
        movieDetails.put("rating", movie.getRating());
        movieDetails.put("numRatings", movie.getNumRatings());
    }

    /**
     * Method that adds the movies from a list to a JsonNode
     *
     * @param node        - the node where the output will be written
     * @param movies      - the list of movies
     * @param currentUser - the current user
     */
    public static void addMovieListtoNode(final ObjectNode node, final ArrayList<Movies> movies,
                                          final Users currentUser) {
        ObjectMapper objectMapper = new ObjectMapper();
        node.put("error", (JsonNode) null);
        ArrayNode currentList = objectMapper.createArrayNode();
        for (Movies movie : movies) {
            if (!movie.getCountriesBanned().contains(currentUser.getCredetials().getCountry())) {
                ObjectNode movieDetails = objectMapper.createObjectNode();
                addMovieToNode(movieDetails, movie);
                currentList.add(movieDetails);
            }
        }
        node.put("currentMoviesList", currentList);
    }

    /**
     * Method that adds the movies from a list to a JsonNode
     *
     * @param list        - the ArrayNode where the movies will be added
     * @param movies      - the list of movies
     * @param currentUser - the current user
     */
    public static void addFeaturedListToList(final ArrayNode list, final ArrayList<Movies> movies,
                                             final Users currentUser) {
        ObjectMapper objectMapper = new ObjectMapper();
        for (Movies movie : movies) {
            if (!movie.getCountriesBanned().contains(currentUser.getCredetials().getCountry())) {
                ObjectNode movieDetails = objectMapper.createObjectNode();
                addMovieToNode(movieDetails, movie);
                list.add(movieDetails);
            }
        }
    }

    /**
     * Method that add the credentials and data of the user to a JsonNode
     *
     * @param node        - the node where the output will be written
     * @param currentUser - the current user
     */
    public static void addCurrentUserToNode(final ObjectNode node, final Users currentUser) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode userNode = objectMapper.createObjectNode();
        ObjectNode creds = objectMapper.createObjectNode();
        addCredentialsToNode(creds, currentUser.getCredetials());
        userNode.put("credentials", creds);
        addUserData(userNode, currentUser);
        node.put("currentUser", userNode);
    }

    /**
     * Method that adds the movies from a list to a JsonNode
     *
     * @param node        - the node where the output will be written
     * @param movies      - the list of movies
     * @param currentUser - the current user
     */
    public void addSearchedMoviesToNode(final ObjectNode node, final ArrayList<Movies> movies,
                                        final Users currentUser) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode moviesList = objectMapper.createArrayNode();
        for (Movies movie : movies) {
            if (!movie.getCountriesBanned().contains(currentUser.getCredetials().getCountry())) {
                if (movie.getName().startsWith(startsWith)) {
                    ObjectNode found = objectMapper.createObjectNode();
                    addMovieToNode(found, movie);
                    moviesList.add(found);
                }
            }
        }
        node.put("error", (JsonNode) null);
        node.put("currentMoviesList", moviesList);
        addCurrentUserToNode(node, currentUser);
    }

    /**
     * Method that executes a feature
     *
     * @param feature - the feature that the user wants to execute
     * @param users   - the list of users
     * @param node    - the node where the output will be written
     * @return - 1 if the feature is not valid, 2 if we want to print the output, 0 otherwise
     */
    public int doFeature(final String feature, final ArrayList<Users> users,
                         final ObjectNode node) {
        if (feature.equals("login")) {
            LoginPage login = new LoginPage(credentials.getName(), credentials.getPassword());
            Users currentUser = login.doLogin(users);
            if (currentUser == null) {
                if (getInstance().getAlreadyLogged() == 1) {
                    getInstance().setName("Homepage autentificat");
                    CommandHistory.getInstance().getHistory().push("Homepage autentificat");
                    return 0;
                }
                getInstance().setName("Homepage neautentificat");
                return 0;
            }
            if (currentUser != null) {
                getInstance().setAlreadyLogged(1);
                getInstance().setCurrentUser(currentUser);
                getInstance().getCurrentUser().setCredetials(currentUser.getCredetials());
                this.credentials = currentUser.getCredetials();
                getInstance().setName("Homepage autentificat");
                return 1;
            }
            if (getInstance().getAlreadyLogged() == 0) {
                getInstance().setName("Homepage neautentificat");
            }
        } else if (feature.equals("register")) {
            RegisterPage register = new RegisterPage(credentials);
            register.doRegister(credentials, users);
            getInstance().setAlreadyLogged(1);
            Users currentUser = new Users(this.credentials);
            getInstance().setCurrentUser(currentUser);
            getInstance().setName("Homepage autentificat");
            return 1;
        } else if (feature.equals("search")) {
            addSearchedMoviesToNode(node, getInstance().getCurrentMovies(),
                    getInstance().getCurrentUser());
            return 2;
        } else if (feature.equals("filter")) {
            if (!getInstance().getName().equals("movies")) {
                return 0;
            }
            if (filters.getSort() != null) {
                if (filters.getSort().getRating() != null) {
                    filters.getSort().doSortByRating(getInstance().getCurrentMovies());
                }
                if (filters.getSort().getDuration() != null) {
                    filters.getSort()
                            .doSortByDuration(getInstance().getCurrentMovies());
                }
                addMovieListtoNode(node, getInstance().getCurrentMovies(),
                        getInstance().getCurrentUser());
                addCurrentUserToNode(node, getInstance().getCurrentUser());
            }
            if (filters.getContains() != null) {
                if (filters.getContains().getGenre() != null) {
                    filters.getContains().addContainedGenres(node,
                            getInstance().getCurrentMovies(),
                            getInstance().getCurrentUser());
                }
                if (filters.getContains().getActors() != null
                        && filters.getContains().getGenre() != null) {
                    {
                        filters.getContains().addContainedActors(node,
                                getInstance().getFilteredMovies(),
                                getInstance().getCurrentUser());
                    }
                } else if (filters.getContains().getActors() != null) {
                    filters.getContains().addContainedActors(node,
                            getInstance().getCurrentMovies(),
                            getInstance().getCurrentUser());
                }
            }
            return 2;
        } else if (getInstance().getName().equals("upgrades")) {
            UpgradesPage upgrades = new UpgradesPage(count, this.feature);
            upgrades.doFeature(getInstance().getCurrentUser());
            return 3;
        } else if (getInstance().getName().equals("see details")) {
            String selectedMovie = getInstance().getSelectedMovie().getName();
            SeeDetailsPage seeDetails = new SeeDetailsPage(selectedMovie, feature);
            return seeDetails.doAction(node, getInstance().getCurrentUser(),
                    getInstance().getSeeDetailsMovies(), this.rate, null);
        }
        return 0;
    }

    /**
     * Method that executes a command from the input
     *
     * @param node - the node where the output will be written
     * @return - 0 if the output is printed, 1 otherwise
     */
    public int executeAction(final ObjectNode node) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode currentMoviesList = objectMapper.createArrayNode();
        switch (this.type) {
            case "change page" -> {
                NextPage nextPage = new NextPage(this.page, this.movie, this.feature, node);
                int err = nextPage.execute();
                if (err == 1) {
                    node.put("error", "Error");
                    node.put("currentMoviesList", currentMoviesList);
                    node.put("currentUser", (JsonNode) null);
                    return 0;
                } else if (err == 2) {
                    return 0;
                } else {
                    return 1;
                }
            }
            case "on page" -> {
                if (this.feature.equals("subscribe")) {
                    int err = doSubscribe(node);
                    if (err == 1 || err == 0) {
                        node.put("error", "Error");
                        node.put("currentMoviesList", currentMoviesList);
                        node.put("currentUser", (JsonNode) null);
                        return 0;
                    } else {
                        return 1;
                    }
                }
                int verif = doFeature(feature, getInstance().getCurrentUsers(), node);
                if (verif == 0) {
                    node.put("error", "Error");
                    node.put("currentMoviesList", currentMoviesList);
                    node.put("currentUser", (JsonNode) null);
                    return 0;
                } else if (verif == 1) {
                    node.put("error", (JsonNode) null);
                    node.put("currentMoviesList", currentMoviesList);
                    ObjectNode userNode = objectMapper.createObjectNode();
                    ObjectNode creds = objectMapper.createObjectNode();
                    addCredentialsToNode(creds, credentials);
                    userNode.put("credentials", creds);
                    addUserData(userNode, getInstance().getCurrentUser());
                    node.put("currentUser", userNode);
                    return 0;
                } else if (verif == 2) {
                    return 0;
                } else if (verif == 3) {
                    return 1;
                }
            }
            case "back" -> {
                int err = 0;
                PrevPage prevPage = new PrevPage(this.movie, this.feature, node);
                err = prevPage.execute();
                if (err == 1 || err == 4) {
                    node.put("error", "Error");
                    node.put("currentMoviesList", currentMoviesList);
                    node.put("currentUser", (JsonNode) null);
                    return 0;
                } else if (err == 2) {
                    return 1;
                } else {
                    return 0;
                }
            }
            case "database" -> {
                Subject subject = new Subject();
                new Database(subject, addedMovie, deletedMovie);
                int err = subject.setState(this.feature, node);
                if (err == 1) {
                    node.put("error", "Error");
                    node.put("currentMoviesList", currentMoviesList);
                    node.put("currentUser", (JsonNode) null);
                    return 0;
                } else {
                    return 1;
                }
            }
            default -> {
            }
        }
        return 1;
    }

    /**
     * Method that adds the searched movies to the node
     *
     * @param node - the node where the output will be written
     */
    private int doSubscribe(final ObjectNode node) {
        if (CurrentPage.getInstance().getName() != "see details") {
            return 1;
        }
        SeeDetailsPage seeDetails;
        seeDetails = new SeeDetailsPage(getInstance().getSelectedMovie().getName(), "subscribe");
        return seeDetails.doAction(node, getInstance().getCurrentUser(),
                getInstance().getSeeDetailsMovies(), this.rate, subscribedGenre);
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(final String page) {
        this.page = page;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(final String feature) {
        this.feature = feature;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }

    public Filters getFilters() {
        return filters;
    }

    public void setFilters(final Filters filters) {
        this.filters = filters;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(final String movie) {
        this.movie = movie;
    }
}
