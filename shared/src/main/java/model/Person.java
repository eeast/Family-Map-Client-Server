package model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Model Object representing a Person
 */
public class Person {
    /**
     * A UNIQUE id for identifying the Person
     */
    @SerializedName("personID")
    private final String person_id;
    /**
     * The username of the associated User
     */
    @SerializedName("associatedUsername")
    private final String assoc_username;
    /**
     * The Person's first name
     */
    @SerializedName("firstName")
    private final String first_name;
    /**
     * The Person's last name
     */
    @SerializedName("lastName")
    private final String last_name;
    /**
     * The Person's gender, as identified by either m or f
     */
    private final String gender;
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
     * Constructor for PersonResult object with predefined parameters
     * Note: father_id, mother_id, and spouse_id are optional variables, and are therefore not included in the
     * constructor; these parameters are set through corresponding setter functions. An alternate constructor is
     * also available, for convenience.
     * @param assoc_username String username of the user associated with the Person identified in person_id
     * @param person_id String unique person id
     * @param first_name String first name of the Person retrieved
     * @param last_name String last name of the Person retrieved
     * @param gender String gender of the person retrieved, as identified by m or f
     */
    public Person(String person_id, String assoc_username, String first_name, String last_name, String gender) {
        this.person_id = person_id;
        this.assoc_username = assoc_username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
    }

    /**
     * Alternate constructor for PersonResult object with predefined parameters
     * @param assoc_username String username of the user associated with the Person identified in person_id
     * @param person_id String unique person id
     * @param first_name String first name of the Person retrieved
     * @param last_name String last name of the Person retrieved
     * @param gender String gender of the person retrieved, as identified by m or f
     * @param father_id String person_id of this Person's father (can be empty)
     * @param mother_id String person_id of this Person's mother (can be empty)
     * @param spouse_id String person_id of this Person's spouse (can be empty)
     */
    public Person(String person_id, String assoc_username, String first_name, String last_name, String gender, String father_id, String mother_id, String spouse_id) {
        this.person_id = person_id;
        this.assoc_username = assoc_username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.father_id = father_id;
        this.mother_id = mother_id;
        this.spouse_id = spouse_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return person_id.equals(person.person_id) && assoc_username.equals(person.assoc_username) && first_name.equals(person.first_name) && last_name.equals(person.last_name) && gender.equals(person.gender) && Objects.equals(father_id, person.father_id) && Objects.equals(mother_id, person.mother_id) && Objects.equals(spouse_id, person.spouse_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person_id, assoc_username, first_name, last_name, gender, father_id, mother_id, spouse_id);
    }

    public String getPerson_id() {
        return person_id;
    }

    public String getAssoc_username() {
        return assoc_username;
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

    public boolean isValid() {
        return person_id != null && assoc_username != null && first_name != null && last_name != null &&
                (gender.equals("m") || gender.equals("f"));
    }
}
