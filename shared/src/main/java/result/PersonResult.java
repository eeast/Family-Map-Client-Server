package result;

import com.google.gson.annotations.SerializedName;

/**
 * Result Object representing the service.result of a service.request for a Person, to be sent to the client
 */
public class PersonResult {
    /**
     * The username of the associated User
     */
    @SerializedName("associatedUsername")
    private String assoc_username;
    /**
     * A UNIQUE id for identifying the Person
     */
    @SerializedName("personID")
    private String person_id;
    /**
     * The Person's first name
     */
    @SerializedName("firstName")
    private String first_name;
    /**
     * The Person's last name
     */
    @SerializedName("lastName")
    private String last_name;
    /**
     * The Person's gender, as identified by either m or f
     */
    private String gender;
    /**
     * The person_id of the Person's father, if applicable
     */
    @SerializedName("fatherID")
    private String father_id;
    /**
     * The person_id of the Person's mother, if applicable
     */
    @SerializedName("motherID")
    private String mother_id;
    /**
     * The person_id of the Person's spouse, if applicable
     */
    @SerializedName("spouseID")
    private String spouse_id;
    /**
     * An error message (only used if the service.request was unsuccessful)
     */
    private String message;
    /**
     * A boolean indicator of success (if true)
     */
    private final boolean success;

    /**
     * Constructor for PersonResult object when a Person was successfully retrieved from the database
     * @param assoc_username String username of the user associated with the Person identified in person_id
     * @param person_id String unique person id
     * @param first_name String first name of the Person retrieved
     * @param last_name String last name of the Person retrieved
     * @param gender String gender ("m" or "f") of the person retrieved
     * Note: father_id, mother_id, and spouse_id are optional variables, and are therefore not included in the
     * constructor; these parameters are set through corresponding setter functions.
     */
    public PersonResult(String assoc_username, String person_id, String first_name, String last_name, String gender) {
        this.assoc_username = assoc_username;
        this.person_id = person_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.success = true;
    }

    /**
     * Constructor for PersonResult object when unable to retrieve the requested person from the database
     * @param message String error message identifying the issue
     */
    public PersonResult(String message) {
        this.message = message;
        this.success = false;
    }

    public String getAssoc_username() {
        return assoc_username;
    }

    public String getPerson_id() {
        return person_id;
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

    public String getFather_id() {
        return father_id;
    }

    public void setFather_id(String father_id) {
        this.father_id = father_id;
    }

    public String getMother_id() {
        return mother_id;
    }

    public void setMother_id(String mother_id) {
        this.mother_id = mother_id;
    }

    public String getSpouse_id() {
        return spouse_id;
    }

    public void setSpouse_id(String spouse_id) {
        this.spouse_id = spouse_id;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
