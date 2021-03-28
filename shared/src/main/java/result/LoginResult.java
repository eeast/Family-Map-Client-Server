package result;

import com.google.gson.annotations.SerializedName;

/**
 * Result Object representing the service.result of a Login Request, to be sent to the client
 */
public class LoginResult {
    /**
     * The User's authorization token
     */
    private String authtoken;
    /**
     * The User's username
     */
    private String username;
    /**
     * The User's Person ID
     */
    @SerializedName("personID")
    private String person_id;
    /**
     * An error message (only used if the service.request was unsuccessful)
     */
    private String message;
    /**
     * A boolean indicator of success (if true)
     */
    private final boolean success;

    /**
     * Constructor for LoginResult object when the user has successfully logged in
     * @param authtoken String authorization token generated for this session
     * @param username String username of the user
     * @param person_id String person_id of the user
     */
    public LoginResult(String authtoken, String username, String person_id) {
        this.authtoken = authtoken;
        this.username = username;
        this.person_id = person_id;
        this.success = true;
    }

    /**
     * Constructor for LoginResult object when unable to find the username and password in the database
     * @param message String error message identifying the issue
     */
    public LoginResult(String message) {
        this.message = message;
        this.success = false;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }

    public String getPerson_id() {
        return person_id;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
