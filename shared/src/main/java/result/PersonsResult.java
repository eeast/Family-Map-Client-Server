package result;

import model.Person;

import java.util.List;

/**
 * Result Object representing the service.result of a service.request for multiple Persons, to be sent to the client
 */
public class PersonsResult {
    /**
     * A List of Persons Objects requested
     */
    private List<Person> data;
    /**
     * An error message (only used if the service.request was unsuccessful)
     */
    private String message;
    /**
     * A boolean indicator of success (if true)
     */
    private final boolean success;

    /**
     *Constructor for PersonsResult object when the requested persons are successfully retrieved from the database
     * @param data List of Person objects requested
     */
    public PersonsResult(List<Person> data) {
        this.data = data;
        this.success = true;
    }

    /**
     * Constructor for PersonsResult when unable to retrieve the requested persons from the database
     * @param message String error message identifying the issue
     */
    public PersonsResult(String message) {
        this.message = message;
        this.success = false;
    }

    public List<Person> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
