package request;

import com.google.gson.annotations.SerializedName;

/**
 * Request Object for the receipt of REGISTER requests from the client
 */
public class RegisterRequest {
    /**
     * The UNIQUE username the User would like to register
     */
    private final String username;
    /**
     * The User's password
     */
    private final String password;
    /**
     * The User's email address
     */
    private final String email;
    /**
     * The User's first name
     */
    @SerializedName("firstName")
    private final String first_name;
    /**
     * The User's last name
     */
    @SerializedName("lastName")
    private final String last_name;
    /**
     * The User's gender, as identified by m or f
     */
    private final String gender;

    /**
     * Constructor for RegisterRequest object with predefined parameters; no parameter may be left blank
     * @param u String username (must be unique within the database)
     * @param p String password
     * @param e String email
     * @param f String first name
     * @param l String last name
     * @param g String gender ("m" or "f")
     */
    public RegisterRequest(String u, String p, String e, String f, String l, String g) {
        this.username = u;
        this.password = p;
        this.email = e;
        this.first_name = f;
        this.last_name = l;
        this.gender = g;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getGender() {
        return gender;
    }

    public boolean isValid() {
        return username != null && password != null && email != null && first_name != null && last_name != null &&
                (gender.equals("m") || gender.equals("f"));
    }
}
