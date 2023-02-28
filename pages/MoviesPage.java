package pages;

public final class MoviesPage extends Page {

    public MoviesPage() {
    }

    /**
     *
     * @param pageName - page name to be visited
     * @return 1 if the page can be visited, 0 otherwise
     */
    public int canVisit(final String pageName) {
        if (pageName.equals("Homepage autentificat") || pageName.equals("movies")
                || pageName.equals("see details") || pageName.equals("upgrades")
                || pageName.equals("logout")) {
            return 1;
        }
        return 0;
    }
}
