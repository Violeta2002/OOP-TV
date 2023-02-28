package fileio;

public final class ActionsInput {
    private String type;
    private String page;
    private String feature;
    private CredentialsInput credentials;
    private FiltersInput filters;
    private String startsWith;
    private String movie;
    private String count;
    private int rate;

    private String subscribedGenre;

    private MoviesInput addedMovie;

    private String deletedMovie;

    public ActionsInput() {
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

    public CredentialsInput getCredentials() {
        return credentials;
    }

    public void setCredentials(final CredentialsInput credentials) {
        this.credentials = credentials;
    }

    public FiltersInput getFilters() {
        return filters;
    }

    public void setFilters(final FiltersInput filters) {
        this.filters = filters;
    }

    public String getStartsWith() {
        return startsWith;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(final String movie) {
        this.movie = movie;
    }

    public String getCount() {
        return count;
    }

    public int getRate() {
        return rate;
    }

    public MoviesInput getAddedMovie() {
        return addedMovie;
    }

    public String getSubscribedGenre() {
        return subscribedGenre;
    }

    public String getDeletedMovie() {
        return deletedMovie;
    }
}
