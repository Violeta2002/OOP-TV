package pages;

public final class HomepageNeautentificatPage extends Page {

    public HomepageNeautentificatPage() {
    }

    /**
     *
     * @param pageName - page name to be visited
     * @return 1 if the page can be visited, 0 otherwise
     */
    public int canVisit(final String pageName) {
        if (pageName.equals("login") || pageName.equals("register")) {
            return 1;
        }
        return 0;
    }
}
