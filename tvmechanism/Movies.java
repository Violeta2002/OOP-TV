package tvmechanism;

import fileio.MoviesInput;

import java.util.ArrayList;
import java.util.HashMap;

public final class Movies {
    private String name;
    private String year;
    private int duration;

    private HashMap<Users, Double> ratings = new HashMap<>();
    private ArrayList<String> genres = new ArrayList<>();
    private ArrayList<String> actors = new ArrayList<>();
    private ArrayList<String> countriesBanned = new ArrayList<>();
    private int numLikes;
    private double rating;
    private int numRatings;

    public Movies() {
    }

    public Movies(final MoviesInput movies) {
        this.name = movies.getName();
        this.actors = movies.getActors();
        this.countriesBanned = movies.getCountriesBanned();
        this.genres = movies.getGenres();
        this.year = movies.getYear();
        this.duration = movies.getDuration();
        this.rating = 0.00;
    }

    public Movies(final Movies movies) {
        this.name = movies.getName();
        this.actors = movies.getActors();
        this.countriesBanned = movies.getCountriesBanned();
        this.genres = movies.getGenres();
        this.year = movies.getYear();
        this.duration = movies.getDuration();
        this.rating = 0.00;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(final String year) {
        this.year = year;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(final ArrayList<String> genres) {
        this.genres = genres;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(final ArrayList<String> actors) {
        this.actors = actors;
    }

    public ArrayList<String> getCountriesBanned() {
        return countriesBanned;
    }

    public void setCountriesBanned(final ArrayList<String> countriesBanned) {
        this.countriesBanned = countriesBanned;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(final int numLikes) {
        this.numLikes = numLikes;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(final double rating) {
        this.rating = rating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(final int numRatings) {
        this.numRatings = numRatings;
    }

    public HashMap<Users, Double> getRatings() {
        return ratings;
    }

    public void setRatings(final HashMap<Users, Double> ratings) {
        this.ratings = ratings;
    }
}
