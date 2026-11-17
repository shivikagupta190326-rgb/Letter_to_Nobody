package ocean.model;

/** A local account. Public messages never store this user's ID. */
public class LocalUser {
    private final String id;
    private final String username;

    public LocalUser(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
}
