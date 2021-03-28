package result;

/**
 * Result Object representing the service.result of a service.request to validate an AuthToken received by a handler
 * that requires authorization.
 */
public class AuthorizationResult {
    /**
     * Unique authtoken received from the handler for which a search was (successfully) performed; null if not found
     */
    private String authtoken;
    /**
     * Username retrieved from the database for the authtoken provided
     */
    private String username;
    /**
     * Feedback message, either confirming completion or identifying the issue
     */
    private String message;
    /**
     * A boolean indicator of success (if true)
     */
    private final boolean success;

    /**
     * Constructor for AuthorizationResult object with a message identifying a positive result of the service.request
     * @param authtoken String Unique identifier received from the handler for looking up the corresponding AuthToken
     * @param username String username retrieved from the database for the authorization token received
     */
    public AuthorizationResult(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
        this.success = true;
    }

    /**
     * Constructor for AuthorizationResult object with a message identifying the results of the service.request
     * @param m String message identifying that the requested clear was successful, of if unsuccessful, an error
     *          message identifying the issue.
     */
    public AuthorizationResult(String m) {
        this.message = m;
        this.success = false;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }
}
