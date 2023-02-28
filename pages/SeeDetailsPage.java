package pages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import tvmechanism.Actions;
import tvmechanism.Movies;
import tvmechanism.Users;

import java.util.ArrayList;
import java.util.Map;

public final class SeeDetailsPage extends  Page {
    private String movie;
    private String action;

    public SeeDetailsPage() {
    }

    public SeeDetailsPage(final String movie, final String action) {
        if (movie != null) {
            this.movie = movie;
        }
        if (action != null) {
            this.action = action;
        }
    }

    /**
     * @param pageName - page name to be visited
     * @return 1 if the page can be visited, 0 otherwise
     */
    public int canVisit(final String pageName) {
        if (pageName.equals("movies") || pageName.equals("Homepage autentificat")
                || pageName.equals("upgrades") || pageName.equals("logout")) {
            return 1;
        }
        return 0;
    }

    /**
     * @param node        - node to be added
     * @param movies      - movies list from which the movie is taken
     * @param currentUser - current user
     * @return - 0 if the movie is not in the list, nrMovies otherwise
     */
    public int seeMovieDetails(final ObjectNode node, final ArrayList<Movies> movies,
                               final Users currentUser) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode movieList = objectMapper.createArrayNode();
        int nrMovies = 0;
        for (Movies movie : movies) {
            if (!movie.getCountriesBanned().contains(currentUser.getCredetials().getCountry())) {
                if (movie.getName().equals(this.movie)) {
                    CurrentPage.getInstance().setSeeDetailsMovies(new ArrayList<>());
                    CurrentPage.getInstance().getSeeDetailsMovies().add(movie);
                    CurrentPage.getInstance().setSelectedMovie(movie);
                    ObjectNode movieNode = objectMapper.createObjectNode();
                    Actions.addMovieToNode(movieNode, movie);
                    movieList.add(movieNode);
                    nrMovies++;
                }
            }
        }
        if (movieList != null) {
            node.put("error", (JsonNode) null);
            node.put("currentMoviesList", movieList);
            Actions.addCurrentUserToNode(node, currentUser);
        }
        return nrMovies;
    }

    /**
     * @param node        - node to be added
     * @param currentUser - current user
     * @param movies      - movies list from which the movie is taken
     * @param rate        - rate acorded to the movie by the user
     * @return - 0 if the action is executed successfully, 2 otherwise
     */
    public int doAction(final ObjectNode node, final Users currentUser,
                        final ArrayList<Movies> movies, final int rate, final String genre) {
        int err;
        switch (this.action) {
            case "purchase" -> {
                err = doPurchase(currentUser, movies);
                if (err == 0) {
                    node.put("error", (JsonNode) null);
                    Actions.addMovieListtoNode(node,
                            CurrentPage.getInstance().getSeeDetailsMovies(), currentUser);
                    Actions.addCurrentUserToNode(node, currentUser);
                    return 2;
                }
                if(err == 2) {
                    return 3;
                }
            }
            case "watch" -> {
                err = doWatch(currentUser, movies);
                if (err == 0) {
                    node.put("error", (JsonNode) null);
                    Actions.addMovieListtoNode(node,
                            CurrentPage.getInstance().getSeeDetailsMovies(), currentUser);
                    Actions.addCurrentUserToNode(node, currentUser);
                    return 2;
                }
                if (err == 2) {
                    return 3;
                }
            }
            case "like" -> {
                err = doLike(currentUser, movies);
                if (err == 0) {
                    node.put("error", (JsonNode) null);
                    Actions.addMovieListtoNode(node,
                            CurrentPage.getInstance().getSeeDetailsMovies(), currentUser);
                    Actions.addCurrentUserToNode(node, currentUser);
                    return 2;
                }
                if (err == 2) {
                    return 3;
                }
            }
            case "rate" -> {
                err = doRate(currentUser, movies, rate);
                if (err == 0) {
                    node.put("error", (JsonNode) null);
                    Actions.addMovieListtoNode(node,
                            CurrentPage.getInstance().getSeeDetailsMovies(), currentUser);
                    Actions.addCurrentUserToNode(node, currentUser);
                    return 2;
                }
            }
            case "subscribe" -> {
                err = subscribe(genre);
                if (err == 0) {
                    node.put("error", (JsonNode) null);
                    Actions.addMovieListtoNode(node,
                            CurrentPage.getInstance().getSeeDetailsMovies(), currentUser);
                    Actions.addCurrentUserToNode(node, currentUser);
                    return 2;
                }
            }
            default -> {
            }
        }
        return 0;
    }

    /**
     * Method that adds a movie to the purchase list of the user and
     * decreases the number of tokens or
     * the number of free movies that can be purchased
     *
     * @param currentUser - current user
     * @param movies      - movies list from which the selected movie is taken
     * @return - 0 if the action is executed successfully, 1 otherwise
     */
    public int doPurchase(final Users currentUser, final ArrayList<Movies> movies) {
        int index = 0;
        for (Movies movie : movies) {
            if (movie.getName().equals(this.movie)
                    && currentUser.getPurchasedMovies().contains(movie)) {
                return 1;
            }
            if (movie.getName().equals(this.movie) && !movie.getCountriesBanned().
                    contains(currentUser.getCredetials().getCountry())) {
                if (currentUser.getCredetials().getAccountType().equals("premium")
                        && (currentUser.getNumFreePremiumMovies() > 0
                        || currentUser.getTokensCount() >= 2)) {
                    index = getIndexCurrentUser();
                    currentUser.getPurchasedMovies().add(movie);
                    if (currentUser.getNumFreePremiumMovies() != 0) {
                        int nrMovies = currentUser.getNumFreePremiumMovies();
                        currentUser.setNumFreePremiumMovies(nrMovies - 1);
                        CurrentPage.getInstance().getCurrentUsers().get(index).
                                setNumFreePremiumMovies(nrMovies - 1);
                    } else {
                        int tokens = currentUser.getTokensCount();
                        CurrentPage.getInstance().getCurrentUsers().get(index).
                                setTokensCount(tokens - 2);
                        currentUser.setTokensCount(tokens - 2);
                    }
                    return 0;
                } else if (currentUser.getCredetials().getAccountType().equals("standard")
                        && currentUser.getTokensCount() >= 2) {
                    currentUser.getPurchasedMovies().add(movie);
                    int tokens = currentUser.getTokensCount();
                    currentUser.setTokensCount(tokens - 2);
                    CurrentPage.getInstance().getCurrentUsers().get(index).
                            setTokensCount(tokens - 2);
                    return 0;
                }
            }
        }
        return 1;
    }

    /**
     *
     * @return
     */
    private int getIndexCurrentUser() {
        int index = 0;
        for (Users user : CurrentPage.getInstance().getCurrentUsers()) {
            if (user.getCredetials().getName().equals(CurrentPage.getInstance()
                    .getCurrentUser().getCredetials().getName())) {
                return index;
            }
            index++;
        }
        return index;
    }

    /**
     * Method that adds a movie to the watched list of the user if the user
     * has purchased the movie and watches it
     *
     * @param currentUser - current user
     * @param movies      - movies list from which the selected movie is taken
     * @return - 0 if the action is executed successfully, 1 otherwise
     */
    public int doWatch(final Users currentUser, final ArrayList<Movies> movies) {
        for (Movies movie : movies) {
            if (movie.getName().equals(this.movie)
                    && currentUser.getWatchedMovies().contains(movie)) {
                return 0;
            }
            if (movie.getName().equals(this.movie) && !movie.getCountriesBanned()
                    .contains(currentUser.getCredetials().getCountry())) {
                if (!currentUser.getPurchasedMovies().contains(movie)) {
                    return 1;
                }
                currentUser.getWatchedMovies().add(movie);
            }
        }
        return 0;
    }

    /**
     * Method that adds a movie to the Liked list of the user if the user has
     * purchased the movie, watched it and likes it
     *
     * @param currentUser - current user
     * @param movies      - movies list from which the selected movie is taken
     * @return - 0 if the action is executed successfully, 1 otherwise
     */
    public int doLike(final Users currentUser, final ArrayList<Movies> movies) {
        for (Movies movie : movies) {
            if (movie.getName().equals(this.movie)
                    && currentUser.getLikedMovies().contains(movie)) {
                return 1;
            }
            if (movie.getName().equals(this.movie) && !movie.getCountriesBanned()
                    .contains(currentUser.getCredetials().getCountry())) {
                if (!currentUser.getWatchedMovies().contains(movie)) {
                    return 1;
                }
                currentUser.getLikedMovies().add(movie);
                int nrLikes = movie.getNumLikes();
                movie.setNumLikes(nrLikes + 1);
            }
        }
        return 0;
    }

    /**
     * Method that adds a movie to the rated list of the user if the user has
     * purchased the movie, watched it and rates it
     * The rate is added to the movie's total rate and the number of rates is increased
     * The current rate is calculated by dividing the total rate by the number of rates
     *
     * @param currentUser - current user
     * @param movies      - movies list from which the selected movie is taken
     * @param rate        - rate acorded to the movie by the user
     * @return - 0 if the action is executed successfully, 1 otherwise
     */
    public int doRate(final Users currentUser, final ArrayList<Movies> movies, final double rate) {
        for (Movies movie : movies) {
            if (movie.getName().equals(this.movie) && !movie.getCountriesBanned()
                    .contains(currentUser.getCredetials().getCountry())) {
                if (!currentUser.getWatchedMovies().contains(movie)) {
                    return 1;
                }
                if (rate > 5 || rate < 1) {
                    return 1;
                }
                movie.getRatings().put(currentUser, rate);
                double newRate = 0;
                for (Map.Entry<Users, Double> entry : movie.getRatings().entrySet()) {
                    newRate += entry.getValue();
                }
                if (movie.getName().equals(this.movie)
                        && currentUser.getRatedMovies().contains(movie)) {
                    movie.setRating(newRate / movie.getNumRatings());
                    return 0;
                }
                movie.setNumRatings(movie.getNumRatings() + 1);
                movie.setRating(newRate / movie.getNumRatings());
                currentUser.getRatedMovies().add(movie);
            }
        }
        return 0;
    }

    /**
     *
     * @param genre
     * @return
     */
    public int subscribe(final String genre) {
        for (Movies movies : CurrentPage.getInstance().getSeeDetailsMovies()) {
            if (movies.getName().equals(this.movie)) {
                if (movies.getGenres().contains(genre)) {
                    if (CurrentPage.getInstance().getSubscriptions().containsKey(CurrentPage.
                            getInstance().getCurrentUser()) && CurrentPage.getInstance()
                            .getSubscriptions().get(CurrentPage.getInstance()
                                    .getCurrentUser()).contains(genre)) {
                        return 1;
                    }
                    if (CurrentPage.getInstance().getSubscriptions().containsKey(CurrentPage.
                            getInstance().getCurrentUser())) {
                        CurrentPage.getInstance().getSubscriptions().get(CurrentPage.getInstance()
                                .getCurrentUser()).add(genre);
                    } else {
                        CurrentPage.getInstance().getSubscriptions().put(CurrentPage.getInstance()
                                .getCurrentUser(), new ArrayList<>());
                        CurrentPage.getInstance().getSubscriptions().get(CurrentPage.getInstance()
                                .getCurrentUser()).add(genre);
                    }
                    return 0;
                }
            }
        }
        return 1;
    }
}
