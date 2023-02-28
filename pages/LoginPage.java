package pages;

import tvmechanism.Users;

import java.util.ArrayList;

public final class LoginPage extends Page {
    private String name;
    private String password;

    public LoginPage() {
    }

    public LoginPage(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    /**
     *
     * @param users - users list in which the user is searched and logged in if
     *              there is nobody already logged in
     * @return null if the user cannot be logged in, the user's credentials otherwise
     */
    public Users doLogin(final ArrayList<Users> users) {
        if (CurrentPage.getInstance().getAlreadyLogged() == 1) {
            return null;
        }
        for (Users user : users) {
            if (user.getCredetials().getName().equals(name)
                    && user.getCredetials().getPassword().equals(password)) {
//                System.out.println("login:" + user.getNumFreePremiumMovies());
               return user;
            }
        }
        return null;
    }



    /**
     *
     * @param pageName - page name to be visited
     * @return 1 if the page can be visited, 0 otherwise
     */
    public int canVisit(final String pageName) {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
