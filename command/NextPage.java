package command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import pages.SeeDetailsPage;
import static pages.CurrentPage.getInstance;
import static tvmechanism.Actions.addCurrentUserToNode;
import static tvmechanism.Actions.addMovieListtoNode;

public final class NextPage implements Command {
    private String page;
    private String movie;

    private String feature;
    private ObjectNode node;

    public NextPage(final String pageName, final String movie,
                    final String feature, final ObjectNode node) {
        this.page = pageName;
        this.movie = movie;
        this.feature = feature;
        this.node = node;
    }

    @Override
    public int execute() {
        return changePage();
    }

    /**
     * Method that changes the current page
     *
     * @return - 1 if the page is not valid, 2 if we want to print the output, 0 otherwise
     */
    public int changePage() {
        if (getInstance().canVisit(page) == 0) {
            return 1;
        }
        int logged = getInstance().getAlreadyLogged();
        if (page.equals("logout")) {
            if (getInstance().getAlreadyLogged() == 0) {
                return 1;
            }
            getInstance().setAlreadyLogged(0);
        }
        if (page.equals("login") && logged == 1) {
            getInstance().setName("Homepage autentificat");
            CommandHistory.getInstance().getHistory().push("Homepage autentificat");
            return 1;
        }
        if (page.equals("movies")) {
            addMovieListtoNode(node, getInstance().getCurrentMovies(),
                    getInstance().getCurrentUser());
            addCurrentUserToNode(node, getInstance().getCurrentUser());
            getInstance().setName("movies");
            CommandHistory.getInstance().getHistory().push("movies");
            return 2;
        }
        if (page.equals("see details")) {
            SeeDetailsPage seeDetails = new SeeDetailsPage(this.movie, this.feature);
            int returnNr;
            if (getInstance().getFiltered() == 1) {
                returnNr = seeDetails.seeMovieDetails(node,
                        getInstance().getFilteredMovies(),
                        getInstance().getCurrentUser()) + 1;
            } else {
                returnNr = seeDetails.seeMovieDetails(node,
                        getInstance().getCurrentMovies(),
                        getInstance().getCurrentUser()) + 1;
            }
            if (returnNr != 1) {
                getInstance().setName("see details");
                CommandHistory.getInstance().getHistory().push("see details");
            }
            return returnNr;
        }
        getInstance().setName(page);
        CommandHistory.getInstance().getHistory().push(page);
        return 0;
    }
}
