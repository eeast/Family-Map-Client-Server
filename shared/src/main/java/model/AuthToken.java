package model;

import java.util.Objects;

/**
 * Model Object representing an Authorization Token
 */
public class AuthToken {
    /**
     * A UNIQUE token issued upon successful login
     */
    private final String authtoken;
    /**
     * The username of the associated User
     */
    private final String user_id;

    /**
     * Constructor for AuthToken object with predefined parameters
     * @param authtoken unique authorization token generated upon successful login
     * @param user_id associated user_id from successful login (used to identify user for subsequent requests)
     */
    public AuthToken(String authtoken, String user_id) {
        this.authtoken = authtoken;
        this.user_id = user_id;
    }

    public String getToken() {
        return authtoken;
    }

    public String getUser_id() {
        return user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken at = (AuthToken) o;
        return authtoken.equals(at.getToken()) && user_id.equals(at.getUser_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(authtoken, user_id);
    }
}
