package result;

import com.google.gson.annotations.SerializedName;

/**
 * Result Object representing the service.result of a service.request to register a new User, to be sent to the client
 */
public class RegisterResult {
    /**
     * A UNIQUE token issued upon successful login
     */
    private String authtoken;
    /**
     * The username of the associated User
     */
    private String username;
    /**
     * The person_id for the User's corresponding Person Object
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
     * Constructor for RegisterResult object when a user has been successful registered and recorded in the database
     * @param auth_token String unique authorization token for the current session
     * @param username String username of the registered user
     * @param person_id String person_id of the registered user
     */
    public RegisterResult(String auth_token, String username, String person_id) {
        this.authtoken = auth_token;
        this.username = username;
        this.person_id = person_id;
        this.success = true;
    }

    /**
     * Constructor for RegisterResult object when unable to complete registration within the database
     * @param message String error message identifying the issue
     */
    public RegisterResult(String message) {
        this.message = message;
        this.success = false;
    }

    public String getAuth_token() {
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
