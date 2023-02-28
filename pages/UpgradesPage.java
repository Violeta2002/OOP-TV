package pages;

import tvmechanism.Users;

public final class UpgradesPage extends  Page {
    private String count;
    private String feature;

    public UpgradesPage() {
    }

    public UpgradesPage(final String count, final String feature) {
        this.count = count;
        this.feature = feature;
    }

    /**
     *
     * @param pageName - page name to be visited
     * @return 1 if the page can be visited, 0 otherwise
     */
    public int canVisit(final String pageName) {
        if (pageName.equals("logout") || pageName.equals("movies")
                || pageName.equals("Homepage autentificat")) {
            return 1;
        }
        return 0;
    }

    /**
     *
     * @param currentUser - current user
     */
    public void doFeature(final Users currentUser) {
        if (this.feature.equals("buy tokens")) {
            buyTokens(currentUser);
        }
        if (this.feature.equals("buy premium account")) {
            buyPremiumAccount(currentUser);
        }
    }

    /**
     * Method used to buy tokens for the current user
     * @param currentUser - current user
     */
    public void buyTokens(final Users currentUser) {
        currentUser.setTokensCount(currentUser.getTokensCount() + Integer.parseInt(this.count));
        int balance = Integer.parseInt(currentUser.getCredetials().getBalance());
        balance = balance - Integer.parseInt(count);
        currentUser.getCredetials().setBalance(String.valueOf(balance));
    }

    /**
     * Method used to buy a premium account for the current user
     * @param currentUser - current user
     */
    public void buyPremiumAccount(final Users currentUser) {
        int tokens = currentUser.getTokensCount();
        currentUser.setTokensCount(tokens - 10);
        currentUser.getCredetials().setAccountType("premium");
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(final String feature) {
        this.feature = feature;
    }
}
