package tvmechanism;

import fileio.CredentialsInput;
import fileio.UsersInput;
import java.util.ArrayList;

public final class Users {
    private Credentials credetials;
    private int tokensCount;
    private int numFreePremiumMovies;
    private ArrayList<Movies> purchasedMovies = new ArrayList<>();
    private ArrayList<Movies> watchedMovies = new ArrayList<>();
    private ArrayList<Movies> likedMovies = new ArrayList<>();
    private ArrayList<Movies> ratedMovies = new ArrayList<>();

//    private ArrayList<Notification> notifications = new ArrayList<>();

//    private ArrayList<String> subscribedMovies = new ArrayList<>();

    public Users(final Credentials credetials) {
        this.credetials = credetials;
        this.numFreePremiumMovies = 15;
    }

    public Users() {
        this.numFreePremiumMovies = 15;
    }

    public Users(final UsersInput users) {
        this.credetials = new Credentials(users.getCredentials());
        this.numFreePremiumMovies = 15;
    }

    public Users(final CredentialsInput credentials) {
        this.credetials = new Credentials(credentials);
        this.numFreePremiumMovies = 15;
    }

    public Credentials getCredetials() {
        return credetials;
    }

    public void setCredetials(final Credentials credetials) {
        this.credetials = credetials;
    }

    public int getTokensCount() {
        return tokensCount;
    }

    public void setTokensCount(final int tokensCount) {
        this.tokensCount = tokensCount;
    }

    public int getNumFreePremiumMovies() {
        return numFreePremiumMovies;
    }

    public void setNumFreePremiumMovies(final int numFreePremiumMovies) {
        this.numFreePremiumMovies = numFreePremiumMovies;
    }

    public ArrayList<Movies> getPurchasedMovies() {
        return purchasedMovies;
    }

    public void setPurchasedMovies(final ArrayList<Movies> purchasedMovies) {
        this.purchasedMovies = purchasedMovies;
    }

    public ArrayList<Movies> getWatchedMovies() {
        return watchedMovies;
    }

    public void setWatchedMovies(final ArrayList<Movies> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }

    public ArrayList<Movies> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(final ArrayList<Movies> likedMovies) {
        this.likedMovies = likedMovies;
    }

    public ArrayList<Movies> getRatedMovies() {
        return ratedMovies;
    }

    public void setRatedMovies(final ArrayList<Movies> ratedMovies) {
        this.ratedMovies = ratedMovies;
    }

}
