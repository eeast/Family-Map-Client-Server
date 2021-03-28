package request;

/**
 * Request Object for the receipt of LOGIN requests from the client
 */
public class LoginRequest {
    /**
     * The UNIQUE username of the User attempting to log in
     */
    private final String username;
    /**
     * The password given by the User attempting to log in
     */
    private final String password;

    /**
     * Constructor for LoginRequest object with predefined parameters
     * @param username String containing the user's username
     * @param password String containing the user's password
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isValid() {
        return (username != null && password != null);
    }
}
