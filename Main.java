
import com.fasterxml.jackson.databind.JsonNode;
import command.CommandHistory;
import pages.CurrentPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import tvmechanism.Actions;
import tvmechanism.Movies;
import tvmechanism.Notification;
import tvmechanism.Users;
import fileio.ActionsInput;
import fileio.Input;
import fileio.MoviesInput;
import fileio.UsersInput;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains the main method
 */
class Main {
    /**
     * Main method
     *
     * @param args - command line arguments
     * @throws IOException - exception
     */
    public static void main(final String[] args) throws IOException {
        action(args[0], args[1]);
    }

    /**
     * @param filePath1 - path to input file
     * @param filePath2 - path to output file
     * @throws IOException - input/output exception
     */
    public static void action(final String filePath1, final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        final Input inputData = objectMapper.readValue(new File(filePath1), Input.class);

        final ArrayNode output = objectMapper.createArrayNode();

        final ArrayList<Movies> movies = new ArrayList<>();
        for (MoviesInput movie : inputData.getMovies()) {
            movies.add(new Movies(movie));
            CurrentPage.getInstance().getFilteredMovies().add(new Movies(movie));
        }

        final ArrayList<Users> users = new ArrayList<>();
        for (UsersInput user : inputData.getUsers()) {
            users.add(new Users(user));
        }

        CurrentPage.getInstance().setNotifcations(new HashMap<>());


        CurrentPage.getInstance().setCurrentMovies(movies);
        CurrentPage.getInstance().setCurrentUsers(users);
        CurrentPage.getInstance().setName("Homepage neautentificat");
        CurrentPage.getInstance().setAlreadyLogged(0);
        CurrentPage.getInstance().setFiltered(0);

        if (CurrentPage.getInstance().getSeeDetailsMovies().size() > 0) {
            CurrentPage.getInstance().getSeeDetailsMovies()
                    .subList(0, CurrentPage.getInstance().getSeeDetailsMovies().size()).clear();
        }

        CommandHistory.getInstance().getHistory().push("Homepage neautentificat");

        int result;
        for (ActionsInput actionsInput : inputData.getActions()) {
            Actions actions = new Actions(actionsInput);
            ObjectNode newNode = objectMapper.createObjectNode();
            result = actions.executeAction(newNode);
            if (result != 1) {
                output.add(newNode);
            }
        }

        if (CurrentPage.getInstance().getCurrentUser()
                .getCredetials().getAccountType().equals("premium")) {
            ObjectNode newNode = objectMapper.createObjectNode();
            doRecommendation(newNode, inputData);
            output.add(newNode);
        }

        while (CommandHistory.getInstance().getHistory().size() > 0) {
            CommandHistory.getInstance().getHistory().pop();
        }
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }

    /**
     * @param newNode
     * @param inputData
     */
    public static void doRecommendation(final ObjectNode newNode, final Input inputData) {
        ObjectMapper objectMapper = new ObjectMapper();
        newNode.put("error", (JsonNode) null);
        newNode.put("currentMoviesList", (JsonNode) null);
        ObjectNode creds = objectMapper.createObjectNode();
        ObjectNode userNode = objectMapper.createObjectNode();
        Actions.addCredentialsToNode(creds, CurrentPage.getInstance()
                .getCurrentUser().getCredetials());
        userNode.put("credentials", creds);
        Notification notification = new Notification();
        initializeTopGenresAndTopMovies(notification, inputData);
        if (!CurrentPage.getInstance().getNotifcations()
                .containsKey(CurrentPage.getInstance().getCurrentUser())) {
            CurrentPage.getInstance().getNotifcations().
                    put(CurrentPage.getInstance().getCurrentUser(), new ArrayList<>());
        }
        CurrentPage.getInstance().getNotifcations()
                .get(CurrentPage.getInstance().getCurrentUser()).add(notification);
        Actions.addUserData(userNode, CurrentPage.getInstance().getCurrentUser());
        newNode.put("currentUser", userNode);
    }

    /**
     * @param notification
     * @param inputData
     */
    public static void initializeTopGenresAndTopMovies(final Notification notification,
                                                       final Input inputData) {
        HashMap<String, Integer> topGenres = new HashMap<>();
        for (Movies movie : CurrentPage.getInstance().getCurrentUser().getLikedMovies()) {
            for (String genre : movie.getGenres()) {
                if (topGenres.containsKey(genre)) {
                    topGenres.put(genre, topGenres.get(genre) + 1);
                } else {
                    topGenres.put(genre, 1);
                }
            }
        }
        HashMap<String, Integer> topLikedMovies = new HashMap<>();
        for (Users user : CurrentPage.getInstance().getCurrentUsers()) {
            for (Movies movie : user.getLikedMovies()) {
                if (topLikedMovies.containsKey(movie.getName())) {
                    topLikedMovies.put(movie.getName(), topLikedMovies.get(movie.getName()) + 1);
                } else {
                    topLikedMovies.put(movie.getName(), 1);
                }
            }
        }
        for (MoviesInput movie : inputData.getMovies()) {
            if (!topLikedMovies.containsKey(movie.getName())) {
                topLikedMovies.put(movie.getName(), 0);
            }
        }

        ArrayList<Movies> topMovies = new ArrayList<>();
        int size = topLikedMovies.size();
        while (size > 0) {
            String topMovieName = getTopLikedMovie(topLikedMovies);
            for (Movies movie : CurrentPage.getInstance().getCurrentMovies()) {
                if (movie.getName().equals(topMovieName)) {
                    topMovies.add(movie);
                    topLikedMovies.remove(topMovieName);
                }
            }
            size--;
        }

        addTopLikedMovie(notification, topGenres, topMovies);
    }

    /**
     * @param topLikedMovies
     * @return
     */
    public static String getTopLikedMovie(final HashMap<String, Integer> topLikedMovies) {
        String topLikedMovie = "";
        int max = 0;
        for (Map.Entry<String, Integer> entry : topLikedMovies.entrySet()) {
            if (entry.getValue() >= max) {
                max = entry.getValue();
                topLikedMovie = entry.getKey();
            }
        }
        return topLikedMovie;
    }

    /**
     * @param notification
     * @param topGenres
     * @param topMovies
     */
    private static void addTopLikedMovie(final Notification notification,
                                         final HashMap<String, Integer> topGenres,
                                         final ArrayList<Movies> topMovies) {
        String topGenre = "";
        int max = 0;
        for (String genre : topGenres.keySet()) {
            if (topGenres.get(genre) > max) {
                max = topGenres.get(genre);
                topGenre = genre;
            }
            if (topGenres.get(genre) == max) {
                if (genre.compareTo(topGenre) < 0) {
                    topGenre = genre;
                }
            }
        }

        if (max == 0) {
            notification.setMovieName("No recommendation");
            notification.setMessage("Recommendation");
        } else {
            for (Movies movie : topMovies) {
                if (movie.getGenres().contains(topGenre)
                        && !CurrentPage.getInstance().getCurrentUser().
                        getWatchedMovies().contains(movie)) {
                    notification.setMovieName(movie.getName());
                    notification.setMessage("Recommendation");
                    return;
                }
            }
            topGenres.remove(topGenre);
            addTopLikedMovie(notification, topGenres, topMovies);
        }
    }
}

