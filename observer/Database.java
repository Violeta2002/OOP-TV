package observer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pages.CurrentPage;
import tvmechanism.Movies;
import tvmechanism.Notification;
import tvmechanism.Users;

import java.util.ArrayList;

import static pages.CurrentPage.getInstance;

public final class Database extends Observer {
    private Movies addedMovie;
    private String deletedMovie;

    public Database(final Subject subject, final Movies addedMovie,
                    final String deletedMovie) {
        this.subject = subject;
        this.addedMovie = addedMovie;
        this.deletedMovie = deletedMovie;
        this.subject.addObserver(this);
    }

    /**
     * @param node
     * @return
     */
    @Override
    public int update(final ObjectNode node) {
        if (this.subject.getState().equals("add")) {
            for (Movies movie : getInstance().getCurrentMovies()) {
                if (movie.getName().equals(addedMovie.getName())) {
                    node.put("error", "Error");
                    node.put("currentMoviesList", CurrentPage.getInstance().getName());
                    node.put("currentUser", (JsonNode) null);
                    return 1;
                }
            }
            getInstance().getCurrentMovies().add(addedMovie);
            notifyByAdd();
            return 0;
        } else if (this.subject.getState().equals("delete")) {
            for (Movies movie : getInstance().getCurrentMovies()) {
                if (movie.getName().equals(deletedMovie)) {
                    getInstance().getCurrentMovies().remove(movie);
                    deleteMovieForEveryUsers(movie);
                    receiveMovieValue();
                    return 0;
                }
            }
            Notification notification = new Notification();
            notification.setMovieName(deletedMovie);
            notification.setMessage("DELETE");
            if (getInstance().getNotifcations()
                    .containsKey(getInstance().getCurrentUser())) {
                getInstance().getNotifcations().get(getInstance()
                        .getCurrentUser()).add(notification);
            } else {
                getInstance().getNotifcations()
                        .put(getInstance().getCurrentUser(), new ArrayList<>());
                getInstance().getNotifcations().get(getInstance()
                        .getCurrentUser()).add(notification);
            }
        }
        return 1;
    }

    /**
     * @param
     */
    private void notifyByAdd() {
        for (String genre : addedMovie.getGenres()) {
            for (Users user : getInstance().getCurrentUsers()) {
                if (getInstance().getSubscriptions().containsKey(user)) {
                    if (getInstance().getSubscriptions().get(user).contains(genre)) {
                        Notification notif = new Notification();
                        notif.setMovieName(addedMovie.getName());
                        notif.setMessage("ADD");
                        if (getInstance().getNotifcations().containsKey(user)) {
                            boolean exist = false;
                            for (Notification notification
                                    : getInstance().getNotifcations().get(user)) {
                                if (notification.getMovieName().equals(addedMovie.getName())
                                        && notification.getMessage().equals("ADD")) {
                                    exist = true;
                                    break;
                                }
                            }
                            if (!exist) {
                                getInstance().getNotifcations().get(user).add(notif);
                            }
                        } else {
                            getInstance().getNotifcations().put(user, new ArrayList<>());
                            getInstance().getNotifcations().get(user).add(notif);
                        }
                    }
                }
            }
        }
    }

    /**
     * @param
     */
    private void receiveMovieValue() {
        for (Users user : getInstance().getCurrentUsers()) {
            if (user.getCredetials().getAccountType().equals("standard")) {
                int oldTokens = CurrentPage.getInstance().getCurrentUser().getTokensCount();
                CurrentPage.getInstance().getCurrentUser().setTokensCount(oldTokens + 2);
            }
            if (user.getCredetials().getAccountType().equals("premium")) {
                int freeMovies = CurrentPage.getInstance()
                        .getCurrentUser().getNumFreePremiumMovies();
                CurrentPage.getInstance().getCurrentUser().setTokensCount(freeMovies + 1);
            }
        }
    }

    /**
     * @param movie
     */
    private void deleteMovieForEveryUsers(final Movies movie) {
        for (Users user : getInstance().getCurrentUsers()) {
            for (Movies userMovie : user.getPurchasedMovies()) {
                if (userMovie.getName().equals(movie.getName())) {
                    user.getPurchasedMovies().remove(userMovie);
                    break;
                }
            }
            for (Movies userMovie : user.getWatchedMovies()) {
                if (userMovie.getName().equals(movie.getName())) {
                    user.getWatchedMovies().remove(userMovie);
                    break;
                }
            }
            for (Movies userMovie : user.getLikedMovies()) {
                if (userMovie.getName().equals(movie.getName())) {
                    user.getLikedMovies().remove(userMovie);
                    break;
                }
            }
            for (Movies userMovie : user.getRatedMovies()) {
                if (userMovie.getName().equals(movie.getName())) {
                    user.getRatedMovies().remove(userMovie);
                    break;
                }
            }
        }
    }
}
