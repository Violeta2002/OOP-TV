package tvmechanism;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ContainsInput;
import fileio.SortInput;
import pages.CurrentPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public final class Filters {
    private  Sort sort;
    private Contains contains;

    public Filters() { }

    private Filters(final FilterBuilder builder) {
        if (builder.sort != null) {
            this.sort = builder.sort;
        }
        if (builder.contains != null) {
            this.contains = builder.contains;
        }
    }

    public Sort getSort() {
        return sort;
    }

    public Contains getContains() {
        return contains;
    }

    // I used the Builder pattern to create the Filters object because it is more flexible
    // and easier to use
    // I used it because I needed to create a Filters object with only one of the two fields
    // (sort or contains).
    public final static class FilterBuilder {
        private Sort sort;
        private Contains contains;

        public FilterBuilder() {
        }

        /**
         *
         * @param sortInput - sort input
         * @return - builder
         */
        public FilterBuilder buildSort(final SortInput sortInput) {
            this.sort = new Sort(sortInput);
            return this;
        }

        /**
         *
         * @param containsInput - contains input
         * @return - builder
         */
        public FilterBuilder buildContains(final ContainsInput containsInput) {
            this.contains = new Contains(containsInput);
            return this;
        }

        /**
         *
         * @return - filters
         */
        public Filters build() {
            return new Filters(this);
        }
    }
}

class Sort {
    private String rating;
    private String duration;

    Sort(final SortInput sortInput) {
        this.duration = sortInput.getDuration();
        this.rating = sortInput.getRating();
    }

    public String getRating() {
        return rating;
    }

    public void setRating(final String rating) {
        this.rating = rating;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(final String duration) {
        this.duration = duration;
    }

    /**
     * Sorts the movies by rating
     * @param movies - movies
     */
    public final void doSortByRating(final ArrayList<Movies> movies) {
        if (this.rating.equals("increasing")) {
            Collections.sort(movies, new Comparator<Movies>() {
                @Override
                public int compare(final Movies o1, final Movies o2) {
                    return (int) (o1.getRating() - o2.getRating());
                }
            });
        } else if (this.rating.equals("decreasing")) {
            Collections.sort(movies, new Comparator<Movies>() {
                @Override
                public int compare(final Movies o1, final Movies o2) {
                    if (o2.getRating() - o1.getRating() < 0) {
                        return -1;
                    }
                    return (int) (o2.getRating() - o1.getRating());
                }
            });
        }
    }

    /**
     * Sorts the movies by duration
     * @param movies - movies
     */
    public final void doSortByDuration(final ArrayList<Movies> movies) {
        if (this.duration.equals("increasing")) {
            Collections.sort(movies, new Comparator<Movies>() {
                @Override
                public int compare(final Movies o1, final Movies o2) {
                    return o1.getDuration() - o2.getDuration();
                }
            });
        } else if (this.duration.equals("decreasing")) {
            Collections.sort(movies, new Comparator<Movies>() {
                @Override
                public int compare(final Movies o1, final Movies o2) {
                    return o2.getDuration() - o1.getDuration();
                }
            });
        }
    }
}

class Contains {
    private ArrayList<String> actors;
    private ArrayList<String> genre;

    Contains(final ContainsInput containsInput) {
        this.actors = containsInput.getActors();
        this.genre = containsInput.getGenre();
    }

    /**
     * Checks if the movie contains the given actors
     * @param node - node in json format
     * @param movies - movies list in which we search for the movies that contain the actors
     * @param currentUser - current user
     */
    public void addContainedActors(final ObjectNode node, final ArrayList<Movies> movies,
                                   final Users currentUser) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode containedList = objectMapper.createArrayNode();
        CurrentPage.getInstance().setFilteredMovies(new ArrayList<>());
        for (Movies movie : movies) {
            int add = 1;
            if (!movie.getCountriesBanned().contains(currentUser.getCredetials().getCountry())) {
                for (String actor : this.actors) {
                    if (!movie.getActors().contains(actor)) {
                        add = 0;
                        break;
                    }
                }
                if (add == 1) {
                    ObjectNode movieNode = objectMapper.createObjectNode();
                    Actions.addMovieToNode(movieNode, movie);
                    containedList.add(movieNode);
                    CurrentPage.getInstance().getFilteredMovies().add(movie);
                }
            }
        }
        node.put("error", (JsonNode) null);
        node.put("currentMoviesList", containedList);
        Actions.addCurrentUserToNode(node, currentUser);
        CurrentPage.getInstance().setFiltered(1);
    }

    /**
     * Checks if the movie contains the given genre
     * @param node - node in json format
     * @param movies - movies list in which we search for the movies that contain the genres
     * @param currentUser  - current user
     */
    public void addContainedGenres(final ObjectNode node, final ArrayList<Movies> movies,
                                   final Users currentUser) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode containedList = objectMapper.createArrayNode();
        CurrentPage.getInstance().setFilteredMovies(new ArrayList<>());
        for (Movies movie : movies) {
            int add = 1;
            if (!movie.getCountriesBanned().contains(currentUser.getCredetials().getCountry())) {
                for (String genre : this.genre) {
                    if (!movie.getGenres().contains(genre)) {
                        add = 0;
                        break;
                    }
                }
                if (add == 1) {
                    ObjectNode movieNode = objectMapper.createObjectNode();
                    Actions.addMovieToNode(movieNode, movie);
                    containedList.add(movieNode);
                    CurrentPage.getInstance().getFilteredMovies().add(movie);
                }
            }
        }
        node.put("error", (JsonNode) null);
        node.put("currentMoviesList", containedList);
        Actions.addCurrentUserToNode(node, currentUser);
        CurrentPage.getInstance().setFiltered(1);
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(final ArrayList<String> actors) {
        this.actors = actors;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }
}
