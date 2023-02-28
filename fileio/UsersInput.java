package fileio;

public final class UsersInput {
    private CredentialsInput credentials;

    public UsersInput(final CredentialsInput credentials) {
        this.credentials = credentials;
    }

    public UsersInput() {
    }

    public CredentialsInput getCredentials() {
        return credentials;
    }

    public void setCredentials(final CredentialsInput credentials) {
        this.credentials = credentials;
    }
}
