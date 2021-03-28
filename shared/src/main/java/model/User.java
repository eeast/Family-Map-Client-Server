package model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Model Object representing a User
 */
public class User {
    /**
     * A UNIQUE username for identifying the User
     */
    private final String username;
    /**
     * The User's password for logging in
     */
    private final String password;
    /**
     * The User's email
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
     * The person_id for the User's corresponding Person Object
     */
    @SerializedName("personID")
    private final String person_id;

    /**
     * Constructor for User Object with predefined parameters
     * @param username A UNIQUE username for identifying the User
     * @param password The User's password for logging in
     * @param email The User's email
     * @param first_name The User's first name
     * @param last_name The User's last name
     * @param gender The User's gender, as identified by m or f
     * @param person_id The person_id for the User's corresponding Person Object
     */
    public User(String username, String password, String email, String first_name, String last_name, String gender,
                String person_id) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.person_id = person_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) && password.equals(user.password) && email.equals(user.email) && first_name.equals(user.first_name) && last_name.equals(user.last_name) && gender.equals(user.gender) && person_id.equals(user.person_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email, first_name, last_name, gender, person_id);
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

    public String getPerson_id() {
        return person_id;
    }

    public boolean isValid() {
        return username != null && password != null && email != null && first_name != null && last_name != null &&
                (gender.equals("m") || gender.equals("f")) && person_id != null;
    }
}
