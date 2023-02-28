package pages;

import tvmechanism.Credentials;
import tvmechanism.Users;

import java.util.ArrayList;

public final class RegisterPage extends Page {
    private String name;
    private String password;
    private String acctountType;
    private String country;
    private String balance;

    public RegisterPage() {
    }

    public RegisterPage(final String name, final String password, final String acctountType,
                        final String country, final String balance) {
        this.name = name;
        this.password = password;
        this.acctountType = acctountType;
        this.country = country;
        this.balance = balance;
    }

    public RegisterPage(final Credentials credentials) {
        this.acctountType = credentials.getAccountType();
        this.balance = credentials.getBalance();
        this.country = credentials.getCountry();
        this.password = credentials.getPassword();
        this.name = credentials.getName();
    }

    /**
     *
     * @param credentials - credentials to be added
     * @param users - users list in which the user is added at registration
     */
    public void doRegister(final Credentials credentials, final ArrayList<Users> users) {
        Users user = new Users(credentials);
        users.add(user);
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

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(final String balance) {
        this.balance = balance;
    }
}
