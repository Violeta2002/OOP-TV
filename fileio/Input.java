package fileio;

import java.util.ArrayList;

public final class Input {
    private ArrayList<UsersInput> users = new ArrayList<>();
    private ArrayList<MoviesInput> movies = new ArrayList<>();
    private ArrayList<ActionsInput> actions = new ArrayList<>();

    public Input() {
    }

    public ArrayList<UsersInput> getUsers() {
        return users;
    }

    public void setUsers(final ArrayList<UsersInput> users) {
        this.users = users;
    }

    public ArrayList<MoviesInput> getMovies() {
        return movies;
    }

    public void setMovies(final ArrayList<MoviesInput> movies) {
        this.movies = movies;
    }

    public ArrayList<ActionsInput> getActions() {
        return actions;
    }

    public void setActions(final ArrayList<ActionsInput> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "Input{"
                + "users="
                + users
                + ", movies="
                + movies
                + ", actions="
                + actions
                + '}';
    }
}
